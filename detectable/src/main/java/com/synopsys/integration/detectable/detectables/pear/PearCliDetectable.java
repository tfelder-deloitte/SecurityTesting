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
package com.synopsys.integration.detectable.detectables.pear;

import java.io.File;

import com.synopsys.integration.detectable.Detectable;
import com.synopsys.integration.detectable.DetectableEnvironment;
import com.synopsys.integration.detectable.Extraction;
import com.synopsys.integration.detectable.ExtractionEnvironment;
import com.synopsys.integration.detectable.detectable.annotation.DetectableInfo;
import com.synopsys.integration.detectable.detectable.exception.DetectableException;
import com.synopsys.integration.detectable.detectable.executable.resolver.PearResolver;
import com.synopsys.integration.detectable.detectable.file.FileFinder;
import com.synopsys.integration.detectable.detectable.result.DetectableResult;
import com.synopsys.integration.detectable.detectable.result.ExecutableNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.FileNotFoundDetectableResult;
import com.synopsys.integration.detectable.detectable.result.PassedDetectableResult;

@DetectableInfo(language = "PHP", forge = "Pear", requirementsMarkdown = "Files: package.xml.<br/><br/> Executable: pear.")
public class PearCliDetectable extends Detectable {
    public static final String PACKAGE_XML_FILENAME = "package.xml";

    private final FileFinder fileFinder;
    private final PearResolver pearResolver;
    private final PearCliExtractor pearCliExtractor;
    private final PearCliDetectableOptions pearCliDetectableOptions;

    private File pearExe;
    private File packageDotXml;

    public PearCliDetectable(final DetectableEnvironment environment, final FileFinder fileFinder, final PearResolver pearResolver, final PearCliExtractor pearCliExtractor, final PearCliDetectableOptions pearCliDetectableOptions) {
        super(environment);
        this.fileFinder = fileFinder;
        this.pearResolver = pearResolver;
        this.pearCliExtractor = pearCliExtractor;
        this.pearCliDetectableOptions = pearCliDetectableOptions;
    }

    @Override
    public DetectableResult applicable() {
        packageDotXml = fileFinder.findFile(environment.getDirectory(), PACKAGE_XML_FILENAME);

        if (packageDotXml == null) {
            return new FileNotFoundDetectableResult(PACKAGE_XML_FILENAME);
        }

        return new PassedDetectableResult();
    }

    @Override
    public DetectableResult extractable() throws DetectableException {
        pearExe = pearResolver.resolvePear();

        if (pearExe == null) {
            return new ExecutableNotFoundDetectableResult("pear");
        }

        return new PassedDetectableResult();
    }

    @Override
    public Extraction extract(final ExtractionEnvironment extractionEnvironment) {
        return pearCliExtractor.extract(pearExe, packageDotXml, environment.getDirectory(), pearCliDetectableOptions.onlyGatherRequired());
    }

}
