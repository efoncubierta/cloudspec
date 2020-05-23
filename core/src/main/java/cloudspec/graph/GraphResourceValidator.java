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
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
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
                .map(resourceV -> this.validateResource(
                        resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                        resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                        assertStatements
                ));
    }

    public List<ResourceValidationResult> validateAll(ResourceDefRef resourceDefRef,
                                                      List<Statement> filterStatements,
                                                      List<Statement> assertStatements) {
        return buildGetAllResourcesFilteredTraversal(resourceDefRef, filterStatements)
                .toStream()
                .map(resourceV -> this.validateResource(
                        resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                        resourceV.value(GraphResourceStore.PROPERTY_RESOURCE_ID),
                        assertStatements
                ))
                .collect(Collectors.toList());
    }

    private GraphTraversal<Vertex, ?> buildGetResourceByIdTraversal(ResourceDefRef resourceDefRef,
                                                                    String resourceId) {
        return graphTraversal.V()
                .has(
                        GraphResourceStore.LABEL_RESOURCE,
                        GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
                )
                .has(
                        GraphResourceStore.PROPERTY_RESOURCE_ID,
                        resourceId
                );
    }

    private ResourceValidationResult validateResource(ResourceDefRef resourceDefRef,
                                                      String resourceId,
                                                      List<Statement> assertStatements) {
        return new ResourceValidationResult(
                resourceDefRef,
                resourceId,
                assertStatements
                        .stream()
                        .flatMap(assertStatement ->
                                buildAssertionTraversals(new ArrayList<>(), assertStatement).stream()
                        )
                        .map(traversal ->
                                buildGetResourceByIdTraversal(resourceDefRef, resourceId).flatMap(traversal).next()
                        )
                        .collect(Collectors.toList())
        );

    }

    private GraphTraversal<Vertex, Vertex> buildGetResourceByIdFilteringTraversal(ResourceDefRef resourceDefRef,
                                                                                  String resourceId,
                                                                                  List<Statement> statements) {
        return graphTraversal.V()
                .has(
                        GraphResourceStore.LABEL_RESOURCE,
                        GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
                )
                .has(
                        GraphResourceStore.PROPERTY_RESOURCE_ID,
                        resourceId
                )
                .and(
                        statements
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private GraphTraversal<Vertex, Vertex> buildGetAllResourcesFilteredTraversal(ResourceDefRef resourceDefRef,
                                                                                 List<Statement> statements) {
        return graphTraversal.V()
                .has(
                        GraphResourceStore.LABEL_RESOURCE,
                        GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
                )
                .and(
                        statements
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private GraphTraversal<?, ?> buildFilteringTraversal(Statement statement) {
        if (statement instanceof PropertyStatement) {
            return buildPropertyFilteringTraversal((PropertyStatement) statement);
        } else if (statement instanceof KeyValueStatement) {
            return buildKeyValueFilteringTraversal((KeyValueStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return buildAssociationFilteringTraversal((AssociationStatement) statement);
        } else if (statement instanceof NestedStatement) {
            return buildNestedFilteringTraversal((NestedStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private List<GraphTraversal<?, AssertValidationResult>> buildAssertionTraversals(List<String> parentPath,
                                                                                     Statement statement) {
        if (statement instanceof PropertyStatement) {
            return Collections.singletonList(
                    buildPropertyAssertionTraversal(parentPath, (PropertyStatement) statement)
            );
        } else if (statement instanceof KeyValueStatement) {
            return Collections.singletonList(
                    buildKeyValueAssertionTraversal(parentPath, (KeyValueStatement) statement)
            );
        } else if (statement instanceof AssociationStatement) {
            return buildAssociationAssertionTraversal(parentPath, (AssociationStatement) statement);
        } else if (statement instanceof NestedStatement) {
            return buildNestedAssertionTraversal(parentPath, (NestedStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private GraphTraversal<?, ?> buildPropertyFilteringTraversal(PropertyStatement statement) {
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate());
    }

    private GraphTraversal<?, ?> buildKeyValueFilteringTraversal(KeyValueStatement statement) {
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .has(GraphResourceStore.PROPERTY_KEY, statement.getKey())
                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate());
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<?, AssertValidationResult> buildPropertyAssertionTraversal(List<String> parentPath,
                                                                                      PropertyStatement statement) {
        List<String> propertyPath = new ArrayList<>(parentPath);
        propertyPath.add(statement.getPropertyName());

        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .fold()
                .coalesce(
                        __.unfold()
                                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                                .fold()
                                .coalesce(
                                        __.unfold()
                                                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate())
                                                .constant(
                                                        new AssertValidationResult(
                                                                propertyPath,
                                                                Boolean.TRUE
                                                        )
                                                ),
                                        __.unfold()
                                                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                                .map(propertyValueV ->
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
                );
    }

    @SuppressWarnings("unchecked")
    private GraphTraversal<?, AssertValidationResult> buildKeyValueAssertionTraversal(List<String> parentPath,
                                                                                      KeyValueStatement statement) {
        List<String> propertyPath = new ArrayList<>(parentPath);
        propertyPath.add(statement.getPropertyName());

        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .fold()
                .coalesce(
                        __.unfold()
                                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                                .fold()
                                .coalesce(
                                        __.unfold()
                                                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                                .has(GraphResourceStore.PROPERTY_KEY, statement.getKey())
                                                .has(GraphResourceStore.PROPERTY_VALUE, statement.getPredicate())
                                                .constant(
                                                        new AssertValidationResult(
                                                                propertyPath,
                                                                Boolean.TRUE
                                                        )
                                                ),
                                        __.unfold()
                                                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                                                .map(propertyValueV ->
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
                );
    }

    private GraphTraversal<?, ?> buildNestedFilteringTraversal(NestedStatement statement) {
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getPropertyName())
                .out(GraphResourceStore.LABEL_HAS_PROPERTY_VALUE)
                .and(
                        statement.getStatements()
                                .stream()
                                .map(this::buildFilteringTraversal)
                                .toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    @SuppressWarnings("unchecked")
    private List<GraphTraversal<?, AssertValidationResult>> buildNestedAssertionTraversal(List<String> parentPath,
                                                                                          NestedStatement statement) {
        List<String> nestedPath = new ArrayList<>(parentPath);
        nestedPath.add(statement.getPropertyName());


        return statement.getStatements()
                .stream()
                .flatMap(stmt -> buildAssertionTraversals(nestedPath, stmt).stream())
                .map(traversal ->
                        __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                                .fold()
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
                )
                .collect(Collectors.toList());
    }

    private GraphTraversal<?, ?> buildAssociationFilteringTraversal(AssociationStatement statement) {
        return __.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                .has(GraphResourceStore.PROPERTY_NAME, statement.getAssociationName())
                .inV()
                .and(
                        Stream.concat(
                                Stream.of(
                                        __.hasLabel(GraphResourceStore.LABEL_RESOURCE)
                                ),
                                statement.getStatements()
                                        .stream()
                                        .map(this::buildFilteringTraversal)
                        ).toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    @SuppressWarnings("unchecked")
    private List<GraphTraversal<?, AssertValidationResult>> buildAssociationAssertionTraversal(List<String> parentPath,
                                                                                               AssociationStatement statement) {
        List<String> associationPath = new ArrayList<>(parentPath);
        associationPath.add(statement.getAssociationName());

        return statement
                .getStatements()
                .stream()
                .flatMap(stmt ->
                        buildAssertionTraversals(associationPath, stmt).stream()
                )
                .map(traversal ->
                        __.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                                .fold()
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

                )
                .collect(Collectors.toList());
    }

    private AssertValidationError buildAssertionError(P<?> predicate, Object value) {
        if (predicate.getBiPredicate().equals(Compare.eq) || predicate.getBiPredicate().equals(Compare.neq)) {
            return new AssertValidationMismatchError(
                    predicate.getOriginalValue(),
                    value
            );
        } else if (predicate.getBiPredicate().equals(Contains.within) || predicate.getBiPredicate().equals(Contains.within)) {
            return new AssertValidationContainError(
                    (List<Object>) value,
                    predicate.getOriginalValue()

            );
        }

        return new AssertValidationUnknownError(
                String.format("Unknown predicate %s", predicate.getBiPredicate())
        );
    }
}
