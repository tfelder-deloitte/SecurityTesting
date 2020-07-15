/**
 * synopsys-detect
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
package com.synopsys.integration.detect.util.filter;

import java.util.Set;

import com.synopsys.integration.util.ExcludedIncludedWildcardFilter;

public class DetectOverrideableFilter extends ExcludedIncludedWildcardFilter implements DetectFilter {
    public DetectOverrideableFilter(final String toExclude, final String toInclude) {
        super(toExclude, toInclude);
    }

    @Override
    public boolean willExclude(final String itemName) {
        if (excludedSet.contains("ALL")) {
            return true;
        } else if (!excludedSet.contains("NONE") && excludedSet.contains(itemName)) {
            return true;
        } else {
            return super.willExclude(itemName);
        }
    }

    @Override
    public boolean willInclude(final String itemName) {
        if (!includedSet.isEmpty()) {
            if (includedSet.contains("ALL")) {
                return true;
            } else if (includedSet.contains("NONE")) {
                return false;
            } else {
                return includedSet.contains(itemName);
            }
        }

        return super.willInclude(itemName);
    }

    // TODO - edit ExcludedIncludedFilter to have getters for includedSet, excludedSet
    public Set<String> getIncludedSet() {
        return includedSet;
    }
}
