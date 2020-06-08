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
package cloudspec.graph

import cloudspec.util.ModelGenerator.randomResourceDef
import cloudspec.util.ModelTestUtils
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GraphResourceDefStoreTest {
    private val graph: Graph = TinkerGraph.open()
    var resourceDefStore = GraphResourceDefStore(graph)

    @Before
    fun before() {
        graph.traversal().V().drop().iterate()
    }

    @Test
    fun shouldNotGetRandomResourceDef() {
        val resourceDefOpt = resourceDefStore.getResourceDef(ModelTestUtils.RESOURCE_DEF_REF)
        assertNull(resourceDefOpt)
    }

    @Test
    fun shouldCreateResourceDef() {
        resourceDefStore.saveResourceDef(ModelTestUtils.RESOURCE_DEF)
        val resourceDef = resourceDefStore.getResourceDef(ModelTestUtils.RESOURCE_DEF_REF)
        assertNotNull(resourceDef)
        assertEquals(ModelTestUtils.RESOURCE_DEF, resourceDef)
    }

    @Test
    fun shouldCreateMultipleResourceDefs() {
        resourceDefStore.saveResourceDef(randomResourceDef())
        resourceDefStore.saveResourceDef(randomResourceDef())
        resourceDefStore.saveResourceDef(randomResourceDef())
        val resourceDefs = resourceDefStore.resourceDefs
        assertNotNull(resourceDefs)
        assertFalse(resourceDefs.isEmpty())
        assertEquals(3, resourceDefs.size.toLong())
    }
}
