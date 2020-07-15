package com.synopsys.integration.detect.lifecycle.boot.decision;

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

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.builder.BuilderStatus;
import com.synopsys.integration.detect.DetectTool;
import com.synopsys.integration.detect.configuration.DetectConfigurationFactory;
import com.synopsys.integration.detect.configuration.connection.BlackDuckConnectionDetails;
import com.synopsys.integration.detect.tool.signaturescanner.BlackDuckSignatureScannerOptions;
import com.synopsys.integration.detect.util.filter.DetectToolFilter;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfigBuilder;

public class ProductDecider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ProductDecision decide(DetectConfigurationFactory detectConfigurationFactory, final File userHome, final DetectToolFilter detectToolFilter) {
        BlackDuckConnectionDetails blackDuckConnectionDetails = detectConfigurationFactory.createBlackDuckConnectionDetails();
        BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions = detectConfigurationFactory.createBlackDuckSignatureScannerOptions();
        return new ProductDecision(determineBlackDuck(blackDuckConnectionDetails, blackDuckSignatureScannerOptions), determinePolaris(detectConfigurationFactory, userHome, detectToolFilter));
    }

    public PolarisDecision determinePolaris(DetectConfigurationFactory detectConfigurationFactory, final File userHome, final DetectToolFilter detectToolFilter) {
        if (!detectToolFilter.shouldInclude(DetectTool.POLARIS)) {
            logger.debug("Polaris will NOT run because it is excluded.");
            return PolarisDecision.skip();
        }
        final PolarisServerConfigBuilder polarisServerConfigBuilder = detectConfigurationFactory.createPolarisServerConfigBuilder(userHome);
        final BuilderStatus builderStatus = polarisServerConfigBuilder.validateAndGetBuilderStatus();
        if (!builderStatus.isValid()) {
            final String polarisUrl = polarisServerConfigBuilder.getUrl();
            if (StringUtils.isBlank(polarisUrl)) {
                logger.debug("Polaris will NOT run: The Polaris url must be provided.");
            } else {
                logger.debug("Polaris will NOT run: " + builderStatus.getFullErrorMessage());
            }
            return PolarisDecision.skip();
        } else {
            logger.debug("Polaris will run: An access token and url were found.");
            return PolarisDecision.runOnline(polarisServerConfigBuilder.build());
        }
    }

    public BlackDuckDecision determineBlackDuck(BlackDuckConnectionDetails blackDuckConnectionDetails, BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions) {
        final boolean offline = blackDuckConnectionDetails.getOffline();
        final Optional<String> blackDuckUrl = blackDuckConnectionDetails.getBlackDuckUrl();
        Optional<String> signatureScannerHostUrl = blackDuckSignatureScannerOptions.getUserProvidedScannerInstallUrl();
        Optional<Path> signatureScannerOfflineLocalPath = blackDuckSignatureScannerOptions.getOfflineLocalScannerInstallPath();
        if (offline) {
            logger.debug("Black Duck will run: Black Duck offline mode was set to true.");
            return BlackDuckDecision.runOffline();
        } else if (signatureScannerHostUrl.isPresent()) {
            logger.info("A Black Duck signature scanner url was provided, which requires Black Duck offline mode.");
            return BlackDuckDecision.runOffline();
        } else if (signatureScannerOfflineLocalPath.isPresent()) {
            logger.info("A local Black Duck signature scanner path was provided, which requires Black Duck offline mode.");
            return BlackDuckDecision.runOffline();
        } else if (blackDuckUrl.isPresent()) {
            logger.debug("Black Duck will run: A Black Duck url was found.");
            return BlackDuckDecision.runOnline();
        } else {
            logger.debug("Black Duck will NOT run: The Black Duck url must be provided or offline mode must be set to true.");
            return BlackDuckDecision.skip();
        }
    }

}
