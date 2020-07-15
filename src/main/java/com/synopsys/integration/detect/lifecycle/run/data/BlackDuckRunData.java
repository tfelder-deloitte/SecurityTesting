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
package com.synopsys.integration.detect.lifecycle.run.data;

import java.util.Optional;

import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.service.BlackDuckServicesFactory;
import com.synopsys.integration.detect.workflow.phonehome.PhoneHomeManager;

public class BlackDuckRunData {
    private final boolean isOnline;
    private final BlackDuckServicesFactory blackDuckServicesFactory;
    private final PhoneHomeManager phoneHomeManager;
    private final BlackDuckServerConfig blackDuckServerConfig;

    public static BlackDuckRunData offline() {
        return new BlackDuckRunData(false, null, null, null);
    }

    public static BlackDuckRunData online(final BlackDuckServicesFactory blackDuckServicesFactory, final PhoneHomeManager phoneHomeManager, final BlackDuckServerConfig blackDuckServerConfig) {
        return new BlackDuckRunData(true, blackDuckServicesFactory, phoneHomeManager, blackDuckServerConfig);
    }

    private BlackDuckRunData(final boolean isOnline, final BlackDuckServicesFactory blackDuckServicesFactory, final PhoneHomeManager phoneHomeManager, final BlackDuckServerConfig blackDuckServerConfig) {
        this.isOnline = isOnline;
        this.blackDuckServicesFactory = blackDuckServicesFactory;
        this.phoneHomeManager = phoneHomeManager;
        this.blackDuckServerConfig = blackDuckServerConfig;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public Optional<BlackDuckServicesFactory> getBlackDuckServicesFactory() {
        return Optional.ofNullable(blackDuckServicesFactory);
    }

    public Optional<BlackDuckServerConfig> getBlackDuckServerConfig() {
        return Optional.ofNullable(blackDuckServerConfig);
    }

    public Optional<PhoneHomeManager> getPhoneHomeManager() {
        return Optional.ofNullable(phoneHomeManager);
    }
}
