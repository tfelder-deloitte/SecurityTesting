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
package com.synopsys.integration.detectable.detectables.pip;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.detectable.codelocation.CodeLocation;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunner;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunnerException;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvResult;
import com.synopsys.integration.detectable.detectables.pip.parser.PipInspectorTreeParser;

public class PipInspectorExtractor {
    private final ExecutableRunner executableRunner;
    private final PipInspectorTreeParser pipInspectorTreeParser;

    public PipInspectorExtractor(final ExecutableRunner executableRunner, final PipInspectorTreeParser pipInspectorTreeParser) {
        this.executableRunner = executableRunner;
        this.pipInspectorTreeParser = pipInspectorTreeParser;
    }

    public Extraction extract(final File directory, final File pythonExe, final File pipInspector, final File setupFile, final List<Path> requirementFilePaths, final String providedProjectName) {
        Extraction extractionResult;
        try {
            final String projectName = getProjectName(directory, pythonExe, setupFile, providedProjectName);
            final List<CodeLocation> codeLocations = new ArrayList<>();
            String projectVersion = null;

            final List<Path> requirementsPaths = new ArrayList<>();

            if (requirementFilePaths.isEmpty()) {
                requirementsPaths.add(null);
            } else {
                requirementsPaths.addAll(requirementFilePaths);
            }

            for (final Path requirementFilePath : requirementsPaths) {
                final List<String> inspectorOutput = runInspector(directory, pythonExe, pipInspector, projectName, requirementFilePath);
                final Optional<PipenvResult> result = pipInspectorTreeParser.parse(inspectorOutput, directory.toString());
                if (result.isPresent()) {
                    codeLocations.add(result.get().getCodeLocation());
                    final String potentialProjectVersion = result.get().getProjectVersion();
                    if (projectVersion == null && StringUtils.isNotBlank(potentialProjectVersion)) {
                        projectVersion = potentialProjectVersion;
                    }
                }
            }

            if (codeLocations.isEmpty()) {
                extractionResult = new Extraction.Builder().failure("The Pip Inspector tree parse failed to produce output.").build();
            } else {
                extractionResult = new Extraction.Builder()
                                       .success(codeLocations)
                                       .projectName(projectName)
                                       .projectVersion(projectVersion)
                                       .build();
            }
        } catch (final Exception e) {
            extractionResult = new Extraction.Builder().exception(e).build();
        }

        return extractionResult;
    }

    private List<String> runInspector(final File sourceDirectory, final File pythonExe, final File inspectorScript, final String projectName, final Path requirementsFilePath) throws ExecutableRunnerException {
        final List<String> inspectorArguments = new ArrayList<>();
        inspectorArguments.add(inspectorScript.getAbsolutePath());

        if (requirementsFilePath != null) {
            inspectorArguments.add(String.format("--requirements=%s", requirementsFilePath.toAbsolutePath().toString()));
        }

        if (StringUtils.isNotBlank(projectName)) {
            inspectorArguments.add(String.format("--projectname=%s", projectName));
        }

        return executableRunner.execute(sourceDirectory, pythonExe, inspectorArguments).getStandardOutputAsList();
    }

    private String getProjectName(final File directory, final File pythonExe, final File setupFile, final String providedProjectName) throws ExecutableRunnerException {
        String projectName = providedProjectName;

        if (StringUtils.isBlank(projectName) && setupFile != null && setupFile.exists()) {
            final List<String> pythonArguments = Arrays.asList(setupFile.getAbsolutePath(), "--name");
            final List<String> output = executableRunner.execute(directory, pythonExe, pythonArguments).getStandardOutputAsList();
            projectName = output.get(output.size() - 1).replace('_', '-').trim();
        }

        return projectName;
    }

}
