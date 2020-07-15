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
package com.synopsys.integration.detectable.detectables.yarn;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.detectable.codelocation.CodeLocation;
import com.synopsys.integration.detectable.detectables.npm.packagejson.model.PackageJson;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLock;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLockParser;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnTransformer;

public class YarnLockExtractor {
    private final YarnLockParser yarnLockParser;
    private final YarnTransformer yarnTransformer;
    private final Gson gson;

    public YarnLockExtractor(final YarnLockParser yarnLockParser, final YarnTransformer yarnTransformer, final Gson gson) {
        this.yarnLockParser = yarnLockParser;
        this.yarnTransformer = yarnTransformer;
        this.gson = gson;
    }

    public Extraction extract(final File yarnLockFile, final File packageJsonFile, final YarnLockOptions yarnLockOptions) {
        try {
            final String packageJsonText = FileUtils.readFileToString(packageJsonFile, StandardCharsets.UTF_8);
            final PackageJson packageJson = gson.fromJson(packageJsonText, PackageJson.class);

            final List<String> yarnLockLines = FileUtils.readLines(yarnLockFile, StandardCharsets.UTF_8);
            final YarnLock yarnLock = yarnLockParser.parseYarnLock(yarnLockLines);

            final DependencyGraph dependencyGraph = yarnTransformer.transform(packageJson, yarnLock, yarnLockOptions.useProductionOnly());

            final CodeLocation detectCodeLocation = new CodeLocation(dependencyGraph);

            return new Extraction.Builder()
                       .projectName(packageJson.name)
                       .projectVersion(packageJson.version)
                       .success(detectCodeLocation)
                       .build();
        } catch (final Exception e) {
            return new Extraction.Builder().exception(e).build();
        }
    }

}
