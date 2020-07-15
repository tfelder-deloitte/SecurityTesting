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
package com.synopsys.integration.detect.exception;

import com.synopsys.integration.detect.exitcode.ExitCodeType;

public class DetectUserFriendlyException extends Exception {
    private static final long serialVersionUID = 1L;

    private final ExitCodeType exitCodeType;

    public DetectUserFriendlyException(final String message, final ExitCodeType exitCodeType) {
        super(message);
        this.exitCodeType = exitCodeType;
    }

    public DetectUserFriendlyException(final String message, final Throwable cause, final ExitCodeType exitCodeType) {
        super(message, cause);
        this.exitCodeType = exitCodeType;
    }

    public ExitCodeType getExitCodeType() {
        return exitCodeType;
    }

}
