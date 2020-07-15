package com.synopsys.integration.detectable.detectables.yarn.unit;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.synopsys.integration.bdio.graph.DependencyGraph;
import com.synopsys.integration.bdio.graph.builder.MissingExternalIdException;
import com.synopsys.integration.bdio.model.Forge;
import com.synopsys.integration.bdio.model.externalid.ExternalId;
import com.synopsys.integration.bdio.model.externalid.ExternalIdFactory;
import com.synopsys.integration.detectable.annotations.UnitTest;
import com.synopsys.integration.detectable.detectables.npm.packagejson.model.PackageJson;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLock;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLockDependency;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLockEntry;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnLockEntryId;
import com.synopsys.integration.detectable.detectables.yarn.parse.YarnTransformer;

@UnitTest
public class YarnTransformerTest {
    @Test
    void doesntThrowOnMissingExternalId() throws MissingExternalIdException {
        // Ensure components not defined in the graph doesn't cause an exception to be thrown. See IDETECT-1974.

        final ExternalIdFactory externalIdFactory = new ExternalIdFactory();
        final YarnTransformer yarnTransformer = new YarnTransformer(externalIdFactory);

        final PackageJson packageJson = new PackageJson();
        packageJson.dependencies = new HashMap<>();
        packageJson.dependencies.put("foo", "fuzzyVersion-1.0");

        final List<YarnLockEntryId> validYarnLockEntryIds = Collections.singletonList(new YarnLockEntryId("foo", "fuzzyVersion-1.0"));
        final List<YarnLockDependency> validYarnLockDependencies = Collections.singletonList(new YarnLockDependency("yarn", "^1.22.4", false));
        final List<YarnLockEntry> yarnLockEntries = Collections.singletonList(new YarnLockEntry(validYarnLockEntryIds, "1.0", validYarnLockDependencies));
        final YarnLock yarnLock = new YarnLock(yarnLockEntries);

        // This should not throw an exception.
        final DependencyGraph dependencyGraph = yarnTransformer.transform(packageJson, yarnLock, false);

        // Sanity check.
        Assertions.assertNotNull(dependencyGraph, "The dependency graph should not be null.");
        Assertions.assertEquals(1, dependencyGraph.getRootDependencies().size(), "Only 'foo:1.0' should appear in the graph.");
        final ExternalId fooExternalId = externalIdFactory.createNameVersionExternalId(Forge.NPMJS, "foo", "1.0");
        Assertions.assertTrue(dependencyGraph.hasDependency(fooExternalId), "Missing the only expected dependency.");
    }
}
