/**
 * detect-configuration
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
package com.synopsys.integration.detect;

import com.synopsys.integration.configuration.util.ProductMajorVersion;

public class DetectMajorVersion extends ProductMajorVersion {
    public static final DetectMajorVersion ONE = new DetectMajorVersion(1);
    public static final DetectMajorVersion TWO = new DetectMajorVersion(2);
    public static final DetectMajorVersion THREE = new DetectMajorVersion(3);
    public static final DetectMajorVersion FOUR = new DetectMajorVersion(4);
    public static final DetectMajorVersion FIVE = new DetectMajorVersion(5);
    public static final DetectMajorVersion SIX = new DetectMajorVersion(6);
    public static final DetectMajorVersion SEVEN = new DetectMajorVersion(7);
    public static final DetectMajorVersion EIGHT = new DetectMajorVersion(8);

    public DetectMajorVersion(final Integer intValue) {
        super(intValue);
    }
}