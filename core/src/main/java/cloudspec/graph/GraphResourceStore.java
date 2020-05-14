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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static final String PROPERTY_KEY = "key";
    public static final String PROPERTY_VALUE = "value";
    public static final String PROPERTY_TYPE = "propertyType";

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
    public void createResource(ResourceDefRef resourceDefRef, String resourceId) {
        createResource(resourceDefRef, resourceId, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void createResource(ResourceDefRef resourceDefRef, String resourceId,
                               List<Property> properties, List<Association> associations) {
        LOGGER.debug("Creating resource '{}' with id '{}'", resourceDefRef, resourceId);

        if (getResourceVertexById(resourceDefRef, resourceId).isPresent()) {
            LOGGER.error("A resource '{}' with id '{}' already exists.",
                    resourceDefRef, resourceId);
            return;
        }

        // create resource vertex
        Vertex resourceV = graphTraversal
                .addV(LABEL_RESOURCE)
                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .property(PROPERTY_RESOURCE_ID, resourceId)
                .next();

        // link resource to resource definition vertex
        Optional<Edge> associationEOpt = graphTraversal
                .V()
                .has(
                        GraphResourceDefStore.LABEL_RESOURCE_DEF,
                        GraphResourceDefStore.PROPERTY_RESOURCE_DEF_REF,
                        resourceDefRef
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
                            resourceDefRef
                    )
            );
        }

        // add properties
        createOrUpdatePropertiesFromVertex(resourceV, properties);

        // add associations
        createOrUpdateAssociationsFromVertex(resourceV, associations);
    }

    @Override
    public Boolean exists(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId).isPresent();
    }

    @Override
    public List<Property> getProperties(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId)
                .map(this::getPropertiesFromVertex)
                .orElse(Collections.emptyList());
    }

    @Override
    public void setProperty(ResourceDefRef resourceDefRef, String resourceId, Property property) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createOrUpdatePropertyFromVertex(resourceV, property));
    }

    @Override
    public void setProperties(ResourceDefRef resourceDefRef, String resourceId, List<Property> properties) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createOrUpdatePropertiesFromVertex(resourceV, properties));
    }

    @Override
    public List<Association> getAssociations(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId)
                .map(this::getAssociationsFromVertex)
                .orElse(Collections.emptyList());
    }

    @Override
    public void setAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createOrUpdateAssociationFromVertex(resourceV, association));
    }

    @Override
    public void setAssociations(ResourceDefRef resourceDefRef, String resourceId, List<Association> associations) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createOrUpdateAssociationsFromVertex(resourceV, associations));
    }

    @Override
    public Optional<Resource> getResource(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceDefRef, resourceId);

        return getResourceVertexById(resourceDefRef, resourceId)
                .flatMap(this::getResourceFromVertex);
    }

    @Override
    public List<Resource> getResourcesByDefinition(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting all resources of type '{}'", resourceDefRef);

        return getResourceVerticesByDefinition(resourceDefRef)
                .map(this::getResourceFromVertex)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private void createOrUpdatePropertiesFromVertex(Vertex sourceV, List<Property> properties) {
        properties.forEach(property -> createOrUpdatePropertyFromVertex(sourceV, property));
    }

    private void createOrUpdatePropertyFromVertex(Vertex sourceV, Property property) {
        LOGGER.debug("- Setting property '{}' with value {}",
                getPropertyPath(sourceV, property.getName()),
                property
        );

        // get property definition
        Optional<Vertex> propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                .out(LABEL_IS_RESOURCE_DEF, LABEL_IS_PROPERTY_DEF)
                .out(GraphResourceDefStore.LABEL_HAS_PROPERTY_DEF)
                .has(GraphResourceDefStore.PROPERTY_NAME, property.getName())
                .tryNext();

        // TODO validate property value with definition
        if (!propertyDefVOpt.isPresent()) {
            LOGGER.error("Property definition '{}' not found. Ignoring it.", property.getName());
            return;
        }

        // get property type
        PropertyType propertyType = propertyDefVOpt.get().value(GraphResourceDefStore.PROPERTY_TYPE);

        // create property vertex
        Vertex propertyFromV;
        switch (propertyType) {
            case MAP:
                propertyFromV = graphTraversal
                        .addV(LABEL_PROPERTY)
                        .property(PROPERTY_TYPE, propertyType)
                        .property(PROPERTY_NAME, property.getName())
                        .next();
                break;
            case KEY_VALUE:
                KeyValue kv = (KeyValue) property.getValue();
                propertyFromV = graphTraversal
                        .addV(LABEL_PROPERTY)
                        .property(PROPERTY_TYPE, propertyType)
                        .property(PROPERTY_NAME, property.getName())
                        .property(PROPERTY_KEY, kv.getKey())
                        .property(PROPERTY_VALUE, kv.getValue())
                        .next();
                break;
            default:
                propertyFromV = graphTraversal
                        .addV(LABEL_PROPERTY)
                        .property(PROPERTY_TYPE, propertyType)
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

        // link source (resource or property) vertex to property vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY)
                .from(sourceV)
                .to(propertyFromV)
                .next();

        // if property type is map, then spread out properties to their own vertex
        if (propertyDefVOpt.get().value(GraphResourceDefStore.PROPERTY_TYPE).equals(PropertyType.MAP)) {
            createOrUpdatePropertiesFromVertex(propertyFromV, ((List<Property>) property.getValue()));
        }
    }

    private void createOrUpdateAssociationsFromVertex(Vertex sourceV, List<Association> associations) {
        associations.forEach(association -> createOrUpdateAssociationFromVertex(sourceV, association));
    }

    private void createOrUpdateAssociationFromVertex(Vertex sourceV, Association association) {
        LOGGER.debug("- Setting association '{}'", association);

        // validate target resource exists
        Optional<Vertex> targetVOpt = getResourceVertexById(association.getResourceDefRef(), association.getResourceId());
        if (!targetVOpt.isPresent()) {
            LOGGER.error("Resource '{}' with id '{}' not found. Ignoring association '{}'.",
                    association.getResourceDefRef(),
                    association.getResourceId(),
                    association.getName()
            );
            return;
        }

        // link source vertex to target vertex
        graphTraversal.addE(LABEL_HAS_ASSOCIATION)
                .property(PROPERTY_NAME, association.getName())
                .from(sourceV)
                .to(targetVOpt.get())
                .next();

    }

    private Stream<Vertex> getResourceVerticesByDefinition(ResourceDefRef resourceDefRef) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .toStream();
    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .tryNext();
    }

    private Optional<Resource> getResourceFromVertex(Vertex resourceV) {
        if (!resourceV.label().equals(LABEL_RESOURCE)) {
            LOGGER.error("Vertex is not of type '{}'.", LABEL_RESOURCE);
            return Optional.empty();
        }

        ResourceDefRef resourceDefRef = resourceV.value(PROPERTY_RESOURCE_DEF_REF);
        String resourceId = resourceV.value(PROPERTY_RESOURCE_ID);

        LOGGER.debug("- Found resource '{}' with id '{}'",
                resourceDefRef,
                resourceId
        );

        return Optional.of(
                new Resource(
                        resourceDefRef,
                        resourceId,
                        getPropertiesFromVertex(resourceV),
                        getAssociationsFromVertex(resourceV)
                )
        );
    }

    private List<Property> getPropertiesFromVertex(Vertex sourceV) {
        return graphTraversal
                .V(sourceV.id())
                .out(LABEL_HAS_PROPERTY)
                .toStream()
                .map(propertyV -> {
                    switch ((PropertyType) propertyV.value(PROPERTY_TYPE)) {
                        case MAP:
                            return new Property(
                                    propertyV.value(PROPERTY_NAME),
                                    getPropertiesFromVertex(propertyV)
                            );
                        case KEY_VALUE:
                            return new Property(
                                    propertyV.value(PROPERTY_NAME),
                                    new KeyValue(propertyV.value(PROPERTY_KEY), propertyV.value(PROPERTY_VALUE))
                            );
                        default:
                            return new Property(
                                    propertyV.value(PROPERTY_NAME),
                                    propertyV.value(PROPERTY_VALUE)
                            );
                    }
                })
                .collect(Collectors.toList());
    }

    private List<Association> getAssociationsFromVertex(Vertex resourceV) {
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

    private String getPropertyPath(Vertex sourceV, String memberName) {
        if (sourceV.label().equals(LABEL_RESOURCE)) {
            return memberName;
        } else {
            return String.format(
                    "%s.%s",
                    getPropertyPath(graphTraversal.V(sourceV.id())
                                    .in(LABEL_HAS_PROPERTY)
                                    .next(),
                            sourceV.value(PROPERTY_NAME)
                    ),
                    memberName
            );
        }
    }
}
