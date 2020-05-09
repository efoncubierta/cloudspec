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

import cloudspec.lang.AssociationStatement;
import cloudspec.lang.PropertyStatement;
import cloudspec.lang.Statement;
import cloudspec.model.ResourceDefRef;
import cloudspec.validator.AssertValidationResult;
import cloudspec.validator.ResourceValidationResult;
import cloudspec.validator.ResourceValidator;
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

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

public class GraphResourceValidator implements ResourceValidator {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceValidator.class);


    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceValidator(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Boolean existById(ResourceDefRef resourceDefRef,
                             String resourceId) {
        return buildGetResourceByIdQuery(resourceDefRef, resourceId).hasNext();
    }

    @Override
    public Boolean existAny(ResourceDefRef resourceDefRef,
                            List<Statement> filterStatements) {
        return buildGetAllResourcesFilteredQuery(resourceDefRef, filterStatements).hasNext();
    }

    @Override
    public Optional<ResourceValidationResult> validateById(ResourceDefRef resourceDefRef,
                                                           String resourceId,
                                                           List<Statement> filterStatements,
                                                           List<Statement> assertStatements) {
        return buildGetResourceByIdFilteredQuery(resourceDefRef, resourceId, filterStatements)
                .valueMap()
                .by(unfold())
                .tryNext()
                .map(resourceMap -> this.validateResource(
                        (ResourceDefRef) resourceMap.get(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceMap.get(GraphResourceStore.PROPERTY_RESOURCE_ID),
                        assertStatements
                ));
    }

    public List<ResourceValidationResult> validateAll(ResourceDefRef resourceDefRef,
                                                      List<Statement> filterStatements,
                                                      List<Statement> assertStatements) {
        return buildGetAllResourcesFilteredQuery(resourceDefRef, filterStatements)
                .valueMap()
                .by(unfold())
                .toStream()
                .map(resourceMap -> this.validateResource(
                        (ResourceDefRef) resourceMap.get(GraphResourceStore.PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceMap.get(GraphResourceStore.PROPERTY_RESOURCE_ID),
                        assertStatements
                ))
                .collect(Collectors.toList());
    }

    private ResourceValidationResult validateResource(ResourceDefRef resourceDefRef,
                                                      String resourceId,
                                                      List<Statement> assertStatements) {
        return new ResourceValidationResult(
                resourceDefRef,
                resourceId,
                assertStatements.stream()
                        .flatMap(assertStatement ->
                                buildTraversalForAssertion(new ArrayList<>(), assertStatement).stream()
                        )
                        .map(traversal ->
                                buildGetResourceByIdQuery(resourceDefRef, resourceId)
                                        .flatMap(traversal)
                                        .next()
                        )
                        .collect(Collectors.toList())
        );

    }

    private GraphTraversal<Vertex, ?> buildGetResourceByIdQuery(ResourceDefRef resourceDefRef,
                                                                String resourceId) {
        return gTraversal.V()
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

    private GraphTraversal<Vertex, ?> buildGetResourceByIdFilteredQuery(ResourceDefRef resourceDefRef,
                                                                        String resourceId,
                                                                        List<Statement> statements) {
        return gTraversal.V()
                .and(
                        Stream.concat(
                                Stream.of(
                                        __.has(
                                                GraphResourceStore.LABEL_RESOURCE,
                                                GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                                                resourceDefRef
                                        ),
                                        __.has(
                                                GraphResourceStore.PROPERTY_RESOURCE_ID,
                                                resourceId
                                        )
                                ),
                                statements.stream().map(this::buildTraversalForFiltering)
                        ).toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private GraphTraversal<Vertex, ?> buildGetAllResourcesFilteredQuery(ResourceDefRef resourceDefRef,
                                                                        List<Statement> statements) {
        return gTraversal.V()
                .and(
                        Stream.concat(
                                Stream.of(
                                        __.has(
                                                GraphResourceStore.LABEL_RESOURCE,
                                                GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                                                resourceDefRef
                                        )
                                ),
                                statements.stream().map(this::buildTraversalForFiltering)
                        ).toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private GraphTraversal<?, ?> buildTraversalForFiltering(Statement statement) {
        if (statement instanceof PropertyStatement) {
            return buildTraversalForPropertyFiltering((PropertyStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return buildTraversalForAssociationFiltering((AssociationStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private List<GraphTraversal<?, AssertValidationResult>> buildTraversalForAssertion(List<String> path,
                                                                                       Statement statement) {
        if (statement instanceof PropertyStatement) {
            return buildTraversalForPropertyAssertion(path, (PropertyStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return buildTraversalForAssociationAssertion(path, (AssociationStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement of type %s", statement.getClass().getCanonicalName())
        );
    }

    private GraphTraversal<?, ?> buildTraversalForPropertyFiltering(PropertyStatement statement) {
        return __.has(
                statement.getPropertyName(),
                statement.getPredicate()
        );
    }

    private List<GraphTraversal<?, AssertValidationResult>> buildTraversalForPropertyAssertion(List<String> path,
                                                                                               PropertyStatement statement) {
        List<String> propertyPath = new ArrayList<>(path);
        propertyPath.add(statement.getPropertyName());

        return Collections.singletonList(
                __.has(statement.getPropertyName(), statement.getPredicate())
                        .fold()
                        .coalesce(
                                __.constant(
                                        new AssertValidationResult(
                                                propertyPath,
                                                Boolean.TRUE
                                        )
                                ),
                                __.constant(
                                        new AssertValidationResult(
                                                propertyPath,
                                                Boolean.FALSE,
                                                String.format(
                                                        "Property '%s' does not exist or match the value '%s'",
                                                        statement.getPropertyName(),
                                                        statement.getPredicate().getOriginalValue()
                                                )
                                        )
                                )
                        )
        );
    }

    private GraphTraversal<?, ?> buildTraversalForAssociationFiltering(AssociationStatement statement) {
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
                                        .map(this::buildTraversalForFiltering)
                        ).toArray(GraphTraversal<?, ?>[]::new)
                );
    }

    private List<GraphTraversal<?, AssertValidationResult>> buildTraversalForAssociationAssertion(List<String> path,
                                                                                                  AssociationStatement statement) {
        List<String> associationPath = new ArrayList<>(path);
        associationPath.add(statement.getAssociationName());

        return statement.getStatements()
                .stream()
                .flatMap(stmt ->
                        buildTraversalForAssertion(associationPath, stmt).stream()
                )
                .map(traversal ->
                        __.outE(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                                .has(GraphResourceStore.PROPERTY_NAME, statement.getAssociationName())
                                .inV()
                                .hasLabel(GraphResourceStore.LABEL_RESOURCE)
                                .flatMap(traversal)
                )
                .collect(Collectors.toList());
    }
}
