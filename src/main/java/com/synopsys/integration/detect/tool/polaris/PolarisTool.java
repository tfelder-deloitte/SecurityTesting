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
package com.synopsys.integration.detect.tool.polaris;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.configuration.config.PropertyConfiguration;
import com.synopsys.integration.detect.configuration.DetectProperties;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.detect.workflow.file.DirectoryManager;
import com.synopsys.integration.detect.workflow.status.Status;
import com.synopsys.integration.detect.workflow.status.StatusType;
import com.synopsys.integration.detectable.detectable.executable.Executable;
import com.synopsys.integration.detectable.detectable.executable.ExecutableOutput;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunner;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunnerException;
import com.synopsys.integration.log.IntLogger;
import com.synopsys.integration.polaris.common.cli.PolarisDownloadUtility;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfig;
import com.synopsys.integration.polaris.common.rest.AccessTokenPolarisHttpClient;

public class PolarisTool {
    private static final String POLARIS_DESCRIPTION_KEY = "POLARIS";

    private final DirectoryManager directoryManager;
    private final ExecutableRunner executableRunner;
    private final EventSystem eventSystem;
    private final PropertyConfiguration detectConfiguration;
    private final PolarisServerConfig polarisServerConfig;

    public PolarisTool(final EventSystem eventSystem, final DirectoryManager directoryManager, final ExecutableRunner executableRunner, final PropertyConfiguration detectConfiguration, final PolarisServerConfig polarisServerConfig) {
        this.directoryManager = directoryManager;
        this.executableRunner = executableRunner;
        this.eventSystem = eventSystem;
        this.detectConfiguration = detectConfiguration;
        this.polarisServerConfig = polarisServerConfig;
    }

    public void runPolaris(final IntLogger logger, final File projectDirectory) {
        logger.info("Polaris determined it should attempt to run.");
        final String polarisUrl = detectConfiguration.getValueOrEmpty(DetectProperties.Companion.getPOLARIS_URL()).orElse(null);
        logger.info("Will use the following polaris url: " + polarisUrl);

        final AccessTokenPolarisHttpClient polarisHttpClient = polarisServerConfig.createPolarisHttpClient(logger);
        final File toolsDirectory = directoryManager.getPermanentDirectory();

        final PolarisDownloadUtility polarisDownloadUtility = PolarisDownloadUtility.fromPolaris(logger, polarisHttpClient, toolsDirectory);
        final Optional<String> polarisCliPath = polarisDownloadUtility.retrievePolarisCliExecutablePath();

        //TODO this should be revised to use PolarisCliExecutable and PolarisCliRunner
        if (polarisCliPath.isPresent()) {
            final Map<String, String> environmentVariables = new HashMap<>();
            environmentVariables.put("COVERITY_UNSUPPORTED", "1");
            environmentVariables.put("POLARIS_USER_INPUT_TIMEOUT_MINUTES", "1");
            polarisServerConfig.populateEnvironmentVariables(environmentVariables::put);

            logger.info("Found polaris cli: " + polarisCliPath.get());

            final String additionalArgs = detectConfiguration.getValueOrEmpty(DetectProperties.Companion.getPOLARIS_ARGUMENTS()).orElse(null);
            final String commandOverride = detectConfiguration.getValueOrEmpty(DetectProperties.Companion.getPOLARIS_COMMAND()).orElse(null);
            final List<String> arguments = new ArrayList<>();
            if (StringUtils.isNotBlank(commandOverride)) {
                if (StringUtils.isNotBlank(additionalArgs)) {
                    logger.error("The provided polaris command will be used and the additional polaris arguments will be discarded. You should only set command or arguments, not both.");
                }
                arguments.addAll(Arrays.asList(commandOverride.split(" ")));
            } else if (StringUtils.isNotBlank(additionalArgs)) {
                arguments.add("analyze");
                arguments.addAll(Arrays.asList(additionalArgs.split(" ")));
            } else {
                arguments.add("analyze");
            }

            final Executable swipExecutable = new Executable(projectDirectory, environmentVariables, polarisCliPath.get(), arguments);
            try {
                final ExecutableOutput output = executableRunner.execute(swipExecutable);
                if (output.getReturnCode() == 0) {
                    eventSystem.publishEvent(Event.StatusSummary, new Status(POLARIS_DESCRIPTION_KEY, StatusType.SUCCESS));
                } else {
                    logger.error("Polaris returned a non-zero exit code.");
                    eventSystem.publishEvent(Event.StatusSummary, new Status(POLARIS_DESCRIPTION_KEY, StatusType.FAILURE));
                }

            } catch (final ExecutableRunnerException e) {
                eventSystem.publishEvent(Event.StatusSummary, new Status(POLARIS_DESCRIPTION_KEY, StatusType.FAILURE));
                logger.error("Couldn't run the executable: " + e.getMessage());
            }
        } else {
            logger.error("Check the logs - the Polaris CLI could not be found.");
            eventSystem.publishEvent(Event.StatusSummary, new Status(POLARIS_DESCRIPTION_KEY, StatusType.FAILURE));
        }
    }

}