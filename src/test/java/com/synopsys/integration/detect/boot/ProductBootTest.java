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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.service.BlackDuckServicesFactory;
import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.lifecycle.boot.decision.BlackDuckDecision;
import com.synopsys.integration.detect.lifecycle.boot.decision.PolarisDecision;
import com.synopsys.integration.detect.lifecycle.boot.decision.ProductDecision;
import com.synopsys.integration.detect.lifecycle.boot.product.BlackDuckConnectivityChecker;
import com.synopsys.integration.detect.lifecycle.boot.product.BlackDuckConnectivityResult;
import com.synopsys.integration.detect.lifecycle.boot.product.PolarisConnectivityChecker;
import com.synopsys.integration.detect.lifecycle.boot.product.PolarisConnectivityResult;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBoot;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBootFactory;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBootOptions;
import com.synopsys.integration.detect.lifecycle.run.data.ProductRunData;
import com.synopsys.integration.polaris.common.configuration.PolarisServerConfig;

public class ProductBootTest {
    @Test
    public void bothProductsSkippedThrows() {
        Assertions.assertThrows(DetectUserFriendlyException.class, () -> {
            testBoot(BlackDuckDecision.skip(), PolarisDecision.skip(), new ProductBootOptions(false, false));
        });
    }

    @Test
    public void blackDuckConnectionFailureThrows() {
        final BlackDuckConnectivityResult connectivityResult = BlackDuckConnectivityResult.failure("Failed to connect");

        Assertions.assertThrows(DetectUserFriendlyException.class, () -> {
            testBoot(BlackDuckDecision.runOnline(), PolarisDecision.skip(), new ProductBootOptions(false, false), connectivityResult, null);
        });
    }

    @Test
    public void polarisConnectionFailureThrows() {
        final PolarisConnectivityResult connectivityResult = PolarisConnectivityResult.failure("Failed to connect");

        Assertions.assertThrows(DetectUserFriendlyException.class, () -> {
            testBoot(BlackDuckDecision.skip(), PolarisDecision.runOnline(null), new ProductBootOptions(false, false), null, connectivityResult);
        });
    }

    @Test
    public void blackDuckFailureWithIgnoreReturnsFalse() throws DetectUserFriendlyException {
        final BlackDuckConnectivityResult connectivityResult = BlackDuckConnectivityResult.failure("Failed to connect");

        final ProductRunData productRunData = testBoot(BlackDuckDecision.runOnline(), PolarisDecision.skip(), new ProductBootOptions(true, false), connectivityResult, null);

        Assertions.assertFalse(productRunData.shouldUseBlackDuckProduct());
        Assertions.assertFalse(productRunData.shouldUsePolarisProduct());
    }

    @Test
    public void blackDuckConnectionFailureWithTestThrows() {
        final BlackDuckConnectivityResult connectivityResult = BlackDuckConnectivityResult.failure("Failed to connect");

        Assertions.assertThrows(DetectUserFriendlyException.class, () -> {
            testBoot(BlackDuckDecision.runOnline(), PolarisDecision.skip(), new ProductBootOptions(false, true), connectivityResult, null);
        });
    }

    @Test
    public void polarisConnectionFailureWithTestThrows() {
        final PolarisConnectivityResult connectivityResult = PolarisConnectivityResult.failure("Failed to connect");

        Assertions.assertThrows(DetectUserFriendlyException.class, () -> {
            testBoot(BlackDuckDecision.skip(), PolarisDecision.runOnline(null), new ProductBootOptions(false, true), null, connectivityResult);
        });
    }

    @Test
    public void blackDuckConnectionSuccessWithTestReturnsNull() throws DetectUserFriendlyException {
        final BlackDuckConnectivityResult connectivityResult = BlackDuckConnectivityResult.success(Mockito.mock(BlackDuckServicesFactory.class), Mockito.mock(BlackDuckServerConfig.class));

        final ProductRunData productRunData = testBoot(BlackDuckDecision.runOnline(), PolarisDecision.skip(), new ProductBootOptions(false, true), connectivityResult, null);

        Assertions.assertNull(productRunData);
    }

    @Test
    public void polarisConnectionSuccessWithTestReturnsNull() throws DetectUserFriendlyException {
        final PolarisConnectivityResult connectivityResult = PolarisConnectivityResult.success();

        final ProductRunData productRunData = testBoot(BlackDuckDecision.skip(), PolarisDecision.runOnline(null), new ProductBootOptions(false, true), null, connectivityResult);

        Assertions.assertNull(productRunData);
    }

    @Test
    public void blackDuckOnlyWorks() throws DetectUserFriendlyException {
        final BlackDuckConnectivityResult connectivityResult = BlackDuckConnectivityResult.success(Mockito.mock(BlackDuckServicesFactory.class), Mockito.mock(BlackDuckServerConfig.class));
        final ProductRunData productRunData = testBoot(BlackDuckDecision.runOnline(), PolarisDecision.skip(), new ProductBootOptions(false, false), connectivityResult, null);

        Assertions.assertTrue(productRunData.shouldUseBlackDuckProduct());
        Assertions.assertFalse(productRunData.shouldUsePolarisProduct());
    }

    @Test
    public void polarisOnlyWorks() throws DetectUserFriendlyException {
        final PolarisDecision polarisDecision = PolarisDecision.runOnline(Mockito.mock(PolarisServerConfig.class));

        final PolarisConnectivityResult polarisConnectivityResult = Mockito.mock(PolarisConnectivityResult.class);
        Mockito.when(polarisConnectivityResult.isSuccessfullyConnected()).thenReturn(true);

        final ProductRunData productRunData = testBoot(BlackDuckDecision.skip(), polarisDecision, new ProductBootOptions(false, false), null, polarisConnectivityResult);

        Assertions.assertFalse(productRunData.shouldUseBlackDuckProduct());
        Assertions.assertTrue(productRunData.shouldUsePolarisProduct());
    }

    private ProductRunData testBoot(final BlackDuckDecision blackDuckDecision, final PolarisDecision polarisDecision, final ProductBootOptions productBootOptions) throws DetectUserFriendlyException {
        return testBoot(blackDuckDecision, polarisDecision, productBootOptions, null, null);
    }

    private ProductRunData testBoot(final BlackDuckDecision blackDuckDecision, final PolarisDecision polarisDecision, final ProductBootOptions productBootOptions, final BlackDuckConnectivityResult blackDuckconnectivityResult,
        final PolarisConnectivityResult polarisConnectivityResult) throws DetectUserFriendlyException {
        final ProductBootFactory productBootFactory = Mockito.mock(ProductBootFactory.class);
        Mockito.when(productBootFactory.createPhoneHomeManager(Mockito.any())).thenReturn(null);

        final ProductDecision productDecision = new ProductDecision(blackDuckDecision, polarisDecision);

        final ProductBoot productBoot = new ProductBoot();

        final BlackDuckConnectivityChecker blackDuckConnectivityChecker = Mockito.mock(BlackDuckConnectivityChecker.class);
        Mockito.when(blackDuckConnectivityChecker.determineConnectivity(Mockito.any())).thenReturn(blackDuckconnectivityResult);

        final PolarisConnectivityChecker polarisConnectivityChecker = Mockito.mock(PolarisConnectivityChecker.class);
        Mockito.when(polarisConnectivityChecker.determineConnectivity(Mockito.any())).thenReturn(polarisConnectivityResult);

        return productBoot.boot(productDecision, productBootOptions, blackDuckConnectivityChecker, polarisConnectivityChecker, productBootFactory);
    }
}
