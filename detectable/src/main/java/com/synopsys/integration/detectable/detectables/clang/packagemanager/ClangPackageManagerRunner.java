/**
 * detectable
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
package com.synopsys.integration.detectable.detectables.clang.packagemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detectable.detectable.executable.ExecutableOutput;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunner;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunnerException;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.ClangPackageManagerResolver;

public class ClangPackageManagerRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean applies(final ClangPackageManager currentPackageManager, final File workingDirectory, final ExecutableRunner executor) {
        final ClangPackageManagerInfo packageManagerInfo = currentPackageManager.getPackageManagerInfo();
        try {
            final ExecutableOutput versionOutput = executor.execute(workingDirectory, packageManagerInfo.getPkgMgrName(), packageManagerInfo.getCheckPresenceCommandArgs());
            logger.debug(String.format("packageStatusOutput: %s", versionOutput.getStandardOutput()));
            if (versionOutput.getStandardOutput().contains(packageManagerInfo.getCheckPresenceCommandOutputExpectedText())) {
                logger.debug(String.format("Found package manager %s", packageManagerInfo.getPkgMgrName()));
                return true;
            }
            logger.debug(String.format("Output of %s %s does not look right; concluding that the %s package manager is not present. The output: %s", packageManagerInfo.getPkgMgrName(), packageManagerInfo.getCheckPresenceCommandArgs(),
                packageManagerInfo.getPkgMgrName(), versionOutput));
        } catch (final ExecutableRunnerException e) {
            logger.debug(String.format("Error executing %s %s; concluding that the %s package manager is not present. The error: %s", packageManagerInfo.getPkgMgrName(), packageManagerInfo.getCheckPresenceCommandArgs(),
                packageManagerInfo.getPkgMgrName(), e.getMessage()));
            return false;
        }
        return false;
    }

    public PackageDetailsResult getAllPackages(final ClangPackageManager currentPackageManager, final File workingDirectory, final ExecutableRunner executableRunner, final Set<File> dependencyFiles) {
        final Set<PackageDetails> packageDetails = new HashSet<>();
        final Set<File> failedDependencyFiles = new HashSet<>();
        for (final File dependencyFile : dependencyFiles) {
            final PackageDetailsResult packageDetailsResult = getPackages(currentPackageManager, workingDirectory, executableRunner, dependencyFile);
            packageDetails.addAll(packageDetailsResult.getFoundPackages());
            failedDependencyFiles.addAll(packageDetailsResult.getFailedDependencyFiles());
        }

        return new PackageDetailsResult(packageDetails, failedDependencyFiles);
    }

    public PackageDetailsResult getPackages(final ClangPackageManager currentPackageManager, final File workingDirectory, final ExecutableRunner executableRunner, final File dependencyFile) {
        final ClangPackageManagerInfo packageManagerInfo = currentPackageManager.getPackageManagerInfo();
        final Set<PackageDetails> dependencyDetails = new HashSet<>();
        final Set<File> failedDependencyFiles = new HashSet<>();
        try {
            final List<String> fileSpecificGetOwnerArgs = new ArrayList<>(packageManagerInfo.getPkgMgrGetOwnerCmdArgs());
            fileSpecificGetOwnerArgs.add(dependencyFile.getAbsolutePath());
            final ExecutableOutput queryPackageOutput = executableRunner.execute(workingDirectory, packageManagerInfo.getPkgMgrCmdString(), fileSpecificGetOwnerArgs);

            final ClangPackageManagerResolver resolver = currentPackageManager.getPackageResolver();
            final List<PackageDetails> packageDetails = resolver.resolvePackages(currentPackageManager.getPackageManagerInfo(), executableRunner, workingDirectory, queryPackageOutput.getStandardOutput());
            dependencyDetails.addAll(packageDetails);
        } catch (final ExecutableRunnerException e) {
            logger.debug(String.format("Error with dependency file %s when running %s", dependencyFile.getAbsolutePath(), packageManagerInfo.getPkgMgrCmdString()));
            logger.error(String.format("Error executing %s: %s", packageManagerInfo.getPkgMgrCmdString(), e.getMessage()));

        }
        return new PackageDetailsResult(dependencyDetails, failedDependencyFiles);
    }

}
