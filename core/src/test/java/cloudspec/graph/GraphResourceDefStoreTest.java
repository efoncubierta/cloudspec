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
package cloudspec.graph;

import cloudspec.model.ResourceDef;
import cloudspec.util.ModelGenerator;
import cloudspec.util.ModelTestUtils;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GraphResourceDefStoreTest {
    private final Graph graph = TinkerGraph.open();
    GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);

    @Before
    public void before() {
        graph.traversal().V().drop().iterate();
    }

    @Test
    public void shouldNotGetRandomResourceDef() {
        Optional<ResourceDef> resourceDefOpt = resourceDefStore.getResourceDef(ModelTestUtils.RESOURCE_DEF_REF);
        assertNotNull(resourceDefOpt);
        assertFalse(resourceDefOpt.isPresent());
    }

    @Test
    public void shouldCreateResourceDef() {
        resourceDefStore.createResourceDef(ModelTestUtils.RESOURCE_DEF);
        Optional<ResourceDef> resourceDefOpt = resourceDefStore.getResourceDef(ModelTestUtils.RESOURCE_DEF_REF);
        assertNotNull(resourceDefOpt);
        assertTrue(resourceDefOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE_DEF, resourceDefOpt.get());
    }

    @Test
    public void shouldCreateMultipleResourceDefs() {
        resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef());
        resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef());
        resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef());

        List<ResourceDef> resourceDefs = resourceDefStore.getResourceDefs();
        assertNotNull(resourceDefs);
        assertFalse(resourceDefs.isEmpty());
        assertEquals(3, resourceDefs.size());
    }
}
