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
package com.synopsys.integration.detect.tool.detector.inspectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.misc.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.workflow.airgap.AirGapInspectorPaths;
import com.synopsys.integration.detect.workflow.file.DirectoryManager;
import com.synopsys.integration.detectable.detectable.exception.DetectableException;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectables.docker.DockerDetectableOptions;
import com.synopsys.integration.detectable.detectables.docker.DockerInspectorInfo;
import com.synopsys.integration.detectable.detectables.docker.DockerInspectorResolver;
import com.synopsys.integration.exception.IntegrationException;

public class ArtifactoryDockerInspectorResolver implements DockerInspectorResolver {
    private static final String IMAGE_INSPECTOR_FAMILY = "blackduck-imageinspector";
    private static final List<String> inspectorNames = Arrays.asList("ubuntu", "alpine", "centos");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DOCKER_SHARED_DIRECTORY_NAME = "docker";

    private final DirectoryManager directoryManager;
    private final AirGapInspectorPaths airGapInspectorPaths;
    private final FileFinder fileFinder;
    private final DockerInspectorInstaller dockerInspectorInstaller;
    private final DockerDetectableOptions dockerDetectableOptions;

    private DockerInspectorInfo resolvedInfo;

    public ArtifactoryDockerInspectorResolver(final DirectoryManager directoryManager, final AirGapInspectorPaths airGapInspectorPaths, final FileFinder fileFinder, final DockerInspectorInstaller dockerInspectorInstaller,
        final DockerDetectableOptions dockerDetectableOptions) {
        this.directoryManager = directoryManager;
        this.airGapInspectorPaths = airGapInspectorPaths;
        this.fileFinder = fileFinder;
        this.dockerInspectorInstaller = dockerInspectorInstaller;
        this.dockerDetectableOptions = dockerDetectableOptions;
    }

    @Override
    public DockerInspectorInfo resolveDockerInspector() throws DetectableException {
        try {
            if (resolvedInfo == null) {
                resolvedInfo = install();
            }
            return resolvedInfo;
        } catch (final Exception e) {
            throw new DetectableException(e);
        }
    }

    private DockerInspectorInfo install() throws IntegrationException, IOException, DetectUserFriendlyException {
        final Optional<File> airGapDockerFolder = airGapInspectorPaths.getDockerInspectorAirGapFile();
        // TODO: Handle null better.
        final Optional<Path> providedJarPath = dockerDetectableOptions.getDockerInspectorPath();

        if (providedJarPath.isPresent()) {
            logger.info("Docker tool will attempt to use the provided docker inspector.");
            return findProvidedJar(providedJarPath.get());
        } else if (airGapDockerFolder.isPresent()) {
            logger.info("Docker tool will attempt to use the air gapped docker inspector.");
            final Optional<DockerInspectorInfo> airGapInspector = findAirGapInspector();
            return airGapInspector.orElse(null);
        } else {
            logger.info("Docker tool will attempt to download or find docker inspector.");
            final File dockerDirectory = directoryManager.getPermanentDirectory(DOCKER_SHARED_DIRECTORY_NAME);
            // TODO: Handle null better.
            final String dockerVersion = dockerDetectableOptions.getDockerInspectorVersion().orElse("");
            return new DockerInspectorInfo(dockerInspectorInstaller.installJar(dockerDirectory, Optional.of(dockerVersion)));
        }
    }

    private Optional<DockerInspectorInfo> findAirGapInspector() {
        return getAirGapJar().map(dockerInspectorJar1 -> new DockerInspectorInfo(dockerInspectorJar1, getAirGapInspectorImageTarfiles()));
    }

    private List<File> getAirGapInspectorImageTarfiles() {
        final List<File> airGapInspectorImageTarfiles;
        airGapInspectorImageTarfiles = new ArrayList<>();
        final String dockerInspectorAirGapPath = airGapInspectorPaths.getDockerInspectorAirGapPath()
                                                     .map(Path::toString)
                                                     .orElse(null);
        for (final String inspectorName : inspectorNames) {
            final File osImage = new File(dockerInspectorAirGapPath, IMAGE_INSPECTOR_FAMILY + "-" + inspectorName + ".tar");
            airGapInspectorImageTarfiles.add(osImage);
        }
        return airGapInspectorImageTarfiles;
    }

    private DockerInspectorInfo findProvidedJar(@NotNull final Path providedJarPath) {
        File providedJar = null;
        
        logger.debug(String.format("Using user-provided docker inspector jar path: %s", providedJarPath));
        final File providedJarCandidate = providedJarPath.toFile();
        if (providedJarCandidate.isFile()) {
            logger.debug(String.format("Found user-specified jar: %s", providedJarCandidate.getAbsolutePath()));
            providedJar = providedJarCandidate;
        }

        return new DockerInspectorInfo(providedJar);
    }

    private Optional<File> getAirGapJar() {
        final Optional<File> airGapDirPath = airGapInspectorPaths.getDockerInspectorAirGapFile();
        if (!airGapDirPath.isPresent()) {
            return Optional.empty();
        }

        logger.debug(String.format("Checking for air gap docker inspector jar file in: %s", airGapDirPath));
        try {
            final List<File> possibleJars = fileFinder.findFiles(airGapDirPath.get(), "*.jar", 1);
            if (possibleJars == null || possibleJars.isEmpty()) {
                logger.error("Unable to locate air gap jar.");
                return Optional.empty();
            } else {
                final File airGapJarFile = possibleJars.get(0);
                logger.info(String.format("Found air gap docker inspector: %s", airGapJarFile.getAbsolutePath()));
                return Optional.of(airGapJarFile);
            }
        } catch (final Exception e) {
            logger.debug(String.format("Did not find a docker inspector jar file in the airgap dir: %s", airGapDirPath));
            return Optional.empty();
        }
    }

}
