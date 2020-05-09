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

import cloudspec.model.Association;
import cloudspec.model.Property;
import cloudspec.model.Resource;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.valueMap;

public class GraphResourceStore implements ResourceStore {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceStore.class);

    public static final String LABEL_RESOURCE = "resource";
    public static final String LABEL_HAS_ASSOCIATION = "hasAssociation";
    public static final String PROPERTY_RESOURCE_ID = "resourceId";
    public static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    public static final String PROPERTY_NAME = "name";

    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceStore(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Optional<Resource> getResource(ResourceDefRef resourceDefRef, String reosurceId) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceDefRef, reosurceId);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, reosurceId)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap ->
                        LOGGER.debug("- Found resource '{}' with id '{}'",
                                resourceMap.get(PROPERTY_RESOURCE_DEF_REF),
                                resourceMap.get(PROPERTY_RESOURCE_ID)
                        )
                )
                .map(this::toResource)
                .findFirst();
    }

    @Override
    public List<Resource> getResourcesByType(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting all resources of type '{}'", resourceDefRef);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap ->
                        LOGGER.debug("- Found resource '{}' with id '{}'",
                                resourceDefRef,
                                resourceMap.get(PROPERTY_RESOURCE_ID)
                        )
                )
                .map(resourceMap -> new Resource(
                        (ResourceDefRef) resourceMap.get(PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceMap.get(PROPERTY_RESOURCE_ID),
                        toProperties(resourceMap),
                        getAssociations(resourceDefRef, (String) resourceMap.get(PROPERTY_RESOURCE_ID))
                ))
                .collect(Collectors.toList());
    }

    public void addResource(Resource resource) {
        LOGGER.debug("Adding resource '{}' with id '{}'", resource.getResourceDefRef(), resource.getResourceId());

        GraphTraversal<Vertex, Vertex> traversal = gTraversal.addV(LABEL_RESOURCE)
                .property(PROPERTY_RESOURCE_DEF_REF, resource.getResourceDefRef())
                .property(PROPERTY_RESOURCE_ID, resource.getResourceId());

        // add properties
        LOGGER.debug("- Found {} properties", resource.getProperties().size());
        resource.getProperties()
                .forEach(property -> {
                    LOGGER.debug("- Adding property '{}'", property);
                    traversal.property(property.getName(), property.getValue());
                });

        // create resource vertex
        Vertex resourceVertex = traversal.next();

        // add associations
        LOGGER.debug("- Found {} association(s)", resource.getAssociations().size());
        resource.getAssociations().forEach(association -> {
            Optional<Vertex> vertexOpt = getResourceVertexById(association.getResourceDefRef(), association.getResourceId());
            if (!vertexOpt.isPresent()) {
                LOGGER.warn(
                        "Resource '{}' with id '{}' not found. Ignoring association with '{}' with id '{}'",
                        association.getResourceDefRef(),
                        association.getResourceId(),
                        resource.getResourceDefRef(),
                        resource.getResourceId()
                );
            } else {
                LOGGER.debug("- Adding association '{}'", association);
                gTraversal.addE(LABEL_HAS_ASSOCIATION)
                        .property(PROPERTY_NAME, association.getName())
                        .from(resourceVertex)
                        .to(vertexOpt.get())
                        .next();
            }
        });
    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        List<Vertex> vertices = gTraversal.V().has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .toList();

        return vertices.size() > 0 ? Optional.of(vertices.get(0)) : Optional.empty();
    }

    private Resource toResource(Map<Object, Object> resourceMap) {
        ResourceDefRef resourceDefRef = (ResourceDefRef) resourceMap.get(PROPERTY_RESOURCE_DEF_REF);
        String resourceId = (String) resourceMap.get(PROPERTY_RESOURCE_ID);

        return new Resource(
                resourceDefRef,
                resourceId,
                toProperties(resourceMap),
                getAssociations(resourceDefRef, resourceId)
        );
    }

    private List<Property> toProperties(Map<Object, Object> resourceMap) {
        return resourceMap.keySet().stream()
                .filter(key ->
                        key instanceof String &&
                                key != PROPERTY_RESOURCE_DEF_REF &&
                                key != PROPERTY_RESOURCE_ID
                )
                .map(key -> {
                    LOGGER.debug("- Found property '{}'", key);
                    return new Property((String) key, resourceMap.get(key));
                })
                .collect(Collectors.toList());
    }

    private List<Association> getAssociations(ResourceDefRef resourceDefRef, String id) {
        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, id)
                .outE(LABEL_HAS_ASSOCIATION).as("association")
                .inV().as("target")
                .select("association", "target")
                .by(valueMap())
                .toStream()
                .map(map -> {
                    // TODO refactor this ugly code
                    Map<String, Object> associationMap = (Map<String, Object>) map.get("association");
                    Map<String, List<Object>> targetMap = (Map<String, List<Object>>) map.get("target");

                    return new Association(
                            (String) associationMap.get(PROPERTY_NAME),
                            (ResourceDefRef) targetMap.get(PROPERTY_RESOURCE_DEF_REF).get(0),
                            (String) targetMap.get(PROPERTY_RESOURCE_ID).get(0)
                    );
                }).collect(Collectors.toList());
    }
}
