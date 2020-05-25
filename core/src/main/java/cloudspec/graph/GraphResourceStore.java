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
import org.apache.tinkerpop.gremlin.structure.Direction;
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
    public void saveResource(ResourceDefRef resourceDefRef, String resourceId) {
        saveResource(resourceDefRef, resourceId, new Properties(), new Associations());
    }

    @Override
    public void saveResource(ResourceDefRef resourceDefRef, String resourceId,
                             Properties properties, Associations associations) {
        LOGGER.debug("Creating resource '{}' with id '{}'", resourceDefRef, resourceId);

        // create resource vertex
        Vertex resourceV = getOrCreateResourceVertexById(resourceDefRef, resourceId);

        // add properties
        savePropertiesFromVertex(resourceV, properties);

        // add associations
        saveAssociationsFromVertex(resourceV, associations);
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
    public void saveProperty(ResourceDefRef resourceDefRef, String resourceId, Property<?> property) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> savePropertyFromVertex(resourceV, property));
    }

    @Override
    public void saveProperties(ResourceDefRef resourceDefRef, String resourceId, Properties properties) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> savePropertiesFromVertex(resourceV, properties));
    }

    @Override
    public Associations getAssociations(ResourceDefRef resourceDefRef, String resourceId) {
        return getResourceVertexById(resourceDefRef, resourceId)
                .map(this::getAssociationsFromVertex)
                .orElse(new Associations());
    }

    @Override
    public void saveAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> saveAssociationFromVertex(resourceV, association));
    }

    @Override
    public void saveAssociations(ResourceDefRef resourceDefRef, String resourceId, Associations associations) {
        // TODO handle missing resource
        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresent(resourceV -> saveAssociationsFromVertex(resourceV, associations));
    }

    private void saveAssociationsFromVertex(Vertex sourceV, Associations associations) {
        associations.forEach(association -> saveAssociationFromVertex(sourceV, association));
    }

    @SuppressWarnings("unchecked")
    private void saveAssociationFromVertex(Vertex sourceV, Association association) {
        LOGGER.debug("- Setting association '{}'", association);

        // TODO remove old associations

        // get target resource
        Vertex targetV = getOrCreateResourceVertexById(
                association.getResourceDefRef(),
                association.getResourceId()
        );

        // link source vertex to target vertex
        graphTraversal
                .V(sourceV.id())
                .fold()
                .coalesce(
                        __.unfold()
                                .outE(LABEL_HAS_ASSOCIATION)
                                .has(PROPERTY_NAME, association.getName())
                                .inV()
                                .has(PROPERTY_RESOURCE_DEF_REF, association.getResourceDefRef())
                                .has(PROPERTY_RESOURCE_ID, association.getResourceId()),
                        __.addE(LABEL_HAS_ASSOCIATION)
                                .property(PROPERTY_NAME, association.getName())
                                .from(sourceV)
                                .to(targetV)
                                .inV()
                ).next();
    }

    private Associations getAssociationsFromVertex(Vertex resourceV) {
        return new Associations(getAssociationsStreamFromVertex(resourceV));
    }

    private Stream<Association> getAssociationsStreamFromVertex(Vertex resourceV) {
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
                });
    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .tryNext();
    }

    private Vertex getOrCreateResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        Vertex resourceV = graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .fold()
                .coalesce(
                        __.unfold(),
                        __.addV(LABEL_RESOURCE)
                                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                                .property(PROPERTY_RESOURCE_ID, resourceId)
                )
                .next();

        if (!resourceV.edges(Direction.OUT, LABEL_IS_RESOURCE_DEF).hasNext()) {
            // get resource
            Optional<Vertex> resourceDefVOpt = graphTraversal
                    .V()
                    .has(
                            GraphResourceDefStore.LABEL_RESOURCE_DEF,
                            GraphResourceDefStore.PROPERTY_RESOURCE_DEF_REF,
                            resourceDefRef
                    )
                    .tryNext();

            if (!resourceDefVOpt.isPresent()) {
                resourceV.remove();
                throw new RuntimeException(
                        String.format(
                                "Resource definition '%s' not found.",
                                resourceDefRef
                        )
                );
            }

            graphTraversal.V()
                    .addE(LABEL_IS_RESOURCE_DEF)
                    .from(resourceV)
                    .to(resourceDefVOpt.get())
                    .next();
        }

        return resourceV;
    }

    private void savePropertiesFromVertex(Vertex sourceV, Properties properties) {
        properties.forEach(property -> savePropertyFromVertex(sourceV, property));
    }

    private Vertex getOrCreatePropertyVertex(Vertex sourceV, Property<?> property) {
        Optional<Vertex> propertyVOpt = graphTraversal.V(sourceV.id())
                .out(LABEL_HAS_PROPERTY)
                .has(PROPERTY_NAME, property.getName())
                .tryNext();

        if (propertyVOpt.isPresent()) {
            return propertyVOpt.get();
        }

        // get property definition vertex
        Optional<Vertex> propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                .coalesce(
                        __.out(LABEL_IS_RESOURCE_DEF),
                        __.in(LABEL_HAS_PROPERTY_VALUE).out(LABEL_IS_PROPERTY_DEF)
                )
                .out(GraphResourceDefStore.LABEL_HAS_PROPERTY_DEF)
                .has(GraphResourceDefStore.PROPERTY_NAME, property.getName())
                .tryNext();

        if (!propertyDefVOpt.isPresent()) {
            throw new RuntimeException(
                    String.format("Property definition '%s' not found.", property.getName())
            );
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

        // link source (resource or property) vertex to property vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY)
                .from(sourceV)
                .to(propertyV)
                .next();

        // link property to property definition vertex
        graphTraversal
                .addE(LABEL_IS_PROPERTY_DEF)
                .from(propertyV)
                .to(propertyDefVOpt.get())
                .next();

        return propertyV;
    }

    private void savePropertyFromVertex(Vertex sourceV, Property<?> property) {
        // sourceV can be a resource or a property value
        LOGGER.debug("- Setting property '{}' with value {}",
                getPropertyPath(sourceV, property.getName()),
                property
        );

        // create property vertex
        Vertex propertyV = getOrCreatePropertyVertex(sourceV, property);

        switch ((PropertyType) propertyV.value(PROPERTY_TYPE)) {
            case NESTED:
                saveNestedPropertyValueFromVertex(propertyV, (NestedPropertyValue) property.getValue());
                break;
            case KEY_VALUE:
                saveKeyValuePropertyValueFromVertex(propertyV, (KeyValue) property.getValue());
                break;
            default:
                saveAtomicPropertyValueFromVertex(propertyV, property.getValue());
        }
    }

    private void saveNestedPropertyValueFromVertex(Vertex propertyV, NestedPropertyValue nestedProperty) {
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .next();

        // link source (property) vertex to property value vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();

        savePropertiesFromVertex(propertyValueV, nestedProperty.getProperties());
        saveAssociationsFromVertex(propertyValueV, nestedProperty.getAssociations());
    }

    private void saveAtomicPropertyValueFromVertex(Vertex propertyV, Object obj) {
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

    private void saveKeyValuePropertyValueFromVertex(Vertex propertyV, KeyValue keyValue) {
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
                        .flatMap(this::getPropertyValuesFromVertex)
        );
    }

    private Stream<Property<?>> getPropertyValuesFromVertex(Vertex propertyV) {
        return graphTraversal
                .V(propertyV.id())
                .out(LABEL_HAS_PROPERTY_VALUE)
                .toStream()
                .map(propertyValueV ->
                        toProperty(
                                propertyV.value(PROPERTY_NAME),
                                propertyV.value(PROPERTY_TYPE),
                                propertyValueV
                        )
                );
    }

    private Property<?> toProperty(String propertyName, PropertyType type, Vertex propertyValueV) {
        switch (type) {
            case INTEGER:
                return new IntegerProperty(propertyName, propertyValueV.value(PROPERTY_VALUE));
            case DOUBLE:
                return new DoubleProperty(propertyName, propertyValueV.value(PROPERTY_VALUE));
            case STRING:
                return new StringProperty(propertyName, propertyValueV.value(PROPERTY_VALUE));
            case BOOLEAN:
                return new BooleanProperty(propertyName, propertyValueV.value(PROPERTY_VALUE));
            case KEY_VALUE:
                return new KeyValueProperty(
                        propertyName,
                        new KeyValue(
                                propertyValueV.value(PROPERTY_KEY),
                                propertyValueV.value(PROPERTY_VALUE)
                        )
                );
            case NESTED:
                return new NestedProperty(
                        propertyName,
                        new NestedPropertyValue(
                                getPropertiesFromVertex(propertyValueV),
                                getAssociationsFromVertex(propertyValueV)
                        )
                );
            default:
                throw new RuntimeException(
                        String.format("Unsupported property type %s", type)
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
