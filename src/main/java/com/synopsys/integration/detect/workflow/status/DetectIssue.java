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

import java.util.Arrays;
import java.util.List;

import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;

public class DetectIssue {
    public DetectIssueType getType() {
        return type;
    }

    public List<String> getMessages() {
        return messages;
    }

    private final DetectIssueType type;
    private final List<String> messages;

    public DetectIssue(final DetectIssueType type, final List<String> messages) {
        this.type = type;
        this.messages = messages;
    }

    public static void publish(final EventSystem eventSystem, final DetectIssueType type, final String... messages) {
        eventSystem.publishEvent(Event.Issue, new DetectIssue(type, Arrays.asList(messages)));
    }
}
