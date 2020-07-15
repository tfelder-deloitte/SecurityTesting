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
package com.synopsys.integration.detector.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detector.base.DetectableCreatable;
import com.synopsys.integration.detector.base.DetectorType;

public class DetectorRuleSetBuilder {
    private final List<DetectorRule> rules = new ArrayList<>();
    private final List<DetectorRuleYieldBuilder> yieldBuilders = new ArrayList<>();
    private final List<DetectorRuleFallbackBuilder> fallbackBuilders = new ArrayList<>();

    public <T extends Detectable> DetectorRuleBuilder addDetector(final DetectorType type, final String name, Class<T> detectableClass, final DetectableCreatable<T> detectableCreatable) {
        final DetectorRuleBuilder ruleBuilder = new DetectorRuleBuilder<T>(name, type, detectableClass, detectableCreatable);
        ruleBuilder.setDetectorRuleSetBuilder(this);
        return ruleBuilder;
    }

    public DetectorRuleSetBuilder add(final DetectorRule rule) {
        rules.add(rule);
        return this;
    }

    public DetectorRuleYieldBuilder yield(final DetectorRule rule) {
        final DetectorRuleYieldBuilder builder = new DetectorRuleYieldBuilder(rule);
        yieldBuilders.add(builder);
        return builder;
    }

    public DetectorRuleFallbackBuilder fallback(final DetectorRule rule) {
        final DetectorRuleFallbackBuilder builder = new DetectorRuleFallbackBuilder(rule);
        fallbackBuilders.add(builder);
        return builder;
    }

    public DetectorRuleSet build() {
        final Map<DetectorRule, Set<DetectorRule>> yieldsToRules = buildYield();
        final Map<DetectorRule, DetectorRule> fallbackToRules = buildFallback();

        final List<DetectorRule> orderedRules = new ArrayList<>();
        boolean atLeastOneRuleAdded = true;

        while (orderedRules.size() < rules.size() && atLeastOneRuleAdded) {
            final List<DetectorRule> satisfiedRules = rules.stream()
                                                          .filter(rule -> !orderedRules.contains(rule))
                                                          .filter(rule -> yieldSatisfied(rule, orderedRules, yieldsToRules))
                                                          .filter(rule -> fallbackSatisfied(rule, orderedRules, fallbackToRules))
                                                          .collect(Collectors.toList());

            atLeastOneRuleAdded = satisfiedRules.size() > 0;
            orderedRules.addAll(satisfiedRules);
        }

        if (orderedRules.size() != rules.size()) {
            throw new RuntimeException("Unable to order detector rules.");
        }

        return new DetectorRuleSet(orderedRules, yieldsToRules, fallbackToRules);
    }

    private Map<DetectorRule, Set<DetectorRule>> buildYield() {
        final Map<DetectorRule, Set<DetectorRule>> yieldsToRules = new HashMap<>();
        for (final DetectorRuleYieldBuilder yieldBuilder : yieldBuilders) {
            if (!yieldsToRules.containsKey(yieldBuilder.getYieldingDetector())) {
                yieldsToRules.put(yieldBuilder.getYieldingDetector(), new HashSet<>());
            }
            yieldsToRules.get(yieldBuilder.getYieldingDetector()).add(yieldBuilder.getYieldingToDetector());
        }
        return yieldsToRules;
    }

    private Map<DetectorRule, DetectorRule> buildFallback() {
        final Map<DetectorRule, DetectorRule> fallbackToRules = new HashMap<>();
        for (final DetectorRuleFallbackBuilder fallbackBuilder : fallbackBuilders) {
            if (fallbackToRules.containsKey(fallbackBuilder.getFailingDetector())) {
                throw new RuntimeException("Each detector can only have a single fallback. Chain fallback detectors with X -> Y -> Z instead of X -> Y and X -> Z.");
            } else {
                fallbackToRules.put(fallbackBuilder.getFailingDetector(), fallbackBuilder.getFallbackToDetector());
            }
        }
        return fallbackToRules;
    }

    //returns true when every fallback with this rule as the 'fallback to' detector has had it's 'failing' detector already been added.
    private boolean fallbackSatisfied(final DetectorRule rule, final List<DetectorRule> orderedRules, final Map<DetectorRule, DetectorRule> fallbackToRules) {
        boolean fallbackSatisfied = true;
        for (final Map.Entry<DetectorRule, DetectorRule> fallback : fallbackToRules.entrySet()) {
            //When the Key detector fails we fallback to the Value detector. Therefore for a given rule, every time it is a Value that corresponding Key detector must have already been added.
            if (rule.equals(fallback.getValue()) && !orderedRules.contains(fallback.getKey())) {
                fallbackSatisfied = false;
            }
        }
        return fallbackSatisfied;
    }

    //returns true when all all detectors the rule yields to have already been added.
    private boolean yieldSatisfied(final DetectorRule rule, final List<DetectorRule> orderedRules, final Map<DetectorRule, Set<DetectorRule>> yieldsToRules) {
        if (yieldsToRules.containsKey(rule)) {
            boolean yieldedSatisfied = true;
            for (final DetectorRule yield : yieldsToRules.get(rule)) {
                if (!orderedRules.contains(yield)) {
                    yieldedSatisfied = false;
                }
            }
            return yieldedSatisfied;
        } else {
            return true;
        }
    }
}
