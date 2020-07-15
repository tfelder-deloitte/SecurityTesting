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
package com.synopsys.integration.detectable.detectables.npm.lockfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.NpmDependency;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.NpmProject;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.NpmRequires;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.PackageLock;
import com.synopsys.integration.detectable.detectables.npm.lockfile.model.PackageLockDependency;
import com.synopsys.integration.detectable.detectables.npm.packagejson.model.PackageJson;

public class NpmDependencyConverter {
    private final ExternalIdFactory externalIdFactory;

    public NpmDependencyConverter(final ExternalIdFactory externalIdFactory) {this.externalIdFactory = externalIdFactory;}

    public NpmProject convertLockFile(final PackageLock packageLock, final Optional<PackageJson> packageJsonOptional) {

        final NpmProject project = new NpmProject(packageLock.name, packageLock.version);

        if (packageLock.dependencies != null) {
            final List<NpmDependency> children = convertPackageMapToDependencies(null, packageLock.dependencies);
            project.addAllResolvedDependencies(children);
        }

        if (packageJsonOptional.isPresent()) {
            final PackageJson packageJson = packageJsonOptional.get();
            if (packageJson.dependencies != null) {
                final List<NpmRequires> rootRequires = convertNameVersionMapToRequires(packageJson.dependencies);
                project.addAllDependencies(rootRequires);
            }

            if (packageJson.devDependencies != null) {
                final List<NpmRequires> rootDevRequires = convertNameVersionMapToRequires(packageJson.devDependencies);
                project.addAllDevDependencies(rootDevRequires);
            }
        }

        return project;
    }

    public List<NpmDependency> convertPackageMapToDependencies(final NpmDependency parent, final Map<String, PackageLockDependency> packageLockDependencyMap) {
        final List<NpmDependency> children = new ArrayList<>();

        if (packageLockDependencyMap == null || packageLockDependencyMap.size() == 0) {
            return children;
        }

        for (final Map.Entry<String, PackageLockDependency> packageEntry : packageLockDependencyMap.entrySet()) {
            final String packageName = packageEntry.getKey();
            final PackageLockDependency packageLockDependency = packageEntry.getValue();

            final NpmDependency dependency = createNpmDependency(packageName, packageLockDependency.version, packageLockDependency.dev);
            dependency.setParent(parent);
            children.add(dependency);

            final List<NpmRequires> requires = convertNameVersionMapToRequires(packageLockDependency.requires);
            dependency.addAllRequires(requires);

            final List<NpmDependency> grandChildren = convertPackageMapToDependencies(dependency, packageLockDependency.dependencies);
            dependency.addAllDependencies(grandChildren);
        }
        return children;
    }

    private NpmDependency createNpmDependency(final String name, final String version, final Boolean isDev) {
        final ExternalId externalId = externalIdFactory.createNameVersionExternalId(Forge.NPMJS, name, version);
        final Dependency graphDependency = new Dependency(name, version, externalId);
        boolean dev = false;
        if (isDev != null && isDev) {
            dev = true;
        }
        return new NpmDependency(name, version, dev, graphDependency);

    }

    public List<NpmRequires> convertNameVersionMapToRequires(final Map<String, String> requires) {
        if (requires == null || requires.size() == 0) {
            return Collections.emptyList();
        }
        return requires.entrySet().stream()
                   .map(entry -> new NpmRequires(entry.getKey(), entry.getValue()))
                   .collect(Collectors.toList());
    }

}
