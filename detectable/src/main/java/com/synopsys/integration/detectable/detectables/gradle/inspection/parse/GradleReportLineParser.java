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
package com.synopsys.integration.detectable.detectables.gradle.inspection.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detectable.detectable.util.DetectableStringUtils;
import com.synopsys.integration.detectable.detectables.gradle.inspection.model.GradleTreeNode;

public class GradleReportLineParser {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String[] TREE_LEVEL_TERMINALS = new String[] { "+---", "\\---" };
    private static final String[] PROJECT_INDICATORS = new String[] { "--- project " };
    private static final String COMPONENT_PREFIX = "--- ";
    private static final String[] REMOVE_SUFFIXES = new String[] { " (*)", " (c)", " (n)" };
    private static final String WINNING_INDICATOR = " -> ";

    public GradleTreeNode parseLine(final String line) {
        final int level = parseTreeLevel(line);
        if (!line.contains(COMPONENT_PREFIX)) {
            return GradleTreeNode.newUnknown(level);
        } else if (StringUtils.containsAny(line, PROJECT_INDICATORS)) {
            return GradleTreeNode.newProject(level);
        } else {
            final List<String> gav = parseGav(line);
            if (gav.size() != 3) {
                logger.trace(String.format("The line can not be reasonably split in to the necessary parts: %s", line)); //All project lines: +--- org.springframework.boot:spring-boot-starter-activemq (n)
                return GradleTreeNode.newUnknown(level);
            } else {
                final String group = gav.get(0);
                final String artifact = gav.get(1);
                final String version = gav.get(2);
                return GradleTreeNode.newGav(level, artifact, version, group);
            }
        }
    }

    private String removeSuffixes(String line) {
        for (String suffix : REMOVE_SUFFIXES) {
            if (line.endsWith(suffix)) {
                final int lastSeenElsewhereIndex = line.lastIndexOf(suffix);
                line = line.substring(0, lastSeenElsewhereIndex);
            }
        }
        return line;
    }

    private List<String> parseGav(final String line) {
        String cleanedOutput = StringUtils.trimToEmpty(line);
        cleanedOutput = cleanedOutput.substring(cleanedOutput.indexOf(COMPONENT_PREFIX) + COMPONENT_PREFIX.length());

        cleanedOutput = removeSuffixes(cleanedOutput);

        // we might need to modify the returned list, so it needs to be an actual ArrayList
        List<String> gavPieces = new ArrayList<>(Arrays.asList(cleanedOutput.split(":")));
        if (cleanedOutput.contains(WINNING_INDICATOR)) {
            // WINNING_INDICATOR can point to an entire GAV not just a version
            final String winningSection = cleanedOutput.substring(cleanedOutput.indexOf(WINNING_INDICATOR) + WINNING_INDICATOR.length());
            if (winningSection.contains(":")) {
                gavPieces = Arrays.asList(winningSection.split(":"));
            } else {
                // the WINNING_INDICATOR is not always preceded by a : so if isn't, we need to clean up from the original split
                if (gavPieces.get(1).contains(WINNING_INDICATOR)) {
                    final String withoutWinningIndicator = gavPieces.get(1).substring(0, gavPieces.get(1).indexOf(WINNING_INDICATOR));
                    gavPieces.set(1, withoutWinningIndicator);
                    // since there was no : we don't have a gav piece for version yet
                    gavPieces.add("");
                }
                gavPieces.set(2, winningSection);
            }
        }

        return gavPieces;
    }

    private int parseTreeLevel(final String line) {
        if (StringUtils.startsWithAny(line, TREE_LEVEL_TERMINALS)) {
            return 0;
        }

        String modifiedLine = DetectableStringUtils.removeEvery(line, TREE_LEVEL_TERMINALS);

        if (!modifiedLine.startsWith("|") && modifiedLine.startsWith(" ")) {
            modifiedLine = "|" + modifiedLine;
        }
        modifiedLine = modifiedLine.replace("     ", "    |");
        modifiedLine = modifiedLine.replace("||", "|");
        if (modifiedLine.endsWith("|")) {
            modifiedLine = modifiedLine.substring(0, modifiedLine.length() - 5);
        }
        final int matches = StringUtils.countMatches(modifiedLine, "|");

        return matches;
    }

}
