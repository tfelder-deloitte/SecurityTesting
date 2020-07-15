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
package com.synopsys.integration.detectable.detectables.bazel.pipeline.stepexecutor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepExecutorFilter implements StepExecutor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String regex;

    public StepExecutorFilter(final String regex) {
        this.regex = regex;
    }

    @Override
    public List<String> process(final List<String> input) {
        final List<String> output = new ArrayList<>();
        logger.trace(String.format("Filtering with regex %s", regex));
        for (final String inputItem : input) {
            if (inputItem.matches(regex)) {
                logger.trace(String.format("Filter keeping: %s", inputItem));
                output.add(inputItem);
            }
        }
        return output;
    }
}
