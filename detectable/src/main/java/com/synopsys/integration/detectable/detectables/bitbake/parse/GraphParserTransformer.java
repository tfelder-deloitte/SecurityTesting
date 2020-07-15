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
package com.synopsys.integration.detectable.detectables.bitbake.parse;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.paypal.digraph.parser.GraphEdge;
import com.paypal.digraph.parser.GraphNode;
import com.paypal.digraph.parser.GraphParser;
import com.synopsys.integration.detectable.detectables.bitbake.model.BitbakeGraph;

public class GraphParserTransformer {
    public BitbakeGraph transform(final GraphParser graphParser) {
        final BitbakeGraph bitbakeGraph = new BitbakeGraph();

        for (final GraphNode graphNode : graphParser.getNodes().values()) {
            final String name = getNameFromNode(graphNode);
            final String version = getVersionFromNode(graphNode).orElse(null);
            if (version != null) {
                bitbakeGraph.addNode(name, version);
            }
        }

        for (final GraphEdge graphEdge : graphParser.getEdges().values()) {
            final String parent = getNameFromNode(graphEdge.getNode1());
            final String child = getNameFromNode(graphEdge.getNode2());
            if (!parent.equals(child)) {
                bitbakeGraph.addChild(parent, child);
            }
        }

        return bitbakeGraph;
    }

    private String getNameFromNode(final GraphNode graphNode) {
        final String[] nodeIdPieces = graphNode.getId().split(".do_");
        return nodeIdPieces[0].replaceAll("\"", "");
    }

    private Optional<String> getVersionFromNode(final GraphNode graphNode) {
        final Optional<String> attribute = getLabelAttribute(graphNode);
        return attribute.map(this::getVersionFromLabel);
    }

    private Optional<String> getLabelAttribute(final GraphNode graphNode) {
        final String attribute = (String) graphNode.getAttribute("label");
        Optional<String> result = Optional.empty();

        if (StringUtils.isNotBlank(attribute)) {
            result = Optional.of(attribute);
        }

        return result;
    }

    private String getVersionFromLabel(final String label) {
        final String[] components = label.split("\\\\n:|\\\\n");
        return components[1];
    }
}
