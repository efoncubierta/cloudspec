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

import cloudspec.model.Resource;
import cloudspec.util.ModelTestUtils;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class GraphResourceStoreTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);
    private final GraphResourceStore resourceStore = new GraphResourceStore(graph);

    {
        resourceDefStore.addResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF);
        resourceDefStore.addResourceDef(ModelTestUtils.RESOURCE_DEF);
        resourceStore.addResource(ModelTestUtils.TARGET_RESOURCE);
        resourceStore.addResource(ModelTestUtils.RESOURCE);
    }

    @Test
    public void shouldNotGetResourceThatDoesntExist() {
        Optional<Resource> resourceOpt = resourceStore.getResource(ModelTestUtils.RESOURCE_DEF_REF, UUID.randomUUID().toString());
        assertNotNull(resourceOpt);
        assertFalse(resourceOpt.isPresent());
    }

    @Test
    public void shouldAddAndGetResource() {
        Optional<Resource> resourceOpt = resourceStore.getResource(ModelTestUtils.RESOURCE.getResourceDefRef(), ModelTestUtils.RESOURCE.getResourceId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.get());
    }
}
