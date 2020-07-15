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
package com.synopsys.integration.detectable.detectables.gradle.functional;

import java.io.File;
import java.util.Optional;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.annotations.UnitTest;
import com.synopsys.integration.detectable.detectable.codelocation.CodeLocation;
import com.synopsys.integration.detectable.detectables.gradle.inspection.model.GradleReport;
import com.synopsys.integration.detectable.detectables.gradle.inspection.parse.GradleReportParser;
import com.synopsys.integration.detectable.detectables.gradle.inspection.parse.GradleReportTransformer;
import com.synopsys.integration.detectable.util.FunctionalTestFiles;
import com.synopsys.integration.detectable.util.graph.MavenGraphAssert;

@UnitTest
public class GradleReportParserFunctionalTest {

    @Test
    void extractCodeLocationTest() {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS); //Does not work on windows due to path issues.

        final GradleReportParser gradleReportParser = new GradleReportParser();
        final Optional<GradleReport> gradleReport = gradleReportParser.parseReport(FunctionalTestFiles.asFile("/gradle/dependencyGraph.txt"));
        Assertions.assertTrue(gradleReport.isPresent());
        final GradleReportTransformer transformer = new GradleReportTransformer(new ExternalIdFactory());
        final CodeLocation codeLocation = transformer.transform(gradleReport.get());
        Assertions.assertNotNull(codeLocation);

        Assertions.assertEquals("hub-detect", gradleReport.get().getProjectName());
        Assertions.assertEquals("2.0.0-SNAPSHOT", gradleReport.get().getProjectVersionName());

        final String actual = new Gson().toJson(codeLocation);

        try {
            JSONAssert.assertEquals(FunctionalTestFiles.asString("/gradle/dependencyGraph-expected.json"), actual, false);
        } catch (final JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void complexTest() {
        final Optional<CodeLocation> codeLocation = buildCodeLocation("/gradle/parse-tests/complex_dependencyGraph.txt");
        Assertions.assertTrue(codeLocation.isPresent());
        final DependencyGraph graph = codeLocation.get().getDependencyGraph();

        final MavenGraphAssert graphAssert = new MavenGraphAssert(graph);
        graphAssert.hasDependency("non-project:with-nested:1.0.0");
        graphAssert.hasDependency("solo:component:4.12");
        graphAssert.hasDependency("some.group:child:2.2.2");
        graphAssert.hasDependency("terminal:child:6.2.3");

        // TODO: Get this to work in a sensible way
        //        graphAssert.noDependency("child-project");
        //        graphAssert.noDependency("nested-parent");
        //        graphAssert.noDependency("spring-webflux");
        //        graphAssert.noDependency("spring-beans");
        //        graphAssert.noDependency("spring-core");
        //        graphAssert.noDependency("spring-web");
        //        graphAssert.noDependency("should-suppress");

        graphAssert.hasRootDependency("solo:component:4.12");
        graphAssert.hasRootDependency("non-project:with-nested:1.0.0");
        graphAssert.hasRootDependency("some.group:parent:5.0.0");
        graphAssert.hasRootDependency("terminal:child:6.2.3");

        final ExternalId parent = graphAssert.hasDependency("some.group:parent:5.0.0");
        final ExternalId child = graphAssert.hasDependency("some.group:child:2.2.2");
        graphAssert.hasParentChildRelationship(parent, child);
    }

    private Optional<CodeLocation> buildCodeLocation(final String resource) {
        final File file = FunctionalTestFiles.asFile(resource);
        final GradleReportParser gradleReportParser = new GradleReportParser();
        final GradleReportTransformer gradleReportTransformer = new GradleReportTransformer(new ExternalIdFactory());

        return gradleReportParser.parseReport(file)
                   .map(gradleReportTransformer::transform);
    }

    @Test
    void testImplementationsGraph() {
        final Optional<CodeLocation> codeLocation = buildCodeLocation("/gradle/gradle_implementations_dependencyGraph.txt");
        Assertions.assertTrue(codeLocation.isPresent());
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(codeLocation.get()));
    }
}
