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
package com.synopsys.integration.detect.workflow.airgap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.detect.util.filter.DetectFilter;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.detect.workflow.report.util.ReportConstants;
import com.synopsys.integration.detect.workflow.result.AirGapDetectResult;

public class AirGapCreator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AirGapPathFinder airGapPathFinder;

    private final EventSystem eventSystem;
    private final GradleAirGapCreator gradleAirGapCreator;
    private final NugetAirGapCreator nugetAirGapCreator;
    private final DockerAirGapCreator dockerAirGapCreator;

    public AirGapCreator(final AirGapPathFinder airGapPathFinder, final EventSystem eventSystem,
        final GradleAirGapCreator gradleAirGapCreator, final NugetAirGapCreator nugetAirGapCreator, final DockerAirGapCreator dockerAirGapCreator) {
        this.airGapPathFinder = airGapPathFinder;
        this.eventSystem = eventSystem;
        this.gradleAirGapCreator = gradleAirGapCreator;
        this.nugetAirGapCreator = nugetAirGapCreator;
        this.dockerAirGapCreator = dockerAirGapCreator;
    }

    public File createAirGapZip(final DetectFilter inspectorFilter, final File outputPath, final String airGapSuffix, final String gradleInspectorVersion) throws DetectUserFriendlyException {
        try {
            logger.info("");
            logger.info(ReportConstants.RUN_SEPARATOR);
            logger.info(ReportConstants.RUN_SEPARATOR);
            logger.info("Detect is in Air Gap Creation mode.");
            logger.info("Detect will create an air gap of itself and then exit.");
            logger.info("The created air gap zip will not be cleaned up.");
            logger.info(String.format("Specify desired inspectors after -z argument in a comma separated list of ALL, NONE, %s", Arrays.stream(AirGapInspectors.values()).map(Enum::name).collect(Collectors.joining(", "))));
            logger.info(ReportConstants.RUN_SEPARATOR);
            logger.info(ReportConstants.RUN_SEPARATOR);
            logger.info("");

            final File detectJar = airGapPathFinder.findDetectJar();
            if (detectJar == null) {
                throw new DetectUserFriendlyException("To create an air gap zip, Detect must be run from a jar and be able to find that jar. Detect was unable to find it's own jar.", ExitCodeType.FAILURE_CONFIGURATION);
            }
            logger.info("The detect jar location: " + detectJar.getCanonicalPath());

            logger.info("Creating zip at location: " + outputPath);

            final String basename = FilenameUtils.removeExtension(detectJar.getName());
            String airGapName = basename + "-air-gap";
            if (StringUtils.isNotBlank(airGapSuffix)) {
                airGapName = airGapName + "-" + airGapSuffix.toLowerCase();
            }
            final File target = new File(outputPath, airGapName + ".zip");
            final File installFolder = new File(outputPath, basename);

            logger.info("Will build the zip in the following folder: " + installFolder.getCanonicalPath());

            logger.info("Installing dependencies.");
            installAllAirGapDependencies(installFolder, inspectorFilter, gradleInspectorVersion);

            logger.info("Copying detect jar.");
            FileUtils.copyFile(detectJar, new File(installFolder, detectJar.getName()));

            logger.info("Zipping into: " + target.getCanonicalPath());
            ZipUtil.pack(installFolder, target);

            logger.info("Cleaning up working directory: " + installFolder.getCanonicalPath());
            FileUtils.deleteDirectory(installFolder);

            logger.info(ReportConstants.RUN_SEPARATOR);
            final String result = target.getCanonicalPath();
            logger.info("Successfully created air gap zip: " + result);
            logger.info(ReportConstants.RUN_SEPARATOR);

            eventSystem.publishEvent(Event.ResultProduced, new AirGapDetectResult(result));
            return target;
        } catch (final IOException e) {
            throw new DetectUserFriendlyException("Failed to create detect air gap zip.", e, ExitCodeType.FAILURE_UNKNOWN_ERROR);
        }
    }

    public void installAllAirGapDependencies(final File zipFolder, final DetectFilter inspectorFilter, final String gradleInspectorVersion) throws DetectUserFriendlyException {
        logger.info(ReportConstants.RUN_SEPARATOR);

        if (inspectorFilter.shouldInclude(AirGapInspectors.GRADLE.name())) {
            logger.info("Will include GRADLE inspector.");
            logger.info("Installing gradle dependencies.");
            final File gradleTemp = airGapPathFinder.createRelativePackagedInspectorsFile(zipFolder, AirGapPathFinder.GRADLE + "-temp");
            final File gradleTarget = airGapPathFinder.createRelativePackagedInspectorsFile(zipFolder, AirGapPathFinder.GRADLE);
            gradleAirGapCreator.installGradleDependencies(gradleTemp, gradleTarget, gradleInspectorVersion);
        } else {
            logger.info("Will NOT include GRADLE inspector.");
        }

        logger.info(ReportConstants.RUN_SEPARATOR);

        if (inspectorFilter.shouldInclude(AirGapInspectors.NUGET.name())) {
            logger.info("Will include NUGET inspector.");
            logger.info("Installing nuget dependencies.");
            final File nugetFolder = airGapPathFinder.createRelativePackagedInspectorsFile(zipFolder, AirGapPathFinder.NUGET);
            nugetAirGapCreator.installNugetDependencies(nugetFolder);
        } else {
            logger.info("Will NOT include NUGET inspector.");
        }

        logger.info(ReportConstants.RUN_SEPARATOR);

        if (inspectorFilter.shouldInclude(AirGapInspectors.DOCKER.name())) {
            logger.info("Will include DOCKER inspector.");
            logger.info("Installing docker dependencies.");
            final File dockerFolder = airGapPathFinder.createRelativePackagedInspectorsFile(zipFolder, AirGapPathFinder.DOCKER);
            dockerAirGapCreator.installDockerDependencies(dockerFolder);
        } else {
            logger.info("Will NOT include DOCKER inspector.");
        }
        logger.info(ReportConstants.RUN_SEPARATOR);
    }

}
