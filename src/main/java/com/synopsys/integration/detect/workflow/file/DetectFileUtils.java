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
package com.synopsys.integration.detect.workflow.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DetectFileUtils {
    private static final Logger logger = LoggerFactory.getLogger(DetectFileUtils.class);

    //About Canonical File Path. This should really only fail/throw in applets or in a truly restricted security context, so most of the time it should work.
    //If we can't get the canonical file path it is acceptable to fall back onto file path - the main drawback being capitalization or subtle things like that may differ between runs on the same file.
    public static String tryGetCanonicalName(File file) {
        return tryGetCanonicalFile(file).getName();
    }

    public static String tryGetCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getPath();
        }
    }

    public static File tryGetCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            return file;
        }
    }

    public static String tryExtractFinalPieceFromCanonicalPath(final File file) {
        return extractFinalPieceFromPath(tryGetCanonicalPath(file));
    }

    public static String extractFinalPieceFromPath(final String path) {
        if (path == null || path.length() == 0) {
            return "";
        }
        final String normalizedPath = FilenameUtils.normalizeNoEndSeparator(path, true);
        return normalizedPath.substring(normalizedPath.lastIndexOf('/') + 1, normalizedPath.length());
    }

    public static File writeToFile(final File file, final String contents) throws IOException {
        return writeToFile(file, contents, true);
    }

    private static File writeToFile(final File file, final String contents, final boolean overwrite) throws IOException {
        if (file == null) {
            return null;
        }
        if (overwrite && file.exists()) {
            FileUtils.deleteQuietly(file);
        }
        if (file.exists()) {
            logger.info(String.format("%s exists and not being overwritten", file.getAbsolutePath()));
        } else {
            FileUtils.write(file, contents, StandardCharsets.UTF_8);
        }
        return file;
    }
}
