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
package com.synopsys.integration.detect.tool.detector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.detect.lifecycle.shutdown.ExitCodeRequest;
import com.synopsys.integration.detect.tool.detector.impl.ExtractionEnvironmentProvider;
import com.synopsys.integration.detect.workflow.codelocation.DetectCodeLocation;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.detect.workflow.nameversion.DetectorNameVersionHandler;
import com.synopsys.integration.detect.workflow.nameversion.PreferredDetectorNameVersionHandler;
import com.synopsys.integration.detect.workflow.status.DetectorStatus;
import com.synopsys.integration.detect.workflow.status.StatusType;
import com.synopsys.integration.detectable.detectable.codelocation.CodeLocation;
import com.synopsys.integration.detector.base.DetectorEvaluation;
import com.synopsys.integration.detector.base.DetectorEvaluationTree;
import com.synopsys.integration.detector.base.DetectorType;
import com.synopsys.integration.detector.evaluation.DetectorEvaluationOptions;
import com.synopsys.integration.detector.evaluation.DetectorEvaluator;
import com.synopsys.integration.detector.finder.DetectorFinder;
import com.synopsys.integration.detector.finder.DetectorFinderDirectoryListException;
import com.synopsys.integration.detector.finder.DetectorFinderOptions;
import com.synopsys.integration.detector.rule.DetectorRule;
import com.synopsys.integration.detector.rule.DetectorRuleSet;

public class DetectorTool {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final DetectorFinder detectorFinder;
    private final ExtractionEnvironmentProvider extractionEnvironmentProvider;
    private final EventSystem eventSystem;
    private final CodeLocationConverter codeLocationConverter;

    public DetectorTool(final DetectorFinder detectorFinder, final ExtractionEnvironmentProvider extractionEnvironmentProvider, final EventSystem eventSystem, final CodeLocationConverter codeLocationConverter) {
        this.detectorFinder = detectorFinder;
        this.extractionEnvironmentProvider = extractionEnvironmentProvider;
        this.eventSystem = eventSystem;
        this.codeLocationConverter = codeLocationConverter;
    }

    public DetectorToolResult performDetectors(final File directory, final DetectorRuleSet detectorRuleSet, final DetectorFinderOptions detectorFinderOptions, final DetectorEvaluationOptions evaluationOptions, final String projectDetector,
        final List<DetectorType> requiredDetectors)
        throws DetectUserFriendlyException {
        logger.debug("Initializing detector system.");
        final Optional<DetectorEvaluationTree> possibleRootEvaluation;
        try {
            logger.debug("Starting detector file system traversal.");
            possibleRootEvaluation = detectorFinder.findDetectors(directory, detectorRuleSet, detectorFinderOptions);

        } catch (final DetectorFinderDirectoryListException e) {
            throw new DetectUserFriendlyException("Detect was unable to list a directory while searching for detectors.", e, ExitCodeType.FAILURE_DETECTOR);
        }

        if (!possibleRootEvaluation.isPresent()) {
            logger.error("The source directory could not be searched for detectors - detector tool failed.");
            logger.error("Please ensure the provided source path is a directory and detect has access.");
            eventSystem.publishEvent(Event.ExitCode, new ExitCodeRequest(ExitCodeType.FAILURE_CONFIGURATION, "Detector tool failed to run on the configured source path."));
            return new DetectorToolResult();
        }

        final DetectorEvaluationTree rootEvaluation = possibleRootEvaluation.get();
        final List<DetectorEvaluation> detectorEvaluations = rootEvaluation.allDescendentEvaluations();

        logger.trace("Setting up detector events.");
        final DetectorEvaluatorBroadcaster eventBroadcaster = new DetectorEvaluatorBroadcaster(eventSystem);
        final DetectorEvaluator detectorEvaluator = new DetectorEvaluator(evaluationOptions);
        detectorEvaluator.setDetectorEvaluatorListener(eventBroadcaster);

        logger.info("Searching for detectors. This may take a while.");
        detectorEvaluator.searchAndApplicableEvaluation(rootEvaluation, new HashSet<>());

        final Set<DetectorType> applicable = detectorEvaluations.stream()
                                                 .filter(DetectorEvaluation::isApplicable)
                                                 .map(DetectorEvaluation::getDetectorRule)
                                                 .map(DetectorRule::getDetectorType)
                                                 .collect(Collectors.toSet());

        eventSystem.publishEvent(Event.ApplicableCompleted, applicable);
        eventSystem.publishEvent(Event.SearchCompleted, rootEvaluation);

        logger.info("");

        logger.debug("Starting detector preparation.");
        detectorEvaluator.extractableEvaluation(rootEvaluation);
        eventSystem.publishEvent(Event.PreparationsCompleted, rootEvaluation);

        logger.debug("Preparing detectors for discovery and extraction.");
        detectorEvaluator.setupDiscoveryAndExtractions(rootEvaluation, extractionEnvironmentProvider::createExtractionEnvironment);

        logger.debug("Counting detectors that will be evaluated.");
        final Integer extractionCount = Math.toIntExact(detectorEvaluations.stream()
                                                            .filter(DetectorEvaluation::isExtractable)
                                                            .count());
        eventSystem.publishEvent(Event.ExtractionCount, extractionCount);
        eventSystem.publishEvent(Event.DiscoveryCount, extractionCount); //right now discovery and extraction are the same. -jp 8/14/19

        logger.debug("Total number of detectors: " + extractionCount);

        logger.debug("Starting detector project discovery.");
        Optional<DetectorType> preferredProjectDetector = Optional.empty();
        if (StringUtils.isNotBlank(projectDetector)) {
            preferredProjectDetector = preferredDetectorTypeFromString(projectDetector);
        }

        final DetectorNameVersionHandler detectorNameVersionHandler;
        if (preferredProjectDetector.isPresent()) {
            detectorNameVersionHandler = new PreferredDetectorNameVersionHandler(preferredProjectDetector.get());
        } else {
            detectorNameVersionHandler = new DetectorNameVersionHandler(Collections.singletonList(DetectorType.GIT));
        }

        detectorEvaluator.discoveryEvaluation(rootEvaluation, new DetectDiscoveryFilter(eventSystem, detectorNameVersionHandler));
        eventSystem.publishEvent(Event.DiscoveriesCompleted, rootEvaluation);

        logger.debug("Starting detector extraction.");
        detectorEvaluator.extractionEvaluation(rootEvaluation);
        eventSystem.publishEvent(Event.ExtractionsCompleted, rootEvaluation);

        logger.debug("Finished detectors.");
        final Map<DetectorType, StatusType> statusMap = extractStatus(detectorEvaluations);
        statusMap.forEach((detectorType, statusType) -> eventSystem.publishEvent(Event.StatusSummary, new DetectorStatus(detectorType, statusType)));
        if (statusMap.containsValue(StatusType.FAILURE)) {
            eventSystem.publishEvent(Event.ExitCode, new ExitCodeRequest(ExitCodeType.FAILURE_DETECTOR, "One or more detectors were not successful."));
        }

        logger.debug("Publishing file events.");
        for (final DetectorEvaluation detectorEvaluation : detectorEvaluations) {
            if (detectorEvaluation.getDetectable() != null) {
                for (final File file : detectorEvaluation.getDetectable().getFoundRelevantFiles()) {
                    eventSystem.publishEvent(Event.CustomerFileOfInterest, file);
                }
            }
            if (detectorEvaluation.getExtraction() != null) {
                for (final File file : detectorEvaluation.getExtraction().getRelevantFiles()) {
                    eventSystem.publishEvent(Event.CustomerFileOfInterest, file);
                }
            }
        }

        Map<CodeLocation, DetectCodeLocation> codeLocationMap = createCodeLocationMap(detectorEvaluations, directory);

        final DetectorToolResult detectorToolResult = new DetectorToolResult(
            detectorNameVersionHandler.finalDecision().getChosenNameVersion().orElse(null),
            new ArrayList<>(codeLocationMap.values()),
            applicable,
            new HashSet<>(),
            rootEvaluation,
            codeLocationMap
        );

        //Check required detector types
        final Set<DetectorType> missingDetectors = requiredDetectors.stream()
                                                       .filter(it -> !applicable.contains(it))
                                                       .collect(Collectors.toSet());
        if (missingDetectors.size() > 0) {
            final String missingDetectorDisplay = missingDetectors.stream().map(Enum::toString).collect(Collectors.joining(","));
            logger.error("One or more required detector types were not found: " + missingDetectorDisplay);
            eventSystem.publishEvent(Event.ExitCode, new ExitCodeRequest(ExitCodeType.FAILURE_DETECTOR_REQUIRED));
        }

        //Completed.
        logger.debug("Finished running detectors.");
        eventSystem.publishEvent(Event.DetectorsComplete, detectorToolResult);

        return detectorToolResult;
    }

    private Map<DetectorType, StatusType> extractStatus(final List<DetectorEvaluation> detectorEvaluations) {
        final Map<DetectorType, StatusType> statusMap = new HashMap<>();
        for (final DetectorEvaluation detectorEvaluation : detectorEvaluations) {
            final DetectorType detectorType = detectorEvaluation.getDetectorRule().getDetectorType();
            if (detectorEvaluation.isApplicable()) {
                final StatusType statusType;
                if (detectorEvaluation.isExtractable()) {
                    if (detectorEvaluation.wasExtractionSuccessful()) {
                        statusType = StatusType.SUCCESS;
                    } else if (detectorEvaluation.wasExtractionFailure()) {
                        statusType = StatusType.FAILURE;
                    } else if (detectorEvaluation.wasExtractionException()) {
                        statusType = StatusType.FAILURE;
                    } else {
                        logger.warn("An issue occurred in the detector system, an unknown evaluation status was created. Please don't do this again.");
                        statusType = StatusType.FAILURE;
                    }
                } else if (detectorEvaluation.isFallbackExtractable() || detectorEvaluation.isPreviousExtractable()) {
                    statusType = StatusType.SUCCESS;
                } else {
                    statusType = StatusType.FAILURE;
                }
                if (statusType == StatusType.FAILURE || !statusMap.containsKey(detectorType)) {
                    statusMap.put(detectorType, statusType);
                }
            }
        }
        return statusMap;
    }

    private Map<CodeLocation, DetectCodeLocation> createCodeLocationMap(List<DetectorEvaluation> detectorEvaluations, File directory) {
        return detectorEvaluations.stream()
                   .filter(DetectorEvaluation::wasExtractionSuccessful)
                   .map(it -> codeLocationConverter.toDetectCodeLocation(directory, it))
                   .map(Map::entrySet)
                   .flatMap(Collection::stream)
                   .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Optional<DetectorType> preferredDetectorTypeFromString(final String detectorTypeRaw) {
        final String detectorType = detectorTypeRaw.trim().toUpperCase();
        if (StringUtils.isNotBlank(detectorType)) {
            if (DetectorType.getPossibleNames().contains(detectorType)) {
                return Optional.of(DetectorType.valueOf(detectorType));
            } else {
                logger.info("A valid preferred detector type was not provided, deciding project name automatically.");
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
