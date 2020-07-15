/**
 * configuration
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
package com.synopsys.integration.configuration.source;

import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import com.synopsys.integration.configuration.util.Bds;
import com.synopsys.integration.configuration.util.KeyUtils;

public class MapPropertySource implements PropertySource {
    private String givenName;
    private Map<String, String> normalizedPropertyMap;

    public MapPropertySource(final String givenName, final Map<String, String> underlyingMap) {
        this.givenName = givenName;
        this.normalizedPropertyMap = Bds.of(underlyingMap)
                                         .toMap(entry -> KeyUtils.normalizeKey(entry.getKey()), Map.Entry::getValue);
    }

    @Override
    @NotNull
    public Boolean hasKey(final String key) {
        return normalizedPropertyMap.containsKey(key);
    }

    @Override
    @Nullable
    public Set<String> getKeys() {
        return normalizedPropertyMap.keySet();
    }

    @Override
    @Nullable
    public String getValue(final String key) {
        return normalizedPropertyMap.getOrDefault(key, null);
    }

    @Override
    @NotNull
    public String getOrigin(final String key) {
        return givenName;
    }

    @Override
    @NotNull
    public String getName() {
        return givenName;
    }
}