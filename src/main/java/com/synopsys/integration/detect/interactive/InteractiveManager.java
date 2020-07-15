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
package com.synopsys.integration.detect.interactive;

import java.io.Console;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.detect.interactive.mode.InteractiveMode;
import com.synopsys.integration.detect.interactive.reader.ConsoleInteractiveReader;
import com.synopsys.integration.detect.interactive.reader.InteractiveReader;
import com.synopsys.integration.detect.interactive.reader.ScannerInteractiveReader;

public class InteractiveManager {
    private final Logger logger = LoggerFactory.getLogger(InteractiveManager.class);

    public List<InteractiveOption> configureInInteractiveMode(final InteractiveMode interactiveMode) {
        // Using an UncloseablePrintStream so we don't accidentally close System.out
        try (final PrintStream interactivePrintStream = new UncloseablePrintStream(System.out)) {
            final InteractiveReader interactiveReader;
            final Console console = System.console();

            if (console != null) {
                interactiveReader = new ConsoleInteractiveReader(console);
            } else {
                logger.warn("It may be insecure to enter passwords because you are running in a virtual console.");
                interactiveReader = new ScannerInteractiveReader(System.in);
            }

            interactiveMode.init(interactivePrintStream, interactiveReader);

            interactiveMode.println("");
            interactiveMode.println("Interactive flag found.");
            interactiveMode.println("Starting default interactive mode.");
            interactiveMode.println("");

            interactiveMode.configure();
            return interactiveMode.getInteractiveOptions();
        }
    }
}
