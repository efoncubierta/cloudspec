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

import arrow.core.None
import arrow.core.Some
import cloudspec.model.Associations
import cloudspec.model.Properties
import cloudspec.model.Resource
import cloudspec.model.Resources
import cloudspec.util.ModelGenerator.randomAssociation
import cloudspec.util.ModelGenerator.randomProperty
import cloudspec.util.ModelGenerator.randomResource
import cloudspec.util.ModelGenerator.randomResourceDef
import cloudspec.util.ModelGenerator.randomResourceDefRef
import cloudspec.util.ModelGenerator.randomResourceRef
import cloudspec.util.ModelTestUtils
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
import org.junit.Before
import org.junit.Test
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
                ModelTestUtils.TARGET_RESOURCE_REF,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        )
        resourceStore.saveResource(
                ModelTestUtils.RESOURCE_REF,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        )
    }

    @Test
    fun shouldNotExistRandomResourceId() {
        assertFalse(
                resourceStore.exists(randomResourceRef())
        )
    }

    @Test
    fun shouldExistResource() {
        assertTrue(
                resourceStore.exists(ModelTestUtils.RESOURCE_REF)
        )
    }

    @Test
    fun shouldNotGetRandomResource() {
        val resourceOpt = resourceStore.resourceById(randomResourceRef())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun shouldGetResource() {
        val resourceOpt = resourceStore.resourceById(
                ModelTestUtils.RESOURCE.ref
        )
        assertTrue(resourceOpt is Some<Resource>)
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.t)
    }

    @Test
    fun shouldNotGetPropertiesOfRandomResource() {
        val propertiesOpt = resourceStore.resourceProperties(randomResourceRef())
        assertTrue(propertiesOpt is None)
    }

    @Test
    fun shouldGetPropertiesOfResource() {
        val propertiesOpt = resourceStore.resourceProperties(
                ModelTestUtils.RESOURCE_REF
        )
        assertTrue(propertiesOpt is Some<Properties>)
        assertEquals(ModelTestUtils.PROPERTIES.size, propertiesOpt.t.size)
        assertEquals(ModelTestUtils.PROPERTIES, propertiesOpt.t)
    }

    @Test
    fun shouldNotGetAssociationsOfRandomResource() {
        val associationsOpt = resourceStore.resourceAssociations(randomResourceRef())
        assertTrue(associationsOpt is None)
    }

    @Test
    fun shouldGetAssociationsOfResource() {
        val associationsOpt = resourceStore.resourceAssociations(ModelTestUtils.RESOURCE_REF)
        assertTrue(associationsOpt is Some<Associations>)
        assertEquals(ModelTestUtils.ASSOCIATIONS, associationsOpt.t)
    }

    @Test
    fun shouldNotGetResourcesByRandomDefinition() {
        val resources: Resources = resourceStore.resourcesByDefinition(
                randomResourceDefRef()
        )
        assertTrue(resources.isEmpty())
    }

    @Test
    fun shouldGetResourcesByDefinition() {
        val resources: Resources = resourceStore.resourcesByDefinition(
                ModelTestUtils.RESOURCE_DEF_REF
        )
        assertFalse(resources.isEmpty())
        assertEquals(1, resources.size)
        assertEquals(ModelTestUtils.RESOURCE, resources[0])
    }

    @Test
    fun shouldNotSetRandomPropertyToRandomResource() {
        val resourceRef = randomResourceRef()
        val property = randomProperty()
        resourceStore.saveProperty(resourceRef, property)
        val propertiesOpt = resourceStore.resourceProperties(resourceRef)
        assertTrue(propertiesOpt is None)
    }

    @Test
    fun shouldNotSetRandomPropertyToResource() {
        val resourceDef = randomResourceDef()
        val (ref) = randomResource(resourceDef)
        val property = randomProperty()
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        assertFails { resourceStore.saveProperty(ref, property) }
        val propertiesOpt = resourceStore.resourceProperties(ref)
        assertTrue(propertiesOpt is Some<Properties>)
        assertTrue(propertiesOpt.t.isEmpty())
    }

    @Test
    fun shouldSetPropertyToResource() {
        val resourceDef = randomResourceDef()
        val (ref, properties) = randomResource(resourceDef)
        val property = properties.elementAt(0)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        resourceStore.saveProperty(ref, property)
        val propsOpt = resourceStore.resourceProperties(ref)
        assertTrue(propsOpt is Some<Properties>)
        assertEquals(1, propsOpt.t.size)
        assertEquals(property, propsOpt.t.elementAt(0))
    }

    @Test
    fun shouldSetMultiplePropertiesToResource() {
        val resourceDef = randomResourceDef()
        val (ref, properties) = randomResource(resourceDef)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        resourceStore.saveProperties(ref, properties)
        val propsOpt = resourceStore.resourceProperties(ref)
        assertTrue(propsOpt is Some<Properties>)
        assertEquals(properties, propsOpt.t)
    }

    @Test
    fun shouldNotSetRandomAssociationToRandomResource() {
        val ref = randomResourceRef()
        val association = randomAssociation()
        resourceStore.saveAssociation(ref, association)
        val assocsOpt = resourceStore.resourceAssociations(ref)
        assertTrue(assocsOpt is None)
    }

    @Test
    fun shouldNotSetRandomAssociationToResource() {
        val resourceDef = randomResourceDef()
        val (ref) = randomResource(resourceDef)
        val association = randomAssociation()
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        assertFails {
            resourceStore.saveAssociation(ref, association)
        }
        val assocsOpt = resourceStore.resourceAssociations(ref)
        assertTrue(assocsOpt is Some<Associations>)
        assertTrue(assocsOpt.t.isEmpty())
    }

    @Test
    fun shouldNotSetAssociationToRandomTargetResource() {
        val resourceDef = randomResourceDef()
        val (ref, _, associations) = randomResource(resourceDef)
        val association = associations.elementAt(0)
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        assertFails { resourceStore.saveAssociation(ref, association) }
        val assocsOpt = resourceStore.resourceAssociations(ref)
        assertTrue(assocsOpt is Some<Associations>)
        assertTrue(assocsOpt.t.isEmpty())
    }

    @Test
    fun shouldSetAssociationToResource() {
        val resourceDef = randomResourceDef()
        val (ref, _, associations) = randomResource(resourceDef)
        val association = associations.elementAt(0)
        val targetResourceDef = randomResourceDef(association.resourceRef.defRef)

        // create target resource
        resourceDefStore.saveResourceDef(targetResourceDef)
        resourceStore.saveResource(association.resourceRef)

        // create resource
        resourceDefStore.saveResourceDef(resourceDef)
        resourceStore.saveResource(ref)
        resourceStore.saveAssociation(ref, association)
        val assocsOpt = resourceStore.resourceAssociations(ref)
        assertTrue(assocsOpt is Some<Associations>)
        assertEquals(1, assocsOpt.t.size)
        assertEquals(association, assocsOpt.t.elementAt(0))
    }

    @Test
    fun shouldSetMultipleAssociationsToResource() {
        val resourceDef = randomResourceDef()
        val (ref, _, associations) = randomResource(resourceDef)
        resourceDefStore.saveResourceDef(resourceDef)

        // create associated resources
        associations.forEach { (_, resourceDefRef1) ->
            resourceDefStore.saveResourceDef(randomResourceDef(resourceDefRef1.defRef))
            resourceStore.saveResource(resourceDefRef1)
        }
        resourceStore.saveResource(ref)
        resourceStore.saveAssociations(ref, associations)
        val assocsOpt = resourceStore.resourceAssociations(ref)
        assertTrue(assocsOpt is Some<Associations>)
        assertEquals(associations, assocsOpt.t)
    }
}
