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
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Boolean exists(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Checking whether resource '{}' with id '{}' exists", resourceDefRef, resourceId);

        return getResourceVertexById(resourceDefRef, resourceId).isPresent();
    }

    @Override
    public Optional<Resource> getResource(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceDefRef, resourceId);

        // get resource vertex by resource id, and map it to resource object
        return getResourceVertexById(resourceDefRef, resourceId)
                .flatMap(this::toResourceFromVertex);
    }

    @Override
    public List<Resource> getResourcesByDefinition(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting all resources of type '{}'", resourceDefRef);

        // get all resource vertices by resource definition reference, and map them to resource objects
        return getResourceVerticesByDefinition(resourceDefRef)
                .map(this::toResourceFromVertex)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Properties> getProperties(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting properties of resource '{}' with id '{}'", resourceDefRef, resourceId);

        // get resource vertex by its id, and map it to a properties object
        return getResourceVertexById(resourceDefRef, resourceId)
                .flatMap(this::toPropertiesFromVertex);
    }

    @Override
    public Optional<Associations> getAssociations(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting associations of resource '{}' with id '{}'", resourceDefRef, resourceId);

        // get resource vertex by its id, and map it to an associations object
        return getResourceVertexById(resourceDefRef, resourceId)
                .flatMap(this::toAssociationsFromVertex);
    }

    @Override
    public void saveResource(ResourceDefRef resourceDefRef, String resourceId) {
        // default to empty properties and associations
        saveResource(resourceDefRef, resourceId, new Properties(), new Associations());
    }

    @Override
    public void saveResource(ResourceDefRef resourceDefRef, String resourceId,
                             Properties properties, Associations associations) {
        LOGGER.debug("Saving resource '{}' with id '{}'", resourceDefRef, resourceId);

        // get, or create, resource vertex by its id
        Vertex resourceV = getOrCreateResourceVertexById(resourceDefRef, resourceId);

        // save resource properties
        savePropertiesFromVertex(resourceV, properties);

        // save resource associations
        saveAssociationsFromVertex(resourceV, associations);
    }

    @Override
    public void saveProperties(ResourceDefRef resourceDefRef, String resourceId, Properties properties) {
        LOGGER.debug("Saving {} properties on resource '{}' with id '{}'",
                properties.size(), resourceDefRef, resourceId);

        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresentOrElse(
                        resourceV -> savePropertiesFromVertex(resourceV, properties),
                        () -> LOGGER.error(
                                "Couldn't save properties. Resource '{}' with id '{}' not found. ",
                                resourceDefRef,
                                resourceId
                        )
                );
    }

    @Override
    public void saveProperty(ResourceDefRef resourceDefRef, String resourceId, Property<?> property) {
        LOGGER.debug("Saving property '{}' on resource '{}' with id '{}'",
                property.getName(), resourceDefRef, resourceId);

        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresentOrElse(
                        resourceV -> savePropertyFromVertex(resourceV, property),
                        () -> LOGGER.error(
                                "Couldn't save property '{}'. Resource '{}' with id '{}' not found. ",
                                property.getName(),
                                resourceDefRef,
                                resourceId
                        )
                );
    }

    @Override
    public void saveAssociations(ResourceDefRef resourceDefRef, String resourceId, Associations associations) {
        LOGGER.debug("Saving {} associations on resource '{}' with id '{}'",
                associations.size(), resourceDefRef, resourceId);

        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresentOrElse(
                        resourceV -> saveAssociationsFromVertex(resourceV, associations),
                        () -> LOGGER.error(
                                "Couldn't save associations. Resource '{}' with id '{}' not found. ",
                                resourceDefRef,
                                resourceId
                        ));
    }

    @Override
    public void saveAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association) {
        LOGGER.debug("Saving association '{}' on resource '{}' with id '{}'",
                association.getName(), resourceDefRef, resourceId);

        getResourceVertexById(resourceDefRef, resourceId)
                .ifPresentOrElse(
                        resourceV -> saveAssociationFromVertex(resourceV, association),
                        () -> LOGGER.error(
                                "Couldn't save association '{}'. Resource '{}' with id '{}' not found. ",
                                association.getName(),
                                resourceDefRef,
                                resourceId
                        ));
    }

    private Optional<Vertex> getResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .tryNext();
    }

    private Stream<Vertex> getResourceVerticesByDefinition(ResourceDefRef resourceDefRef) {
        return graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .toStream();
    }

    @SuppressWarnings("unchecked")
    private Vertex getOrCreateResourceVertexById(ResourceDefRef resourceDefRef, String resourceId) {
        LOGGER.debug("Getting, or creating, resource vertex for resource '{}' with id '{}'",
                resourceDefRef, resourceId);

        Vertex resourceV = graphTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, resourceId)
                .fold()
                // check whether the resource vertex exist, or create it otherwise
                .coalesce(
                        __.unfold(),
                        __.addV(LABEL_RESOURCE)
                                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                                .property(PROPERTY_RESOURCE_ID, resourceId)
                )
                .next();

        // new resource vertex must have an edge to its resource definition vertex
        if (!resourceV.edges(Direction.OUT, LABEL_IS_RESOURCE_DEF).hasNext()) {
            LOGGER.debug("Created vertex for resource '{}' with id '{}'",
                    resourceDefRef, resourceId);

            // get resource definition vertex
            Optional<Vertex> resourceDefVOpt = graphTraversal
                    .V()
                    .has(
                            GraphResourceDefStore.LABEL_RESOURCE_DEF,
                            GraphResourceDefStore.PROPERTY_RESOURCE_DEF_REF,
                            resourceDefRef
                    )
                    .tryNext();

            // if resource definition vertex doesn't exist, roll back and throw an error
            if (resourceDefVOpt.isEmpty()) {
                resourceV.remove();
                throw new RuntimeException(
                        String.format(
                                "Resource definition '%s' not found.",
                                resourceDefRef
                        )
                );
            }

            // create edge between resource and resource definition edges
            graphTraversal.V()
                    .addE(LABEL_IS_RESOURCE_DEF)
                    .from(resourceV)
                    .to(resourceDefVOpt.get())
                    .next();
        }

        return resourceV;
    }

    @SuppressWarnings("unchecked")
    private Vertex getOrCreatePropertyVertex(Vertex sourceV, Property<?> property) {
        LOGGER.debug("Getting, or creating, property vertex '{}'", property.getName());

        // source vertex can be a resource or a property value
        // get property vertex by its name
        Optional<Vertex> propertyVOpt = graphTraversal.V(sourceV.id())
                .out(LABEL_HAS_PROPERTY)
                .has(PROPERTY_NAME, property.getName())
                .tryNext();

        // return if exists
        if (propertyVOpt.isPresent()) {
            return propertyVOpt.get();
        }

        LOGGER.debug("Property not found. Creating it");

        // get property definition vertex
        Optional<Vertex> propertyDefVOpt = graphTraversal
                .V(sourceV.id())
                // attempt to get the resource definition or property definition vertex
                // depending on whether the source vertex is a resource or a property value
                .coalesce(
                        // if source vertex is a resource, this should return a resource definition vertex
                        __.out(LABEL_IS_RESOURCE_DEF),
                        // otherwise, it should be a property value, so this should return the property property
                        // definition vertex traversing through its property vertex
                        __.in(LABEL_HAS_PROPERTY_VALUE).out(LABEL_IS_PROPERTY_DEF)
                )
                .out(GraphResourceDefStore.LABEL_HAS_PROPERTY_DEF)
                .has(GraphResourceDefStore.PROPERTY_NAME, property.getName())
                .tryNext();

        // property definition must exists to be able to create the property
        if (propertyDefVOpt.isEmpty()) {
            throw new RuntimeException(
                    String.format("Property definition '%s' not found.", property.getName())
            );
        }

        // get property typ
        Vertex propertyDefV = propertyDefVOpt.get();
        PropertyType propertyType = propertyDefV.value(GraphResourceDefStore.PROPERTY_TYPE);
        Boolean multiValued = propertyDefV.value(GraphResourceDefStore.PROPERTY_IS_MULTIVALUED);

        // create property vertex
        Vertex propertyV = graphTraversal
                .addV(LABEL_PROPERTY)
                .property(PROPERTY_NAME, property.getName())
                .property(PROPERTY_TYPE, propertyType)
                .property(PROPERTY_IS_MULTIVALUED, multiValued)
                .next();

        // create edge between source (resource or property value) vertex and property vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY)
                .from(sourceV)
                .to(propertyV)
                .next();

        // create edge between property vertex and property definition vertex
        graphTraversal
                .addE(LABEL_IS_PROPERTY_DEF)
                .from(propertyV)
                .to(propertyDefVOpt.get())
                .next();

        return propertyV;
    }

    private void savePropertiesFromVertex(Vertex sourceV, Properties properties) {
        properties.forEach(property -> savePropertyFromVertex(sourceV, property));
    }

    private void savePropertyFromVertex(Vertex sourceV, Property<?> property) {
        // source vertex can be a resource or a property value
        LOGGER.debug("- Saving property '{}' with value {}",
                toMemberPath(sourceV, property.getName()),
                property
        );

        // create property vertex
        Vertex propertyV = getOrCreatePropertyVertex(sourceV, property);

        // save property value depending on the property type
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
        // nested property values are empty, but with edges to properties and associations
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .next();

        // add edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();

        // save nested properties
        savePropertiesFromVertex(propertyValueV, nestedProperty.getProperties());

        // save nested associations
        saveAssociationsFromVertex(propertyValueV, nestedProperty.getAssociations());
    }

    private void saveAtomicPropertyValueFromVertex(Vertex propertyV, Object obj) {
        // atomic property values hold a value
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_VALUE, obj)
                .next();

        // create edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();
    }

    private void saveKeyValuePropertyValueFromVertex(Vertex propertyV, KeyValue keyValue) {
        // key value property values hold a key and a value properties
        Vertex propertyValueV = graphTraversal.V(propertyV.id())
                .addV(LABEL_PROPERTY_VALUE)
                .property(PROPERTY_KEY, keyValue.getKey())
                .property(PROPERTY_VALUE, keyValue.getValue())
                .next();

        // create edge between property and property value edges
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_VALUE)
                .from(propertyV)
                .to(propertyValueV)
                .next();
    }

    private void saveAssociationsFromVertex(Vertex sourceV, Associations associations) {
        associations.forEach(association -> saveAssociationFromVertex(sourceV, association));
    }

    @SuppressWarnings("unchecked")
    private void saveAssociationFromVertex(Vertex sourceV, Association association) {
        // source vertex can be a resource or a property value
        LOGGER.debug("- Saving association '{}' with value {}",
                toMemberPath(sourceV, association.getName()),
                association
        );

        // TODO remove old associations?

        // get, or create, target resource by it ids
        Vertex targetV = getOrCreateResourceVertexById(
                association.getResourceDefRef(),
                association.getResourceId()
        );

        // link source resource to target resource
        graphTraversal
                .V(sourceV.id())
                .fold()
                // check whether the association already exists, or create it otherwise
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

    private Optional<Resource> toResourceFromVertex(Vertex resourceV) {
        // check whether the vertex is a resource
        if (!resourceV.label().equals(LABEL_RESOURCE)) {
            LOGGER.error("Vertex is not of type '{}'.", LABEL_RESOURCE);
            return Optional.empty();
        }

        // get resource data
        ResourceDefRef resourceDefRef = resourceV.value(PROPERTY_RESOURCE_DEF_REF);
        String resourceId = resourceV.value(PROPERTY_RESOURCE_ID);

        LOGGER.debug("Found resource '{}' with id '{}'",
                resourceDefRef,
                resourceId
        );

        // return resource
        return toPropertiesFromVertex(resourceV)
                .flatMap(properties ->
                        toAssociationsFromVertex(resourceV)
                                .map(associations ->
                                        new Resource(
                                                resourceDefRef,
                                                resourceId,
                                                properties,
                                                associations
                                        )
                                )
                );
    }

    private Optional<Properties> toPropertiesFromVertex(Vertex sourceV) {
        // check whether the vertex is a resource or property value
        if (!sourceV.label().equals(LABEL_RESOURCE) && !sourceV.label().equals(LABEL_PROPERTY_VALUE)) {
            LOGGER.error("Vertex is not of type '{}' or '{}'.", LABEL_RESOURCE, LABEL_PROPERTY_VALUE);
            return Optional.empty();
        }

        // source can be a resource or a property value
        return Optional.of(
                new Properties(
                        graphTraversal
                                .V(sourceV.id())
                                .out(LABEL_HAS_PROPERTY)
                                .toStream()
                                .flatMap(this::toPropertyValuesFromVertex)
                )
        );
    }

    private Stream<Property<?>> toPropertyValuesFromVertex(Vertex propertyV) {
        // check whether the vertex is a property
        if (!propertyV.label().equals(LABEL_PROPERTY)) {
            LOGGER.error("Vertex is not of type '{}'.", LABEL_PROPERTY_VALUE);
            return Stream.empty();
        }

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
                )
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<Property<?>> toProperty(String propertyName, PropertyType type, Vertex propertyValueV) {
        switch (type) {
            case INTEGER:
                return Optional.of(
                        new IntegerProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
                );
            case DOUBLE:
                return Optional.of(
                        new DoubleProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
                );
            case STRING:
                return Optional.of(
                        new StringProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
                );
            case BOOLEAN:
                return Optional.of(
                        new BooleanProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
                );
            case DATE:
                return Optional.of(
                        new DateProperty(propertyName, propertyValueV.value(PROPERTY_VALUE))
                );
            case KEY_VALUE:
                return Optional.of(
                        new KeyValueProperty(
                                propertyName,
                                new KeyValue(
                                        propertyValueV.value(PROPERTY_KEY),
                                        propertyValueV.value(PROPERTY_VALUE)
                                )
                        )
                );
            case NESTED:
                return toPropertiesFromVertex(propertyValueV)
                        .flatMap(properties ->
                                toAssociationsFromVertex(propertyValueV)
                                        .map(associations ->
                                                new NestedProperty(
                                                        propertyName,
                                                        new NestedPropertyValue(
                                                                properties,
                                                                associations
                                                        )
                                                )
                                        )
                        );
            default:
                LOGGER.error("Unsupported property type {}", type);
                return Optional.empty();
        }
    }

    private Optional<Associations> toAssociationsFromVertex(Vertex sourceV) {
        // check whether the vertex is a resource or property value
        if (!sourceV.label().equals(LABEL_RESOURCE) && !sourceV.label().equals(LABEL_PROPERTY_VALUE)) {
            LOGGER.error("Vertex is not of type '{}' or '{}'.", LABEL_RESOURCE, LABEL_PROPERTY_VALUE);
            return Optional.empty();
        }

        return Optional.of(
                new Associations(toAssociationsStreamFromVertex(sourceV))
        );
    }

    private Stream<Association> toAssociationsStreamFromVertex(Vertex sourceV) {
        // check whether the vertex is a resource or property value
        if (!sourceV.label().equals(LABEL_RESOURCE) && !sourceV.label().equals(LABEL_PROPERTY_VALUE)) {
            LOGGER.error("Vertex is not of type '{}' or '{}'.", LABEL_RESOURCE, LABEL_PROPERTY_VALUE);
            return Stream.empty();
        }

        // get all association edges
        List<Edge> associationEdges = graphTraversal
                .V(sourceV.id())
                .outE(LABEL_HAS_ASSOCIATION)
                .toList();

        // map association edges to association objects
        return associationEdges
                .stream()
                .flatMap(associationEdge ->
                        graphTraversal
                                .E(associationEdge.id())
                                .inV()
                                .map(vertex -> new Association(
                                        associationEdge.value(PROPERTY_NAME),
                                        vertex.get().value(PROPERTY_RESOURCE_DEF_REF),
                                        vertex.get().value(PROPERTY_RESOURCE_ID)
                                ))
                                .toStream()
                );
    }

    private String toMemberPath(Vertex sourceV, String memberName) {
        if (sourceV.label().equals(LABEL_RESOURCE)) {
            return memberName;
        } else if (sourceV.label().equals(LABEL_PROPERTY_VALUE)) {
            return toMemberPath(
                    graphTraversal
                            .V(sourceV.id())
                            .in(LABEL_HAS_PROPERTY_VALUE)
                            .next(),
                    memberName);
        } else {
            return String.format(
                    "%s.%s",
                    toMemberPath(
                            graphTraversal.V(sourceV.id())
                                    .in(LABEL_HAS_PROPERTY)
                                    .next(),
                            sourceV.value(PROPERTY_NAME)
                    ),
                    memberName
            );
        }
    }
}
