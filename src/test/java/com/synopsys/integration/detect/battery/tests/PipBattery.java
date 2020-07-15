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
public class PipBattery {
    @Test
    void lock() {
        final BatteryTest test = new BatteryTest("pip-cli");
        test.sourceDirectoryNamed("linux-pip");
        test.sourceFileNamed("setup.py");
        test.executableFromResourceFiles(DetectProperties.Companion.getDETECT_PYTHON_PATH(), "pip-name.xout", "pip-inspector.xout");
        test.git("https://github.com/nvbn/thefuck.git", "master");
        test.expectBdioResources();
        test.run();
        //detect.pip.requirements.path = requirements.txt
    }

    @Test
    void pipenv_cli() {
        final BatteryTest test = new BatteryTest("pipenv-cli");
        test.sourceDirectoryNamed("pipenv-cli-django");
        test.sourceFileNamed("Pipfile.lock");
        test.sourceFileNamed("Pipfile");
        test.executable(DetectProperties.Companion.getDETECT_PYTHON_PATH(), "jpadilla/django-project-template", "");
        test.executableFromResourceFiles(DetectProperties.Companion.getDETECT_PIPENV_PATH(), "pip-freeze.xout", "pipenv-graph.xout");
        test.git("https://github.com/jpadilla/django-project-template.git", "master");
        test.expectBdioResources();
        test.run();
    }

    @Test
    void pipenv_cli_projectonly() {
        final BatteryTest test = new BatteryTest("pipenv-cli-projectonly");
        test.sourceDirectoryNamed("pipenv-cli-projectonly");
        test.sourceFileNamed("Pipfile.lock");
        test.sourceFileNamed("Pipfile");
        test.executable(DetectProperties.Companion.getDETECT_PYTHON_PATH(), "django-debug-toolbar", "2.0");
        test.executableFromResourceFiles(DetectProperties.Companion.getDETECT_PIPENV_PATH(), "pip-freeze.xout", "pipenv-graph.xout");
        test.property(DetectProperties.Companion.getDETECT_PIP_ONLY_PROJECT_TREE(), "true");
        test.property(DetectProperties.Companion.getDETECT_PIP_PROJECT_NAME(), "django-debug-toolbar");
        test.property(DetectProperties.Companion.getDETECT_PIP_PROJECT_VERSION_NAME(), "2.0");
        test.git("https://github.com/jpadilla/django-project-template.git", "master");
        test.expectBdioResources();
        test.run();
    }
}

