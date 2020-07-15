/**
 * synopsys-detect
 * <p>
 * Copyright (c) 2020 Synopsys, Inc.
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.synopsys.integration.detect.integration;

import com.synopsys.integration.blackduck.service.model.ProjectVersionWrapper;
import com.synopsys.integration.detect.Application;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class CompleteRiskReportTest extends BlackDuckIntegrationTest {
    @Test
    public void testRiskReportWithoutPath() throws Exception {
        testRiskReportIsPopulated(false);
    }

    @Test
    public void testRiskReportWithPath() throws Exception {
        testRiskReportIsPopulated(true);
    }

    public void testRiskReportIsPopulated(final boolean includePath) throws Exception {
        final Path tempReportDirectoryPath = Files.createTempDirectory("junit_report");
        final File reportDirectory;
        if (includePath) {
            reportDirectory = tempReportDirectoryPath.toFile();
        } else {
            reportDirectory = new File(".");
        }

        final String projectName = "synopsys-detect-junit";
        final String projectVersionName = "risk-report";
        final ProjectVersionWrapper projectVersionWrapper = assertProjectVersionReady(projectName, projectVersionName);
        List<File> pdfFiles = getPdfFiles(reportDirectory);
        assertEquals(0, pdfFiles.size());
        reportService.createReportPdfFile(reportDirectory, projectVersionWrapper.getProjectView(), projectVersionWrapper.getProjectVersionView());
        pdfFiles = getPdfFiles(reportDirectory);
        assertEquals(1, pdfFiles.size());
        final long initialFileLength = pdfFiles.get(0).length();
        assertTrue(initialFileLength > 0);
        FileUtils.deleteQuietly(pdfFiles.get(0));
        pdfFiles = getPdfFiles(reportDirectory);
        assertEquals(0, pdfFiles.size());

        final List<String> detectArgs = getInitialArgs(projectName, projectVersionName);
        detectArgs.add("--detect.risk.report.pdf=true");
        if (includePath) {
            detectArgs.add("--detect.risk.report.pdf.path=" + reportDirectory.toString());
        }

        detectArgs.add("--detect.tools=DETECTOR");
        detectArgs.forEach(System.out::println);
        Application.main(detectArgs.toArray(new String[0]));

        pdfFiles = getPdfFiles(reportDirectory);
        assertEquals(1, pdfFiles.size());
        final long postLength = pdfFiles.get(0).length();
        assertTrue(postLength > initialFileLength);
    }

    private List<File> getPdfFiles(final File directory) {
        final File[] files = directory.listFiles();
        if (files != null) {
            return Arrays.stream(files)
                    .filter(file -> file.getName().endsWith(".pdf"))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
