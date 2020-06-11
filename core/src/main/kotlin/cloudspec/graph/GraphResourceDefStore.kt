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

import arrow.core.*
import arrow.syntax.collections.flatten
import cloudspec.model.AssociationDef
import cloudspec.model.PropertyDef
import cloudspec.model.ResourceDef
import cloudspec.model.ResourceDefRef
import cloudspec.store.ResourceDefStore
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.slf4j.LoggerFactory

/**
 * Graph-based implementation of resource definition store.
 *
 * This class implement [ResourceDefStore] using Tinkerpop graphs.
 */
class GraphResourceDefStore(val graph: Graph) : ResourceDefStore {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val graphTraversal: GraphTraversalSource = graph.traversal()

    override fun saveResourceDef(resourceDef: ResourceDef) {
        logger.debug("Adding resource definition '${resourceDef.ref}'")

        // get definition ref
        val resourceDefRef = resourceDef.ref

        // create resource definition vertex
        val resourceV = graphTraversal
                .addV(LABEL_RESOURCE_DEF)
                .property(PROPERTY_PROVIDER_NAME, resourceDefRef.providerName)
                .property(PROPERTY_GROUP_NAME, resourceDefRef.groupName)
                .property(PROPERTY_NAME, resourceDefRef.resourceName)
                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .property(PROPERTY_DESCRIPTION, resourceDef.description)
                .next()

        // add property definitions
        addPropertyDefs(resourceV, resourceDef.properties)

        // add association definitions
        addAssociationDefs(resourceV, resourceDef.associations)
    }

    override fun getResourceDef(ref: ResourceDefRef): Option<ResourceDef> {
        logger.debug("Getting resource definition '${ref}'")

        return graphTraversal
                .V()
                // get resource definition vertex by reference
                .has(LABEL_RESOURCE_DEF, PROPERTY_RESOURCE_DEF_REF, ref)
                .toList()
                // print out debugging information
                .onEach {
                    logger.debug("- Found resource definition '${it.value<Any>(PROPERTY_RESOURCE_DEF_REF)}'")
                }
                // map vertex to resource definition
                .map { toResourceDefFromVertex(it) }
                .flatten()
                .firstOrNone()
    }

    override val resourceDefs: List<ResourceDef>
        get() {
            logger.debug("Getting all resource definitions")

            return graphTraversal
                    .V()
                    // get all resource definition vertices
                    .hasLabel(LABEL_RESOURCE_DEF)
                    .toList()
                    // print out debugging information
                    .onEach {
                        logger.debug("- Found resource definition '${it.value<Any>(PROPERTY_RESOURCE_DEF_REF)}'")
                    }
                    .map { toResourceDefFromVertex(it) }
                    .flatten()
        }

    private fun toResourceDefFromVertex(resourceDefV: Vertex): Option<ResourceDef> {
        return when (resourceDefV.label()) {
            LABEL_RESOURCE_DEF -> ResourceDef(resourceDefV.value(PROPERTY_RESOURCE_DEF_REF),
                                              resourceDefV.value(PROPERTY_DESCRIPTION),
                                              getPropertyDefs(resourceDefV),
                                              getAssociationDefs(resourceDefV)).toOption()
            else -> {
                logger.error("Vertex is not of type '${LABEL_RESOURCE_DEF}'.")
                none()
            }
        }
    }

    private fun toPropertyDefFromVertex(propertyDefV: Vertex): Option<PropertyDef> {
        return when (propertyDefV.label()) {
            LABEL_PROPERTY_DEF -> PropertyDef(propertyDefV.value(PROPERTY_NAME),
                                              propertyDefV.value(PROPERTY_DESCRIPTION),
                                              propertyDefV.value(PROPERTY_TYPE),
                                              propertyDefV.value(PROPERTY_IS_MULTIVALUED),
                                              propertyDefV.value(PROPERTY_EXAMPLE_VALUES),
                                              getPropertyDefs(propertyDefV),
                                              getAssociationDefs(propertyDefV)).toOption()
            else -> {
                logger.error("Vertex is not of type '${LABEL_PROPERTY_DEF}'.")
                none()
            }
        }
    }

    private fun toAssociationDefFromVertex(associationDefV: Vertex): Option<AssociationDef> {
        return when (associationDefV.label()) {
            LABEL_ASSOCIATION_DEF -> AssociationDef(associationDefV.value(PROPERTY_NAME),
                                                    associationDefV.value(PROPERTY_DESCRIPTION),
                                                    associationDefV.value(PROPERTY_RESOURCE_DEF_REF),
                                                    associationDefV.value(PROPERTY_IS_MANY)).toOption()
            else -> {
                logger.error("Vertex is not of type '{}'.", LABEL_ASSOCIATION_DEF)
                none()
            }
        }
    }

    private fun getPropertyDefs(sourceV: Vertex): Set<PropertyDef> {
        return graphTraversal
                .V(sourceV.id())
                // get all property definition vertices
                .out(LABEL_HAS_PROPERTY_DEF)
                .toList()
                // print out debugging information
                .onEach {
                    logger.debug("- Found property definition '${it.value<String>(PROPERTY_NAME)}'")
                }
                .map { toPropertyDefFromVertex(it) }
                .flatten()
                .toSet()
    }

    private fun getAssociationDefs(sourceV: Vertex): Set<AssociationDef> {
        return graphTraversal
                .V(sourceV.id())
                .out(LABEL_HAS_ASSOCIATION_DEF)
                .toList()
                .onEach {
                    logger.debug("- Found association definition '${it.value<String>(PROPERTY_NAME)}'")
                }
                .map { toAssociationDefFromVertex(it) }
                .flatten()
                .toSet()
    }

    private fun addPropertyDefs(sourceV: Vertex, propertyDefs: Set<PropertyDef>) {
        propertyDefs.forEach { addPropertyDef(sourceV, it) }
    }

    private fun addPropertyDef(sourceV: Vertex, propertyDef: PropertyDef) {
        logger.debug("- Adding property definition '${getPropertyPath(sourceV, propertyDef.name)}' " +
                             "with value $propertyDef")

        // create property definition vertex
        val propertyFromV = graphTraversal
                .addV(LABEL_PROPERTY_DEF)
                .property(PROPERTY_NAME, propertyDef.name)
                .property(PROPERTY_DESCRIPTION, propertyDef.description)
                .property(PROPERTY_TYPE, propertyDef.propertyType)
                .property(PROPERTY_IS_MULTIVALUED, propertyDef.isMultiValued)
                .property(PROPERTY_EXAMPLE_VALUES, propertyDef.exampleValues)
                .next()

        // create edge between source (resource or property def) and property definition vertices
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_DEF)
                .from(sourceV)
                .to(propertyFromV)
                .next()

        // add nested property definitions
        addPropertyDefs(propertyFromV, propertyDef.properties)

        // add nested association definitions
        addAssociationDefs(propertyFromV, propertyDef.associations)
    }

    private fun addAssociationDefs(sourceV: Vertex, associationDefs: Set<AssociationDef>) {
        associationDefs.forEach { addAssociationDef(sourceV, it) }
    }

    private fun addAssociationDef(sourceV: Vertex, associationDef: AssociationDef) {
        logger.debug("- Adding association definition '${getPropertyPath(sourceV, associationDef.name)}' " +
                             "with value $associationDef")

        // create association definition vertex
        val associationDefV = graphTraversal
                .addV(LABEL_ASSOCIATION_DEF)
                .property(PROPERTY_NAME, associationDef.name)
                .property(PROPERTY_DESCRIPTION, associationDef.description)
                .property(PROPERTY_RESOURCE_DEF_REF, associationDef.defRef)
                .property(PROPERTY_IS_MANY, associationDef.isMany)
                .next()

        // create edge between source (resource or property definition) and association definition vertices
        graphTraversal
                .addE(LABEL_HAS_ASSOCIATION_DEF)
                .from(sourceV)
                .to(associationDefV)
                .next()
    }

    private fun getPropertyPath(sourceV: Vertex, memberName: String): String {
        return when (sourceV.label()) {
            LABEL_RESOURCE_DEF -> memberName
            else -> getPropertyPath(graphTraversal.V(sourceV.id())
                                            .`in`(LABEL_HAS_PROPERTY_DEF)
                                            .next(),
                                    sourceV.value(PROPERTY_NAME)).let { "${it}.${memberName}" }
        }
    }

    companion object {
        const val LABEL_RESOURCE_DEF = "resourceDef"
        const val LABEL_PROPERTY_DEF = "propertyDef"
        const val LABEL_ASSOCIATION_DEF = "associationDef"
        const val LABEL_HAS_PROPERTY_DEF = "hasPropertyDef"
        const val LABEL_HAS_ASSOCIATION_DEF = "hasAssociationDef"
        const val PROPERTY_RESOURCE_DEF_REF = "resourceDefRef"
        const val PROPERTY_PROVIDER_NAME = "providerName"
        const val PROPERTY_GROUP_NAME = "groupName"
        const val PROPERTY_NAME = "name"
        const val PROPERTY_EXAMPLE_VALUES = "exampleValues"
        const val PROPERTY_DESCRIPTION = "description"
        const val PROPERTY_TYPE = "type"
        const val PROPERTY_IS_MULTIVALUED = "isMultivalued"
        const val PROPERTY_IS_MANY = "isMany"
    }

}
