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

import cloudspec.lang.*;
import cloudspec.model.ResourceDefRef;
import cloudspec.validator.*;
import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.apache.tinkerpop.gremlin.process.traversal.Contains;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Graph-based implementation of resource validator.
 * <p>
 * This class implement {@link ResourceValidator} using Tinkerpop graphs created with
 * {@link GraphResourceStore} and {@link GraphResourceDefStore}
 */
public class GraphResourceValidator implements ResourceValidator {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceValidator.class);

    private final GraphTraversalSource graphTraversal;

    /**
     * Constructor.
     *
     * @param graph TinkerPop Graph.
     */
    public GraphResourceValidator(Graph graph) {
        this.graphTraversal = graph.traversal();
    }

    @Override
    public Boolean existById(ResourceDefRef resourceDefRef,
                             String resourceId) {
        return buildGetResourceByIdTraversal(resourceDefRef, resourceId).hasNext();
    }

    @Override
    public Boolean existAny(ResourceDefRef resourceDefRef,
                            List<Statement> filterStatements) {
        return buildGetAllResourcesFilteredTraversal(resourceDefRef, filterStatements).hasNext();
    }

    @Override
    public Optional<ResourceValidationResult> validateById(ResourceDefRef resourceDefRef,
                                                           String resourceId,
                                                           List<Statement> filterStatements,
                                                           List<Statement> assertStatements) {
        return buildGetResourceByIdFilteringTraversal(resourceDefRef, resourceId, filterStatements)
                .tryNext()
                .flatMap(resourceV ->
                        this.validateResource(
                                resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                                resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                                assertStatements
                        )
                );
    }

    public List<ResourceValidationResult> validateAll(ResourceDefRef resourceDefRef,
                                                      List<Statement> filterStatements,
                                                      List<Statement> assertStatements) {
        return buildGetAllResourcesFilteredTraversal(resourceDefRef, filterStatements)
                .toStream()
                .map(resourceV ->
                        validateResource(
                                resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                                resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                                assertStatements
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<ResourceValidationResult> validateResource(ResourceDefRef resourceDefRef,
                                                                String resourceId,
                                                                List<Statement> assertStatements) {
        // validate resource exists
        if (!existById(resourceDefRef, resourceId)) {
            LOGGER.error(
                    "Resource '{}' with id '{}' cannot be validated because it doesn't exist.",
                    resourceDefRef, resourceId
            );
            return Optional.empty();
        }

        // process assessments
        var assertValidationResults = assertStatements
                .stream()
                .flatMap(assertStatement ->
                        // map assert statements to assertion traversals
                        buildAssertionTraversals(new ArrayList<>(), assertStatement)
                )
                .map(traversal ->
                        // validate resource
                        validateResourceByAssertTraversal(resourceDefRef, resourceId, traversal)
                )
                .collect(Collectors.toList());

        // build resource validation result
        return Optional.of(
                new ResourceValidationResult(
                        resourceDefRef,
                        resourceId,
                        assertValidationResults
                )
        );
    }

    private AssertValidationResult validateResourceByAssertTraversal(ResourceDefRef resourceDefRef,
                                                                     String resourceId,
                                                                     GraphTraversal<?, AssertValidationResult> assertTraversal) {
        // get resource and apply assert traversal
        return buildGetResourceByIdTraversal(resourceDefRef, resourceId)
                .flatMap(assertTraversal)
                .next();
    }

    private GraphTraversal<Vertex, ?> buildGetResourcesByDefinitionTraversal(ResourceDefRef resourceDefRef) {
        // get all resource vertex by resource definition
        return graphTraversal.V()
                .has(
                        GraphResourceStore.LABEL_RESOURCE,
                        GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
                );
    }

    private GraphTraversal<Vertex, ?> buildGetResourceByIdTraversal(ResourceDefRef resourceDefRef,
                                                                    String resourceId) {
        // get all resource vertex by resource definition and resource id
        return buildGetResourcesByDefinitionTraversal(resourceDefRef)
                .has(
                        GraphResourceStore.PROPERTY_RESOURCE_ID,
                        resourceId
                );
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<Vertex, Vertex> buildGetResourceByIdFilteringTraversal(ResourceDefRef resourceDefRef,
                                                                                  String resourceId,
                                                                                  List<Statement> statements) {
        // get resource by definition and id and apply statements
        return (GraphTraversal<Vertex, Vertex>) buildGetResourceByIdTraversal(resourceDefRef, resourceId)
                .and(
                        statements
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<Vertex, Vertex> buildGetAllResourcesFilteredTraversal(ResourceDefRef resourceDefRef,
                                                                                 List<Statement> statements) {
        return (GraphTraversal<Vertex, Vertex>) buildGetResourcesByDefinitionTraversal(resourceDefRef)
                .and(
                        statements
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private GraphTraversal<Vertex, Vertex> buildFilteringTraversal(Statement statement) {
        if (statement instanceof KeyValueStatement) {
            return buildKeyValueFilteringTraversal((KeyValueStatement) statement);
        } else if (statement instanceof PropertyStatement) {
            return buildPropertyFilteringTraversal((PropertyStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return buildAssociationFilteringTraversal((AssociationStatement) statement);
        } else if (statement instanceof NestedStatement) {
            return buildNestedFilteringTraversal((NestedStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private Stream<GraphTraversal<Vertex, AssertValidationResult>> buildAssertionTraversals(List<String> parentPath,
                                                                                            Statement statement) {
        if (statement instanceof KeyValueStatement) {
            return buildKeyValueAssertionTraversal(parentPath, (KeyValueStatement) statement);
        } else if (statement instanceof PropertyStatement) {
            return buildPropertyAssertionTraversal(parentPath, (PropertyStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return buildAssociationAssertionTraversal(parentPath, (AssociationStatement) statement);
        } else if (statement instanceof NestedStatement) {
            return buildNestedAssertionTraversal(parentPath, (NestedStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private GraphTraversal<Vertex, Vertex> buildPropertyFilteringTraversal(PropertyStatement statement) {
        // traverse to property vertex matching the property name
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                // traverse to property value vertex matching the predicate
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate());
    }

    private GraphTraversal<Vertex, Vertex> buildKeyValueFilteringTraversal(KeyValueStatement statement) {
        // traverse to property vertex matching the property name
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                // traverse to property value vertex matching the key and the predicate
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_KEY, statement.getKey())
                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate());
    }

    @SuppressWarnings("unchecked")
    private Stream<GraphTraversal<Vertex, AssertValidationResult>> buildPropertyAssertionTraversal(List<String> parentPath,
                                                                                                   PropertyStatement statement) {
        // add property element to the path
        var propertyPath = new ArrayList<>(parentPath);
        propertyPath.add(statement.getPropertyName());

        return Stream.of(
                __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                        .fold()
                        // check whether property with name exist, or return not found error
                        .coalesce(
                                __.unfold()
                                        .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                                        .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                        // check whether property value matches predicate, or return mismatch error
                                        .coalesce(
                                                __.has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate())
                                                        .constant(
                                                                new AssertValidationResult(
                                                                        propertyPath,
                                                                        Boolean.TRUE
                                                                )
                                                        ),
                                                __.map((Traverser<Vertex> propertyValueV) ->
                                                        new AssertValidationResult(
                                                                propertyPath,
                                                                Boolean.FALSE,
                                                                buildAssertionError(
                                                                        statement.getPredicate(),
                                                                        propertyValueV.get().value(GraphResourceStore.PROPERTY_VALUE)
                                                                )
                                                        )
                                                )
                                        ),
                                __.constant(
                                        new AssertValidationResult(
                                                propertyPath,
                                                Boolean.FALSE,
                                                new AssertValidationMemberNotFoundError("Property does not exist")

                                        )
                                )
                        )
        );
    }

    @SuppressWarnings("unchecked")
    private Stream<GraphTraversal<Vertex, AssertValidationResult>> buildKeyValueAssertionTraversal(List<String> parentPath,
                                                                                                   KeyValueStatement statement) {
        // add property element to path
        var propertyPath = new ArrayList<>(parentPath);
        propertyPath.add(statement.getPropertyName());

        return Stream.of(
                __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                        .fold()
                        // check whether property with name exist, or return not found error
                        .coalesce(
                                __.unfold()
                                        .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                                        .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                        // check whether property value has the key, or return key found error
                                        .coalesce(
                                                __.has(GraphResourceStore.PROPERTY_KEY, statement.getKey())
                                                        // check whether property value matches predicate, or return mismatch error
                                                        .coalesce(
                                                                __.has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate())
                                                                        .constant(
                                                                                new AssertValidationResult(
                                                                                        propertyPath,
                                                                                        Boolean.TRUE
                                                                                )
                                                                        ),
                                                                __.map((Traverser<Vertex> propertyValueV) ->
                                                                        new AssertValidationResult(
                                                                                propertyPath,
                                                                                Boolean.FALSE,
                                                                                buildAssertionError(
                                                                                        statement.getPredicate(),
                                                                                        propertyValueV.get().value(GraphResourceStore.PROPERTY_VALUE)
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                __.constant(
                                                        new AssertValidationResult(
                                                                propertyPath,
                                                                Boolean.FALSE,
                                                                new AssertValidationKeyNotFoundError(
                                                                        String.format(
                                                                                "Key %s not found",
                                                                                statement.getKey()
                                                                        )
                                                                )
                                                        )
                                                )
                                        ),
                                __.constant(
                                        new AssertValidationResult(
                                                propertyPath,
                                                Boolean.FALSE,
                                                new AssertValidationMemberNotFoundError("Property does not exist")
                                        )
                                )
                        )
        );
    }

    private GraphTraversal<Vertex, Vertex> buildNestedFilteringTraversal(NestedStatement statement) {
        // traverse to property vertex matching the property name
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                // traverse to property value and apply nested statements from there
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                // and validate nested statements
                .and(
                        statement.getStatements()
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    @SuppressWarnings("unchecked")
    private Stream<GraphTraversal<Vertex, AssertValidationResult>> buildNestedAssertionTraversal(List<String> parentPath,
                                                                                                 NestedStatement statement) {
        // add property element to path
        var nestedPath = new ArrayList<>(parentPath);
        nestedPath.add(statement.getPropertyName());

        return statement
                .getStatements()
                .stream()
                // map nested statement to assertion traversals
                .flatMap(stmt -> buildAssertionTraversals(nestedPath, stmt))
                .map(traversal ->
                        __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                                .fold()
                                // check whether property with name exist, or return not found error
                                .coalesce(
                                        __.unfold()
                                                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                                                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                                .flatMap(traversal),
                                        __.constant(
                                                new AssertValidationResult(
                                                        nestedPath,
                                                        Boolean.FALSE,
                                                        new AssertValidationMemberNotFoundError("Property does not exist")
                                                )
                                        )
                                )
                );
    }

    private GraphTraversal<Vertex, Vertex> buildAssociationFilteringTraversal(AssociationStatement statement) {
        // traverse to association edge matching the property name
        return __.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getAssociationName())
                // traverse the target resource vertex
                .inV()
                .hasLabel(GraphResourceStore.LABEL_RESOURCE)
                // and association nested statements
                .and(
                        statement.getStatements()
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    @SuppressWarnings("unchecked")
    private Stream<GraphTraversal<Vertex, AssertValidationResult>> buildAssociationAssertionTraversal(List<String> parentPath,
                                                                                                      AssociationStatement statement) {
        // add association element to path
        var associationPath = new ArrayList<>(parentPath);
        associationPath.add(statement.getAssociationName());

        return statement
                .getStatements()
                .stream()
                // map association statements to assertion traversals
                .flatMap(stmt ->
                        buildAssertionTraversals(associationPath, stmt)
                )
                .map(traversal ->
                        __.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                                .fold()
                                // check whether association with name exist, or return not found error
                                .coalesce(
                                        __.unfold()
                                                .has(GraphResourceStore.PROPERTY_NAME, statement.getAssociationName())
                                                .inV()
                                                .hasLabel(GraphResourceStore.LABEL_RESOURCE)
                                                .flatMap(traversal),
                                        __.constant(
                                                new AssertValidationResult(
                                                        associationPath,
                                                        Boolean.FALSE,
                                                        new AssertValidationMemberNotFoundError("Association does not exist")
                                                )
                                        )
                                )

                );
    }

    @SuppressWarnings("unchecked")
    private AssertValidationError buildAssertionError(P<?> predicate, Object value) {
        if (predicate.getBiPredicate().equals(Compare.eq) || predicate.getBiPredicate().equals(Compare.neq)) {
            return new AssertValidationMismatchError(
                    predicate.getOriginalValue(),
                    value
            );
        } else if (predicate.getBiPredicate().equals(Contains.within) || predicate.getBiPredicate().equals(Contains.within)) {
            return new AssertValidationContainError(
                    (List<Object>) predicate.getOriginalValue(),
                    value
            );
        }

        return new AssertValidationUnknownError(
                String.format("Unknown predicate %s", predicate.getBiPredicate())
        );
    }
}
