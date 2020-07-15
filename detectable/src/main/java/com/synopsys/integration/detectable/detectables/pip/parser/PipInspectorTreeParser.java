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
package com.synopsys.integration.detectable.detectables.pip.parser;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.bdio.graph.MutableDependencyGraph;
import com.synopsys.integration.bdio.graph.MutableMapDependencyGraph;
import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.detectable.codelocation.CodeLocation;
import com.synopsys.integration.detectable.detectable.util.DependencyHistory;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvResult;

public class PipInspectorTreeParser {
    private final Logger logger = LoggerFactory.getLogger(PipInspectorTreeParser.class);

    public static final String SEPARATOR = "==";
    public static final String UNKNOWN_PROJECT_NAME = "n?";
    public static final String UNKNOWN_PROJECT_VERSION = "v?";
    public static final String UNKNOWN_REQUIREMENTS_PREFIX = "r?";
    public static final String UNPARSEABLE_REQUIREMENTS_PREFIX = "p?";
    public static final String UNKNOWN_PACKAGE_PREFIX = "--";
    public static final String INDENTATION = "    ";

    private final ExternalIdFactory externalIdFactory;

    public PipInspectorTreeParser(final ExternalIdFactory externalIdFactory) {
        this.externalIdFactory = externalIdFactory;
    }

    public Optional<PipenvResult> parse(final List<String> pipInspectorOutputAsList, final String sourcePath) {
        PipenvResult parseResult = null;

        final MutableDependencyGraph graph = new MutableMapDependencyGraph();
        final DependencyHistory history = new DependencyHistory();
        Dependency project = null;

        for (final String line : pipInspectorOutputAsList) {
            final String trimmedLine = StringUtils.trimToEmpty(line);
            if (StringUtils.isEmpty(trimmedLine) || !trimmedLine.contains(SEPARATOR) || trimmedLine.startsWith(UNKNOWN_REQUIREMENTS_PREFIX) || trimmedLine.startsWith(UNPARSEABLE_REQUIREMENTS_PREFIX) || trimmedLine.startsWith(
                UNKNOWN_PACKAGE_PREFIX)) {
                parseErrorsFromLine(trimmedLine);
                continue;
            }

            final Dependency currentDependency = parseDependencyFromLine(trimmedLine, sourcePath);
            final int lineLevel = getLineLevel(line);
            try {
                history.clearDependenciesDeeperThan(lineLevel);
            } catch (final IllegalStateException e) {
                logger.warn(String.format("Problem parsing line '%s': %s", line, e.getMessage()));
            }

            if (project == null) {
                project = currentDependency;
            } else if (project.equals(history.getLastDependency())) {
                graph.addChildToRoot(currentDependency);
            } else if (history.isEmpty()) {
                graph.addChildToRoot(currentDependency);
            } else {
                graph.addChildWithParents(currentDependency, history.getLastDependency());
            }

            history.add(currentDependency);
        }

        if (project != null) {
            final CodeLocation codeLocation = new CodeLocation(graph, project.getExternalId());
            parseResult = new PipenvResult(project.getName(), project.getVersion(), codeLocation);
        }

        return Optional.ofNullable(parseResult);
    }

    private void parseErrorsFromLine(final String trimmedLine) {
        if (trimmedLine.startsWith(UNKNOWN_REQUIREMENTS_PREFIX)) {
            logger.error(String.format("Pip inspector could not find requirements file @ %s", trimmedLine.substring(UNKNOWN_REQUIREMENTS_PREFIX.length())));
        }

        if (trimmedLine.startsWith(UNPARSEABLE_REQUIREMENTS_PREFIX)) {
            logger.error(String.format("Pip inspector could not parse requirements file @ %s", trimmedLine.substring(UNPARSEABLE_REQUIREMENTS_PREFIX.length())));
        }

        if (trimmedLine.startsWith(UNKNOWN_PACKAGE_PREFIX)) {
            logger.error(String.format("Pip inspector could not resolve the package: %s", trimmedLine.substring(UNKNOWN_PACKAGE_PREFIX.length())));
        }
    }

    private Dependency parseDependencyFromLine(final String line, final String sourcePath) {
        final String[] segments = line.split(SEPARATOR);

        String name = segments[0].trim();
        String version = segments[1].trim();
        ExternalId externalId = externalIdFactory.createNameVersionExternalId(Forge.PYPI, name, version);

        if (name.equals(UNKNOWN_PROJECT_NAME) || version.equals(
            UNKNOWN_PROJECT_VERSION)) {    //TODO: Pip needs some love. It shouldn't have to do this here but the change seems non-trivial. A code location with no external id should be created as such.
            externalId = externalIdFactory.createPathExternalId(Forge.PYPI, sourcePath);
        }

        name = name.equals(UNKNOWN_PROJECT_NAME) ? "" : name;
        version = version.equals(UNKNOWN_PROJECT_VERSION) ? "" : version;

        return new Dependency(name, version, externalId);
    }

    // TODO: This should be more strict. Currently successfully parses a graph with an indentation of three spaces instead of 4.
    private int getLineLevel(final String line) {
        int level = 0;
        String tmpLine = line;
        while (tmpLine.startsWith(INDENTATION)) {
            tmpLine = tmpLine.replaceFirst(INDENTATION, "");
            level++;
        }

        return level;
    }
}
