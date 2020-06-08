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

import cloudspec.lang.*
import cloudspec.model.ResourceDefRef
import cloudspec.validator.*
import org.apache.tinkerpop.gremlin.process.traversal.Compare
import org.apache.tinkerpop.gremlin.process.traversal.Contains
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.Traverser
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Graph
import org.apache.tinkerpop.gremlin.structure.Vertex
import org.slf4j.LoggerFactory
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as underscore


/**
 * Graph-based implementation of resource validator.
 *
 * This class implement [ResourceValidator] using Tinkerpop graphs created with
 * [GraphResourceStore] and [GraphResourceDefStore]
 */
class GraphResourceValidator(val graph: Graph) : ResourceValidator {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val graphTraversal: GraphTraversalSource = graph.traversal()

    override fun existById(ref: ResourceDefRef, id: String): Boolean {
        return buildGetResourceByIdTraversal(ref, id).hasNext()
    }

    override fun existAny(ref: ResourceDefRef, statements: List<Statement>): Boolean {
        return buildGetAllResourcesFilteredTraversal(ref, statements).hasNext()
    }

    override fun validateById(ref: ResourceDefRef,
                              id: String,
                              filterStatements: List<Statement>,
                              assertStatements: List<Statement>): ResourceValidationResult? {
        return buildGetResourceByIdFilteringTraversal(ref, id, filterStatements)
                .tryNext()
                .orElse(null)
                ?.let {
                    validateResource(it.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                                     it.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                                     assertStatements)
                }
    }

    override fun validateAll(ref: ResourceDefRef,
                             filterStatements: List<Statement>,
                             assertStatements: List<Statement>): List<ResourceValidationResult> {
        return buildGetAllResourcesFilteredTraversal(ref, filterStatements)
                .toList()
                .mapNotNull { resourceV ->
                    validateResource(resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                                     resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                                     assertStatements)
                }
    }

    private fun validateResource(ref: ResourceDefRef,
                                 id: String,
                                 assertStatements: List<Statement>): ResourceValidationResult? {
        // validate resource exists
        if (!existById(ref, id)) {
            logger.error(
                    "Resource '${ref}' with id '${id}' cannot be validated because it doesn't exist."
            )
            return null
        }

        // process assessments
        val assertValidationResults = assertStatements
                .flatMap {
                    // map assert statements to assertion traversals
                    buildAssertionTraversals(emptyList(), it)
                }
                .map {
                    // validate resource
                    validateResourceByAssertTraversal(ref, id, it)
                }

        // build resource validation result
        return ResourceValidationResult(ref, id, assertValidationResults)
    }

    private fun validateResourceByAssertTraversal(ref: ResourceDefRef,
                                                  id: String,
                                                  assertTraversal: GraphTraversal<*, AssertValidationResult>): AssertValidationResult {
        // get resource and apply assert traversal
        return buildGetResourceByIdTraversal(ref, id)
                .flatMap(assertTraversal)
                .next()
    }

    private fun buildGetResourcesByDefinitionTraversal(ref: ResourceDefRef): GraphTraversal<Vertex, *> {
        // get all resource vertex by resource definition
        return graphTraversal.V()
                .has(GraphResourceStore.LABEL_RESOURCE,
                     GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                     ref)
    }

    private fun buildGetResourceByIdTraversal(ref: ResourceDefRef,
                                              id: String): GraphTraversal<Vertex, *> {
        // get all resource vertex by resource definition and resource id
        return buildGetResourcesByDefinitionTraversal(ref)
                .has(GraphResourceStore.PROPERTY_RESOURCE_ID, id)
    }

    private fun buildGetResourceByIdFilteringTraversal(ref: ResourceDefRef,
                                                       id: String,
                                                       statements: List<Statement>): GraphTraversal<Vertex, Vertex> {
        // get resource by definition and id and apply statements
        return buildGetResourceByIdTraversal(ref, id)
                .and(*statements
                        .map { buildFilteringTraversal(it) }
                        .toTypedArray()
                ) as GraphTraversal<Vertex, Vertex>
    }

    private fun buildGetAllResourcesFilteredTraversal(ref: ResourceDefRef,
                                                      statements: List<Statement>): GraphTraversal<Vertex, Vertex> {
        return buildGetResourcesByDefinitionTraversal(ref)
                .and(*statements
                        .map { buildFilteringTraversal(it) }
                        .toTypedArray()
                ) as GraphTraversal<Vertex, Vertex>
    }

    private fun buildFilteringTraversal(statement: Statement): GraphTraversal<Vertex, Vertex> {
        return when (statement) {
            is KeyValueStatement -> buildKeyValueFilteringTraversal(statement)
            is PropertyStatement -> buildPropertyFilteringTraversal(statement)
            is AssociationStatement -> buildAssociationFilteringTraversal(statement)
            is NestedStatement -> buildNestedFilteringTraversal(statement)
        }
    }

    private fun buildAssertionTraversals(parentPath: List<String>,
                                         statement: Statement): List<GraphTraversal<Vertex, AssertValidationResult>> {
        return when (statement) {
            is KeyValueStatement -> buildKeyValueAssertionTraversal(parentPath, statement)
            is PropertyStatement -> buildPropertyAssertionTraversal(parentPath, statement)
            is AssociationStatement -> buildAssociationAssertionTraversal(parentPath, statement)
            is NestedStatement -> buildNestedAssertionTraversal(parentPath, statement)
        }
    }

    private fun buildPropertyFilteringTraversal(statement: PropertyStatement): GraphTraversal<Vertex, Vertex> {
        // traverse to property vertex matching the property name
        return underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                // traverse to property value vertex matching the predicate
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_VALUE, statement.predicate)
    }

    private fun buildKeyValueFilteringTraversal(statement: KeyValueStatement): GraphTraversal<Vertex, Vertex> {
        // traverse to property vertex matching the property name
        return underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                // traverse to property value vertex matching the key and the predicate
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_KEY, statement.key)
                .has(GraphResourceStore.PROPERTY_VALUE, statement.predicate)
    }

    private fun buildPropertyAssertionTraversal(parentPath: List<String>,
                                                statement: PropertyStatement): List<GraphTraversal<Vertex, AssertValidationResult>> {
        // add property element to the path
        val propertyPath = parentPath.plus(statement.propertyName)

        return listOf(
                underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                        .fold()
                        // check whether property with name exist, or return not found error
                        .coalesce(
                                underscore.unfold<Any>()
                                        .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                                        .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                        // check whether property value matches predicate, or return mismatch error
                                        .coalesce(
                                                underscore.has<Any>(GraphResourceStore.PROPERTY_VALUE, statement.predicate)
                                                        .constant(
                                                                AssertValidationResult(propertyPath, true)
                                                        ),
                                                underscore.map { propertyValueV: Traverser<Vertex> ->
                                                    AssertValidationResult(
                                                            propertyPath,
                                                            false,
                                                            buildAssertionError(statement.predicate,
                                                                                propertyValueV.get()
                                                                                        .value(GraphResourceStore.PROPERTY_VALUE)))
                                                }
                                        ),
                                underscore.constant(
                                        AssertValidationResult(propertyPath,
                                                               false,
                                                               AssertValidationMemberNotFoundError("Property does not exist"))
                                )
                        )
        )
    }

    private fun buildKeyValueAssertionTraversal(parentPath: List<String>,
                                                statement: KeyValueStatement): List<GraphTraversal<Vertex, AssertValidationResult>> {
        // add property element to path
        val propertyPath = parentPath.plus(statement.propertyName)

        return listOf(
                underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                        .fold() // check whether property with name exist, or return not found error
                        .coalesce(
                                underscore.unfold<Any>()
                                        .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                                        .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE) // check whether property value has the key, or return key found error
                                        .coalesce(
                                                underscore.has<Any>(GraphResourceStore.PROPERTY_KEY, statement.key) // check whether property value matches predicate, or return mismatch error
                                                        .coalesce(
                                                                underscore.has<Any>(GraphResourceStore.PROPERTY_VALUE, statement.predicate)
                                                                        .constant(
                                                                                AssertValidationResult(propertyPath,
                                                                                                       true)
                                                                        ),
                                                                underscore.map { propertyValueV: Traverser<Vertex> ->
                                                                    AssertValidationResult(propertyPath,
                                                                                           false,
                                                                                           buildAssertionError(statement.predicate,
                                                                                                               propertyValueV.get().value(GraphResourceStore.PROPERTY_VALUE)))
                                                                }
                                                        ),
                                                underscore.constant(
                                                        AssertValidationResult(propertyPath,
                                                                               false,
                                                                               AssertValidationKeyNotFoundError("Key ${statement.key} not found"))
                                                )
                                        ),
                                underscore.constant(
                                        AssertValidationResult(
                                                propertyPath,
                                                false,
                                                AssertValidationMemberNotFoundError("Property does not exist")
                                        )
                                )
                        )
        )
    }

    private fun buildNestedFilteringTraversal(statement: NestedStatement): GraphTraversal<Vertex, Vertex> {
        // traverse to property vertex matching the property name
        return underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                // traverse to property value and apply nested statements from there
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                // and validate nested statements
                .and(
                        *statement.statements
                                .map { buildFilteringTraversal(it) }
                                .toTypedArray()
                )
    }

    private fun buildNestedAssertionTraversal(parentPath: List<String>,
                                              statement: NestedStatement): List<GraphTraversal<Vertex, AssertValidationResult>> {
        // add property element to path
        val nestedPath = parentPath.plus(statement.propertyName)

        return statement.statements
                // map nested statement to assertion traversals
                .flatMap { buildAssertionTraversals(nestedPath, it) }
                .map {
                    underscore.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                            .fold() // check whether property with name exist, or return not found error
                            .coalesce(
                                    underscore.unfold<Any>()
                                            .has(GraphResourceStore.PROPERTY_NAME, statement.propertyName)
                                            .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                            .flatMap(it),
                                    underscore.constant(
                                            AssertValidationResult(nestedPath,
                                                                   false,
                                                                   AssertValidationMemberNotFoundError("Property does not exist"))
                                    )
                            )
                }
    }

    private fun buildAssociationFilteringTraversal(statement: AssociationStatement): GraphTraversal<Vertex, Vertex> {
        // traverse to association edge matching the property name
        return underscore.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                .has(GraphResourceStore.PROPERTY_NAME, statement.associationName)
                // traverse the target resource vertex
                .inV()
                .hasLabel(GraphResourceStore.LABEL_RESOURCE)
                // and association nested statements
                .and(
                        *statement.statements
                                .map { buildFilteringTraversal(it) }
                                .toTypedArray()
                )
    }

    private fun buildAssociationAssertionTraversal(parentPath: List<String>,
                                                   statement: AssociationStatement): List<GraphTraversal<Vertex, AssertValidationResult>> {
        // add association element to path
        val associationPath = parentPath.plus(statement.associationName)

        return statement.statements
                // map association statements to assertion traversals
                .flatMap { buildAssertionTraversals(associationPath, it) }
                .map {
                    underscore.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                            .fold()
                            // check whether association with name exist, or return not found error
                            .coalesce(
                                    underscore.unfold<Any>()
                                            .has(GraphResourceStore.PROPERTY_NAME, statement.associationName)
                                            .inV()
                                            .hasLabel(GraphResourceStore.LABEL_RESOURCE)
                                            .flatMap(it),
                                    underscore.constant(
                                            AssertValidationResult(associationPath,
                                                                   false,
                                                                   AssertValidationMemberNotFoundError("Association does not exist"))
                                    )
                            )
                }
    }

    private fun buildAssertionError(predicate: P<*>, value: Any): AssertValidationError {
        return when (predicate.biPredicate) {
            Compare.eq,
            Compare.neq -> AssertValidationMismatchError(predicate.originalValue,
                                                         value)
            Contains.within,
            Contains.without -> AssertValidationContainError(predicate.originalValue as List<Any>,
                                                             value)
            else -> AssertValidationUnknownError("Unknown predicate ${predicate.biPredicate}")
        }
    }
}
