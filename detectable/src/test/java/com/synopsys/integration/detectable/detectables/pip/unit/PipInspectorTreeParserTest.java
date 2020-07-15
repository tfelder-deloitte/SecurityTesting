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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.annotations.UnitTest;
import com.synopsys.integration.detectable.detectables.pip.model.PipenvResult;
import com.synopsys.integration.detectable.detectables.pip.parser.PipInspectorTreeParser;
import com.synopsys.integration.detectable.util.graph.NameVersionGraphAssert;

@UnitTest
public class PipInspectorTreeParserTest {
    private PipInspectorTreeParser parser;

    @BeforeEach
    public void init() {
        parser = new PipInspectorTreeParser(new ExternalIdFactory());
    }

    @Test
    public void validTest() {
        final List<String> pipInspectorOutput = Arrays.asList(
            "projectName==projectVersionName",
            "   with-dashes==1.0.0",
            "   Uppercase==2.0.0",
            "      child==3.0.0",
            "   test==4.0.0"
        );

        final Optional<PipenvResult> validParse = parser.parse(pipInspectorOutput, "");
        Assertions.assertTrue(validParse.isPresent());
        Assertions.assertEquals("projectName", validParse.get().getProjectName());
        Assertions.assertEquals("projectVersionName", validParse.get().getProjectVersion());

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.PYPI, validParse.get().getCodeLocation().getDependencyGraph());
        graphAssert.hasRootDependency("with-dashes", "1.0.0");
        graphAssert.hasRootDependency("Uppercase", "2.0.0");
        graphAssert.hasRootDependency("test", "4.0.0");
        graphAssert.hasParentChildRelationship("Uppercase", "2.0.0", "child", "3.0.0");

        graphAssert.hasRootSize(3);
    }

    @Test
    public void invalidParseTest() {
        final List<String> invalidText = new ArrayList<>();
        invalidText.add("i am not a valid file");
        invalidText.add("the status should be optional.empty()");
        final Optional<PipenvResult> invalidParse = parser.parse(invalidText, "");
        Assertions.assertFalse(invalidParse.isPresent());
    }

    @Test
    public void errorTest() {
        final List<String> invalidText = new ArrayList<>();
        invalidText.add(PipInspectorTreeParser.UNKNOWN_PACKAGE_PREFIX + "probably_an_internal_dependency_PY");
        invalidText.add(PipInspectorTreeParser.UNPARSEABLE_REQUIREMENTS_PREFIX + "/not/a/real/path/encrypted/requirements.txt");
        invalidText.add(PipInspectorTreeParser.UNKNOWN_REQUIREMENTS_PREFIX + "/not/a/real/path/requirements.txt");
        final Optional<PipenvResult> invalidParse = parser.parse(invalidText, "");
        Assertions.assertFalse(invalidParse.isPresent());
    }
}
