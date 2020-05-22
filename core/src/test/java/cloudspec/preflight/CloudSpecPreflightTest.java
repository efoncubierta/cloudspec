/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
