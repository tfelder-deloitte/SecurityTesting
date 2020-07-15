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
package com.synopsys.integration.detectable.detectables.clang;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.ExtractionEnvironment;
import com.synopsys.integration.detectable.detectable.annotation.DetectableInfo;
import com.synopsys.integration.detectable.detectable.exception.DetectableException;
import com.synopsys.integration.detectable.detectable.executable.ExecutableRunner;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectable.result.DetectableResult;
import com.synopsys.integration.detectable.detectable.result.ExecutableNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.FileNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.PassedDetectableResult;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.ClangPackageManager;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.ClangPackageManagerRunner;

@DetectableInfo(language = "C or C++", forge = "Derived from the Linux distribution.", requirementsMarkdown = "File: compile_commands.json. <br /><br /> Executable: Linux package manager.")
public class ClangDetectable extends Detectable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String JSON_COMPILATION_DATABASE_FILENAME = "compile_commands.json";
    private final ClangExtractor clangExtractor;
    private final ClangDetectableOptions options;
    private File jsonCompilationDatabaseFile = null;
    private final FileFinder fileFinder;
    private final ExecutableRunner executableRunner;
    private final List<ClangPackageManager> availablePackageManagers;
    private final ClangPackageManagerRunner packageManagerRunner;

    private ClangPackageManager selectedPackageManager;

    public ClangDetectable(final DetectableEnvironment environment, final ExecutableRunner executableRunner, final FileFinder fileFinder, final List<ClangPackageManager> availablePackageManagers, final ClangExtractor clangExtractor,
        final ClangDetectableOptions options, final ClangPackageManagerRunner packageManagerRunner) {
        super(environment);
        this.fileFinder = fileFinder;
        this.availablePackageManagers = availablePackageManagers;
        this.executableRunner = executableRunner;
        this.clangExtractor = clangExtractor;
        this.options = options;
        this.packageManagerRunner = packageManagerRunner;
    }

    @Override
    public DetectableResult applicable() {
        jsonCompilationDatabaseFile = fileFinder.findFile(environment.getDirectory(), JSON_COMPILATION_DATABASE_FILENAME);
        if (jsonCompilationDatabaseFile == null) {
            return new FileNotFoundDetectableResult(JSON_COMPILATION_DATABASE_FILENAME);
        }
        return new PassedDetectableResult();
    }

    @Override
    public DetectableResult extractable() throws DetectableException {
        selectedPackageManager = findPkgMgr(environment.getDirectory());
        if (selectedPackageManager == null) {
            logger.warn("Unable to execute any supported package manager; Please make sure that one of the supported clang package managers is on the PATH");
            return new ExecutableNotFoundDetectableResult("supported Linux package manager");
        }
        return new PassedDetectableResult();
    }

    @Override
    public Extraction extract(final ExtractionEnvironment extractionEnvironment) {
        //addRelevantDiagnosticFile(jsonCompilationDatabaseFile);
        return clangExtractor.extract(selectedPackageManager, packageManagerRunner, environment.getDirectory(), extractionEnvironment.getOutputDirectory(), jsonCompilationDatabaseFile, options.isCleanup());
    }

    private ClangPackageManager findPkgMgr(final File workingDirectory) {
        for (final ClangPackageManager pkgMgrCandidate : availablePackageManagers) {
            if (packageManagerRunner.applies(pkgMgrCandidate, workingDirectory, executableRunner)) {
                return pkgMgrCandidate;
            }
        }
        return null;
    }
}
