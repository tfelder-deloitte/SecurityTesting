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
package com.synopsys.integration.detectable.detectables.maven.functional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.detectable.exception.DetectableException;
import com.synopsys.integration.detectable.detectable.executable.ExecutableOutput;
import com.synopsys.integration.detectable.detectable.executable.resolver.MavenResolver;
import com.synopsys.integration.detectable.detectables.maven.cli.MavenCliExtractorOptions;
import com.synopsys.integration.detectable.functional.DetectableFunctionalTest;
import com.synopsys.integration.detectable.util.graph.NameVersionGraphAssert;

public class MavenPomDetectableTest extends DetectableFunctionalTest {
    private static final String POM_FILENAME = "pom.xml";

    public MavenPomDetectableTest() throws IOException {
        super("mavenpom");
    }

    @Override
    protected void setup() throws IOException {
        addFile(Paths.get("pom.xml"));

        ExecutableOutput mavenDependencyTreeOutput = createStandardOutput(
            "7275 [main] [INFO] --- maven-dependency-plugin:2.10:tree (default-cli) @ hub-teamcity-common ---",
            "7450 [main] [INFO] com.blackducksoftware.integration:hub-teamcity-common:jar:3.2.0-SNAPSHOT",
            "7509 [main] [INFO] +- com.blackducksoftware.integration:hub-common:jar:13.1.2:compile",
            "7560 [main] [INFO] |  +- com.blackducksoftware.integration:hub-common-rest:jar:2.1.3:compile",
            "7638 [main] [INFO] |  |  +- com.blackducksoftware.integration:integration-common:jar:6.0.2:compile",
            "9623 [main] [INFO] +- junit:junit:jar:4.12:test",
            "9868 [main] [INFO] |  \\- org.hamcrest:hamcrest-core:jar:1.3:test",
            "10331 [main] [INFO] +- org.powermock:powermock-api-mockito:jar:1.6.6:test",
            "10424 [main] [INFO] |  +- org.mockito:mockito-core:jar:1.10.19:test",
            "10849 [main] [INFO] |  \\- org.powermock:powermock-api-mockito-common:jar:1.6.6:test",
            "11063 [main] [INFO] |     \\- org.powermock:powermock-api-support:jar:1.6.6:test"

        );
        addExecutableOutput(mavenDependencyTreeOutput, "maven", "test", "dependency:tree", "-T1");
    }

    @NotNull
    @Override
    public Detectable create(@NotNull final DetectableEnvironment detectableEnvironment) {
        class MavenPomResolverTest implements MavenResolver {

            @Override
            public File resolveMaven(final DetectableEnvironment environment) throws DetectableException {
                return new File("maven");
            }
        }
        return detectableFactory.createMavenPomDetectable(detectableEnvironment, new MavenPomResolverTest(), new MavenCliExtractorOptions("test", "", "", "", ""));
    }

    @Override
    public void assertExtraction(@NotNull final Extraction extraction) {
        Assertions.assertEquals(1, extraction.getCodeLocations().size(), "A code location should have been generated.");

        final NameVersionGraphAssert graphAssert = new NameVersionGraphAssert(Forge.MAVEN, extraction.getCodeLocations().get(0).getDependencyGraph());
        graphAssert.hasRootSize(3);

        // ExternalIdFactory sets group for Maven external Ids
        ExternalIdFactory externalIdFactory = new ExternalIdFactory();
        ExternalId junit = externalIdFactory.createMavenExternalId("junit", "junit", "4.12");
        ExternalId powermockApiMockito = externalIdFactory.createMavenExternalId("org.powermock", "powermock-api-mockito", "1.6.6");
        ExternalId hubCommon = externalIdFactory.createMavenExternalId("com.blackducksoftware.integration", "hub-common", "13.1.2");
        ExternalId powermockApiMockitoCommon = externalIdFactory.createMavenExternalId("org.powermock", "powermock-api-mockito-common", "1.6.6");
        ExternalId powermockApiSupport = externalIdFactory.createMavenExternalId("org.powermock", "powermock-api-support", "1.6.6");
        ExternalId hamcrestCore = externalIdFactory.createMavenExternalId("org.hamcrest", "hamcrest-core", "1.3");
        ExternalId hubCommonRest = externalIdFactory.createMavenExternalId("com.blackducksoftware.integration", "hub-common-rest", "2.1.3");
        ExternalId mockitoCore = externalIdFactory.createMavenExternalId("org.mockito", "mockito-core", "1.10.19");
        ExternalId integrationCommon = externalIdFactory.createMavenExternalId("com.blackducksoftware.integration", "integration-common", "6.0.2");

        graphAssert.hasRootDependency(junit);
        graphAssert.hasRootDependency(powermockApiMockito);
        graphAssert.hasRootDependency(hubCommon);

        graphAssert.hasParentChildRelationship(powermockApiMockitoCommon, powermockApiSupport);
        graphAssert.hasParentChildRelationship(junit, hamcrestCore);
        graphAssert.hasParentChildRelationship(powermockApiMockito, powermockApiMockitoCommon);
        graphAssert.hasParentChildRelationship(powermockApiMockito, mockitoCore);
        graphAssert.hasParentChildRelationship(hubCommonRest, integrationCommon);
        graphAssert.hasParentChildRelationship(hubCommon, hubCommonRest);
    }
}
