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
package com.synopsys.integration.detectable.detectables.pip.unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.annotations.UnitTest;
import com.synopsys.integration.detectable.detectables.pip.model.PipFreeze;
import com.synopsys.integration.detectable.detectables.pip.model.PipFreezeEntry;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvGraph;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvGraphDependency;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvGraphEntry;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvResult;
import com.synopsys.integration.detectable.detectables.pip.parser.PipenvTransformer;
import com.synopsys.integration.detectable.util.graph.NameVersionGraphAssert;

@UnitTest
public class PipenvTransformerTest {
    @Test
    void resolvesFuzzyVersion() {
        final List<PipFreezeEntry> pipFreezeEntries = new ArrayList<>();
        pipFreezeEntries.add(new PipFreezeEntry("example", "2.0.0"));
        final PipFreeze pipFreeze = new PipFreeze(pipFreezeEntries);

        final List<PipenvGraphEntry> pipenvGraphEntries = new ArrayList<>();
        pipenvGraphEntries.add(new PipenvGraphEntry("example", "fuzzy", new ArrayList<>()));
        final PipenvGraph pipenvGraph = new PipenvGraph(pipenvGraphEntries);

        final PipenvTransformer pipenvTransformer = new PipenvTransformer(new ExternalIdFactory());
        final PipenvResult result = pipenvTransformer.transform("", "", pipFreeze, pipenvGraph, false);
        final DependencyGraph graph = result.getCodeLocation().getDependencyGraph();

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.PYPI, graph);
        graphAssert.hasDependency("example", "2.0.0");
    }

    @Test
    void resolvesLowercaseNameWithFreezeCapital() {
        final List<PipFreezeEntry> pipFreezeEntries = new ArrayList<>();
        pipFreezeEntries.add(new PipFreezeEntry("Example", "2.0.0"));
        final PipFreeze pipFreeze = new PipFreeze(pipFreezeEntries);

        final List<PipenvGraphEntry> pipenvGraphEntries = new ArrayList<>();
        pipenvGraphEntries.add(new PipenvGraphEntry("example", "fuzzy", new ArrayList<>()));
        final PipenvGraph pipenvGraph = new PipenvGraph(pipenvGraphEntries);

        final PipenvTransformer pipenvTransformer = new PipenvTransformer(new ExternalIdFactory());
        final PipenvResult result = pipenvTransformer.transform("", "", pipFreeze, pipenvGraph, false);
        final DependencyGraph graph = result.getCodeLocation().getDependencyGraph();

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.PYPI, graph);
        graphAssert.hasDependency("Example", "2.0.0");
    }

    @Test
    void usesProjectDependencyAsRoot() {
        final PipFreeze pipFreeze = new PipFreeze(new ArrayList<>());

        final List<PipenvGraphEntry> pipenvGraphEntries = new ArrayList<>();
        final List<PipenvGraphDependency> children = new ArrayList<>();
        children.add(new PipenvGraphDependency("shouldBeAtRoot", "shouldbeAtRootVersion", new ArrayList<>()));
        pipenvGraphEntries.add(new PipenvGraphEntry("projectName", "projectVersion", children));
        final PipenvGraph pipenvGraph = new PipenvGraph(pipenvGraphEntries);

        final PipenvTransformer pipenvTransformer = new PipenvTransformer(new ExternalIdFactory());
        final PipenvResult result = pipenvTransformer.transform("projectName", "projectVersion", pipFreeze, pipenvGraph, false);
        final DependencyGraph graph = result.getCodeLocation().getDependencyGraph();

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.PYPI, graph);
        graphAssert.hasRootDependency("shouldBeAtRoot", "shouldbeAtRootVersion");
    }

    @Test
    void ignoresNonProject() {
        final PipFreeze pipFreeze = new PipFreeze(new ArrayList<>());

        final List<PipenvGraphEntry> pipenvGraphEntries = new ArrayList<>();
        pipenvGraphEntries.add(new PipenvGraphEntry("projectName", "projectVersion", Collections.singletonList(new PipenvGraphDependency("child", "childVersion", Collections.emptyList()))));
        pipenvGraphEntries.add(new PipenvGraphEntry("non-projectName", "non-projectVersion", new ArrayList<>()));
        final PipenvGraph pipenvGraph = new PipenvGraph(pipenvGraphEntries);

        final PipenvTransformer pipenvTransformer = new PipenvTransformer(new ExternalIdFactory());
        final PipenvResult result = pipenvTransformer.transform("projectName", "projectVersion", pipFreeze, pipenvGraph, true);
        final DependencyGraph graph = result.getCodeLocation().getDependencyGraph();

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.PYPI, graph);
        graphAssert.hasRootDependency("child", "childVersion");
        graphAssert.hasNoDependency("non-projectName", "non-projectVersion");
    }
}
