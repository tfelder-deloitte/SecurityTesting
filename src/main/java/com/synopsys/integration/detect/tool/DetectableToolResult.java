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
package com.synopsys.integration.detect.tool;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.antlr.v4.runtime.misc.Nullable;

import com.synopsys.integration.detect.workflow.codelocation.DetectCodeLocation;
import com.synopsys.integration.detect.workflow.project.DetectToolProjectInfo;
import com.synopsys.integration.detectable.detectable.result.DetectableResult;

public class DetectableToolResult {
    private enum DetectableToolResultType {
        SKIPPED,
        FAILED,
        SUCCESS
    }

    private final DetectableToolResultType resultType;
    private final List<DetectCodeLocation> detectCodeLocations;

    @Nullable
    private final File dockerTar;
    @Nullable
    private final DetectToolProjectInfo detectToolProjectInfo;
    @Nullable
    private final DetectableResult failedExtractableResult;

    public DetectableToolResult(final DetectableToolResultType resultType, final DetectToolProjectInfo detectToolProjectInfo, final List<DetectCodeLocation> detectCodeLocations, final File dockerTar,
        final DetectableResult failedExtractableResult) {
        this.resultType = resultType;
        this.detectToolProjectInfo = detectToolProjectInfo;
        this.detectCodeLocations = detectCodeLocations;
        this.dockerTar = dockerTar;
        this.failedExtractableResult = failedExtractableResult;
    }

    public static DetectableToolResult skip() {
        return new DetectableToolResult(DetectableToolResultType.SKIPPED, null, Collections.emptyList(), null, null);
    }

    //extractableResult is a workaround for docker. Technically we want to throw an exception when docker extractable fails for Windows but neither the tool nor the detectable supports that.
    //This allows the caller to make determinations based on the extractable result.
    public static DetectableToolResult failed(DetectableResult extractableResult) {
        return new DetectableToolResult(DetectableToolResultType.FAILED, null, Collections.emptyList(), null, extractableResult);
    }

    public static DetectableToolResult failed() {
        return failed(null);
    }

    public static DetectableToolResult success(final List<DetectCodeLocation> codeLocations, @Nullable final DetectToolProjectInfo projectInfo, @Nullable final File dockerTar) {
        return new DetectableToolResult(DetectableToolResultType.SUCCESS, projectInfo, codeLocations, dockerTar, null);
    }

    public Optional<File> getDockerTar() {
        return Optional.ofNullable(dockerTar);
    }

    public Optional<DetectToolProjectInfo> getDetectToolProjectInfo() {
        return Optional.ofNullable(detectToolProjectInfo);
    }

    public List<DetectCodeLocation> getDetectCodeLocations() {
        return detectCodeLocations;
    }

    public boolean isFailure() {
        return resultType == DetectableToolResultType.FAILED;
    }

    public Optional<DetectableResult> getFailedExtractableResult() {
        return Optional.ofNullable(failedExtractableResult);
    }
}
