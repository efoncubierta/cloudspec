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

import arrow.core.Option
import arrow.core.Some
import arrow.core.none
import arrow.core.toOption
import arrow.syntax.collections.flatten
import cloudspec.model.*
import cloudspec.store.ResourceStore
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Direction
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.slf4j.LoggerFactory
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as underscore

/**
 * Graph-based implementation of resource store.
 *
 * This class implement [ResourceStore] using Tinkerpop graphs.
 */
class GraphResourceStore(val graph: Graph) : ResourceStore {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val graphTraversal: GraphTraversalSource = graph.traversal()

    override fun exists(ref: ResourceDefRef, id: String): Boolean {
        logger.debug("Checking whether resource '${ref}' with id '${id}' exists")
        return getResourceVertexById(ref, id) is Some<Vertex>
    }

    override fun resourceById(ref: ResourceDefRef, id: String): Option<Resource> {
        logger.debug("Getting resource '${ref}' with id '${id}'")

        // get resource vertex by resource id, and map it to resource object
        return getResourceVertexById(ref, id).flatMap {
            toResourceFromVertex(it)
        }
    }

    override fun resourcesByDefinition(ref: ResourceDefRef): Resources {
        logger.debug("Getting all resources of type '${ref}'")

        // get all resource vertices by resource definition reference, and map them to resource objects
        return getResourceVerticesByDefinition(ref)
                .map { toResourceFromVertex(it) }
                .flatten()
    }

    override fun resourceProperties(ref: ResourceDefRef, id: String): Option<Properties> {
        logger.debug("Getting properties of resource '${ref}' with id '${id}'")

        // get resource vertex by its id, and map it to a properties object
        return getResourceVertexById(ref, id).map {
            toPropertiesFromVertex(it)
        }
    }

    override fun resourceAssociations(ref: ResourceDefRef, id: String): Option<Associations> {
        logger.debug("Getting associations of resource '${ref}' with id '${id}'")

        // get resource vertex by its id, and map it to an associations object
        return getResourceVertexById(ref, id).map {
            toAssociationsFromVertex(it)
        }
    }

    override fun saveResource(ref: ResourceDefRef, id: String): Unit {
        // default to empty properties and associations
        saveResource(ref, id, emptySet(), emptySet())
    }

    override fun saveResource(ref: ResourceDefRef, id: String, properties: Properties, associations: Associations): Unit {
        logger.debug("Saving resource '${ref}' with id '${id}'")

        // get, or create, resource vertex by its id
        val resourceV = getOrCreateResourceVertexById(ref, id)

        // save resource properties
        savePropertiesFromVertex(resourceV, properties)

        // save resource associations
        saveAssociationsFromVertex(resourceV, associations)
    }

    override fun saveProperties(ref: ResourceDefRef, id: String, properties: Properties): Unit {
        logger.debug("Saving ${properties.size} properties on resource '${ref}' with id '${id}'")

        when (val vertex = getResourceVertexById(ref, id)) {
            is Some ->
                savePropertiesFromVertex(vertex.t, properties)
            else ->
                logger.error("Couldn't save properties. Resource '${ref}' with id '${id}' not found. ")
        }
    }

    override fun saveProperty(ref: ResourceDefRef, id: String, property: Property<*>) {
        logger.debug("Saving property '${property.name}' on resource '${ref}' with id '${id}'")

        when (val vertex = getResourceVertexById(ref, id)) {
            is Some ->
                savePropertyFromVertex(vertex.t, property)
            else ->
                logger.error("Couldn't save property '${property.name}'. " +
                                     "Resource '${ref}' with id '${id}' not found. ")
        }
    }

    override fun saveAssociations(ref: ResourceDefRef, id: String, associations: Associations): Unit {
        logger.debug("Saving ${associations.size} associations on resource '${ref}' with id '${id}'")

        when (val vertex = getResourceVertexById(ref, id)) {
            is Some ->
                saveAssociationsFromVertex(vertex.t, associations)
            else ->
                logger.error("Couldn't save associations. Resource '${ref}' with id '${id}' not found.")
        }
    }

    override fun saveAssociation(ref: ResourceDefRef, id: String, association: Association): Unit {
        logger.debug("Saving association '${association.name}' on resource '${ref}' with id '${id}'")

        when (val vertex = getResourceVertexById(ref, id)) {
            is Some ->
                saveAssociationFromVertex(vertex.t, association)
            else ->
                logger.error("Couldn't save association '${association.name}'. " +
                                     "Resource '${ref}' with id '${id}' not found.")
        }
    }

    private fun getResourceVertexById(ref: ResourceDefRef, id: String): Option<Vertex> {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, ref)
                .has(PROPERTY_RESOURCE_ID, id)
                .tryNext()
                .orElse(null)
                .toOption()
    }

    private fun getResourceVerticesByDefinition(ref: ResourceDefRef): List<Vertex> {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, ref)
                .toList()
    }

    private fun getOrCreateResourceVertexById(ref: ResourceDefRef, id: String): Vertex {
        logger.debug("Getting, or creating, resource vertex for resource '${ref}' with id '${id}'")

        val resourceV = graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, ref)
                .has(PROPERTY_RESOURCE_ID, id)
                .fold() // check whether the resource vertex exist, or create it otherwise
                .coalesce(
                        underscore.unfold(),
                        underscore.addV<Any>(LABEL_RESOURCE)
                                .property(PROPERTY_RESOURCE_DEF_REF, ref)
                                .property(PROPERTY_RESOURCE_ID, id)
                )
                .next()

        // new resource vertex must have an edge to its resource definition vertex
        if (!resourceV.edges(Direction.OUT, LABEL_IS_RESOURCE_DEF).hasNext()) {
            logger.debug("Created vertex for resource '${ref}' with id '${id}'")

            // get resource definition vertex
            val resourceDefVOpt = graphTraversal
                    .V()
                    .has(GraphResourceDefStore.LABEL_RESOURCE_DEF,
                         GraphResourceDefStore.PROPERTY_RESOURCE_DEF_REF,
                         ref)
                    .tryNext()

            // if resource definition vertex doesn't exist, roll back and throw an error
            if (resourceDefVOpt.isEmpty) {
                resourceV.remove()
                throw RuntimeException("Resource definition '${ref}' not found.")
            }

            // create edge between resource and resource definition edges
            graphTraversal.V()
                    .addE(LABEL_IS_RESOURCE_DEF)
                    .from(resourceV)
                    .to(resourceDefVOpt.get())
                    .next()
        }
        return resourceV
    }

    private fun getOrCreatePropertyVertex(sourceV: Vertex, property: Property<*>): Vertex {
        logger.debug("Getting, or creating, property vertex '${property.name}'")

        // source vertex can be a resource or a property value
        // get property vertex by its name
        val propertyVOpt = graphTraversal.V(sourceV.id())
                .out(LABEL_HAS_PROPERTY)
                .has(PROPERTY_NAME, property.name)
                .tryNext()

        // return if exists
        if (propertyVOpt.isPresent) {
            return propertyVOpt.get()
        }

        logger.debug("Property not found. Creating it")

        // get property definition vertex
        val propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                // attempt to get the resource definition or property definition vertex
                // depending on whether the source vertex is a resource or a property value
                .coalesce(
                        // if source vertex is a resource, this should return a resource definition vertex
                        underscore.out(LABEL_IS_RESOURCE_DEF),
                        // otherwise, it should be a property value, so this should return the property property
                        // definition vertex traversing through its property vertex
                        underscore.`in`(LABEL_HAS_PROPERTY_VALUE).out(LABEL_IS_PROPERTY_DEF)
                )
                .out(GraphResourceDefStore.LABEL_HAS_PROPERTY_DEF)
                .has(GraphResourceDefStore.PROPERTY_NAME, property.name)
                .tryNext()

        // property definition must exists to be able to create the property
        if (propertyDefVOpt.isEmpty) {
            throw RuntimeException("Property definition '${property.name}' not found.")
        }

        // get property typ
        val propertyDefV = propertyDefVOpt.get()
        val propertyType = propertyDefV.value<PropertyType>(GraphResourceDefStore.PROPERTY_TYPE)
        val multiValued = propertyDefV.value<Boolean>(GraphResourceDefStore.PROPERTY_IS_MULTIVALUED)

        // create property vertex
        val propertyV = graphTraversal
                .addV(LABEL_PROPERTY)
                .property(PROPERTY_NAME, property.name)
                .property(PROPERTY_TYPE, propertyType)
                .property(PROPERTY_IS_MULTIVALUED, multiValued)
                .next()

        // create edge between source (resource or property value) vertex and property vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY)
                .from(sourceV)
                .to(propertyV)
                .next()

        // create edge between property vertex and property definition vertex
        graphTraversal
                .addE(LABEL_IS_PROPERTY_DEF)
                .from(propertyV)
                .to(propertyDefVOpt.get())
                .next()

        return propertyV
    }

    private fun savePropertiesFromVertex(sourceV: Vertex, properties: Properties): Unit {
        properties.forEach { savePropertyFromVertex(sourceV, it) }
    }

    private fun savePropertyFromVertex(sourceV: Vertex, property: Property<*>): Unit {
        // source vertex can be a resource or a property value
        logger.debug("- Saving property '${toMemberPath(sourceV, property.name)}' with value $property")

        // create property vertex
        val propertyV = getOrCreatePropertyVertex(sourceV, property)
        when (property) {
            is NestedProperty -> saveNestedPropertyValueFromVertex(propertyV, property.value)
            is KeyValueProperty -> saveKeyValuePropertyValueFromVertex(propertyV, property.value)
            else -> saveAtomicPropertyValueFromVertex(propertyV, property.value)
        }
    }

    private fun saveNestedPropertyValueFromVertex(propertyV: Vertex, nestedProperty: NestedPropertyValue?): Unit {
        // nested property values are empty, but with edges to properties and associations
        val propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .next()

        // add edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next()

        // save nested properties
        savePropertiesFromVertex(propertyValueV, nestedProperty?.properties ?: emptySet())

        // save nested associations
        saveAssociationsFromVertex(propertyValueV, nestedProperty?.associations ?: emptySet())
    }

    private fun saveAtomicPropertyValueFromVertex(propertyV: Vertex, obj: Any?): Unit {
        // atomic property values hold a value
        val propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_VALUE, obj)
                .next()

        // create edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next()
    }

    private fun saveKeyValuePropertyValueFromVertex(propertyV: Vertex, keyValue: KeyValue?): Unit {
        // key value property values hold a key and a value properties
        val propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_KEY, keyValue?.key)
                .property(PROPERTY_VALUE, keyValue?.value)
                .next()

        // create edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next()
    }

    private fun saveAssociationsFromVertex(sourceV: Vertex, associations: Associations): Unit {
        associations.forEach { saveAssociationFromVertex(sourceV, it) }
    }

    private fun saveAssociationFromVertex(sourceV: Vertex, association: Association): Unit {
        // source vertex can be a resource or a property value
        logger.debug("- Saving association '${toMemberPath(sourceV, association.name)}' with value $association")

        // TODO remove old associations?

        // get, or create, target resource by it ids
        val targetV = getOrCreateResourceVertexById(association.resourceDefRef, association.resourceId)

        // link source resource to target resource
        graphTraversal
                .V(sourceV.id())
                .fold()
                // check whether the association already exists, or create it otherwise
                .coalesce(
                        underscore.unfold<Any>()
                                .outE(LABEL_HAS_ASSOCIATION)
                                .has(PROPERTY_NAME, association.name)
                                .inV()
                                .has(PROPERTY_RESOURCE_DEF_REF, association.resourceDefRef)
                                .has(PROPERTY_RESOURCE_ID, association.resourceId),
                        underscore.addE<Any>(LABEL_HAS_ASSOCIATION)
                                .property(PROPERTY_NAME, association.name)
                                .from(sourceV)
                                .to(targetV)
                                .inV()
                ).next()
    }

    private fun toResourceFromVertex(resourceV: Vertex): Option<Resource> {
        return when (resourceV.label()) {
            LABEL_RESOURCE -> {
                // get resource data
                val resourceDefRef = resourceV.value<ResourceDefRef>(PROPERTY_RESOURCE_DEF_REF)
                val resourceId = resourceV.value<String>(PROPERTY_RESOURCE_ID)
                logger.debug("Found resource '${resourceDefRef}' with id '${resourceId}'")

                // return resource
                Resource(resourceDefRef,
                         resourceId,
                         toPropertiesFromVertex(resourceV),
                         toAssociationsFromVertex(resourceV)).toOption()
            }
            else -> {
                logger.error("Vertex is not of type '${LABEL_RESOURCE}'.")
                none()
            }
        }
    }

    private fun toPropertiesFromVertex(sourceV: Vertex): Properties {
        return when (sourceV.label()) {
            LABEL_RESOURCE,
            LABEL_PROPERTY_VALUE -> {
                graphTraversal
                        .V(sourceV.id())
                        .out(LABEL_HAS_PROPERTY)
                        .toList()
                        .flatMap { toPropertyValuesFromVertex(it) }
                        .toSet()
            }
            else -> {
                logger.error("Vertex is not of type '${LABEL_RESOURCE}' or '${LABEL_PROPERTY_VALUE}'.")
                emptySet()
            }
        }
    }

    private fun toPropertyValuesFromVertex(propertyV: Vertex): Properties {
        return when (propertyV.label()) {
            LABEL_PROPERTY -> {
                graphTraversal
                        .V(propertyV.id())
                        .out(LABEL_HAS_PROPERTY_VALUE)
                        .toList()
                        .map {
                            toProperty(propertyV.value(PROPERTY_NAME), propertyV.value(PROPERTY_TYPE), it)
                        }
                        .flatten()
                        .toSet()
            }
            else -> {
                logger.error("Vertex is not of type '${LABEL_PROPERTY_VALUE}'.")
                emptySet()
            }
        }
    }

    private fun toProperty(propertyName: String, type: PropertyType, propertyValueV: Vertex): Option<Property<*>> {
        return when (type) {
            PropertyType.NUMBER -> NumberProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
            PropertyType.STRING -> StringProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
            PropertyType.BOOLEAN -> BooleanProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
            PropertyType.DATE -> InstantProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
            PropertyType.KEY_VALUE -> KeyValueProperty(propertyName,
                                                       KeyValue(propertyValueV.value(PROPERTY_KEY),
                                                                propertyValueV.value(PROPERTY_VALUE)))
            PropertyType.NESTED -> NestedProperty(propertyName,
                                                  NestedPropertyValue(toPropertiesFromVertex(propertyValueV),
                                                                      toAssociationsFromVertex(propertyValueV)))
        }.toOption()
    }

    private fun toAssociationsFromVertex(sourceV: Vertex): Associations {
        return when (sourceV.label()) {
            LABEL_RESOURCE,
            LABEL_PROPERTY_VALUE -> {
                // get all association edges
                val associationEdges = graphTraversal
                        .V(sourceV.id())
                        .outE(LABEL_HAS_ASSOCIATION)
                        .toList()

                // map association edges to association objects
                associationEdges
                        .flatMap {
                            graphTraversal
                                    .E(it.id())
                                    .inV()
                                    .map { v ->
                                        Association(it.value(PROPERTY_NAME),
                                                    v.get().value(PROPERTY_RESOURCE_DEF_REF),
                                                    v.get().value(PROPERTY_RESOURCE_ID))
                                    }
                                    .toList()
                        }
                        .toSet()
            }
            else -> {
                logger.error("Vertex is not of type '${LABEL_RESOURCE}' or '${LABEL_PROPERTY_VALUE}'.")
                emptySet()
            }
        }
    }

    private fun toMemberPath(sourceV: Vertex, memberName: String): String {
        return when (sourceV.label()) {
            LABEL_RESOURCE -> {
                memberName
            }
            LABEL_PROPERTY_VALUE -> {
                toMemberPath(graphTraversal
                                     .V(sourceV.id())
                                     .`in`(LABEL_HAS_PROPERTY_VALUE)
                                     .next(),
                             memberName)
            }
            else -> {
                toMemberPath(graphTraversal.V(sourceV.id())
                                     .`in`(LABEL_HAS_PROPERTY)
                                     .next(),
                             sourceV.value(PROPERTY_NAME)).let { "${it}.${memberName}" }
            }
        }
    }

    companion object {
        const val LABEL_RESOURCE = "resource"
        const val LABEL_PROPERTY = "property"
        const val LABEL_PROPERTY_VALUE = "propertyValue"
        const val LABEL_IS_RESOURCE_DEF = "isResourceDef"
        const val LABEL_HAS_PROPERTY = "hasProperty"
        const val LABEL_HAS_PROPERTY_VALUE = "hasPropertyValue"
        const val LABEL_IS_PROPERTY_DEF = "isPropertyDef"
        const val LABEL_HAS_ASSOCIATION = "hasAssociation"
        const val PROPERTY_RESOURCE_ID = "resourceId"
        const val PROPERTY_RESOURCE_DEF_REF = "resourceDefRef"
        const val PROPERTY_NAME = "name"
        const val PROPERTY_KEY = "key"
        const val PROPERTY_VALUE = "value"
        const val PROPERTY_TYPE = "propertyType"
        const val PROPERTY_IS_MULTIVALUED = "isMultiValued"
    }
}
