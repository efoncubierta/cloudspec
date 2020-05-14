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
