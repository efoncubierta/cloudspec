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

import cloudspec.lang.AssertExpr;
import cloudspec.lang.WithExpr;
import cloudspec.model.ResourceDefRef;
import cloudspec.validator.ResourceValidator;
import cloudspec.validator.ResourceValidatorResult;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class GraphResourceValidator implements ResourceValidator {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceValidator.class);

    private static final String LABEL_RESOURCE = "resource";
    private static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";

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
    public ResourceValidatorResult validateById(ResourceDefRef resourceDefRef, String resourceId, List<WithExpr> withExprs, List<AssertExpr> assertExprs) {
        return null;
    }

    public List<ResourceValidatorResult> validateAll(ResourceDefRef resourceDefRef, List<WithExpr> withExprs, List<AssertExpr> assertExprs) {
        GraphTraversal<Vertex, Vertex> resourcesTraversal = gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef.toString())
                .as("r");

        addFilters(resourcesTraversal, withExprs);

//        return resourcesTraversal.hasNext();
        return Collections.emptyList();
    }

    private void addFilters(GraphTraversal<Vertex, Vertex> resourcesTraversal, List<WithExpr> withs) {
        withs.forEach(withExpr -> {
            resourcesTraversal.has(withExpr.getPropertyName(), withExpr.getPredicate());
        });
    }
}
