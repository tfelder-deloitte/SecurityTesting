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
package com.synopsys.integration.detectable.detectables.git.parsing;

import java.io.File;

import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.ExtractionEnvironment;
import com.synopsys.integration.detectable.detectable.annotation.DetectableInfo;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectable.result.DetectableResult;
import com.synopsys.integration.detectable.detectable.result.FileNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.PassedDetectableResult;

@DetectableInfo(language = "various", forge = "N/A", requirementsMarkdown = "Files: .git/config, .git/HEAD.")
public class GitParseDetectable extends Detectable {
    private static final String GIT_DIRECTORY_NAME = ".git";
    private static final String GIT_CONFIG_FILENAME = "config";
    private static final String GIT_HEAD_FILENAME = "HEAD";

    private final FileFinder fileFinder;
    private final GitParseExtractor gitParseExtractor;

    private File gitConfigFile;
    private File gitHeadFile;

    public GitParseDetectable(final DetectableEnvironment environment, final FileFinder fileFinder, final GitParseExtractor gitParseExtractor) {
        super(environment);
        this.fileFinder = fileFinder;
        this.gitParseExtractor = gitParseExtractor;
    }

    @Override
    public DetectableResult applicable() {
        final File gitDirectory = fileFinder.findFile(environment.getDirectory(), GIT_DIRECTORY_NAME);
        if (gitDirectory == null) {
            return new FileNotFoundDetectableResult(GIT_DIRECTORY_NAME);
        }

        gitConfigFile = fileFinder.findFile(gitDirectory, GIT_CONFIG_FILENAME);
        gitHeadFile = fileFinder.findFile(gitDirectory, GIT_HEAD_FILENAME);

        if (gitConfigFile == null) {
            return new FileNotFoundDetectableResult(GIT_CONFIG_FILENAME);
        } else if (gitHeadFile == null) {
            return new FileNotFoundDetectableResult(GIT_HEAD_FILENAME);
        }

        return new PassedDetectableResult();
    }

    @Override
    public DetectableResult extractable() {
        return new PassedDetectableResult();
    }

    @Override
    public Extraction extract(final ExtractionEnvironment extractionEnvironment) {
        return gitParseExtractor.extract(gitConfigFile, gitHeadFile);
    }

}
