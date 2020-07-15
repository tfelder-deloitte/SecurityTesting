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
package com.synopsys.integration.detectable.detectables.bazel.functional.bazel.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.synopsys.integration.detectable.detectables.bazel.WorkspaceRule;
import com.synopsys.integration.detectable.detectables.bazel.pipeline.Pipelines;
import com.synopsys.integration.detectable.detectables.bazel.pipeline.stepexecutor.BazelCommandExecutor;
import com.synopsys.integration.detectable.detectables.bazel.pipeline.stepexecutor.BazelVariableSubstitutor;
import com.synopsys.integration.detectable.detectables.bazel.pipeline.stepexecutor.StepExecutor;
import com.synopsys.integration.exception.IntegrationException;

public class PipelineTest {
    private static final List<String> STANDARD_BAZEL_COMMAND_ARGS = Arrays.asList("cquery", "--noimplicit_deps", "kind(j.*import, deps(/:testTarget))", "--output", "build");
    private static final String CQUERY_OUTPUT_SIMPLE = createStandardOutput(
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:41:1",
        "jvm_import(",
        "  name = \"com_google_guava_guava\",",
        "  tags = [\"maven_coordinates=com.google.guava:guava:27.0-jre\"], ",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/com/google/guava/guava/27.0-jre/guava-27.0-jre.jar\"],",
        "  deps = [\"@exclusion_testing//:com_google_guava_listenablefuture\", \"@exclusion_testing//:com_google_code_findbugs_jsr305\", \"@exclusion_testing//:com_google_guava_failureaccess\", \"@exclusion_testing//:com_google_errorprone_error_prone_annotations\", \"@exclusion_testing//:org_checkerframework_checker_qual\"],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/maven/BUILD:103:1",
        "jvm_import(",
        "  name = \"org_hamcrest_hamcrest_core\",",
        "  tags = [\"maven_coordinates=org.hamcrest:hamcrest-core:2.1\"],  ",
        "  jars = [\"@maven//:v1/https/jcenter.bintray.com/org/hamcrest/hamcrest-core/2.1/hamcrest-core-2.1.jar\"],",
        "  deps = [\"@maven//:org_hamcrest_hamcrest\"],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/maven/BUILD:115:1",
        "jvm_import(",
        "  name = \"org_hamcrest_hamcrest\",",
        "  tags = [\"maven_coordinates=org.hamcrest:hamcrest:2.1\"], ",
        "  jars = [\"@maven//:v1/https/jcenter.bintray.com/org/hamcrest/hamcrest/2.1/hamcrest-2.1.jar\"],",
        "  deps = [],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:30:1",
        "jvm_import(",
        "  name = \"com_google_guava_failureaccess\",",
        "  tags = [\"maven_coordinates=com.google.guava:failureaccess:1.0\"],",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/com/google/guava/failureaccess/1.0/failureaccess-1.0.jar\"],",
        "  deps = [],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:68:1",
        "jvm_import(",
        "  name = \"org_checkerframework_checker_qual\",",
        "  tags = [\"maven_coordinates=org.checkerframework:checker-qual:2.5.2\"],",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/org/checkerframework/checker-qual/2.5.2/checker-qual-2.5.2.jar\"],",
        "  deps = [],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:19:1",
        "jvm_import(",
        "  name = \"com_google_errorprone_error_prone_annotations\", ",
        "  tags = [\"maven_coordinates=com.google.errorprone:error_prone_annotations:2.2.0\"],",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/com/google/errorprone/error_prone_annotations/2.2.0/error_prone_annotations-2.2.0.jar\"],",
        "  deps = [],",
        ")",
        "",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:57:1",
        "jvm_import(",
        "  name = \"com_google_guava_listenablefuture\",",
        "  tags = [\"maven_coordinates=com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava\"],",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar\"],",
        "  deps = [],",
        ")",
        "# /root/.cache/bazel/_bazel_root/896e039de1e50d7e2de0b14a9acf4028/external/exclusion_testing/BUILD:8:1",
        "jvm_import(",
        "  name = \"com_google_code_findbugs_jsr305\",",
        "  tags = [\"maven_coordinates=com.google.code.findbugs:jsr305:3.0.2\"],",
        "  jars = [\"@exclusion_testing//:v1/https/repo1.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar\"],",
        "  deps = [],",
        ")");

    private static final String CQUERY_OUTPUT_MIXED_TAGS = createStandardOutput(
        "java_import(",
        "  name = \"sso-adminsdk\",",
        "  tags = [\"__SOME_OTHER_TAG__\"],",
        "  generator_name = \"sso-adminsdk\",",
        "  generator_function = \"java_import\",",
        "  generator_location = \"/home/dail/test/b-cls-bazel/2e7559aa3afc160ade8ae4cb99d56da9/external/rd_platform_services/BUILD.bazel:238\",",
        "  jars = [\"@rd_platform_services//:lin64/jars/sso-adminsdk.jar\"],",
        ")",
        "",
        "# /home/dail/test/b-cls-bazel/2e7559aa3afc160ade8ae4cb99d56da9/external/thing_cis/BUILD.bazel:20:5",
        "java_import(",
        "  name = \"thing-common-client\",",
        "  tags = [\"__SOME_OTHER_TAG__\", \"maven_coordinates=com.company.thing:thing-common-client:2.100.0\"],",
        "  generator_name = \"thing-common-client\",",
        "  generator_function = \"java_import\",",
        "  generator_location = \"/home/dail/test/b-cls-bazel/2e7559aa3afc160ade8ae4cb99d56da9/external/thing_cis/BUILD.bazel:20\",",
        "  jars = [\"@thing_cis//:java-toolkit/runtime/thing-common-client-2.100.0.jar\"],",
        "  srcjar = \"@thing_cis//:java-toolkit/runtime/thing-common-client-2.100.0-sources.jar\",",
        ")",
        "",
        "# /home/dail/test/b-cls-bazel/2e7559aa3afc160ade8ae4cb99d56da9/external/cls_maven_stubs/BUILD:41:1",
        "jvm_import(",
        "  name = \"javax_servlet_javax_servlet_api\",",
        "  tags = [\"maven_coordinates=javax.servlet:javax.servlet-api:3.0.1\"],",
        "  jars = [\"@cls_maven_stubs//:v1/https/build-artifactory.eng.company.com/artifactory/maven/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1.jar\"],",
        "  srcjar = \"@cls_maven_stubs//:v1/https/build-artifactory.eng.company.com/artifactory/maven/javax/servlet/javax.servlet-api/3.0.1/javax.servlet-api-3.0.1-sources.jar\",",
        "  deps = [],",
        "  neverlink = True,",
        ")");

    private static final String CQUERY_OUTPUT_MIXED_TAGS_REVERSED_ORDER = createStandardOutput(
        "java_import(",
        "  name = \"thing-common-client\",",
        "  tags = [\"maven_coordinates=com.company.thing:thing-common-client:2.100.0\", \"__SOME_OTHER_TAG__\"],",
        "  generator_name = \"thing-common-client\",",
        "  generator_function = \"java_import\",",
        "  generator_location = \"/home/dail/test/b-cls-bazel/2e7559aa3afc160ade8ae4cb99d56da9/external/thing_cis/BUILD.bazel:20\",",
        "  jars = [\"@thing_cis//:java-toolkit/runtime/thing-common-client-2.100.0.jar\"],",
        "  srcjar = \"@thing_cis//:java-toolkit/runtime/thing-common-client-2.100.0-sources.jar\",",
        ")");

    @Test
    public void testMavenInstall() throws IntegrationException {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS);

        final List<String> userProvidedCqueryAdditionalOptions = null;

        final List<String> results = doTest(STANDARD_BAZEL_COMMAND_ARGS, userProvidedCqueryAdditionalOptions, CQUERY_OUTPUT_SIMPLE);
        assertEquals(8, results.size());
        assertEquals("com.google.guava:guava:27.0-jre", results.get(0));
        assertEquals("com.google.code.findbugs:jsr305:3.0.2", results.get(7));
    }

    @Test
    public void testMavenInstallMixedTags() throws IntegrationException {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS);

        final List<String> userProvidedCqueryAdditionalOptions = null;

        final List<String> results = doTest(STANDARD_BAZEL_COMMAND_ARGS, userProvidedCqueryAdditionalOptions, CQUERY_OUTPUT_MIXED_TAGS);
        assertEquals(2, results.size());
        assertEquals("com.company.thing:thing-common-client:2.100.0", results.get(0));
        assertEquals("javax.servlet:javax.servlet-api:3.0.1", results.get(1));
    }

    @Test
    public void testMavenInstallMixedTagsReversedOrder() throws IntegrationException {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS);

        final List<String> userProvidedCqueryAdditionalOptions = null;

        final List<String> results = doTest(STANDARD_BAZEL_COMMAND_ARGS, userProvidedCqueryAdditionalOptions, CQUERY_OUTPUT_MIXED_TAGS_REVERSED_ORDER);
        assertEquals(1, results.size());
        assertEquals("com.company.thing:thing-common-client:2.100.0", results.get(0));
    }

    @Test
    public void testMavenInstallCqueryAdditionalOptions() throws IntegrationException {
        Assumptions.assumeFalse(SystemUtils.IS_OS_WINDOWS);

        final List<String> userProvidedCqueryAdditionalOptions = Arrays.asList("--option1=a", "--option2=b");
        final List<String> expectedBazelCommandArgs = Arrays.asList("cquery", "--noimplicit_deps", "--option1=a", "--option2=b", "kind(j.*import, deps(/:testTarget))", "--output", "build");

        List<String> results = doTest(expectedBazelCommandArgs, userProvidedCqueryAdditionalOptions, CQUERY_OUTPUT_SIMPLE);
        assertEquals(8, results.size());
        assertEquals("com.google.guava:guava:27.0-jre", results.get(0));
        assertEquals("com.google.code.findbugs:jsr305:3.0.2", results.get(7));
    }

    private List<String> doTest(final List<String> expectedBazelCommandArgs, final List<String> userProvidedCqueryAdditionalOptions, final String input) throws IntegrationException {
        final BazelCommandExecutor bazelCommandExecutor = Mockito.mock(BazelCommandExecutor.class);
        Mockito.when(bazelCommandExecutor.executeToString(expectedBazelCommandArgs)).thenReturn(Optional.of(input));
        final BazelVariableSubstitutor bazelVariableSubstitutor = new BazelVariableSubstitutor("/:testTarget", userProvidedCqueryAdditionalOptions);

        final Pipelines pipelines = new Pipelines(bazelCommandExecutor, bazelVariableSubstitutor);
        System.out.printf("# maven_install steps: %d\n", pipelines.get(WorkspaceRule.MAVEN_INSTALL).size());
        List<String> intermediateResult = new ArrayList<>();
        for (final StepExecutor stepExecutor : pipelines.get(WorkspaceRule.MAVEN_INSTALL)) {
            intermediateResult = stepExecutor.process(intermediateResult);
        }
        return intermediateResult;
    }

    private static String createStandardOutput(final String... outputLines) {
        return String.join(System.lineSeparator(), outputLines);
    }
}
