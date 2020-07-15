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
package com.synopsys.integration.detectable.detectables.cpan.functional;

import java.io.File;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.detectable.executable.ExecutableOutput;
import com.synopsys.integration.detectable.functional.DetectableFunctionalTest;
import com.synopsys.integration.detectable.util.graph.NameVersionGraphAssert;

public class CpanCliDetectableTest extends DetectableFunctionalTest {
    public CpanCliDetectableTest() throws IOException {
        super("cpan");
    }

    @Override
    protected void setup() throws IOException {
        addFile("Makefile.PL");

        final ExecutableOutput cpanListOutput = createStandardOutput(
            "ExtUtils::MakeMaker\t7.24",
            "perl\t5.1",
            "Test::More\t1.3"
        );
        addExecutableOutput(getOutputDirectory(), cpanListOutput, "cpan", "-l");

        final ExecutableOutput cpanmShowDepsOutput = createStandardOutput(
            "--> Working on .",
            "Configuring App-cpanminus-1.7043 ... OK",
            "ExtUtils::MakeMaker~6.58",
            "Test::More",
            "perl~5.008001",
            "ExtUtils::MakeMaker"
        );
        addExecutableOutput(getOutputDirectory(), cpanmShowDepsOutput, "cpanm", "--showdeps", ".");
    }

    @NotNull
    @Override
    public Detectable create(@NotNull final DetectableEnvironment detectableEnvironment) {
        return detectableFactory.createCpanCliDetectable(detectableEnvironment, () -> new File("cpan"), () -> new File("cpanm"));
    }

    @Override
    public void assertExtraction(@NotNull final Extraction extraction) {
        Assertions.assertEquals(1, extraction.getCodeLocations().size());

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.CPAN, extraction.getCodeLocations().get(0).getDependencyGraph());
        graphAssert.hasRootSize(3);
        graphAssert.hasRootDependency("Test-More", "1.3");
        graphAssert.hasRootDependency("ExtUtils-MakeMaker", "7.24");
        graphAssert.hasRootDependency("perl", "5.1");

    }
}
