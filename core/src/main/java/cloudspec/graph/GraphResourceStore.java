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
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 *         <li><strong>type (PropertyType)</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     Property values are <strong>propertyValue</strong> vertices, with the following properties:
 *     <ul>
 *         <li>
 *             If property is of an atomic type (integer, string or boolean):
 *             <ul>
 *                 <li><strong>value (Object)</strong></li>
 *             </ul>
 *         </li>
 *         <li>
 *             If property is a key value type:
 *             <ul>
 *                 <li><strong>key (String)</strong></li>
 *                 <li><strong>value (String)</strong></li>
 *             </ul>
 *         </li>
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
 *         <li>property --- [isPropertyDef]    ---> propertyDef</li>
 *         <li>property --- [hasPropertyValue] ---> propertyValue</li>
 *         <li>property --- [hasProperty]      ---> property</li>
 *     </ul>
 * </li>
 * </ul>
 */
public class GraphResourceStore implements ResourceStore {
    public static final String LABEL_RESOURCE = "resource";
    public static final String LABEL_PROPERTY = "property";
    public static final String LABEL_PROPERTY_VALUE = "propertyValue";
    public static final String LABEL_IS_RESOURCE_DEF = "isResourceDef";
    public static final String LABEL_HAS_PROPERTY = "hasProperty";
    public static final String LABEL_HAS_PROPERTY_VALUE = "hasPropertyValue";
    public static final String LABEL_IS_PROPERTY_DEF = "isPropertyDef";
    public static final String LABEL_HAS_ASSOCIATION = "hasAssociation";
    public static final String PROPERTY_RESOURCE_ID = "resourceId";
    public static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_KEY = "key";
    public static final String PROPERTY_VALUE = "value";
    public static final String PROPERTY_TYPE = "propertyType";
    public static final String PROPERTY_IS_MULTIVALUED = "isMultiValued";
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceStore.class);
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
        createResource(resourceDefRef, resourceId, new Properties(), new Associations());
    }

    @Override
    public void createResource(ResourceDefRef resourceDefRef, String resourceId,
                               Properties properties, Associations associations) {
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
        createPropertiesFromVertex(resourceV, properties);

        // add associations
        createAssociationsFromVertex(resourceV, associations);
    }

    @Override
    public Boolean exists(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId).isPresent();
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

    @Override
    public Properties getProperties(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId)
                .map(this::getPropertiesFromVertex)
                .orElse(new Properties());
    }

    @Override
    public void setProperty(ResourceDefRef resourceDefRef, String resourceId, Property property) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createPropertyFromVertex(resourceV, property));
    }

    @Override
    public void setProperties(ResourceDefRef resourceDefRef, String resourceId, Properties properties) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createPropertiesFromVertex(resourceV, properties));
    }

    @Override
    public Associations getAssociations(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId)
                .map(this::getAssociationsFromVertex)
                .orElse(new Associations());
    }

    @Override
    public void setAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createAssociationFromVertex(resourceV, association));
    }

    @Override
    public void setAssociations(ResourceDefRef resourceDefRef, String resourceId, Associations associations) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> createAssociationsFromVertex(resourceV, associations));
    }

    private void createAssociationsFromVertex(Vertex sourceV, Associations associations) {
        associations.forEach(association -> createAssociationFromVertex(sourceV, association));
    }

    private void createAssociationFromVertex(Vertex sourceV, Association association) {
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

    private Associations getAssociationsFromVertex(Vertex resourceV) {
        return new Associations(
                graphTraversal
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
                        })
        );
    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .tryNext();
    }

    private void createPropertiesFromVertex(Vertex sourceV, Properties properties) {
        properties.forEach(property -> createPropertyFromVertex(sourceV, property));
    }

    @SuppressWarnings("unchecked")
    private void createPropertyFromVertex(Vertex sourceV, Property property) {
        // sourceV can be a resource or a property value
        LOGGER.debug("- Setting property '{}' with value {}",
                getPropertyPath(sourceV, property.getName()),
                property
        );

        // get property definition
        Optional<Vertex> propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                .coalesce(
                        __.out(LABEL_IS_RESOURCE_DEF),
                        __.in(LABEL_HAS_PROPERTY_VALUE).out(LABEL_IS_PROPERTY_DEF)
                )
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
        Boolean multiValued = propertyDefVOpt.get().value(GraphResourceDefStore.PROPERTY_IS_MULTIVALUED);

        // create property vertex
        Vertex propertyV = graphTraversal
                .addV(LABEL_PROPERTY)
                .property(PROPERTY_NAME, property.getName())
                .property(PROPERTY_TYPE, propertyType)
                .property(PROPERTY_IS_MULTIVALUED, multiValued)
                .next();

        // link property to property definition vertex
        graphTraversal
                .addE(LABEL_IS_PROPERTY_DEF)
                .from(propertyV)
                .to(propertyDefVOpt.get())
                .next();

        // link source (resource or property) vertex to property vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY)
                .from(sourceV)
                .to(propertyV)
                .next();

        switch (propertyType) {
            case NESTED:
                if (multiValued) {
                    createNestedPropertyValuesFromVertex(propertyV, (List<Properties>) property.getValue());
                } else {
                    createNestedPropertyValueFromVertex(propertyV, (Properties) property.getValue());
                }
                break;
            case KEY_VALUE:
                if (multiValued) {
                    createKeyValuePropertyValuesFromVertex(propertyV, (List<KeyValue>) property.getValue());
                } else {
                    createKeyValuePropertyValueFromVertex(propertyV, (KeyValue) property.getValue());
                }
                break;
            default:
                if (multiValued) {
                    createAtomicPropertyValuesFromVertex(propertyV, (List<Object>) property.getValue());
                } else {
                    createAtomicPropertyValueFromVertex(propertyV, property.getValue());
                }
        }
    }

    private void createNestedPropertyValuesFromVertex(Vertex propertyV, List<Properties> properties) {
        properties.forEach(nestedProperties -> createNestedPropertyValueFromVertex(propertyV, nestedProperties));
    }

    private void createNestedPropertyValueFromVertex(Vertex propertyV, Properties properties) {
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .next();

        // link source (property) vertex to property value vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();

        createPropertiesFromVertex(propertyValueV, properties);
    }

    private void createAtomicPropertyValuesFromVertex(Vertex propertyV, List<Object> objs) {
        objs.forEach(obj -> createAtomicPropertyValueFromVertex(propertyV, obj));
    }

    private void createAtomicPropertyValueFromVertex(Vertex propertyV, Object obj) {
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_VALUE, obj)
                .next();

        // link source (property) vertex to property value vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();
    }

    private void createKeyValuePropertyValuesFromVertex(Vertex propertyV, List<KeyValue> keyValues) {
        keyValues.forEach(keyValue -> createKeyValuePropertyValueFromVertex(propertyV, keyValue));
    }

    private void createKeyValuePropertyValueFromVertex(Vertex propertyV, KeyValue keyValue) {
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_KEY, keyValue.getKey())
                .property(PROPERTY_VALUE, keyValue.getValue())
                .next();

        // link source (property) vertex to property value vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();
    }

    private Stream<Vertex> getResourceVerticesByDefinition(ResourceDefRef resourceDefRef) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .toStream();
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

    private Properties getPropertiesFromVertex(Vertex sourceV) {
        // source can be a resource or a property value
        return new Properties(
                graphTraversal
                        .V(sourceV.id())
                        .out(LABEL_HAS_PROPERTY)
                        .toStream()
                        .map(this::getPropertyFromVertex)
        );
    }

    private Property getPropertyFromVertex(Vertex propertyV) {
        switch ((PropertyType) propertyV.value(PROPERTY_TYPE)) {
            case NESTED:
                return getNestedPropertyValuesFromVertex(propertyV);
            case KEY_VALUE:
                return getKeyValuePropertyValuesFromVertex(propertyV);
            default:
                return getAtomicPropertyValueFromVertex(propertyV);
        }
    }

    private Property getAtomicPropertyValueFromVertex(Vertex propertyV) {
        List<Object> propertyValues = graphTraversal
                .V(propertyV.id())
                .out(LABEL_HAS_PROPERTY_VALUE)
                .toStream()
                .map(propertyValueV -> propertyValueV.value(PROPERTY_VALUE))
                .collect(Collectors.toList());

        if (propertyV.value(PROPERTY_IS_MULTIVALUED).equals(Boolean.TRUE)) {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    propertyValues
            );
        } else {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    propertyValues.isEmpty() ? null : propertyValues.get(0)
            );
        }
    }

    private Property getKeyValuePropertyValuesFromVertex(Vertex propertyV) {
        List<KeyValue> keyValues = graphTraversal
                .V(propertyV.id())
                .out(LABEL_HAS_PROPERTY_VALUE)
                .toStream()
                .map(
                        propertyValueV -> new KeyValue(
                                propertyValueV.value(PROPERTY_KEY),
                                propertyValueV.value(PROPERTY_VALUE)
                        )
                )
                .collect(Collectors.toList());

        if (propertyV.value(PROPERTY_IS_MULTIVALUED).equals(Boolean.TRUE)) {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    keyValues
            );
        } else {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    keyValues.isEmpty() ? null : keyValues.get(0)
            );
        }
    }

    private Property getNestedPropertyValuesFromVertex(Vertex propertyV) {
        List<Properties> nestedValues = graphTraversal
                .V(propertyV.id())
                .out(LABEL_HAS_PROPERTY_VALUE)
                .toStream()
                .map(this::getPropertiesFromVertex)
                .collect(Collectors.toList());

        if (propertyV.value(PROPERTY_IS_MULTIVALUED).equals(Boolean.TRUE)) {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    nestedValues
            );
        } else {
            return new Property(
                    propertyV.value(PROPERTY_NAME),
                    nestedValues.isEmpty() ? null : nestedValues.get(0)
            );
        }
    }

    private String getPropertyPath(Vertex sourceV, String memberName) {
        if (sourceV.label().equals(LABEL_RESOURCE)) {
            return memberName;
        } else if (sourceV.label().equals(LABEL_PROPERTY_VALUE)) {
            return getPropertyPath(
                    graphTraversal
                            .V(sourceV.id())
                            .in(LABEL_HAS_PROPERTY_VALUE)
                            .next(),
                    memberName);
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
