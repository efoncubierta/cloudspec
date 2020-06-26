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
package cloudspec.preflight

import cloudspec.ProvidersRegistry
import cloudspec.graph.GraphResourceDefStore
import cloudspec.lang.AssociationStatement
import cloudspec.lang.KeyValueStatement
import cloudspec.lang.NestedStatement
import cloudspec.lang.PropertyStatement
import cloudspec.model.Module
import cloudspec.model.ProviderTest
import cloudspec.model.Rule
import cloudspec.util.CloudSpecTestUtils
import cloudspec.util.ModelGenerator.randomConfigValue
import cloudspec.util.ModelGenerator.randomName
import cloudspec.util.ModelGenerator.randomResourceDef
import cloudspec.util.ModelGenerator.randomResourceDefRef
import cloudspec.util.ModelTestUtils
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFails

class CloudSpecPreflightTest {
    private val graph: Graph = TinkerGraph.open()
    private val resourceDefStore = GraphResourceDefStore(graph)
    private val providersRegistry = ProvidersRegistry()
    private val preflight = CloudSpecPreflight(providersRegistry, resourceDefStore)

    init {
        providersRegistry.register(ProviderTest.TEST_PROVIDER)
    }

    @Before
    fun before() {
        graph.traversal().V().drop().iterate()
    }


    @Test
    fun shouldFailWithRandomConfigDef() {
        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         randomResourceDefRef(),
                                         emptyList(),
                                         emptyList(),
                                         listOf(randomConfigValue()))
                            ))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldFailWithRandomResourceDefString() {
        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         randomResourceDefRef(),
                                         emptyList(),
                                         emptyList(),
                                         emptyList())
                            ))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldFailWithRandomPropertyDef() {
        val resourceDef = randomResourceDef()
        resourceDefStore.saveResourceDef(resourceDef)

        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         resourceDef.ref,
                                         listOf(
                                                 PropertyStatement(randomName(),
                                                                   P.eq(0))
                                         ),
                                         emptyList(),
                                         emptyList())
                            ))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldFailWithRandomKeyValuePropertyDef() {
        val resourceDef = randomResourceDef()
        resourceDefStore.saveResourceDef(resourceDef)

        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         resourceDef.ref,
                                         listOf(
                                                 KeyValueStatement(randomName(),
                                                                   randomName(),
                                                                   P.eq(0))
                                         ),
                                         emptyList(),
                                         emptyList())
                            ))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldFailWithRandomNestedPropertyDef() {
        val resourceDef = randomResourceDef()
        resourceDefStore.saveResourceDef(resourceDef)

        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         resourceDef.ref,
                                         listOf(
                                                 NestedStatement(resourceDef.properties.elementAt(0).name,
                                                                 listOf(
                                                                         PropertyStatement(randomName(),
                                                                                           P.eq(0))
                                                                 ))
                                         ),
                                         emptyList(),
                                         emptyList()
                                    )))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldFailWithRandomAssociationDef() {
        val resourceDef = randomResourceDef()
        resourceDefStore.saveResourceDef(resourceDef)

        val module = Module(randomName(),
                            emptyList(),
                            listOf(
                                    Rule(randomName(),
                                         resourceDef.ref,
                                         listOf(
                                                 AssociationStatement(randomName(),
                                                                      emptyList())
                                         ),
                                         emptyList(),
                                         emptyList())
                            ))

        assertFails {
            preflight.preflight(module)
        }
    }

    @Test
    fun shouldPassWithValidCloudSpec() {
        resourceDefStore.saveResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF)
        resourceDefStore.saveResourceDef(ModelTestUtils.RESOURCE_DEF)
        preflight.preflight(CloudSpecTestUtils.TEST_MODULE)
    }
}
