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
package com.synopsys.integration.detect.lifecycle.boot.product;

public class PolarisConnectivityResult {
    private final boolean successfullyConnected;

    //if failure, the following is populated
    private final String failureReason;

    private PolarisConnectivityResult(final boolean successfullyConnected, final String failureReason) {
        this.successfullyConnected = successfullyConnected;
        this.failureReason = failureReason;
    }

    public static PolarisConnectivityResult success() {
        return new PolarisConnectivityResult(true, null);
    }

    public static PolarisConnectivityResult failure(final String reason) {
        return new PolarisConnectivityResult(false, reason);
    }

    public boolean isSuccessfullyConnected() {
        return successfullyConnected;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
