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
package com.synopsys.integration.detect.boot;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.synopsys.integration.builder.BuilderStatus;
import com.synopsys.integration.configuration.util.Bds;
import com.synopsys.integration.detect.DetectTool;
import com.synopsys.integration.detect.configuration.DetectConfigurationFactory;
import com.synopsys.integration.detect.configuration.connection.BlackDuckConnectionDetails;
import com.synopsys.integration.detect.lifecycle.boot.decision.BlackDuckDecision;
import com.synopsys.integration.detect.lifecycle.boot.decision.PolarisDecision;
import com.synopsys.integration.detect.lifecycle.boot.decision.ProductDecider;
import com.synopsys.integration.detect.tool.signaturescanner.BlackDuckSignatureScannerOptions;
import com.synopsys.integration.detect.util.filter.DetectToolFilter;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfigBuilder;

class ProductDeciderTest {
    private String VALID_URL = "http://example";

    @Test
    public void shouldRunPolarisWhenConfigValid() {
        File userHome = Mockito.mock(File.class);
        DetectToolFilter detectToolFilter = mockToolFilterForPolaris(true);
        DetectConfigurationFactory detectConfigurationFactory = mockDetectConfigurationFactoryForPolaris(true);

        PolarisDecision polarisDecision = new ProductDecider().determinePolaris(detectConfigurationFactory, userHome, detectToolFilter);
        Assertions.assertTrue(polarisDecision.shouldRun());
    }

    @Test
    public void shouldNotRunPolarisWhenConfigInvalid() {
        File userHome = Mockito.mock(File.class);
        DetectToolFilter detectToolFilter = mockToolFilterForPolaris(true);
        DetectConfigurationFactory detectConfigurationFactory = mockDetectConfigurationFactoryForPolaris(false);

        PolarisDecision polarisDecision = new ProductDecider().determinePolaris(detectConfigurationFactory, userHome, detectToolFilter);
        Assertions.assertFalse(polarisDecision.shouldRun());
    }

    @Test
    public void shouldNotRunPolarisWhenExcluded() {
        File userHome = Mockito.mock(File.class);
        DetectToolFilter detectToolFilter = mockToolFilterForPolaris(false);
        DetectConfigurationFactory detectConfigurationFactory = mockDetectConfigurationFactoryForPolaris(true);

        PolarisDecision polarisDecision = new ProductDecider().determinePolaris(detectConfigurationFactory, userHome, detectToolFilter);
        Assertions.assertFalse(polarisDecision.shouldRun());
    }

    @Test
    public void shouldRunBlackDuckOfflineWhenOverride() {
        BlackDuckConnectionDetails blackDuckConnectionDetails = blackDuckConnectionDetails(true, null);
        BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions = blackDuckSignatureScannerOptions(null, null);
        BlackDuckDecision productDecision = new ProductDecider().determineBlackDuck(blackDuckConnectionDetails, blackDuckSignatureScannerOptions);

        Assertions.assertTrue(productDecision.shouldRun());
        Assertions.assertTrue(productDecision.isOffline());
    }

    @Test
    public void shouldRunBlackDuckOfflineWhenInstallUrl() {
        BlackDuckConnectionDetails blackDuckConnectionDetails = blackDuckConnectionDetails(true, null);
        BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions = blackDuckSignatureScannerOptions(null, VALID_URL);
        BlackDuckDecision productDecision = new ProductDecider().determineBlackDuck(blackDuckConnectionDetails, blackDuckSignatureScannerOptions);

        Assertions.assertTrue(productDecision.shouldRun());
        Assertions.assertTrue(productDecision.isOffline());
    }

    @Test
    public void shouldRunBlackDuckOfflineWhenInstallPath() {
        BlackDuckConnectionDetails blackDuckConnectionDetails = blackDuckConnectionDetails(true, null);
        BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions = blackDuckSignatureScannerOptions(Mockito.mock(Path.class), null);
        BlackDuckDecision productDecision = new ProductDecider().determineBlackDuck(blackDuckConnectionDetails, blackDuckSignatureScannerOptions);

        Assertions.assertTrue(productDecision.shouldRun());
        Assertions.assertTrue(productDecision.isOffline());
    }

    @Test
    public void shouldRunBlackDuckOnline() {
        BlackDuckConnectionDetails blackDuckConnectionDetails = blackDuckConnectionDetails(false, VALID_URL);
        BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions = blackDuckSignatureScannerOptions(null, null);
        BlackDuckDecision productDecision = new ProductDecider().determineBlackDuck(blackDuckConnectionDetails, blackDuckSignatureScannerOptions);

        Assertions.assertTrue(productDecision.shouldRun());
        Assertions.assertFalse(productDecision.isOffline());
    }

    private DetectToolFilter mockToolFilterForPolaris(boolean includesPolaris) {
        DetectToolFilter detectToolFilter = Mockito.mock(DetectToolFilter.class);
        Mockito.when(detectToolFilter.shouldInclude(DetectTool.POLARIS)).thenReturn(includesPolaris);
        return detectToolFilter;
    }

    private BlackDuckSignatureScannerOptions blackDuckSignatureScannerOptions(Path offlineScannerInstallPath, String userProvidedScannerInstallUrl) {
        return new BlackDuckSignatureScannerOptions(Bds.listOf(), Bds.listOf(), Bds.listOf(), offlineScannerInstallPath, null, userProvidedScannerInstallUrl, 1024, 1, false, null, false, null, null, null, 1, null, false);
    }

    private BlackDuckConnectionDetails blackDuckConnectionDetails(boolean offline, String blackduckUrl) {
        return new BlackDuckConnectionDetails(offline, blackduckUrl, null, null, null);
    }

    private DetectConfigurationFactory mockDetectConfigurationFactoryForPolaris(boolean returnsValid) {
        PolarisServerConfigBuilder polarisServerConfigBuilder = new MockPolarisServerConfigBuilder(returnsValid);
        DetectConfigurationFactory detectConfigurationFactory = Mockito.mock(DetectConfigurationFactory.class);
        Mockito.when(detectConfigurationFactory.createPolarisServerConfigBuilder(Mockito.any())).thenReturn(
            polarisServerConfigBuilder
        );
        return detectConfigurationFactory;
    }

    class MockPolarisServerConfigBuilder extends PolarisServerConfigBuilder {
        private final boolean isValid;

        MockPolarisServerConfigBuilder(final boolean isValid) {
            this.isValid = isValid;
        }

        @Override
        protected void validate(BuilderStatus builderStatus) {
            if (!isValid) {
                builderStatus.addErrorMessage("Invalid polaris config!");
            }
        }
    }
}
