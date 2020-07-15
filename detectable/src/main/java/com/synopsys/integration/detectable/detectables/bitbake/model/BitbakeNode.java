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
package com.synopsys.integration.detectable.detectables.bitbake.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BitbakeNode {
    private final String name;
    private String version = null;
    private final Set<String> children = new HashSet<>();

    public BitbakeNode(final String name) {this.name = name;}

    public void addChild(final String child) {
        this.children.add(child);
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    public Set<String> getChildren() {
        return children;
    }

}
