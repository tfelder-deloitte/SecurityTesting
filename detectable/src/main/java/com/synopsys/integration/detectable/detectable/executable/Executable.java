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
package com.synopsys.integration.detectable.detectable.executable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Executable {
    private final File workingDirectory;
    private final Map<String, String> environmentVariables = new HashMap<>();
    private final List<String> command = new ArrayList<>();

    public Executable(final File workingDirectory, final Map<String, String> environmentVariables, final List<String> command) {
        this.workingDirectory = workingDirectory;
        this.environmentVariables.putAll(environmentVariables);
        this.command.addAll(command);
    }

    public Executable(final File workingDirectory, final Map<String, String> environmentVariables, final String exeCmd, final List<String> executableArguments) {
        this.workingDirectory = workingDirectory;
        if (environmentVariables != null) {
            this.environmentVariables.putAll(environmentVariables);
        }
        this.command.add(exeCmd);
        this.command.addAll(executableArguments);
    }

    public ProcessBuilder createProcessBuilder() {
        final List<String> processBuilderArguments = createProcessBuilderArguments();
        final ProcessBuilder processBuilder = new ProcessBuilder(processBuilderArguments);
        processBuilder.directory(workingDirectory);
        final Map<String, String> processBuilderEnvironment = processBuilder.environment();
        final Map<String, String> systemEnv = System.getenv();
        for (final Map.Entry<String, String> systemEnvEntry : systemEnv.entrySet()) {
            populateEnvironmentMap(processBuilderEnvironment, systemEnvEntry.getKey(), systemEnvEntry.getValue());
        }
        for (final Map.Entry<String, String> environmentVariableEntry : environmentVariables.entrySet()) {
            populateEnvironmentMap(processBuilderEnvironment, environmentVariableEntry.getKey(), environmentVariableEntry.getValue());
        }
        return processBuilder;
    }

    public String getMaskedExecutableDescription() {
        final List<String> arguments = new ArrayList<>();
        for (final String argument : createProcessBuilderArguments()) {
            if (argument.matches(".*password.*=.*")) {
                final String maskedArgument = argument.substring(0, argument.indexOf('=') + 1) + "********";
                arguments.add(maskedArgument);
            } else {
                arguments.add(argument);
            }
        }
        return StringUtils.join(arguments, ' ');
    }

    public File getWorkingDirectory() {
        return workingDirectory;
    }

    public List<String> getCommand() {
        return command;
    }

    private List<String> createProcessBuilderArguments() {
        // ProcessBuilder can only be called with a List<java.lang.String> so do any needed conversion
        final List<String> processBuilderArguments = new ArrayList<>(command);
        return processBuilderArguments;
    }

    private void populateEnvironmentMap(final Map<String, String> environment, final Object key, final Object value) {
        // ProcessBuilder's environment's keys and values must be non-null java.lang.String's
        if (key != null && value != null) {
            final String keyString = key.toString();
            final String valueString = value.toString();
            if (keyString != null && valueString != null) {
                environment.put(keyString, valueString);
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Executable that = (Executable) o;

        if (!getWorkingDirectory().equals(that.getWorkingDirectory()))
            return false;
        if (!environmentVariables.equals(that.environmentVariables))
            return false;
        return getCommand().equals(that.getCommand());
    }

    @Override
    public int hashCode() {
        int result = getWorkingDirectory().hashCode();
        result = 31 * result + environmentVariables.hashCode();
        result = 31 * result + getCommand().hashCode();
        return result;
    }
}
