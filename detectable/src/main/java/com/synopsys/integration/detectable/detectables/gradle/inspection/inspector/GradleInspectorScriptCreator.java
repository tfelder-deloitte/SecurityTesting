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
package com.synopsys.integration.detectable.detectables.gradle.inspection.inspector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detectable.detectable.exception.DetectableException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GradleInspectorScriptCreator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String GRADLE_SCRIPT_TEMPLATE_FILENAME = "init-script-gradle.ftl";

    private final Configuration configuration;

    public GradleInspectorScriptCreator(final Configuration configuration) {
        this.configuration = configuration;
    }

    public File createOfflineGradleInspector(final File templateFile, final GradleInspectorScriptOptions scriptOptions, final String airGapLibraryPaths) throws DetectableException {
        return createGradleInspector(templateFile, scriptOptions, null, airGapLibraryPaths);
    }

    public File createOnlineGradleInspector(final File templateFile, final GradleInspectorScriptOptions scriptOptions, final String resolvedOnlineInspectorVersion) throws DetectableException {
        return createGradleInspector(templateFile, scriptOptions, resolvedOnlineInspectorVersion, null);
    }

    private File createGradleInspector(final File templateFile, final GradleInspectorScriptOptions scriptOptions, final String resolvedOnlineInspectorVersion, final String airGapLibraryPaths) throws DetectableException {
        logger.debug("Generating the gradle script file.");
        final Map<String, String> gradleScriptData = new HashMap<>();

        gradleScriptData.put("airGapLibsPath", StringEscapeUtils.escapeJava(Optional.ofNullable(airGapLibraryPaths).orElse("")));
        gradleScriptData.put("gradleInspectorVersion", StringEscapeUtils.escapeJava(Optional.ofNullable(resolvedOnlineInspectorVersion).orElse("")));
        gradleScriptData.put("excludedProjectNames", scriptOptions.getExcludedProjectNames().orElse(""));
        gradleScriptData.put("includedProjectNames", scriptOptions.getIncludedProjectNames().orElse(""));
        gradleScriptData.put("excludedConfigurationNames", scriptOptions.getExcludedConfigurationNames().orElse(""));
        gradleScriptData.put("includedConfigurationNames", scriptOptions.getIncludedConfigurationNames().orElse(""));
        gradleScriptData.put("customRepositoryUrl", scriptOptions.getGradleInspectorRepositoryUrl());

        try {
            populateGradleScriptWithData(templateFile, gradleScriptData);
        } catch (final IOException | TemplateException e) {
            throw new DetectableException("Failed to generate the Gradle Inspector script from the given template file: " + templateFile.toString(), e);
        }
        logger.trace(String.format("Successfully created Gradle Inspector: %s", templateFile.toString()));
        return templateFile;
    }

    private void populateGradleScriptWithData(final File targetFile, final Map<String, String> gradleScriptData) throws IOException, TemplateException {
        final Template gradleScriptTemplate = configuration.getTemplate(GRADLE_SCRIPT_TEMPLATE_FILENAME);
        try (final Writer fileWriter = new FileWriter(targetFile)) {
            gradleScriptTemplate.process(gradleScriptData, fileWriter);
        }
    }
}
