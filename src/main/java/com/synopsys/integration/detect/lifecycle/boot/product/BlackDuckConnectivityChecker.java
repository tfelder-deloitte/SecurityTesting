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

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.blackduck.api.generated.discovery.ApiDiscovery;
import com.synopsys.integration.blackduck.api.generated.response.CurrentVersionView;
import com.synopsys.integration.blackduck.api.generated.view.RoleAssignmentView;
import com.synopsys.integration.blackduck.api.generated.view.UserGroupView;
import com.synopsys.integration.blackduck.api.generated.view.UserView;
import com.synopsys.integration.blackduck.configuration.BlackDuckServerConfig;
import com.synopsys.integration.blackduck.service.BlackDuckService;
import com.synopsys.integration.blackduck.service.BlackDuckServicesFactory;
import com.synopsys.integration.blackduck.service.UserGroupService;
import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.exception.IntegrationException;
import com.synopsys.integration.log.SilentIntLogger;
import com.synopsys.integration.log.Slf4jIntLogger;
import com.synopsys.integration.rest.client.ConnectionResult;

public class BlackDuckConnectivityChecker {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BlackDuckConnectivityResult determineConnectivity(final BlackDuckServerConfig blackDuckServerConfig)
        throws DetectUserFriendlyException {

        logger.debug("Detect will check communication with the Black Duck server.");

        final ConnectionResult connectionResult = blackDuckServerConfig.attemptConnection(new SilentIntLogger());

        if (connectionResult.isFailure()) {
            logger.error("Failed to connect to the Black Duck server");
            logger.debug(String.format("The Black Duck server responded with a status code of %d", connectionResult.getHttpStatusCode()));
            return BlackDuckConnectivityResult.failure(connectionResult.getFailureMessage().orElse("Could not reach the Black Duck server or the credentials were invalid."));
        }

        logger.info("Connection to the Black Duck server was successful.");

        final BlackDuckServicesFactory blackDuckServicesFactory = blackDuckServerConfig.createBlackDuckServicesFactory(new Slf4jIntLogger(logger));

        try {
            final BlackDuckService blackDuckService = blackDuckServicesFactory.createBlackDuckService();
            final CurrentVersionView currentVersion = blackDuckService.getResponse(ApiDiscovery.CURRENT_VERSION_LINK_RESPONSE);

            logger.info(String.format("Successfully connected to Black Duck (version %s)!", currentVersion.getVersion()));

            final UserView userView = blackDuckService.getResponse(ApiDiscovery.CURRENT_USER_LINK_RESPONSE);
            logger.debug("Connected as: " + userView.getUserName());

            final UserGroupService userGroupService = blackDuckServicesFactory.createUserGroupService();
            final List<RoleAssignmentView> response = userGroupService.getRolesForUser(userView);
            logger.debug("Roles: " + response.stream().map(RoleAssignmentView::getName).distinct().collect(Collectors.joining(", ")));

            final List<UserGroupView> groups = blackDuckService.getAllResponses(userView.getFirstLink("usergroups").get(), UserGroupView.class);
            logger.debug("Group: " + groups.stream().map(UserGroupView::getName).distinct().collect(Collectors.joining(", ")));

        } catch (final IntegrationException e) {
            throw new DetectUserFriendlyException("Could not determine which version of Black Duck detect connected to or which user is connecting.", e, ExitCodeType.FAILURE_BLACKDUCK_CONNECTIVITY);
        }

        return BlackDuckConnectivityResult.success(blackDuckServicesFactory, blackDuckServerConfig);
    }
}
