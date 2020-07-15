/**
 * detector
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
package com.synopsys.integration.detector.finder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detector.base.DetectorEvaluation;
import com.synopsys.integration.detector.base.DetectorEvaluationTree;
import com.synopsys.integration.detector.rule.DetectorRuleSet;

public class DetectorFinder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Optional<DetectorEvaluationTree> findDetectors(final File initialDirectory, final DetectorRuleSet detectorRuleSet, final DetectorFinderOptions options) throws DetectorFinderDirectoryListException {
        return findDetectors(initialDirectory, detectorRuleSet, 0, options);
    }

    private Optional<DetectorEvaluationTree> findDetectors(final File directory, final DetectorRuleSet detectorRuleSet, final int depth, final DetectorFinderOptions options)
        throws DetectorFinderDirectoryListException {

        if (depth > options.getMaximumDepth()) {
            logger.trace("Skipping directory as it exceeds max depth: " + directory.toString());
            return Optional.empty();
        }

        if (null == directory || Files.isSymbolicLink(directory.toPath()) || !directory.isDirectory()) {
            final String directoryString = Optional.ofNullable(directory).map(File::toString).orElse("null");
            logger.trace("Skipping file as it is not a directory: " + directoryString);
            return Optional.empty();
        }

        logger.debug("Traversing directory: " + directory.getPath()); //TODO: Finding the perfect log level here is important. At INFO, we log a lot during a deep traversal but if we don't we might look stuck.
        final List<DetectorEvaluation> evaluations = detectorRuleSet.getOrderedDetectorRules().stream()
                                                         .map(DetectorEvaluation::new)
                                                         .collect(Collectors.toList());

        final Set<DetectorEvaluationTree> children = new HashSet<>();

        final List<File> subDirectories = findFilteredSubDirectories(directory, options.getFileFilter());
        for (final File subDirectory : subDirectories) {
            final Optional<DetectorEvaluationTree> childEvaluationSet = findDetectors(subDirectory, detectorRuleSet, depth + 1, options);
            childEvaluationSet.ifPresent(children::add);
        }

        return Optional.of(new DetectorEvaluationTree(directory, depth, detectorRuleSet, evaluations, children));
    }

    private List<File> findFilteredSubDirectories(final File directory, final Predicate<File> filePredicate) throws DetectorFinderDirectoryListException {
        try (final Stream<Path> pathStream = Files.list(directory.toPath())) {
            return pathStream.map(Path::toFile)
                       .filter(File::isDirectory)
                       .filter(filePredicate)
                       .collect(Collectors.toList());
        } catch (final IOException e) {
            throw new DetectorFinderDirectoryListException(String.format("Could not get the subdirectories for %s. %s", directory.getAbsolutePath(), e.getMessage()), e);
        }
    }
}
