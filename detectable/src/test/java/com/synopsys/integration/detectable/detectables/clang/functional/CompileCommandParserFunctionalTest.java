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
package com.synopsys.integration.detectable.detectables.clang.functional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.synopsys.integration.detectable.annotations.FunctionalTest;
import com.synopsys.integration.detectable.detectables.clang.compilecommand.CompileCommand;
import com.synopsys.integration.detectable.detectables.clang.compilecommand.CompileCommandDatabaseParser;
import com.synopsys.integration.detectable.detectables.clang.compilecommand.CompileCommandParser;
import com.synopsys.integration.detectable.util.FunctionalTestFiles;

@FunctionalTest
public class CompileCommandParserFunctionalTest {
    @Test
    public void testCanParseCommandDatabase() throws IOException {
        final CompileCommandDatabaseParser compileCommandDatabaseParser = new CompileCommandDatabaseParser(new Gson());

        final List<CompileCommand> compileCommands = compileCommandDatabaseParser.parseCompileCommandDatabase(FunctionalTestFiles.asFile("/clang/compile_commands.json"));

        assertEquals(182, compileCommands.size());
        final CompileCommand first = compileCommands.get(0);
        assertEquals("/home/jslave/sean/mainline/nsulate/src", first.directory);
        assertEquals(
            "/usr/bin/env CCACHE_CPP2=yes /usr/bin/ccache /usr/bin/clang++-3.6   -DAVX2=1 -DCMAKE_BUILD_TYPE=\\\"Debug\\\" -DCMAKE_CC_FLAGS=\"\\\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp --std=c11 -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC --std=c11\\\"\" -DCMAKE_CXX_FLAGS=\"\\\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG -mcx16 -msse4.2 -mavx2  -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG\\\"\" -DCMAKE_CXX_FLAGS_DEBUG=\"\\\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG -mcx16 -msse4.2 -mavx2  \\\"\" -DCMAKE_CXX_FLAGS_RELEASE=\"\\\"-O3 -DNDEBUG -O3 \\\"\" -DCMAKE_VERSION=\\\"3.5.1\\\" -DNSULATE_PROJECT_COMMIT=\"\\\"b079181 Create smoke Test suites\\\"\" -DNSULATE_SYSTEM=\"\\\"Ubuntu 16045 LTS\\\"\" -DNSULATE_SYSTEM_PROCESSOR=\"\\\"Linux srv-narnia 4.15.0-36-generic x86_64 GNU/Linux\\\"\" -DNSULATE_TIME_OF_BUILD=\"\\\"Wednesday 14-11-2018 03:22 UTC\\\"\" -DNSULATE_VERSION=\\\"1.2.82\\\" -I/home/jslave/sean/mainline/nsulate/include -I/home/jslave/sean/mainline/nsulate/src -I/home/jslave/sean/mainline/nsulate/graph/include -isystem /usr/include/c++/v1 -isystem /usr/include/libcxxabi -isystem /usr/local/cuda/include -I/home/jslave/sean/mainline/nsulate/readerwriterqueue-prefix/src/readerwriterqueue -I/home/jslave/sean/mainline/nsulate/concurrentqueue-prefix/src/concurrentqueue -I/root/.conan/data/checksum-bitmap/1.0.644/nyriad/dev/package/ac304798f3306eec310ec8a30261a26f653dd38b/include -I/root/.conan/data/core/0.0.2/nyriad/dev/package/e8cd1f5fa5f5ad9b1f4f9652758fedd1b1ef065f/include -I/root/.conan/data/erasure/0.650/jenkins/dev/package/98058521142923cfb539534d57486b99f31ccab1/include -I/root/.conan/data/gf-complete/3.0.0/nyriad/dev/package/37524c4ab24b907f1d2725b76a3ee93977be8909/include -I/root/.conan/data/isal/2.16.0/nyriad/dev/package/37524c4ab24b907f1d2725b76a3ee93977be8909/include -I/root/.conan/data/jerasure/2.0.0/nyriad/dev/package/37524c4ab24b907f1d2725b76a3ee93977be8909/include -I/root/.conan/data/nanomsg/1.0.0/nyriad/stable/package/25b14d8ed043113c3ac693a16a46570ef79ece8c/include -I/root/.conan/data/ncrypt/0.704/jenkins/dev/package/98058521142923cfb539534d57486b99f31ccab1/include -I/root/.conan/data/protobuf/3.3.0/nyriad/stable/package/bc5f4c3d74935aed16f42942f35e8e7f81eb3a9d/include -I/root/.conan/data/concurrentqueue/1.0.0/nyriad/dev/package/5ab84d6acfe1f23c4fae0ab88f26e3a396351ac9/include -I/root/.conan/data/gtest/1.8.0/nyriad/dev/package/fdfa7eb0463b4ac0cd336cfae4b973bea2f67262/include -I/home/jslave/sean/mainline/nsulate/../ambigraph/include -I/home/jslave/sean/mainline/nsulate/../ambigraph/src -I/home/jslave/sean/mainline/nsulate/../upac/src -I/home/jslave/sean/mainline/nsulate/../upac/src/memory_management -I/home/jslave/sean/mainline/nsulate/../upac/src/plugin_executors -I/home/jslave/sean/mainline/nsulate/../upac/src/scheduler -I/home/jslave/sean/mainline/nsulate/../upac/src/task -I/home/jslave/sean/mainline/nsulate/../upac/common/include -I/home/jslave/sean/mainline/nsulate/src/nodes   -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG -mcx16 -msse4.2 -mavx2     -DNN_STATIC_LIB=ON -Wno-extended-offsetof -o CMakeFiles/cli_proto.dir/cli.pb.cc.o -c /home/jslave/sean/mainline/nsulate/src/cli.pb.cc",
            first.command);
        assertEquals("/home/jslave/sean/mainline/nsulate/src/cli.pb.cc", first.file);
        assertEquals(0, first.arguments.length);
    }

    @Test
    public void testCanParseArgumentsFromCommandDatabase() throws IOException {
        final CompileCommandDatabaseParser compileCommandDatabaseParser = new CompileCommandDatabaseParser(new Gson());

        final List<CompileCommand> compileCommands = compileCommandDatabaseParser.parseCompileCommandDatabase(FunctionalTestFiles.asFile("/clang/compile_commands_args.json"));

        final CompileCommand first = compileCommands.get(0);
        final CompileCommandParser compileCommandParser = new CompileCommandParser();

        final List<String> result = compileCommandParser.parseCommand(first, Collections.emptyMap());

        assertEquals(66, result.size());
        int i = 0;
        assertEquals("/usr/bin/env", result.get(i++));
        assertEquals("CCACHE_CPP2=yes", result.get(i++));
        assertEquals("/usr/bin/ccache", result.get(i++));
        assertEquals("/usr/bin/clang++-3.6", result.get(i++));
        assertEquals("-DAVX2=1", result.get(i++));
        assertEquals("-DCMAKE_BUILD_TYPE=\"Debug\"", result.get(i++));
        assertEquals("-DCMAKE_CC_FLAGS=\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp --std=c11 -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC --std=c11\"", result.get(i++));
        assertEquals(
            "-DCMAKE_CXX_FLAGS=\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG -mcx16 -msse4.2 -mavx2  -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG\"",
            result.get(i++));
        assertEquals("-DCMAKE_CXX_FLAGS_DEBUG=\" -ggdb -Werror -Wall -Wstrict-aliasing=2 -pedantic -fPIC -fopenmp -stdlib=libc++ -std=c++14 -DLOG_INTERNAL_ERROR=LOG_DEBUG -mcx16 -msse4.2 -mavx2  \"", result.get(i++));
        assertEquals("-DCMAKE_CXX_FLAGS_RELEASE=\"-O3 -DNDEBUG -O3 \"", result.get(i++));
        assertEquals("-DCMAKE_VERSION=\"3.5.1\"", result.get(i++));
        assertEquals("-DNSULATE_PROJECT_COMMIT=\"b079181 Create smoke Test suites\"", result.get(i++));
        assertEquals("-DNSULATE_SYSTEM=\"Ubuntu 16045 LTS\"", result.get(i++));
        assertEquals("-DNSULATE_SYSTEM_PROCESSOR=\"Linux srv-narnia 4.15.0-36-generic x86_64 GNU/Linux\"", result.get(i++));
        assertEquals("-DNSULATE_TIME_OF_BUILD=\"Wednesday 14-11-2018 03:22 UTC\"", result.get(i++));
        assertEquals("-DNSULATE_VERSION=\"1.2.82\"", result.get(i++));
        assertEquals("-I/home/jslave/sean/mainline/nsulate/include", result.get(i++));
        assertEquals("-I/home/jslave/sean/mainline/nsulate/src", result.get(i++));
    }
}
