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
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

public class GraphResourceStore implements ResourceStore {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceStore.class);

    private static final String LABEL_RESOURCE = "resource";
    private static final String LABEL_PROPERTY = "property";
    private static final String LABEL_ASSOCIATION = "association";
    private static final String LABEL_HAS_PROPERTY = "hasProperty";
    private static final String LABEL_HAS_ASSOCIATION = "hasAssociation";
    private static final String PROPERTY_RESOURCE_ID = "resourceId";
    private static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_VALUE = "value";

    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceStore(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Optional<Resource> getResource(ResourceDefRef resourceDefRef, String id) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceDefRef, id);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, id)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap -> LOGGER.debug("- Found resource '{}' with id '{}'",
                        resourceMap.get(PROPERTY_RESOURCE_DEF_REF), resourceMap.get(PROPERTY_RESOURCE_ID)))
                .map(resourceMap -> new Resource(
                        (ResourceDefRef) resourceMap.get(PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceMap.get(PROPERTY_RESOURCE_ID),
                        getProperties(resourceDefRef, id),
                        getAssociations(resourceDefRef, id)
                ))
                .findFirst();
    }

    private List<Property> getProperties(ResourceDefRef resourceDefRef, String id) {
        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, id)
                .out(LABEL_HAS_PROPERTY)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(propertyMap -> LOGGER.debug("- Found property '{}'", propertyMap.get(PROPERTY_NAME)))
                .map(propertyMap -> new Property(
                        (String) propertyMap.get(PROPERTY_NAME),
                        (Object) propertyMap.get(PROPERTY_VALUE)
                ))
                .collect(Collectors.toList());
    }

    private List<Association> getAssociations(ResourceDefRef resourceDefRef, String id) {
        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .has(PROPERTY_RESOURCE_ID, id)
                .out(LABEL_HAS_ASSOCIATION)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(associationMap -> LOGGER.debug("- Found association '{}'", associationMap.get(PROPERTY_NAME)))
                .map(associationMap -> new Association(
                        (String) associationMap.get(PROPERTY_NAME),
                        (ResourceDefRef) associationMap.get(PROPERTY_RESOURCE_DEF_REF),
                        (String) associationMap.get(PROPERTY_RESOURCE_ID)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> getResourcesByType(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting all resources of type '{}'", resourceDefRef);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap -> LOGGER.debug("- Found resource '{}' with id '{}'", resourceDefRef, resourceMap.get(PROPERTY_RESOURCE_ID)))
                .map(resourceDefMap -> new Resource(
                        (ResourceDefRef) resourceDefMap.get(PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceDefMap.get(PROPERTY_RESOURCE_ID),
                        getProperties(resourceDefRef, (String) resourceDefMap.get(PROPERTY_RESOURCE_ID)),
                        getAssociations(resourceDefRef, (String) resourceDefMap.get(PROPERTY_RESOURCE_ID))
                ))
                .collect(Collectors.toList());
    }

    public void addResource(Resource resource) {
        LOGGER.debug("Adding resource '{}' with id '{}'", resource.getResourceDefRef(), resource.getResourceId());

        Vertex resourceVertex = gTraversal.addV(LABEL_RESOURCE)
                .property(PROPERTY_RESOURCE_DEF_REF, resource.getResourceDefRef())
                .property(PROPERTY_RESOURCE_ID, resource.getResourceId())
                .next();

        // add property definitions
        addProperties(resource.getProperties())
                .forEach(propertyVertex -> {
                    gTraversal.addE(LABEL_HAS_PROPERTY)
                            .from(resourceVertex)
                            .to(propertyVertex)
                            .iterate();
                });

        // add association definitions
        addAssociations(resource.getAssociations())
                .forEach(associationVertex -> {
                    gTraversal.addE(LABEL_HAS_ASSOCIATION)
                            .from(resourceVertex)
                            .to(associationVertex)
                            .iterate();
                });
    }

    private List<Vertex> addProperties(List<Property> properties) {
        LOGGER.debug("- Found {} properties", properties.size());
        return properties
                .stream()
                .map(this::addProperty)
                .collect(Collectors.toList());
    }

    private Vertex addProperty(Property property) {
        LOGGER.debug("- Adding property '{}'", property);

        return gTraversal.addV(LABEL_PROPERTY)
                .property(PROPERTY_NAME, property.getName())
                .property(PROPERTY_VALUE, property.getValue())
                .next();
    }

    private List<Vertex> addAssociations(List<Association> associations) {
        LOGGER.debug("- Found {} association(s)", associations.size());

        return associations
                .stream()
                .map(this::addAssociation)
                .collect(Collectors.toList());
    }

    private Vertex addAssociation(Association association) {
        LOGGER.debug("- Adding association '{}'", association);

        return gTraversal.addV(LABEL_ASSOCIATION)
                .property(PROPERTY_NAME, association.getName())
                .property(PROPERTY_RESOURCE_DEF_REF, association.getResourceDefRef())
                .property(PROPERTY_RESOURCE_ID, association.getResourceId())
                .next();
    }
}
