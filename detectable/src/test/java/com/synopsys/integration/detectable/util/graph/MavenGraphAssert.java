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
package com.synopsys.integration.detectable.util.graph;

import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalId;

public class MavenGraphAssert extends GraphAssert {

    public MavenGraphAssert(final DependencyGraph graph) {
        super(Forge.MAVEN, graph);
    }

    private ExternalId gavToExternalId(String gav) {
        String[] pieces = gav.split(":");
        ExternalId id = externalIdFactory.createMavenExternalId(pieces[0], pieces[1], pieces[2]);
        return id;
    }

    public ExternalId hasRootDependency(String gav) {
        return this.hasRootDependency(gavToExternalId(gav));
    }

    public ExternalId hasDependency(String gav) {
        return this.hasDependency(gavToExternalId(gav));
    }

    public ExternalId noDependency(String gav) {
        return this.hasNoDependency(gavToExternalId(gav));
    }
}
