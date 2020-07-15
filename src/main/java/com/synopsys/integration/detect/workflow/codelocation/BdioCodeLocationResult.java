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
package com.synopsys.integration.detect.workflow.codelocation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.synopsys.integration.detector.base.DetectorType;

public class BdioCodeLocationResult {
    private final List<BdioCodeLocation> bdioCodeLocations;
    private final Map<DetectCodeLocation, String> codeLocationNames;
    private final Set<DetectorType> failedBomToolGroups;

    public BdioCodeLocationResult(final List<BdioCodeLocation> bdioCodeLocations, final Set<DetectorType> failedBomToolGroups, final Map<DetectCodeLocation, String> codeLocationNames) {
        this.bdioCodeLocations = bdioCodeLocations;
        this.failedBomToolGroups = failedBomToolGroups;
        this.codeLocationNames = codeLocationNames;
    }

    public Map<DetectCodeLocation, String> getCodeLocationNames() {
        return codeLocationNames;
    }

    public List<BdioCodeLocation> getBdioCodeLocations() {
        return bdioCodeLocations;
    }

    public Set<DetectorType> getFailedBomToolGroupTypes() {
        return failedBomToolGroups;
    }
}
