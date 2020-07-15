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
package com.synopsys.integration.detect.workflow.report;

import java.util.Map;

import com.synopsys.integration.detect.tool.detector.DetectorToolResult;
import com.synopsys.integration.detect.workflow.codelocation.DetectCodeLocation;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.detect.workflow.report.writer.DebugLogReportWriter;
import com.synopsys.integration.detect.workflow.report.writer.ReportWriter;
import com.synopsys.integration.detect.workflow.report.writer.TraceLogReportWriter;
import com.synopsys.integration.detector.base.DetectorEvaluation;
import com.synopsys.integration.detector.base.DetectorEvaluationTree;

public class ReportManager {
    // all entry points to reporting
    private final EventSystem eventSystem;

    // Summary, print collections or final groups or information.
    private final SearchSummaryReporter searchSummaryReporter;
    private final PreparationSummaryReporter preparationSummaryReporter;
    private final ExtractionSummaryReporter extractionSummaryReporter;
    private final DiscoverySummaryReporter discoverySummaryReporter;
    private final DetectorIssuePublisher detectorIssuePublisher;

    private final ReportWriter traceLogWriter = new TraceLogReportWriter();
    private final ReportWriter debugLogWriter = new DebugLogReportWriter();
    private final ExtractionLogger extractionLogger;
    private final DiscoveryLogger discoveryLogger;

    public static ReportManager createDefault(final EventSystem eventSystem) {
        return new ReportManager(eventSystem, new PreparationSummaryReporter(), new ExtractionSummaryReporter(), new SearchSummaryReporter(), new DiscoverySummaryReporter(), new DetectorIssuePublisher(), new ExtractionLogger(),
            new DiscoveryLogger());
    }

    public ReportManager(final EventSystem eventSystem,
        final PreparationSummaryReporter preparationSummaryReporter, final ExtractionSummaryReporter extractionSummaryReporter, final SearchSummaryReporter searchSummaryReporter,
        final DiscoverySummaryReporter discoverySummaryReporter, final DetectorIssuePublisher detectorIssuePublisher,
        final ExtractionLogger extractionLogger, final DiscoveryLogger discoveryLogger) {
        this.eventSystem = eventSystem;
        this.preparationSummaryReporter = preparationSummaryReporter;
        this.extractionSummaryReporter = extractionSummaryReporter;
        this.searchSummaryReporter = searchSummaryReporter;
        this.discoverySummaryReporter = discoverySummaryReporter;
        this.detectorIssuePublisher = detectorIssuePublisher;
        this.extractionLogger = extractionLogger;
        this.discoveryLogger = discoveryLogger;

        eventSystem.registerListener(Event.SearchCompleted, this::searchCompleted);
        eventSystem.registerListener(Event.PreparationsCompleted, this::preparationsCompleted);
        eventSystem.registerListener(Event.DiscoveriesCompleted, this::discoveriesCompleted);
        eventSystem.registerListener(Event.DetectorsComplete, this::bomToolsComplete);

        eventSystem.registerListener(Event.CodeLocationsCalculated, event -> codeLocationsCompleted(event.getCodeLocationNames()));

        eventSystem.registerListener(Event.DiscoveryCount, this::discoveryCount);
        eventSystem.registerListener(Event.DiscoveryStarted, this::discoveryStarted);
        eventSystem.registerListener(Event.DiscoveryEnded, this::discoveryEnded);

        eventSystem.registerListener(Event.ExtractionCount, this::exractionCount);
        eventSystem.registerListener(Event.ExtractionStarted, this::exractionStarted);
        eventSystem.registerListener(Event.ExtractionEnded, this::exractionEnded);

    }

    // Reports
    public void searchCompleted(final DetectorEvaluationTree rootEvaluation) {
        searchSummaryReporter.print(debugLogWriter, rootEvaluation);
        final DetailedSearchSummaryReporter detailedSearchSummaryReporter = new DetailedSearchSummaryReporter();
        detailedSearchSummaryReporter.print(traceLogWriter, rootEvaluation);
    }

    public void preparationsCompleted(final DetectorEvaluationTree detectorEvaluationTree) {
        preparationSummaryReporter.write(debugLogWriter, detectorEvaluationTree);
    }

    public void discoveryCount(final Integer count) {
        discoveryLogger.setDiscoveryCount(count);
    }

    public void discoveryStarted(final DetectorEvaluation detectorEvaluation) {
        discoveryLogger.discoveryStarted(detectorEvaluation);
    }

    public void discoveryEnded(final DetectorEvaluation detectorEvaluation) {
        discoveryLogger.discoveryEnded(detectorEvaluation);
    }

    public void exractionCount(final Integer count) {
        extractionLogger.setExtractionCount(count);
    }

    public void exractionStarted(final DetectorEvaluation detectorEvaluation) {
        extractionLogger.extractionStarted(detectorEvaluation);
    }

    public void exractionEnded(final DetectorEvaluation detectorEvaluation) {
        extractionLogger.extractionEnded(detectorEvaluation);
    }

    private DetectorToolResult detectorToolResult;

    public void bomToolsComplete(final DetectorToolResult detectorToolResult) {
        this.detectorToolResult = detectorToolResult;
    }

    public void discoveriesCompleted(final DetectorEvaluationTree detectorEvaluationTree) {
        discoverySummaryReporter.writeSummary(debugLogWriter, detectorEvaluationTree);
    }

    public void codeLocationsCompleted(final Map<DetectCodeLocation, String> codeLocationNameMap) {
        if (detectorToolResult != null && detectorToolResult.getRootDetectorEvaluationTree().isPresent()) {
            extractionSummaryReporter.writeSummary(debugLogWriter, detectorToolResult.getRootDetectorEvaluationTree().get(), detectorToolResult.getCodeLocationMap(), codeLocationNameMap, false);
        }
    }

    public void printDetectorIssues() {
        if (detectorToolResult != null && detectorToolResult.getRootDetectorEvaluationTree().isPresent()) {
            detectorIssuePublisher.publishEvents(eventSystem, detectorToolResult.getRootDetectorEvaluationTree().get());
        }
    }
}
