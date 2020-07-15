/**
 * synopsys-detect
 *
 * Copyright (c) 2020 Synopsys, Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.detect.tool.signaturescanner;

import java.io.File;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.blackduck.codelocation.signaturescanner.ScanBatchRunner;
import com.synopsys.integration.blackduck.codelocation.signaturescanner.command.ScanCommandRunner;
import com.synopsys.integration.blackduck.codelocation.signaturescanner.command.ScanPathsUtility;
import com.synopsys.integration.blackduck.codelocation.signaturescanner.command.ScannerZipInstaller;
import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.rest.BlackDuckHttpClient;
import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.rest.client.IntHttpClient;
import com.synopsys.integration.util.CleanupZipExpander;
import com.synopsys.integration.util.IntEnvironmentVariables;
import com.synopsys.integration.util.OperatingSystemType;

public class ScanBatchRunnerFactory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IntEnvironmentVariables intEnvironmentVariables;
    private final SignatureScannerLogger slf4jIntLogger;
    private final OperatingSystemType operatingSystemType;
    private final ScanPathsUtility scanPathsUtility;
    private final ScanCommandRunner scanCommandRunner;

    public ScanBatchRunnerFactory(final IntEnvironmentVariables intEnvironmentVariables, final ExecutorService executorService) {
        this.intEnvironmentVariables = intEnvironmentVariables;
        slf4jIntLogger = new SignatureScannerLogger(logger);
        operatingSystemType = OperatingSystemType.determineFromSystem();
        scanPathsUtility = new ScanPathsUtility(slf4jIntLogger, intEnvironmentVariables, operatingSystemType);
        scanCommandRunner = new ScanCommandRunner(slf4jIntLogger, intEnvironmentVariables, scanPathsUtility, executorService);
    }

    public ScanBatchRunner withInstall(final BlackDuckServerConfig blackDuckServerConfig) {
        // will will use the server to download/update the scanner - this is the most likely situation
        BlackDuckHttpClient blackDuckHttpClient = blackDuckServerConfig.createBlackDuckHttpClient(slf4jIntLogger);
        CleanupZipExpander cleanupZipExpander = new CleanupZipExpander(slf4jIntLogger);
        ScannerZipInstaller scannerZipInstaller = new ScannerZipInstaller(slf4jIntLogger, blackDuckHttpClient, cleanupZipExpander, scanPathsUtility, blackDuckServerConfig.getBlackDuckUrl().toString(), operatingSystemType);
        final ScanBatchRunner scanBatchManager = ScanBatchRunner.createComplete(intEnvironmentVariables, scannerZipInstaller, scanPathsUtility, scanCommandRunner);
        return scanBatchManager;
    }

    public ScanBatchRunner withoutInstall(final File defaultInstallDirectory) {
        // either we were given an existing path for the scanner or
        // we are offline - either way, we won't attempt to manage the install
        return ScanBatchRunner.createWithNoInstaller(intEnvironmentVariables, defaultInstallDirectory, scanPathsUtility, scanCommandRunner);
    }

    public ScanBatchRunner withUserProvidedUrl(final String userProvidedScannerInstallUrl, final IntHttpClient restConnection) throws DetectUserFriendlyException {
        // we will use the provided url to download/update the scanner
        final CleanupZipExpander cleanupZipExpander = new CleanupZipExpander(slf4jIntLogger);
        final ScannerZipInstaller scannerZipInstaller = new ScannerZipInstaller(slf4jIntLogger, restConnection, cleanupZipExpander, scanPathsUtility, userProvidedScannerInstallUrl, operatingSystemType);

        return ScanBatchRunner.createComplete(intEnvironmentVariables, scannerZipInstaller, scanPathsUtility, scanCommandRunner);
    }

}
