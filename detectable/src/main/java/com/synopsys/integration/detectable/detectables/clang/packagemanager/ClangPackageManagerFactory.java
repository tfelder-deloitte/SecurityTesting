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
package com.synopsys.integration.detectable.detectables.clang.packagemanager;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.ApkArchitectureResolver;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.ApkPackageManagerResolver;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.DpkgPackageManagerResolver;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.DpkgVersionResolver;
import com.synopsys.integration.detectable.detectables.clang.packagemanager.resolver.RpmPackageManagerResolver;

public class ClangPackageManagerFactory {
    private final ClangPackageManagerInfoFactory packageManagerInfoFactory;

    public ClangPackageManagerFactory(final ClangPackageManagerInfoFactory packageManagerInfoFactory) {
        this.packageManagerInfoFactory = packageManagerInfoFactory;
    }

    public static ClangPackageManagerFactory standardFactory() {
        return new ClangPackageManagerFactory(ClangPackageManagerInfoFactory.standardFactory());
    }

    public List<ClangPackageManager> createPackageManagers() {
        final List<ClangPackageManager> packageManagers = new ArrayList<>();

        packageManagers.add(new ClangPackageManager(packageManagerInfoFactory.apk(), new ApkPackageManagerResolver(new ApkArchitectureResolver())));
        packageManagers.add(new ClangPackageManager(packageManagerInfoFactory.dpkg(), new DpkgPackageManagerResolver(new DpkgVersionResolver())));
        packageManagers.add(new ClangPackageManager(packageManagerInfoFactory.rpm(), new RpmPackageManagerResolver(new Gson())));

        return packageManagers;
    }

}