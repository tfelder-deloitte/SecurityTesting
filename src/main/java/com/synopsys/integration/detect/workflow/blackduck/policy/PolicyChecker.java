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
package com.synopsys.integration.detect.workflow.blackduck.policy;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synopsys.integration.blackduck.api.generated.enumeration.PolicyRuleSeverityType;
import com.synopsys.integration.blackduck.api.generated.enumeration.PolicyStatusType;
import com.synopsys.integration.blackduck.api.generated.view.ComponentPolicyRulesView;
import com.synopsys.integration.blackduck.api.generated.view.ProjectVersionComponentView;
import com.synopsys.integration.blackduck.api.generated.view.ProjectVersionView;
import com.synopsys.integration.blackduck.service.BlackDuckService;
import com.synopsys.integration.blackduck.service.ProjectBomService;
import com.synopsys.integration.blackduck.service.model.PolicyStatusDescription;
import com.synopsys.integration.configuration.util.Bdo;
import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.detect.lifecycle.shutdown.ExitCodeRequest;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.exception.IntegrationException;

public class PolicyChecker {
    private final Logger logger = LoggerFactory.getLogger(PolicyChecker.class);

    private final EventSystem eventSystem;
    private final BlackDuckService blackDuckService;
    private final ProjectBomService projectBomService;

    public PolicyChecker(final EventSystem eventSystem, final BlackDuckService blackDuckService, final ProjectBomService projectBomService) {
        this.eventSystem = eventSystem;
        this.blackDuckService = blackDuckService;
        this.projectBomService = projectBomService;
    }

    public void checkPolicy(final List<PolicyRuleSeverityType> policySeverities, final ProjectVersionView projectVersionView) throws IntegrationException {
        final Optional<PolicyStatusDescription> policyStatusDescription = fetchPolicyStatusDescription(projectVersionView);

        if (policyStatusDescription.isPresent()) {
            logger.info(policyStatusDescription.get().getPolicyStatusMessage());

            if (arePolicySeveritiesViolated(policyStatusDescription.get(), policySeverities)) {
                fetchAndLogPolicyViolations(projectVersionView);
                eventSystem.publishEvent(Event.ExitCode, new ExitCodeRequest(ExitCodeType.FAILURE_POLICY_VIOLATION, policyStatusDescription.get().getPolicyStatusMessage()));
            }
        } else {
            final String availableLinks = StringUtils.join(projectVersionView.getAvailableLinks(), ", ");
            logger.warn(String.format("It is not possible to check the policy status for this project/version. The policy-status link must be present. The available links are: %s", availableLinks));
        }
    }

    public Optional<PolicyStatusDescription> fetchPolicyStatusDescription(final ProjectVersionView version) throws IntegrationException {
        return Bdo.of(projectBomService.getPolicyStatusForVersion(version))
                   .peek(policyStatus -> logger.info(String.format("Policy Status: %s", policyStatus.getOverallStatus().name())))
                   .map(PolicyStatusDescription::new)
                   .toOptional();
    }

    public void fetchAndLogPolicyViolations(final ProjectVersionView projectVersionView) throws IntegrationException {
        logger.info("Searching BOM for components in violation of policy rules.");

        final List<ProjectVersionComponentView> bomComponents = projectBomService.getComponentsForProjectVersion(projectVersionView);
        for (final ProjectVersionComponentView projectVersionComponentView : bomComponents) {
            if (projectVersionComponentView.getPolicyStatus().equals(PolicyStatusType.NOT_IN_VIOLATION)) {
                continue;
            }

            for (final ComponentPolicyRulesView componentPolicyRulesView : blackDuckService.getAllResponses(projectVersionComponentView, ProjectVersionComponentView.POLICY_RULES_LINK_RESPONSE)) {
                logger.info(String.format("Policy rule \"%s\" was violated by component \"%s:%s\" (%s)",
                    componentPolicyRulesView.getName(),
                    projectVersionComponentView.getComponentName(),
                    projectVersionComponentView.getComponentVersionName(),
                    projectVersionComponentView.getComponentVersion()
                ));

                final String policyRuleSuffix;
                if (StringUtils.isNotBlank(componentPolicyRulesView.getDescription())) {
                    policyRuleSuffix = String.format(" with description: %s", componentPolicyRulesView.getDescription());
                } else {
                    policyRuleSuffix = ".";
                }

                logger.info(String.format("Policy rule \"%s\" has a severity type of %s%s",
                    componentPolicyRulesView.getName(),
                    componentPolicyRulesView.getSeverity().prettyPrint(),
                    policyRuleSuffix
                ));

            }
        }
    }

    private boolean arePolicySeveritiesViolated(final PolicyStatusDescription policyStatusDescription, final List<PolicyRuleSeverityType> policySeverities) {
        return policySeverities.stream()
                   .map(policyStatusDescription::getCountOfSeverity)
                   .anyMatch(severityCount -> severityCount > 0);
    }

}
