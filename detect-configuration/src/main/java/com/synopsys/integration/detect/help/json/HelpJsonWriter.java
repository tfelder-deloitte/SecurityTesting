/**
 * detect-configuration
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
package com.synopsys.integration.detect.help.json;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.synopsys.integration.configuration.property.Property;
import com.synopsys.integration.configuration.util.Group;
import com.synopsys.integration.detect.exitcode.ExitCodeType;

public class HelpJsonWriter {
    private final Logger logger = LoggerFactory.getLogger(HelpJsonWriter.class);

    private final Gson gson;

    public HelpJsonWriter(final Gson gson) {
        this.gson = gson;
    }

    public void writeGsonDocument(final String filename, final List<Property> detectOptions, final List<HelpJsonDetector> buildDetectors, final List<HelpJsonDetector> buildlessDetectors) {
        final HelpJsonData data = new HelpJsonData();

        data.getOptions().addAll(detectOptions.stream().map(this::convertOption).collect(Collectors.toList()));
        data.getExitCodes().addAll(Stream.of(ExitCodeType.values()).map(this::convertExitCode).collect(Collectors.toList()));
        data.setBuildlessDetectors(buildlessDetectors);
        data.setBuildDetectors(buildDetectors);

        try {
            try (final Writer writer = new FileWriter(filename)) {
                gson.toJson(data, writer);
            }

            logger.info(filename + " was created in your current directory.");
        } catch (final IOException e) {
            logger.error("There was an error when creating the html file", e);
        }
    }

    public HelpJsonExitCode convertExitCode(final ExitCodeType exitCodeType) {
        final HelpJsonExitCode helpJsonExitCode = new HelpJsonExitCode();
        helpJsonExitCode.setExitCodeKey(exitCodeType.name());
        helpJsonExitCode.setExitCodeValue(exitCodeType.getExitCode());
        helpJsonExitCode.setExitCodeDescription(exitCodeType.getDescription());
        return helpJsonExitCode;
    }

    public HelpJsonOption convertOption(final Property property) {
        final HelpJsonOption helpJsonOption = new HelpJsonOption();

        helpJsonOption.setPropertyName(property.getName());
        helpJsonOption.setPropertyKey(property.getKey());
        helpJsonOption.setPropertyType(property.describeType() == null ? "None" : property.describeType());
        helpJsonOption.setAddedInVersion(property.getFromVersion());
        helpJsonOption.setDefaultValue(property.describeDefault());

        helpJsonOption.setGroup(property.getPropertyGroupInfo().getPrimaryGroup().getName());
        String superGroupName = "";
        if (property.getPropertyGroupInfo().getPrimaryGroup().getSuperGroup().isPresent()) {
            superGroupName = property.getPropertyGroupInfo().getPrimaryGroup().getSuperGroup().get().getName();
        }
        helpJsonOption.setSuperGroup(superGroupName);
        helpJsonOption.setAdditionalGroups(property.getPropertyGroupInfo().getAdditionalGroups().stream().map(Group::getName).collect(Collectors.toList()));
        helpJsonOption.setCategory(property.getCategory() == null ? "" : property.getCategory().getName());
        helpJsonOption.setDescription(property.getPropertyHelpInfo().getShortText());
        helpJsonOption.setDetailedDescription(property.getPropertyHelpInfo().getLongText() == null ? "" : property.getPropertyHelpInfo().getLongText());
        helpJsonOption.setDeprecated(property.getPropertyDeprecationInfo() != null);
        if (property.getPropertyDeprecationInfo() != null) {
            helpJsonOption.setDeprecatedDescription(property.getPropertyDeprecationInfo().getDescription());
            helpJsonOption.setDeprecatedFailInVersion(property.getPropertyDeprecationInfo().getFailInVersion().getDisplayValue());
            helpJsonOption.setDeprecatedRemoveInVersion(property.getPropertyDeprecationInfo().getRemoveInVersion().getDisplayValue());
        }
        helpJsonOption.setStrictValues(property.isOnlyExampleValues());
        helpJsonOption.setCaseSensitiveValues(property.isCaseSensitive());
        helpJsonOption.setAcceptableValues(property.listExampleValues().stream().map(Objects::toString).collect(Collectors.toList()));
        helpJsonOption.setHasAcceptableValues(property.listExampleValues().size() > 0);
        helpJsonOption.setCommaSeparatedList(property.isCommaSeparated());
        return helpJsonOption;
    }
}
