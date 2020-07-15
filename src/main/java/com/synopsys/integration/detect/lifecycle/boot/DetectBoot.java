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
package com.synopsys.integration.detect.lifecycle.boot;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.synopsys.integration.configuration.config.PropertyConfiguration;
import com.synopsys.integration.configuration.help.PropertyConfigurationHelpContext;
import com.synopsys.integration.configuration.property.Property;
import com.synopsys.integration.configuration.property.PropertyDeprecationInfo;
import com.synopsys.integration.configuration.property.types.path.PathResolver;
import com.synopsys.integration.configuration.property.types.path.SimplePathResolver;
import com.synopsys.integration.configuration.property.types.path.TildeInPathResolver;
import com.synopsys.integration.configuration.source.MapPropertySource;
import com.synopsys.integration.configuration.source.PropertySource;
import com.synopsys.integration.configuration.source.SpringConfigurationPropertySource;
import com.synopsys.integration.detect.DetectInfo;
import com.synopsys.integration.detect.RunBeanConfiguration;
import com.synopsys.integration.detect.configuration.DetectConfigurationFactory;
import com.synopsys.integration.detect.configuration.DetectGroup;
import com.synopsys.integration.detect.configuration.DetectProperties;
import com.synopsys.integration.detect.configuration.DetectableOptionFactory;
import com.synopsys.integration.detect.configuration.connection.ConnectionDetails;
import com.synopsys.integration.detect.configuration.connection.ConnectionFactory;
import com.synopsys.integration.detect.exception.DetectUserFriendlyException;
import com.synopsys.integration.detect.exitcode.ExitCodeType;
import com.synopsys.integration.detect.help.DetectArgumentState;
import com.synopsys.integration.detect.help.DetectArgumentStateParser;
import com.synopsys.integration.detect.help.json.HelpJsonDetector;
import com.synopsys.integration.detect.help.json.HelpJsonWriter;
import com.synopsys.integration.detect.help.print.DetectInfoPrinter;
import com.synopsys.integration.detect.help.print.HelpPrinter;
import com.synopsys.integration.detect.interactive.InteractiveManager;
import com.synopsys.integration.detect.interactive.InteractiveOption;
import com.synopsys.integration.detect.interactive.mode.DefaultInteractiveMode;
import com.synopsys.integration.detect.lifecycle.DetectContext;
import com.synopsys.integration.detect.lifecycle.boot.decision.ProductDecider;
import com.synopsys.integration.detect.lifecycle.boot.decision.ProductDecision;
import com.synopsys.integration.detect.lifecycle.boot.product.BlackDuckConnectivityChecker;
import com.synopsys.integration.detect.lifecycle.boot.product.PolarisConnectivityChecker;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBoot;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBootFactory;
import com.synopsys.integration.detect.lifecycle.boot.product.ProductBootOptions;
import com.synopsys.integration.detect.lifecycle.run.RunOptions;
import com.synopsys.integration.detect.lifecycle.run.data.ProductRunData;
import com.synopsys.integration.detect.lifecycle.shutdown.ExitCodeRequest;
import com.synopsys.integration.detect.tool.detector.DetectorRuleFactory;
import com.synopsys.integration.detect.tool.detector.impl.DetectDetectableFactory;
import com.synopsys.integration.detect.tool.detector.impl.DetectExecutableResolver;
import com.synopsys.integration.detect.tool.detector.inspectors.DockerInspectorInstaller;
import com.synopsys.integration.detect.tool.detector.inspectors.GradleInspectorInstaller;
import com.synopsys.integration.detect.tool.detector.inspectors.nuget.NugetInspectorInstaller;
import com.synopsys.integration.detect.type.OperatingSystemType;
import com.synopsys.integration.detect.util.filter.DetectFilter;
import com.synopsys.integration.detect.util.filter.DetectOverrideableFilter;
import com.synopsys.integration.detect.util.filter.DetectToolFilter;
import com.synopsys.integration.detect.workflow.ArtifactResolver;
import com.synopsys.integration.detect.workflow.DetectRun;
import com.synopsys.integration.detect.workflow.airgap.AirGapCreator;
import com.synopsys.integration.detect.workflow.airgap.AirGapPathFinder;
import com.synopsys.integration.detect.workflow.airgap.DockerAirGapCreator;
import com.synopsys.integration.detect.workflow.airgap.GradleAirGapCreator;
import com.synopsys.integration.detect.workflow.airgap.NugetAirGapCreator;
import com.synopsys.integration.detect.workflow.diagnostic.DiagnosticSystem;
import com.synopsys.integration.detect.workflow.event.Event;
import com.synopsys.integration.detect.workflow.event.EventSystem;
import com.synopsys.integration.detect.workflow.file.DirectoryManager;
import com.synopsys.integration.detect.workflow.profiling.DetectorProfiler;
import com.synopsys.integration.detect.workflow.report.writer.InfoLogReportWriter;
import com.synopsys.integration.detect.workflow.status.DetectIssue;
import com.synopsys.integration.detect.workflow.status.DetectIssueType;
import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.detectable.annotation.DetectableInfo;
import com.synopsys.integration.detectable.detectable.executable.impl.CachedExecutableResolverOptions;
import com.synopsys.integration.detectable.detectable.executable.impl.SimpleExecutableFinder;
import com.synopsys.integration.detectable.detectable.executable.impl.SimpleExecutableResolver;
import com.synopsys.integration.detectable.detectable.executable.impl.SimpleExecutableRunner;
import com.synopsys.integration.detectable.detectable.executable.impl.SimpleLocalExecutableFinder;
import com.synopsys.integration.detectable.detectable.executable.impl.SimpleSystemExecutableFinder;
import com.synopsys.integration.detectable.detectable.file.impl.SimpleFileFinder;
import com.synopsys.integration.detector.rule.DetectorRule;
import com.synopsys.integration.detector.rule.DetectorRuleSet;
import com.synopsys.integration.rest.proxy.ProxyInfo;

import freemarker.template.Configuration;

public class DetectBoot {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DetectBootFactory detectBootFactory;

    public DetectBoot(final DetectBootFactory detectBootFactory) {
        this.detectBootFactory = detectBootFactory;
    }

    public DetectBootResult boot(final DetectRun detectRun, final String[] sourceArgs, final ConfigurableEnvironment environment, final EventSystem eventSystem, final DetectContext detectContext) {
        final ObjectMapper objectMapper = detectBootFactory.createObjectMapper();
        final DocumentBuilder xml = detectBootFactory.createXmlDocumentBuilder();
        final Configuration configuration = detectBootFactory.createConfiguration();

        final DetectInfo detectInfo = detectContext.getBean(DetectInfo.class);
        final Gson gson = detectContext.getBean(Gson.class);

        List<PropertySource> propertySources;
        try {
            propertySources = new ArrayList<>(SpringConfigurationPropertySource.fromConfigurableEnvironment(environment, false));
        } catch (final RuntimeException e) {
            logger.error("An unknown property source was found, detect will still continue.", e);
            propertySources = new ArrayList<>(SpringConfigurationPropertySource.fromConfigurableEnvironment(environment, true));
        }

        final DetectArgumentState detectArgumentState = parseDetectArgumentState(sourceArgs);

        if (detectArgumentState.isHelp() || detectArgumentState.isDeprecatedHelp() || detectArgumentState.isVerboseHelp()) {
            printAppropriateHelp(DetectProperties.Companion.getProperties(), detectArgumentState);
            return DetectBootResult.exit(new PropertyConfiguration(propertySources));
        }

        if (detectArgumentState.isHelpJsonDocument()) {
            printHelpJsonDocument(DetectProperties.Companion.getProperties(), detectInfo, gson);
            return DetectBootResult.exit(new PropertyConfiguration(propertySources));
        }

        printDetectInfo(detectInfo);

        if (detectArgumentState.isInteractive()) {
            final List<InteractiveOption> interactiveOptions = startInteractiveMode(propertySources);
            final Map<String, String> interactivePropertyMap = interactiveOptions.stream()
                                                                   .collect(Collectors.toMap(option -> option.getDetectProperty().getKey(), InteractiveOption::getInteractiveValue));
            final PropertySource interactivePropertySource = new MapPropertySource("interactive", interactivePropertyMap);
            propertySources.add(0, interactivePropertySource);
        }
        final PropertyConfiguration detectConfiguration = new PropertyConfiguration(propertySources);

        logger.debug("Configuration processed completely.");

        final Boolean printFull = detectConfiguration.getValueOrDefault(DetectProperties.Companion.getDETECT_SUPPRESS_CONFIGURATION_OUTPUT());
        final Optional<DetectBootResult> configurationResult = printConfiguration(printFull, detectConfiguration, eventSystem, detectInfo);
        if (configurationResult.isPresent()) {
            return configurationResult.get();
        }

        logger.debug("Initializing Detect.");

        final PathResolver pathResolver;
        if (detectInfo.getCurrentOs() != OperatingSystemType.WINDOWS && detectConfiguration.getValueOrDefault(DetectProperties.Companion.getDETECT_RESOLVE_TILDE_IN_PATHS())) {
            logger.info("Tilde's will be automatically resolved to USER HOME.");
            pathResolver = new TildeInPathResolver(SystemUtils.USER_HOME);
        } else {
            pathResolver = new SimplePathResolver();
        }
        final DetectConfigurationFactory detectConfigurationFactory = new DetectConfigurationFactory(detectConfiguration, pathResolver);
        final DirectoryManager directoryManager = new DirectoryManager(detectConfigurationFactory.createDirectoryOptions(), detectRun);
        final Optional<DiagnosticSystem> diagnosticSystem = createDiagnostics(detectConfiguration, detectRun, detectInfo, detectArgumentState, eventSystem, directoryManager);

        logger.debug("Main boot completed. Deciding what Detect should do.");

        if (detectArgumentState.isGenerateAirGapZip()) {
            final DetectOverrideableFilter inspectorFilter = new DetectOverrideableFilter("", detectArgumentState.getParsedValue());
            final String airGapSuffix = inspectorFilter.getIncludedSet().stream().sorted().collect(Collectors.joining("-"));
            final File airGapZip;
            try {
                airGapZip = createAirGapZip(inspectorFilter, detectConfiguration, pathResolver, directoryManager, gson, eventSystem, configuration, airGapSuffix);
            } catch (final DetectUserFriendlyException e) {
                return DetectBootResult.exception(e, detectConfiguration, directoryManager, diagnosticSystem);
            }
            return DetectBootResult.exit(detectConfiguration, airGapZip, directoryManager, diagnosticSystem);
        }

        final RunOptions runOptions = detectConfigurationFactory.createRunOptions();
        final DetectToolFilter detectToolFilter = runOptions.getDetectToolFilter();

        logger.info("");
        final ProductDecision productDecision = new ProductDecider().decide(detectConfigurationFactory, directoryManager.getUserHome(), detectToolFilter);

        logger.debug("Decided what products will be run. Starting product boot.");

        final ProductBootFactory productBootFactory = new ProductBootFactory(detectInfo, eventSystem, detectConfigurationFactory);
        final ProductBoot productBoot = new ProductBoot();
        final ProductRunData productRunData;
        final ProductBootOptions productBootOptions = detectConfigurationFactory.createProductBootOptions();
        try {
            productRunData = productBoot.boot(productDecision, productBootOptions, new BlackDuckConnectivityChecker(), new PolarisConnectivityChecker(), productBootFactory);
        } catch (final DetectUserFriendlyException e) {
            return DetectBootResult.exception(e, detectConfiguration, directoryManager, diagnosticSystem);
        }

        if (productRunData == null) {
            logger.info("No products to run, Detect is complete.");
            return DetectBootResult.exit(detectConfiguration, directoryManager, diagnosticSystem);
        }

        final ProxyInfo detectableProxyInfo;
        try {
            detectableProxyInfo = detectConfigurationFactory.createBlackDuckProxyInfo();
        } catch (final DetectUserFriendlyException e) {
            return DetectBootResult.exception(e, detectConfiguration, directoryManager, diagnosticSystem);
        }

        final DetectableOptionFactory detectableOptionFactory = new DetectableOptionFactory(detectConfiguration, diagnosticSystem, pathResolver, detectableProxyInfo);
        final DetectorProfiler profiler = new DetectorProfiler(eventSystem);

        //Finished, populate the detect context
        detectContext.registerBean(detectRun);
        detectContext.registerBean(eventSystem);
        detectContext.registerBean(profiler);

        detectContext.registerBean(detectConfiguration);
        detectContext.registerBean(detectableOptionFactory);
        detectContext.registerBean(detectConfigurationFactory);

        detectContext.registerBean(directoryManager);
        detectContext.registerBean(objectMapper);
        detectContext.registerBean(xml);
        detectContext.registerBean(configuration);

        detectContext.registerConfiguration(RunBeanConfiguration.class);
        detectContext.lock(); //can only refresh once, this locks and triggers refresh.

        return DetectBootResult.run(detectConfiguration, productRunData, directoryManager, diagnosticSystem);
    }

    private void printAppropriateHelp(final List<Property> properties, final DetectArgumentState detectArgumentState) {
        final HelpPrinter helpPrinter = new HelpPrinter();
        helpPrinter.printAppropriateHelpMessage(System.out, properties, Arrays.asList(DetectGroup.values()), DetectGroup.BLACKDUCK_SERVER, detectArgumentState);
    }

    private void printHelpJsonDocument(final List<Property> properties, final DetectInfo detectInfo, final Gson gson) {
        final DetectorRuleFactory ruleFactory = new DetectorRuleFactory();
        // TODO: Is there a better way to build a fake set of rules?
        final DetectDetectableFactory mockFactory = new DetectDetectableFactory(null, null, null, null, null, null, null);
        final DetectorRuleSet build = ruleFactory.createRules(mockFactory, false);
        final DetectorRuleSet buildless = ruleFactory.createRules(mockFactory, true);
        final List<HelpJsonDetector> buildDetectors = build.getOrderedDetectorRules().stream().map(detectorRule -> convertDetectorRule(detectorRule, build)).collect(Collectors.toList());
        final List<HelpJsonDetector> buildlessDetectors = buildless.getOrderedDetectorRules().stream().map(detectorRule -> convertDetectorRule(detectorRule, buildless)).collect(Collectors.toList());

        final HelpJsonWriter helpJsonWriter = new HelpJsonWriter(gson);
        helpJsonWriter.writeGsonDocument(String.format("synopsys-detect-%s-help.json", detectInfo.getDetectVersion()), properties, buildDetectors, buildlessDetectors);
    }

    private HelpJsonDetector convertDetectorRule(final DetectorRule rule, final DetectorRuleSet ruleSet) {
        final HelpJsonDetector helpData = new HelpJsonDetector();
        helpData.setDetectorName(rule.getName());
        helpData.setDetectorDescriptiveName(rule.getDescriptiveName());
        helpData.setDetectorType(rule.getDetectorType().toString());
        helpData.setMaxDepth(rule.getMaxDepth());
        helpData.setNestable(rule.isNestable());
        helpData.setNestInvisible(rule.isNestInvisible());
        helpData.setYieldsTo(ruleSet.getYieldsTo(rule).stream().map(DetectorRule::getDescriptiveName).collect(Collectors.toList()));
        helpData.setFallbackTo(ruleSet.getFallbackFrom(rule).map(DetectorRule::getDescriptiveName).orElse(""));

        //Attempt to create the detectable.
        //Not currently possible. Need a full DetectableConfiguration to be able to make Detectables.
        final Class<Detectable> detectableClass = rule.getDetectableClass();
        final Optional<DetectableInfo> infoSearch = Arrays.stream(detectableClass.getAnnotations())
                                                        .filter(annotation -> annotation instanceof DetectableInfo)
                                                        .map(annotation -> (DetectableInfo) annotation)
                                                        .findFirst();

        if (infoSearch.isPresent()) {
            final DetectableInfo info = infoSearch.get();
            helpData.setDetectableLanguage(info.language());
            helpData.setDetectableRequirementsMarkdown(info.requirementsMarkdown());
            helpData.setDetectableForge(info.forge());
        }

        return helpData;
    }

    private void printDetectInfo(final DetectInfo detectInfo) {
        final DetectInfoPrinter detectInfoPrinter = new DetectInfoPrinter();
        detectInfoPrinter.printInfo(System.out, detectInfo);
    }

    private Optional<DetectBootResult> printConfiguration(final boolean fullConfiguration, final PropertyConfiguration detectConfiguration, final EventSystem eventSystem,
        final DetectInfo detectInfo) {

        final Map<String, String> additionalNotes = new HashMap<>();

        final List<Property> deprecatedProperties = DetectProperties.Companion.getProperties()
                                                        .stream()
                                                        .filter(property -> property.getPropertyDeprecationInfo() != null)
                                                        .collect(Collectors.toList());

        final Map<String, List<String>> deprecationMessages = new HashMap<>();
        final List<Property> usedFailureProperties = new ArrayList<>();
        for (final Property property : deprecatedProperties) {
            if (detectConfiguration.wasKeyProvided(property.getKey())) {
                final PropertyDeprecationInfo deprecationInfo = property.getPropertyDeprecationInfo();

                if (deprecationInfo == null) {
                    logger.debug("A deprecated property is missing deprecation info.");
                    continue;
                }

                additionalNotes.put(property.getKey(), "\t *** DEPRECATED ***");
                final String deprecationMessage = deprecationInfo.getDeprecationText();

                deprecationMessages.put(property.getKey(), new ArrayList<>(Collections.singleton(deprecationMessage)));
                DetectIssue.publish(eventSystem, DetectIssueType.DEPRECATION, property.getKey(), "\t" + deprecationMessage);

                if (detectInfo.getDetectMajorVersion() >= deprecationInfo.getFailInVersion().getIntValue()) {
                    usedFailureProperties.add(property);
                }
            }
        }

        //First print the entire configuration.
        final PropertyConfigurationHelpContext detectConfigurationReporter = new PropertyConfigurationHelpContext(detectConfiguration);
        final InfoLogReportWriter infoLogReportWriter = new InfoLogReportWriter();
        if (!fullConfiguration) {
            detectConfigurationReporter.printCurrentValues(infoLogReportWriter::writeLine, DetectProperties.Companion.getProperties(), additionalNotes);
        }

        //Next check for options that are just plain bad, ie giving an detector type we don't know about.
        final Map<String, List<String>> errorMap = detectConfigurationReporter.findPropertyParseErrors(DetectProperties.Companion.getProperties());
        if (errorMap.size() > 0) {
            final Map.Entry<String, List<String>> entry = errorMap.entrySet().iterator().next();
            return Optional.of(DetectBootResult.exception(new DetectUserFriendlyException(entry.getKey() + ": " + entry.getValue().get(0), ExitCodeType.FAILURE_GENERAL_ERROR), detectConfiguration));
        }

        if (usedFailureProperties.size() > 0) {
            detectConfigurationReporter.printPropertyErrors(infoLogReportWriter::writeLine, DetectProperties.Companion.getProperties(), deprecationMessages);

            logger.warn(StringUtils.repeat("=", 60));
            logger.warn("Configuration is using deprecated properties that must be updated for this major version.");
            logger.warn("You MUST fix these deprecation issues for detect to proceed.");
            logger.warn("To ignore these messages and force detect to exit with success supply --" + DetectProperties.Companion.getDETECT_FORCE_SUCCESS().getKey() + "=true");
            logger.warn("This will not force detect to run, but it will pretend to have succeeded.");
            logger.warn(StringUtils.repeat("=", 60));

            eventSystem.publishEvent(Event.ExitCode, new ExitCodeRequest(ExitCodeType.FAILURE_CONFIGURATION));
            return Optional.of(DetectBootResult.exit(detectConfiguration));
        }

        return Optional.empty();
    }

    private List<InteractiveOption> startInteractiveMode(final List<PropertySource> propertySources) {
        final InteractiveManager interactiveManager = new InteractiveManager();
        final DefaultInteractiveMode defaultInteractiveMode = new DefaultInteractiveMode(propertySources);
        return interactiveManager.configureInInteractiveMode(defaultInteractiveMode);
    }

    private DetectArgumentState parseDetectArgumentState(final String[] sourceArgs) {
        final DetectArgumentStateParser detectArgumentStateParser = new DetectArgumentStateParser();
        final DetectArgumentState detectArgumentState = detectArgumentStateParser.parseArgs(sourceArgs);
        return detectArgumentState;
    }

    private Optional<DiagnosticSystem> createDiagnostics(
        final PropertyConfiguration propertyConfiguration, final DetectRun detectRun, final DetectInfo detectInfo, final DetectArgumentState detectArgumentState, final EventSystem eventSystem, final DirectoryManager directoryManager) {
        if (detectArgumentState.isDiagnostic() || detectArgumentState.isDiagnosticExtended()) {
            final boolean extendedMode = detectArgumentState.isDiagnosticExtended();
            final DiagnosticSystem diagnosticSystem = new DiagnosticSystem(extendedMode, propertyConfiguration, detectRun, detectInfo, directoryManager, eventSystem);
            return Optional.of(diagnosticSystem);
        } else {
            return Optional.empty();
        }
    }

    private File createAirGapZip(final DetectFilter inspectorFilter, final PropertyConfiguration detectConfiguration, final PathResolver pathResolver, final DirectoryManager directoryManager, final Gson gson,
        final EventSystem eventSystem,
        final Configuration configuration,
        final String airGapSuffix)
        throws DetectUserFriendlyException {
        final DetectConfigurationFactory detectConfigurationFactory = new DetectConfigurationFactory(detectConfiguration, pathResolver);
        final ConnectionDetails connectionDetails = detectConfigurationFactory.createConnectionDetails();
        final ConnectionFactory connectionFactory = new ConnectionFactory(connectionDetails);
        final ArtifactResolver artifactResolver = new ArtifactResolver(connectionFactory, gson);

        //TODO: This is awful, why is making this so convoluted.
        final SimpleFileFinder fileFinder = new SimpleFileFinder();
        final SimpleExecutableFinder simpleExecutableFinder = SimpleExecutableFinder.forCurrentOperatingSystem(fileFinder);
        final SimpleLocalExecutableFinder localExecutableFinder = new SimpleLocalExecutableFinder(simpleExecutableFinder);
        final SimpleSystemExecutableFinder simpleSystemExecutableFinder = new SimpleSystemExecutableFinder(simpleExecutableFinder);
        final SimpleExecutableResolver executableResolver = new SimpleExecutableResolver(new CachedExecutableResolverOptions(false), localExecutableFinder, simpleSystemExecutableFinder);
        final DetectExecutableResolver detectExecutableResolver = new DetectExecutableResolver(executableResolver, detectConfigurationFactory.createExecutablePaths());
        final GradleInspectorInstaller gradleInspectorInstaller = new GradleInspectorInstaller(artifactResolver);
        final SimpleExecutableRunner simpleExecutableRunner = new SimpleExecutableRunner();
        final GradleAirGapCreator gradleAirGapCreator = new GradleAirGapCreator(detectExecutableResolver, gradleInspectorInstaller, simpleExecutableRunner, configuration);

        final NugetAirGapCreator nugetAirGapCreator = new NugetAirGapCreator(new NugetInspectorInstaller(artifactResolver));
        final DockerAirGapCreator dockerAirGapCreator = new DockerAirGapCreator(new DockerInspectorInstaller(artifactResolver));

        final AirGapCreator airGapCreator = new AirGapCreator(new AirGapPathFinder(), eventSystem, gradleAirGapCreator, nugetAirGapCreator, dockerAirGapCreator);
        final String gradleInspectorVersion = detectConfiguration.getValueOrEmpty(DetectProperties.Companion.getDETECT_GRADLE_INSPECTOR_VERSION()).orElse(null);
        return airGapCreator.createAirGapZip(inspectorFilter, directoryManager.getRunHomeDirectory(), airGapSuffix, gradleInspectorVersion);
    }
}
