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
package com.synopsys.integration.detect.workflow.report.output;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FormattedOutput {
    @SerializedName("formatVersion")
    public String formatVersion = "";

    @SerializedName("detectVersion")
    public String detectVersion = "";

    @SerializedName("projectName")
    public String projectName = "";

    @SerializedName("projectVersion")
    public String projectVersion = "";

    @SerializedName("detectors")
    public List<FormattedDetectorOutput> detectors = new ArrayList<>();

    @SerializedName("status")
    public List<FormattedStatusOutput> status = new ArrayList<>();

    @SerializedName("issues")
    public List<FormattedIssueOutput> issues = new ArrayList<>();

    @SerializedName("results")
    public List<FormattedResultOutput> results = new ArrayList<>();

    @SerializedName("codeLocations")
    public List<FormattedCodeLocationOutput> codeLocations = new ArrayList<>();

}

