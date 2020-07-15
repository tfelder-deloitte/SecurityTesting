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
package com.synopsys.integration.detectable.detectables.bazel.pipeline.stepexecutor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.synopsys.integration.exception.IntegrationException;

public class StepExecutorParseEachXml implements StepExecutor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String xPathToElement;
    private final String targetAttributeName;

    public StepExecutorParseEachXml(final String xPathToElement, final String targetAttributeName) {
        this.xPathToElement = xPathToElement;
        this.targetAttributeName = targetAttributeName;
    }

    @Override
    public List<String> process(final List<String> input) throws IntegrationException {
        final List<String> results = new ArrayList<>();
        for (final String xmlDoc : input) {
            final List<String> values;
            try {
                values = parseAttributeValuesWithGivenXPathQuery(xmlDoc, xPathToElement, targetAttributeName);
            } catch (IOException | SAXException | ParserConfigurationException | XPathExpressionException e) {
                final String msg = String.format("Error parsing xml %s for xPath query %s, attribute name: %s", xmlDoc, xPathToElement, targetAttributeName);
                logger.debug(msg);
                throw new IntegrationException(msg, e);
            }
            results.addAll(values);
        }
        return results;
    }

    private List<String> parseAttributeValuesWithGivenXPathQuery(final String xmlString, final String xPathExpression, final String targetAttributeName) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        logger.trace(String.format("xPathExpression: %s, targetAttributeName: %s", xPathExpression, targetAttributeName));
        final List<String> parsedValues = new ArrayList<>();
        InputStream xmlInputStream = new ByteArrayInputStream(xmlString.getBytes());

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(xmlInputStream);
        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(xPathExpression).evaluate(xmlDocument, XPathConstants.NODESET);
        for (int i=0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            logger.trace(String.format("parsed value: %s", node.getAttributes().getNamedItem(targetAttributeName).getTextContent()));
            parsedValues.add(node.getAttributes().getNamedItem(targetAttributeName).getTextContent());
        }
        return parsedValues;
    }
}
