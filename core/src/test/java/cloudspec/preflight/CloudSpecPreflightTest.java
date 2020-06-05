/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.preflight;

import cloudspec.graph.GraphResourceDefStore;
import cloudspec.lang.*;
import cloudspec.model.ResourceDef;
import cloudspec.util.CloudSpecTestUtils;
import cloudspec.util.ModelGenerator;
import cloudspec.util.ModelTestUtils;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertThrows;

public class CloudSpecPreflightTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);
    private final CloudSpecPreflight preflight = new CloudSpecPreflight(resourceDefStore);

    @Before
    public void before() {
        graph.traversal().V().drop().iterate();
    }

    @Test
    public void shouldFailWithMalformedResourceDefString() {
        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                ModelGenerator.randomName(),
                                                new WithExpr(
                                                        Collections.emptyList()
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldFailWithRandomResourceDefString() {
        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                ModelGenerator.randomResourceDefRef().toString(),
                                                new WithExpr(
                                                        Collections.emptyList()
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldFailWithRandomPropertyDef() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();

        resourceDefStore.createResourceDef(resourceDef);

        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                resourceDef.getRef().toString(),
                                                new WithExpr(
                                                        Collections.singletonList(
                                                                new PropertyStatement(
                                                                        ModelGenerator.randomName(),
                                                                        P.eq(0)
                                                                )
                                                        )
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldFailWithRandomKeyValuePropertyDef() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();

        resourceDefStore.createResourceDef(resourceDef);

        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                resourceDef.getRef().toString(),
                                                new WithExpr(
                                                        Collections.singletonList(
                                                                new KeyValueStatement(
                                                                        ModelGenerator.randomName(),
                                                                        ModelGenerator.randomName(),
                                                                        P.eq(0)
                                                                )
                                                        )
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldFailWithRandomNestedPropertyDef() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();

        resourceDefStore.createResourceDef(resourceDef);

        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                resourceDef.getRef().toString(),
                                                new WithExpr(
                                                        Collections.singletonList(
                                                                new NestedStatement(
                                                                        resourceDef.getProperties().get(0).getName(),
                                                                        Collections.singletonList(
                                                                                new PropertyStatement(
                                                                                        ModelGenerator.randomName(),
                                                                                        P.eq(0)
                                                                                )
                                                                        )
                                                                )

                                                        )
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldFailWithRandomAssociationDef() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();

        resourceDefStore.createResourceDef(resourceDef);

        CloudSpec spec = new CloudSpec(
                ModelGenerator.randomName(),
                Collections.singletonList(
                        new GroupExpr(
                                ModelGenerator.randomName(),
                                Collections.singletonList(
                                        new RuleExpr(
                                                ModelGenerator.randomName(),
                                                resourceDef.getRef().toString(),
                                                new WithExpr(
                                                        Collections.singletonList(
                                                                new AssociationStatement(
                                                                        ModelGenerator.randomName(),
                                                                        Collections.emptyList()
                                                                )
                                                        )
                                                ),
                                                new AssertExpr(
                                                        Collections.emptyList()
                                                )
                                        )
                                )
                        )
                )
        );

        assertThrows(CloudSpecPreflightException.class, () -> preflight.preflight(spec));
    }

    @Test
    public void shouldPassWithValidCloudSpec() {
        resourceDefStore.createResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF);
        resourceDefStore.createResourceDef(ModelTestUtils.RESOURCE_DEF);

        preflight.preflight(CloudSpecTestUtils.TEST_SPEC);
    }
}
