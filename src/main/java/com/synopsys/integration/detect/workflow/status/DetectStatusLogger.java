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
package com.synopsys.integration.detect.workflow.status;

import java.util.List;
import java.util.stream.Collectors;

import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.detect.workflow.result.DetectResult;
import com.synopsys.integration.log.IntLogger;

public class DetectStatusLogger {
    public void logDetectStatus(final IntLogger logger, final List<Status> statusSummaries, final List<DetectResult> detectResults, final List<DetectIssue> detectIssues, final ExitCodeType exitCodeType) {
        // sort by type, and within type, sort by description
        statusSummaries.sort((left, right) -> {
            if (left.getClass() == right.getClass()) {
                return left.getDescriptionKey().compareTo(right.getDescriptionKey());
            } else {
                return left.getClass().getName().compareTo(right.getClass().getName());
            }
        });
        logger.info("");
        logger.info("");

        if (!detectIssues.isEmpty()) {
            logger.info("======== Detect Issues ========");
            logger.info("");
            final List<DetectIssue> detectors = detectIssues.stream().filter(issue -> issue.getType() == DetectIssueType.DETECTOR).collect(Collectors.toList());
            if (detectors.size() > 0) {
                logger.info("DETECTORS:");
                detectors.stream().flatMap(issue -> issue.getMessages().stream()).forEach(line -> logger.info("\t" + line));
                logger.info("");
            }
            final List<DetectIssue> exceptions = detectIssues.stream().filter(issue -> issue.getType() == DetectIssueType.EXCEPTION).collect(Collectors.toList());
            if (exceptions.size() > 0) {
                logger.info("EXCEPTIONS:");
                exceptions.stream().flatMap(issue -> issue.getMessages().stream()).forEach(line -> logger.info("\t" + line));
                logger.info("");
            }
            final List<DetectIssue> deprecations = detectIssues.stream().filter(issue -> issue.getType() == DetectIssueType.DEPRECATION).collect(Collectors.toList());
            if (deprecations.size() > 0) {
                logger.info("DEPRECATIONS:");
                deprecations.stream().flatMap(issue -> issue.getMessages().stream()).forEach(line -> logger.info("\t" + line));
                logger.info("");
            }
        }

        if (!detectResults.isEmpty()) {
            logger.info("======== Detect Result ========");
            logger.info("");
            for (final DetectResult detectResult : detectResults) {
                logger.info(detectResult.getResultMessage());
            }
            logger.info("");
        }

        logger.info("======== Detect Status ========");
        logger.info("");
        Class<? extends Status> previousSummaryClass = null;

        for (final Status status : statusSummaries) {
            if (previousSummaryClass != null && !previousSummaryClass.equals(status.getClass())) {
                logger.info("");
            }
            logger.info(String.format("%s: %s", status.getDescriptionKey(), status.getStatusType().toString()));

            previousSummaryClass = status.getClass();
        }

        logger.info(String.format("Overall Status: %s", exitCodeType.toString()));
        logger.info("");
        logger.info("===============================");
        logger.info("");

    }
}