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
package com.synopsys.integration.detectable.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.synopsys.integration.detectable.detectable.file.FileFinder;

public class MockFileFinder implements FileFinder {

    Map<Integer, List<File>> files = new HashMap<>();

    public static MockFileFinder withFile(File file) {
        MockFileFinder finder = new MockFileFinder();
        finder.addFile(file, 0);
        return finder;
    }

    public static MockFileFinder withFileNamed(String name) {
        MockFileFinder finder = new MockFileFinder();
        finder.addFileNamed(name, 0);
        return finder;
    }

    public static MockFileFinder withFilesNamed(String... names) {
        MockFileFinder finder = new MockFileFinder();
        for (String name : names) {
            finder.addFileNamed(name, 0);
        }
        return finder;
    }

    public void addFile(File file, int depth) {
        if (!files.containsKey(depth)) {
            files.put(depth, new ArrayList<>());
        }
        files.get(depth).add(file);
    }

    public void addFileNamed(String name, int depth) {
        if (!files.containsKey(depth)) {
            files.put(depth, new ArrayList<>());
        }
        files.get(depth).add(new File(name));
    }

    @Override
    public List<File> findFiles(final File directoryToSearch, final List<String> filenamePatterns, final int depth, boolean findInsideMatchingDirectories) {
        List<File> found = new ArrayList<>();
        for (int i = 0; i <= depth; i++) {
            if (files.containsKey(i)) {
                List<File> possibles = files.get(i);
                for (String pattern : filenamePatterns) {
                    FileFilter fileFilter = new WildcardFileFilter(pattern);
                    for (File possible : possibles) {
                        if (fileFilter.accept(possible)) {
                            found.add(possible);
                        }
                    }

                }
            }
        }
        return found;
    }
}
