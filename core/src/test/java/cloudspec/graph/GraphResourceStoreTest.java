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
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class GraphResourceStoreTest {
    @Test
    public void shouldNotGetResourceThatDoesntExist() {
        GraphResourceStore store = new GraphResourceStore(TinkerGraph.open());
        Optional<Resource> resourceOpt = store.getResource(ModelTestUtils.RESOURCE_DEF_REF, ModelTestUtils.RESOURCE_ID);
        assertNotNull(resourceOpt);
        assertFalse(resourceOpt.isPresent());
    }

    @Test
    public void shouldAddAndGetResource() {
        GraphResourceStore store = new GraphResourceStore(TinkerGraph.open());

        store.addResource(ModelTestUtils.TARGET_RESOURCE);
        store.addResource(ModelTestUtils.RESOURCE);

        Optional<Resource> resourceOpt = store.getResource(ModelTestUtils.RESOURCE.getResourceDefRef(), ModelTestUtils.RESOURCE.getResourceId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.get());
    }
}
