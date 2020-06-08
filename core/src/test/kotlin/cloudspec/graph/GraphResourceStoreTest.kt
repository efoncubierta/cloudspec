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

import cloudspec.model.Resource
import cloudspec.util.ModelGenerator.randomAssociation
import cloudspec.util.ModelGenerator.randomProperty
import cloudspec.util.ModelGenerator.randomResource
import cloudspec.util.ModelGenerator.randomResourceDef
import cloudspec.util.ModelGenerator.randomResourceDefRef
import cloudspec.util.ModelGenerator.randomResourceId
import cloudspec.util.ModelTestUtils
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.Before
import org.junit.Test
import java.util.function.Consumer
import kotlin.test.*

class GraphResourceStoreTest {
    private val graph: Graph = TinkerGraph.open()
    private val resourceDefStore = GraphResourceDefStore(graph)
    private val resourceStore = GraphResourceStore(graph)

    @Before
    fun before() {
        graph.traversal().V().drop().iterate()
        resourceDefStore.saveResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF)
        resourceDefStore.saveResourceDef(ModelTestUtils.RESOURCE_DEF)
        resourceStore.saveResource(
                ModelTestUtils.TARGET_RESOURCE_DEF_REF,
                ModelTestUtils.TARGET_RESOURCE_ID,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        )
        resourceStore.saveResource(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        )
    }

    @Test
    fun shouldNotExistRandomResourceId() {
        assertFalse(
                resourceStore.exists(
                        randomResourceDefRef(),
                        randomResourceId()
                )
        )
    }

    @Test
    fun shouldExistResource() {
        assertTrue(
                resourceStore.exists(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        ModelTestUtils.RESOURCE_ID
                )
        )
    }

    @Test
    fun shouldNotGetRandomResource() {
        val resource = resourceStore.resourceById(
                randomResourceDefRef(),
                randomResourceId()
        )
        assertNull(resource)
    }

    @Test
    fun shouldGetResource() {
        val resource = resourceStore.resourceById(
                ModelTestUtils.RESOURCE.resourceDefRef,
                ModelTestUtils.RESOURCE.resourceId
        )
        assertNotNull(resource)
        assertEquals(ModelTestUtils.RESOURCE, resource)
    }

    @Test
    fun shouldNotGetPropertiesOfRandomResource() {
        val properties = resourceStore.resourceProperties(
                randomResourceDefRef(),
                randomResourceId()
        )
        assertNull(properties)
    }

    @Test
    fun shouldGetPropertiesOfResource() {
        val properties = resourceStore.resourceProperties(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        )
        assertNotNull(properties)
        assertEquals(ModelTestUtils.PROPERTIES.size, properties.size)
        assertEquals(ModelTestUtils.PROPERTIES, properties)
    }

    @Test
    fun shouldNotGetAssociationsOfRandomResource() {
        val associations = resourceStore.resourceAssociations(
                randomResourceDefRef(),
                randomResourceId()
        )
        assertNull(associations)
    }

    @Test
    fun shouldGetAssociationsOfResource() {
        val associations = resourceStore.resourceAssociations(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        )
        assertNotNull(associations)
        assertEquals(ModelTestUtils.ASSOCIATIONS, associations)
    }

    @Test
    fun shouldNotGetResourcesByRandomDefinition() {
        val resources: List<Resource> = resourceStore.resourcesByDefinition(
                randomResourceDefRef()
        )
        assertNotNull(resources)
        assertTrue(resources.isEmpty())
    }

    @Test
    fun shouldGetResourcesByDefinition() {
        val resources: List<Resource> = resourceStore.resourcesByDefinition(
                ModelTestUtils.RESOURCE_DEF_REF
        )
        assertNotNull(resources)
        assertFalse(resources.isEmpty())
        assertEquals(1, resources.size)
        assertEquals(ModelTestUtils.RESOURCE, resources[0])
    }

    @Test
    fun shouldNotSetRandomPropertyToRandomResource() {
        val resourceDefRef = randomResourceDefRef()
        val resourceId = randomResourceId()
        val property = randomProperty()
        resourceStore.saveProperty(
                resourceDefRef,
                resourceId,
                property
        )
        val properties = resourceStore.resourceProperties(resourceDefRef, resourceId)
        assertNull(properties)
    }

    @Test
    fun shouldNotSetRandomPropertyToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId) = randomResource(resourceDef)
        val property = randomProperty()
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        assertFails { resourceStore.saveProperty(resourceDefRef, resourceId, property) }
        val properties = resourceStore.resourceProperties(resourceDefRef, resourceId)
        assertNotNull(properties)
        assertTrue(properties.isEmpty())
    }

    @Test
    fun shouldSetPropertyToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId, properties) = randomResource(resourceDef)
        val property = properties.elementAt(0)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        resourceStore.saveProperty(resourceDefRef, resourceId, property)
        val props = resourceStore.resourceProperties(resourceDefRef, resourceId)
        assertNotNull(props)
        assertEquals(1, props.size)
        assertEquals(property, props.elementAt(0))
    }

    @Test
    fun shouldSetMultiplePropertiesToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId, properties) = randomResource(resourceDef)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        resourceStore.saveProperties(resourceDefRef, resourceId, properties)
        val props = resourceStore.resourceProperties(resourceDefRef, resourceId)
        assertNotNull(props)
        assertEquals(properties, props)
    }

    @Test
    fun shouldNotSetRandomAssociationToRandomResource() {
        val resourceDefRef = randomResourceDefRef()
        val resourceId = randomResourceId()
        val association = randomAssociation()
        resourceStore.saveAssociation(
                resourceDefRef,
                resourceId,
                association
        )
        val assocs = resourceStore.resourceAssociations(resourceDefRef, resourceId)
        assertNull(assocs)
    }

    @Test
    fun shouldNotSetRandomAssociationToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId) = randomResource(resourceDef)
        val association = randomAssociation()
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        assertFails {
            resourceStore.saveAssociation(
                    resourceDefRef,
                    resourceId,
                    association
            )
        }
        val assocs = resourceStore.resourceAssociations(resourceDefRef, resourceId)
        assertNotNull(assocs)
        assertTrue(assocs.isEmpty())
    }

    @Test
    fun shouldNotSetAssociationToRandomTargetResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId, _, associations) = randomResource(resourceDef)
        val association = associations.elementAt(0)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        assertFails { resourceStore.saveAssociation(resourceDefRef, resourceId, association) }
        val assocs = resourceStore.resourceAssociations(resourceDefRef, resourceId)
        assertNotNull(assocs)
        assertTrue(assocs.isEmpty())
    }

    @Test
    fun shouldSetAssociationToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId, _, associations) = randomResource(resourceDef)
        val association = associations.elementAt(0)
        val targetResourceDef = randomResourceDef(association.resourceDefRef)

        // create target resource
        resourceDefStore.saveResourceDef(targetResourceDef)
        resourceStore.saveResource(association.resourceDefRef, association.resourceId)

        // create resource
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(resourceDefRef, resourceId)
        resourceStore.saveAssociation(resourceDefRef, resourceId, association)
        val assocs = resourceStore.resourceAssociations(resourceDefRef, resourceId)
        assertNotNull(assocs)
        assertEquals(1, assocs.size)
        assertEquals(association, assocs.elementAt(0))
    }

    @Test
    fun shouldSetMultipleAssociationsToResource() {
        val resourceDef = randomResourceDef()
        val (resourceDefRef, resourceId, _, associations) = randomResource(resourceDef)
        resourceDefStore.saveResourceDef(resourceDef)

        // create associated resources
        associations.forEach(Consumer { (_, resourceDefRef1, resourceId1) ->
            resourceDefStore.saveResourceDef(randomResourceDef(resourceDefRef1))
            resourceStore.saveResource(resourceDefRef1, resourceId1)
        })
        resourceStore.saveResource(resourceDefRef, resourceId)
        resourceStore.saveAssociations(resourceDefRef, resourceId, associations)
        val assocs = resourceStore.resourceAssociations(resourceDefRef, resourceId)
        assertNotNull(assocs)
        assertEquals(associations, assocs)
    }
}
