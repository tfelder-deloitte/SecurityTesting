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
package com.synopsys.integration.detectable.detectable.inspector.nuget.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detectable.detectable.inspector.nuget.NugetInspectorOptions;

public class NugetInspectorArguments {
    private static final Logger logger = LoggerFactory.getLogger(NugetInspectorArguments.class);

    //At the time of righting, both inspectors (exe and dotnet) use the same arguments so a shared static method is provided.
    //If they diverge the options object protects the argument conversion so each inspector can convert as they see fit.
    public static List<String> fromInspectorOptions(final NugetInspectorOptions nugetInspectorOptions, final File sourcePath, final File outputDirectory) throws IOException {
        final List<String> options = new ArrayList<>(Arrays.asList(
            "--target_path=" + sourcePath.getCanonicalPath(),
            "--output_directory=" + outputDirectory.getCanonicalPath(),
            "--ignore_failure=" + nugetInspectorOptions.isIgnoreFailures()));

        nugetInspectorOptions.getExcludedModules()
            .ifPresent(arg -> options.add("--excluded_modules=" + arg));

        nugetInspectorOptions.getIncludedModules()
            .ifPresent(arg -> options.add("--included_modules=" + arg));

        final List<String> nugetPackagesRepo = nugetInspectorOptions.getPackagesRepoUrl();
        if (nugetPackagesRepo != null && nugetPackagesRepo.size() > 0) {
            final String packagesRepos = String.join(",", nugetPackagesRepo);
            options.add("--packages_repo_url=" + packagesRepos);
        }

        nugetInspectorOptions.getNugetConfigPath()
            .ifPresent(arg -> options.add("--nuget_config_path=" + arg.toString()));

        if (logger.isTraceEnabled()) {
            options.add("-v");
        }

        return options;
    }
}
