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

import cloudspec.model.Function;
import cloudspec.model.Property;
import cloudspec.model.Resource;
import cloudspec.model.ResourceFqn;
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
    private static final String LABEL_FUNCTION = "function";
    private static final String LABEL_HAS_PROPERTY = "hasProperty";
    private static final String LABEL_HAS_FUNCTION = "hasFunction";
    private static final String PROPERTY_RESOURCE_ID = "resource_id";
    private static final String PROPERTY_FQN = "fqn";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_VALUE = "value";

    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceStore(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Optional<Resource> getResource(ResourceFqn resourceFqn, String id) {
        LOGGER.debug("Getting resource '{}' with id '{}'", resourceFqn, id);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_FQN, resourceFqn)
                .has(PROPERTY_RESOURCE_ID, id)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap -> LOGGER.debug("- Found resource '{}' with id '{}'",
                        resourceMap.get(PROPERTY_FQN), resourceMap.get(PROPERTY_RESOURCE_ID)))
                .map(resourceMap -> new Resource(
                        (ResourceFqn) resourceMap.get(PROPERTY_FQN),
                        (String) resourceMap.get(PROPERTY_RESOURCE_ID),
                        getProperties(resourceFqn, id),
                        getFunctions(resourceFqn, id)
                ))
                .findFirst();
    }

    private List<Property> getProperties(ResourceFqn resourceFqn, String id) {
        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_FQN, resourceFqn)
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

    private List<Function> getFunctions(ResourceFqn resourceFqn, String id) {
        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_FQN, resourceFqn)
                .has(PROPERTY_RESOURCE_ID, id)
                .out(LABEL_HAS_FUNCTION)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(functionMap -> LOGGER.debug("- Found function '{}'", functionMap.get(PROPERTY_NAME)))
                .map(functionMap -> new Function(
                        (String) functionMap.get(PROPERTY_NAME)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> getResourcesByType(ResourceFqn resourceFqn) {
        LOGGER.debug("Getting all resources of type '{}'", resourceFqn);

        return gTraversal.V()
                .has(LABEL_RESOURCE, PROPERTY_FQN, resourceFqn)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceMap -> LOGGER.debug("- Found resource '{}' with id '{}'", resourceFqn, resourceMap.get(PROPERTY_RESOURCE_ID)))
                .map(resourceDefMap -> new Resource(
                        (ResourceFqn) resourceDefMap.get(PROPERTY_FQN),
                        (String) resourceDefMap.get(PROPERTY_RESOURCE_ID),
                        getProperties(resourceFqn, (String) resourceDefMap.get(PROPERTY_RESOURCE_ID)),
                        getFunctions(resourceFqn, (String) resourceDefMap.get(PROPERTY_RESOURCE_ID))
                ))
                .collect(Collectors.toList());
    }

    public void addResource(Resource resource) {
        LOGGER.debug("Adding resource '{}' with id '{}'", resource.getFqn(), resource.getResourceId());

        Vertex resourceVertex = gTraversal.addV(LABEL_RESOURCE)
                .property(PROPERTY_FQN, resource.getFqn())
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

        // add function definitions
        addFunctions(resource.getFunctions())
                .forEach(functionVertex -> {
                    gTraversal.addE(LABEL_HAS_FUNCTION)
                            .from(resourceVertex)
                            .to(functionVertex)
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

    private List<Vertex> addFunctions(List<Function> functions) {
        LOGGER.debug("- Found {} function(s)", functions.size());

        return functions
                .stream()
                .map(this::addFunction)
                .collect(Collectors.toList());
    }

    private Vertex addFunction(Function function) {
        LOGGER.debug("- Adding function '{}'", function);

        return gTraversal.addV(LABEL_FUNCTION)
                .property(PROPERTY_NAME, function.getName())
                .next();
    }
}
