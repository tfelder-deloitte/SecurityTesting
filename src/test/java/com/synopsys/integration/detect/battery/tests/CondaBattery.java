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
package com.synopsys.integration.detect.battery.tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.synopsys.integration.detect.battery.BatteryTest;
import com.synopsys.integration.detect.configuration.DetectProperties;

@Tag("battery")
public class CondaBattery {
    @Test
    void lock() {
        final BatteryTest test = new BatteryTest("conda-list");
        test.sourceDirectoryNamed("linux-conda");
        test.sourceFileNamed("environment.yml");
        test.sourceFileNamed("setup.py");
        test.executableFromResourceFiles(DetectProperties.Companion.getDETECT_CONDA_PATH(), "conda-list.xout", "conda-info.xout");
        test.executableFromResourceFiles(DetectProperties.Companion.getDETECT_PYTHON_PATH(), "python-setup.xout", "python-inspector.xout");
        test.expectBdioResources();
        test.run();
    }
}

