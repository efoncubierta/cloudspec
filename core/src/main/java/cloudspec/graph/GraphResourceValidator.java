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
import cloudspec.validator.ResourceValidator;
import cloudspec.validator.ResourceValidatorResult;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GraphResourceValidator implements ResourceValidator {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceValidator.class);


    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceValidator() {
        this(TinkerGraph.open());
    }

    public GraphResourceValidator(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Boolean existById(ResourceDefRef resourceDefRef, String resourceId) {
        return gTraversal.V()
                .has(
                        GraphResourceStore.LABEL_RESOURCE,
                        GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
                )
                .has(
                        GraphResourceStore.PROPERTY_RESOURCE_ID,
                        resourceId
                ).hasNext();
    }

    @Override
    public Boolean existAny(ResourceDefRef resourceDefRef, Statement filterStatement) {
        return gTraversal.V()
                .and(
                        __.has(
                                GraphResourceStore.LABEL_RESOURCE,
                                GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                                resourceDefRef
                        ),
                        getStatementTraversal(filterStatement)
                ).hasNext();
    }

    @Override
    public ResourceValidatorResult validateById(ResourceDefRef resourceDefRef,
                                                String resourceId,
                                                Statement filterStatement,
                                                Statement assertStatement) {
        return null;
    }

    public List<ResourceValidatorResult> validateAll(ResourceDefRef resourceDefRef,
                                                     Statement filterStatement,
                                                     Statement assertStatement) {
        GraphTraversal<Vertex, ?> resourcesTraversal = gTraversal.V()
                .and(
                        __.has(
                                GraphResourceStore.LABEL_RESOURCE,
                                GraphResourceStore.PROPERTY_RESOURCE_DEF_REF,
                                resourceDefRef
                        ),
                        getStatementTraversal(filterStatement)
                );

        System.out.println(resourcesTraversal);
        System.out.println(resourcesTraversal.hasNext());

//        return resourcesTraversal.hasNext();
        return Collections.emptyList();
    }

    private GraphTraversal<?, ?> getStatementTraversal(Statement statement) {
        if (statement instanceof PropertyStatement) {
            return getPropertyTraversal((PropertyStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            return getAssociationTraversal((AssociationStatement) statement);
        } else if (statement instanceof CombinedStatement) {
            return getCombinedTraversal((CombinedStatement) statement);
        }

        throw new RuntimeException(
                String.format("Unsupported statement type %s", statement.getClass().getCanonicalName())
        );
    }

    private GraphTraversal<?, ?> getPropertyTraversal(PropertyStatement propertyStatement) {
        return __.out(GraphResourceStore.LABEL_HAS_PROPERTY)
                .has(GraphResourceStore.LABEL_PROPERTY, GraphResourceStore.PROPERTY_NAME, propertyStatement.getPropertyName())
                .has(GraphResourceStore.PROPERTY_VALUE, propertyStatement.getPredicate());
    }

    private GraphTraversal<?, ?> getAssociationTraversal(AssociationStatement associationStatement) {
        return __.out(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                .has(
                        GraphResourceStore.LABEL_ASSOCIATION,
                        GraphResourceStore.PROPERTY_NAME,
                        associationStatement.getAssociationName()
                )
                .in(GraphResourceStore.LABEL_HAS_ASSOCIATION)
                .and(
                        __.hasLabel(
                                GraphResourceStore.LABEL_RESOURCE
                        ),
                        getStatementTraversal(associationStatement.getStatement())
                );
    }

    private GraphTraversal<?, ?> getCombinedTraversal(CombinedStatement combinedStatement) {
        GraphTraversal<?, ?>[] traversals = combinedStatement.getStatements()
                .stream()
                .map(this::getStatementTraversal)
                .collect(Collectors.toList())
                .toArray(new GraphTraversal<?, ?>[combinedStatement.getStatements().size()]);

        if (combinedStatement.getLogicalOperator().equals(LogicalOperator.AND)) {
            return __.and(traversals);
        } else {
            return __.or(traversals);
        }
    }
}
