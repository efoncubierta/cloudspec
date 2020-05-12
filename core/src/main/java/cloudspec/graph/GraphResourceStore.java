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

import cloudspec.model.*;
import cloudspec.store.ResourceStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.valueMap;

/**
 * Graph-based implementation of resource store.
 * <p>
 * This class implement {@link ResourceStore} using Tinkerpop graphs.
 * <p>
 * Graph model:
 * <ul>
 * <li>
 *     Resources are <strong>resource</strong> vertices, with the following properties:
 *     <ul>
 *         <li><strong>resourceDefRef (ResourceDefRef)</strong></li>
 *         <li><strong>resourceId (String)</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     Properties are <strong>property</strong> vertices, with the following properties:
 *     <ul>
 *         <li><strong>name (String)</strong></li>
 *         <li><strong>value (Object)</strong></li>
 *         <li><strong>isMap (Boolean)</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     Associations are <strong>hasAssociation</strong> edges, with the following properties:
 *     <ul>
 *         <li><strong>name (String)</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     <strong>resource</strong> vertices has the following out edges:
 *     <ul>
 *         <li>resource --- [isResourceDef]  ---> resourceDef</li>
 *         <li>resource --- [hasProperty]    ---> property</li>
 *         <li>resource --- [hasAssociation] ---> resource</li>
 *     </ul>
 * </li>
 * <li>
 *     <strong>property</strong> vertices has the following out edges:
 *     <ul>
 *         <li>property --- [isPropertyDef] ---> propertyDef</li>
 *         <li>property --- [hasProperty]   ---> property</li>
 *     </ul>
 * </li>
 * </ul>
 */
public class GraphResourceStore implements ResourceStore {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceStore.class);

    public static final String LABEL_RESOURCE = "resource";
    public static final String LABEL_PROPERTY = "property";
    public static final String LABEL_IS_RESOURCE_DEF = "isResourceDef";
    public static final String LABEL_HAS_PROPERTY = "hasProperty";
    public static final String LABEL_IS_PROPERTY_DEF = "isPropertyDef";
    public static final String LABEL_HAS_ASSOCIATION = "hasAssociation";
    public static final String PROPERTY_RESOURCE_ID = "resourceId";
    public static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_VALUE = "value";
    public static final String PROPERTY_IS_MAP = "isMap";

    private final GraphTraversalSource graphTraversal;

    /**
     * Constructor.
     *
     * @param graph TinkerPop Graph.
     */
    public GraphResourceStore(Graph graph) {
        this.graphTraversal = graph.traversal();
    }

    @Override
    public Optional<Resource> getResource(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceDefRef, resourceId);

        return graphTraversal
                .V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .toStream()
                .peek(resourceV ->
                        LOGGER.debug("- Found resource '{}' with id '{}'",
                                resourceV.value(PROPERTY_RESOURCE_DEF_REF),
                                resourceV.value(PROPERTY_RESOURCE_ID)
                        )
                )
                .map(this::toResource)
                .findFirst();
    }

    @Override
    public List<Resource> getResourcesByType(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting all resources of type '{}'", resourceDefRef);

        return graphTraversal
                .V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .toStream()
                .peek(resourceV ->
                        LOGGER.debug("- Found resource '{}' with id '{}'",
                                resourceDefRef,
                                resourceV.value(PROPERTY_RESOURCE_ID)
                        )
                )
                .map(resourceV -> new Resource(
                        resourceV.value(PROPERTY_RESOURCE_DEF_REF),
                        resourceV.value(PROPERTY_RESOURCE_ID),
                        getProperties(resourceV),
                        getAssociations(resourceV)
                ))
                .collect(Collectors.toList());
    }

    public void addResource(Resource resource) {
        LOGGER.debug("Adding resource '{}' with id '{}'",
                resource.getResourceDefRef(), resource.getResourceId());

        // create resource
        Vertex resourceV = graphTraversal
                .addV(LABEL_RESOURCE)
                .property(PROPERTY_RESOURCE_DEF_REF, resource.getResourceDefRef())
                .property(PROPERTY_RESOURCE_ID, resource.getResourceId())
                .next();

        // link resource to resource definition vertex
        Optional<Edge> associationEOpt = graphTraversal
                .V()
                .has(
                        GraphResourceDefStore.LABEL_RESOURCE_DEF,
                        GraphResourceDefStore.PROPERTY_RESOURCE_DEF_REF,
                        resource.getResourceDefRef()
                )
                .as("resourceDef")
                .addE(LABEL_IS_RESOURCE_DEF)
                .from(resourceV)
                .to("resourceDef")
                .tryNext();

        if (!associationEOpt.isPresent()) {
            throw new RuntimeException(
                    String.format(
                            "Resource definition '%s' not found.",
                            resource.getResourceDefRef()
                    )
            );
        }

        // add properties
        addProperties(resourceV, resource.getProperties());

        // add associations
        addAssociations(resourceV, resource.getAssociations());
    }

    private void addProperties(Vertex sourceV, List<Property> properties) {
        LOGGER.debug("- Found {} properties", properties.size());
        properties
                .stream()
                .map(property -> addProperty(sourceV, property))
                .forEach(propertyVertex ->
                        graphTraversal
                                .addE(LABEL_HAS_PROPERTY)
                                .from(sourceV)
                                .to(propertyVertex)
                                .next()
                );
    }

    private Vertex addProperty(Vertex sourceV, Property property) {
        LOGGER.debug("- Adding property '{}'", property);

        // get property definition
        Optional<Vertex> propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                .out(LABEL_IS_RESOURCE_DEF, LABEL_IS_PROPERTY_DEF)
                .out(GraphResourceDefStore.LABEL_HAS_PROPERTY_DEF)
                .has(GraphResourceDefStore.PROPERTY_NAME, property.getName())
                .tryNext();

        // TODO validate property value with definition
        if (!propertyDefVOpt.isPresent()) {
            throw new RuntimeException(
                    String.format(
                            "Property definition '%s' not found",
                            property.getName()
                    )
            );
        }

        // create property vertex
        Vertex propertyFromV;
        if (propertyDefVOpt.get().value(GraphResourceDefStore.PROPERTY_TYPE).equals(PropertyType.MAP)) {
            propertyFromV = graphTraversal
                    .addV(LABEL_PROPERTY)
                    .property(PROPERTY_IS_MAP, Boolean.TRUE)
                    .property(PROPERTY_NAME, property.getName())
                    .next();
        } else {
            propertyFromV = graphTraversal
                    .addV(LABEL_PROPERTY)
                    .property(PROPERTY_IS_MAP, Boolean.FALSE)
                    .property(PROPERTY_NAME, property.getName())
                    .property(PROPERTY_VALUE, property.getValue())
                    .next();
        }

        // link property to property definition vertex
        graphTraversal
                .addE(LABEL_IS_PROPERTY_DEF)
                .from(propertyFromV)
                .to(propertyDefVOpt.get())
                .next();

        // add nested properties
        if (propertyDefVOpt.get().value(GraphResourceDefStore.PROPERTY_TYPE).equals(PropertyType.MAP)) {
            // if property type is map, then spread out properties to their own vertex
            addProperties(propertyFromV, ((List<Property>) property.getValue()));
        }

        return propertyFromV;
    }

    private void addAssociations(Vertex sourceV, List<Association> associations) {
        LOGGER.debug("- Found {} association(s)", associations.size());

        associations.forEach(association -> addAssociation(sourceV, association));
    }

    private void addAssociation(Vertex sourceV, Association association) {
        // validate target resource exists
        Optional<Vertex> vertexOpt = getResourceVertexById(association.getResourceDefRef(), association.getResourceId());
        if (!vertexOpt.isPresent()) {
            throw new RuntimeException(
                    String.format("Resource '%s' with id '%s' not found.",
                            association.getResourceDefRef(),
                            association.getResourceId()
                    )
            );
        }

        LOGGER.debug("- Adding association '{}'", association);
        graphTraversal.addE(LABEL_HAS_ASSOCIATION)
                .property(PROPERTY_NAME, association.getName())
                .from(sourceV)
                .to(vertexOpt.get())
                .next();

    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        List<Vertex> vertices = graphTraversal.V().has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .toList();

        return vertices.size() > 0 ? Optional.of(vertices.get(0)) : Optional.empty();
    }

    private Resource toResource(Vertex resourceV) {
        ResourceDefRef resourceDefRef = resourceV.value(PROPERTY_RESOURCE_DEF_REF);
        String resourceId = resourceV.value(PROPERTY_RESOURCE_ID);

        return new Resource(
                resourceDefRef,
                resourceId,
                getProperties(resourceV),
                getAssociations(resourceV)
        );
    }

    private List<Property> getProperties(Vertex sourceV) {
        return graphTraversal
                .V(sourceV.id())
                .out(LABEL_HAS_PROPERTY)
                .toStream()
                .map(propertyV -> {
                    if (propertyV.value(PROPERTY_IS_MAP)) {
                        return new Property(
                                propertyV.value(PROPERTY_NAME),
                                getProperties(propertyV)
                        );
                    } else {
                        return new Property(
                                propertyV.value(PROPERTY_NAME),
                                propertyV.value(PROPERTY_VALUE)
                        );
                    }
                })
                .collect(Collectors.toList());
    }

    private List<Association> getAssociations(Vertex resourceV) {
        return graphTraversal
                .V(resourceV.id())
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
