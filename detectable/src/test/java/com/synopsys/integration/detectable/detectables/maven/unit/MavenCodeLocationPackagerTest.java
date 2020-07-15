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
package com.synopsys.integration.detectable.detectables.maven.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.model.dependency.Dependency;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.annotations.UnitTest;
import com.synopsys.integration.detectable.detectables.maven.cli.MavenCodeLocationPackager;
import com.synopsys.integration.detectable.detectables.maven.cli.ScopedDependency;

@UnitTest
public class MavenCodeLocationPackagerTest {
    @Test
    public void testParseProject() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(new ExternalIdFactory());

        Dependency dependency = mavenCodeLocationPackager.textToProject("stuff:things:jar:0.0.1");
        assertNotNull(dependency);

        dependency = mavenCodeLocationPackager.textToProject("stuff:things:jar:classifier:0.0.1");
        assertNotNull(dependency);

        dependency = mavenCodeLocationPackager.textToProject("stuff:things:jar");
        assertNull(dependency);

        dependency = mavenCodeLocationPackager.textToProject("stuff:things:jar:classifier:0.0.1:monkey");
        assertNull(dependency);
    }

    @Test
    public void testParseDependency() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(new ExternalIdFactory());

        ScopedDependency dependency = mavenCodeLocationPackager.textToDependency("stuff:things:jar:0.0.1:compile");
        assertNotNull(dependency);

        dependency = mavenCodeLocationPackager.textToDependency("stuff:things:jar:classifier:0.0.1:test");
        assertNotNull(dependency);

        dependency = mavenCodeLocationPackager.textToDependency("stuff:things:jar");
        assertNull(dependency);

        dependency = mavenCodeLocationPackager.textToDependency("stuff:things:jar:classifier:0.0.1");
        assertNotNull(dependency);
    }

    @Test
    public void testIsLineRelevant() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertTrue(mavenCodeLocationPackager.isLineRelevant("weird garbage 3525356 [thingsINFO 346534623465] stuff"));

        assertTrue(mavenCodeLocationPackager.isLineRelevant("[thingsINFO 346534623465]stuff"));

        assertTrue(mavenCodeLocationPackager.isLineRelevant("[thingsINFO]  stuff"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant(" [INFO]     "));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("weird garbage 3525356 [thingsINFO 346534623465]"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[thingsINFO 346534623465]"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[thingsINFO]"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant(" [INFO]"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant(" "));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[INFO] Downloaded"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[INFO] stuff and thingsDownloaded stuff and things"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[INFO] Downloading"));

        assertFalse(mavenCodeLocationPackager.isLineRelevant("[INFO] stuff and things Downloadingstuff and things"));
    }

    @Test
    public void testTrimLogLevel() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        String actualLine = "";
        final String expectedValue = "thing";

        actualLine = mavenCodeLocationPackager.trimLogLevel("weird garbage 3525356 [thingsINFO 346534623465]" + expectedValue);
        assertEquals(expectedValue, actualLine);

        actualLine = mavenCodeLocationPackager.trimLogLevel("[thingsINFO 346534623465]" + expectedValue);
        assertEquals(expectedValue, actualLine);

        actualLine = mavenCodeLocationPackager.trimLogLevel("[thingsINFO]" + expectedValue);
        assertEquals(expectedValue, actualLine);

        actualLine = mavenCodeLocationPackager.trimLogLevel(" [INFO] " + expectedValue);
        assertEquals(expectedValue, actualLine);
    }

    @Test
    public void testIsProjectSection() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertFalse(mavenCodeLocationPackager.isProjectSection(" "));

        assertFalse(mavenCodeLocationPackager.isProjectSection("       "));

        assertFalse(mavenCodeLocationPackager.isProjectSection("---maven-dependency-plugin:"));

        assertFalse(mavenCodeLocationPackager.isProjectSection("---maven-dependency-plugin:other      stuff"));

        assertFalse(mavenCodeLocationPackager.isProjectSection("maven-dependency-plugin:tree      stuff"));

        assertTrue(mavenCodeLocationPackager.isProjectSection("---maven-dependency-plugin:tree      stuff"));

        assertTrue(mavenCodeLocationPackager.isProjectSection("things --- stuff maven-dependency-plugin garbage:tree      stuff"));

        assertTrue(mavenCodeLocationPackager.isProjectSection("things --- stuff maven-dependency-plugin:tree      stuff"));

        assertTrue(mavenCodeLocationPackager.isProjectSection("---maven-dependency-plugin:tree"));

        assertTrue(mavenCodeLocationPackager.isProjectSection("      ---       maven-dependency-plugin      :       tree"));
    }

    @Test
    public void testIsDependencyTreeUpdates() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertTrue(mavenCodeLocationPackager.isDependencyTreeUpdates("artifact com.google.guava:guava:jar:15.0:compile checking for updates from"));

        assertTrue(mavenCodeLocationPackager.isDependencyTreeUpdates("         artifact       com.google.guava:guava:         checking for updates"));

        assertTrue(mavenCodeLocationPackager.isDependencyTreeUpdates("      checking for updates   artifact       com.google.guava:guava:      "));

        assertTrue(mavenCodeLocationPackager.isDependencyTreeUpdates("checking for updates"));

        assertFalse(mavenCodeLocationPackager.isDependencyTreeUpdates("com.google.guava:guava:jar:15.0:compile"));

        assertFalse(mavenCodeLocationPackager.isDependencyTreeUpdates("+- com.google.guava:guava:jar:15.0:compile"));

        assertFalse(mavenCodeLocationPackager.isDependencyTreeUpdates("|  \\- com.google.guava:guava:jar:15.0:compile"));
    }

    @Test
    public void testIsGav() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertFalse(mavenCodeLocationPackager.isGav(" "));

        assertFalse(mavenCodeLocationPackager.isGav("       "));

        assertFalse(mavenCodeLocationPackager.isGav("::::"));

        assertFalse(mavenCodeLocationPackager.isGav(" : : : : "));

        assertFalse(mavenCodeLocationPackager.isGav("group"));

        assertFalse(mavenCodeLocationPackager.isGav("group:artifact"));

        assertFalse(mavenCodeLocationPackager.isGav("group:artifact:version"));

        assertFalse(mavenCodeLocationPackager.isGav("group-artifact:type-classifier-version:scope-garbage"));

        assertFalse(mavenCodeLocationPackager.isGav("group:artifact::classifier:version: :garbage"));

        assertTrue(mavenCodeLocationPackager.isGav("group:artifact:type:version"));

        assertTrue(mavenCodeLocationPackager.isGav("group:artifact:type:classifier:version"));

        assertTrue(mavenCodeLocationPackager.isGav("group:artifact:type:classifier:version:scope"));

        assertTrue(mavenCodeLocationPackager.isGav("group:artifact:type:classifier:version:scope:garbage"));
    }

    @Test
    public void testIndexOfEndOfSegments() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertEquals(-1, mavenCodeLocationPackager.indexOfEndOfSegments(""));

        assertEquals(-1, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things"));

        assertEquals(-1, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "things", "and"));

        assertEquals(-1, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "things", "and", "stuff"));

        assertEquals(5, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "stuff"));

        assertEquals(9, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "stuff", "and"));

        assertEquals(16, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "stuff", "and", "things"));

        assertEquals(9, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "and"));

        assertEquals(16, mavenCodeLocationPackager.indexOfEndOfSegments("stuff and things", "things"));
    }

    @Test
    public void testDoesLineContainSegmentsInOrder() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(null);

        assertFalse(mavenCodeLocationPackager.doesLineContainSegmentsInOrder(""));

        assertFalse(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things"));

        assertFalse(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "things", "and"));

        assertFalse(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "things", "and", "stuff"));

        assertTrue(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "stuff"));

        assertTrue(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "stuff", "and"));

        assertTrue(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "stuff", "and", "things"));

        assertTrue(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "and"));

        assertTrue(mavenCodeLocationPackager.doesLineContainSegmentsInOrder("stuff and things", "things"));
    }

    @Test
    public void testLineWithExtraTextAfterScope() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(new ExternalIdFactory());

        String line = "[INFO] |  |  |  \\- org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:jar:3.8.0.v20160509-0411:compile (version selected from constraint [3.8.0,3.8.1))";
        line = mavenCodeLocationPackager.trimLogLevel(line);
        final String cleanedLine = mavenCodeLocationPackager.calculateCurrentLevelAndCleanLine(line);
        final Dependency dependency = mavenCodeLocationPackager.textToDependency(cleanedLine);
        assertEquals("org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:3.8.0.v20160509-0411", dependency.getExternalId().createExternalId());
    }

    @Test
    public void testLineWithUnknownScope() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(new ExternalIdFactory());

        String line = "[INFO] |  |  |  \\- org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:jar:3.8.0.v20160509-0411:pants (version selected from constraint [3.8.0,3.8.1))";
        line = mavenCodeLocationPackager.trimLogLevel(line);
        final String cleanedLine = mavenCodeLocationPackager.calculateCurrentLevelAndCleanLine(line);
        final ScopedDependency scopedDependency = mavenCodeLocationPackager.textToDependency(cleanedLine);
        assertEquals("org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:3.8.0.v20160509-0411", scopedDependency.getExternalId().createExternalId());
    }

    @Test
    public void testLineWithBadColonPlacement() {
        final MavenCodeLocationPackager mavenCodeLocationPackager = new MavenCodeLocationPackager(new ExternalIdFactory());

        String line = "[INFO] |  |  |  \\- org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:jar:3.8.0.v20160509-0411:pants (version selected from: [3.8.0,3.8.1))";
        line = mavenCodeLocationPackager.trimLogLevel(line);
        final String cleanedLine = mavenCodeLocationPackager.calculateCurrentLevelAndCleanLine(line);
        final Dependency dependency = mavenCodeLocationPackager.textToDependency(cleanedLine);
        assertEquals("org.eclipse.scout.sdk.deps:org.eclipse.core.jobs:pants (version selected from", dependency.getExternalId().createExternalId());
    }
}
