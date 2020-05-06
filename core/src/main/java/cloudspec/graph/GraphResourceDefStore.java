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
import cloudspec.store.ResourceDefStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.unfold;

public class GraphResourceDefStore implements ResourceDefStore {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceDefStore.class);

    private static final String LABEL_RESOURCE_DEF = "resourceDef";
    private static final String LABEL_PROPERTY_DEF = "propertyDef";
    private static final String LABEL_ASSOCIATION_DEF = "associationDef";
    private static final String LABEL_HAS_PROPERTY_DEF = "hasPropertyDef";
    private static final String LABEL_HAS_ASSOCIATION_DEF = "hasAssociationDef";
    private static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    private static final String PROPERTY_PROVIDER_NAME = "providerName";
    private static final String PROPERTY_GROUP_NAME = "groupName";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_DESCRIPTION = "description";
    private static final String PROPERTY_TYPE = "type";
    private static final String PROPERTY_IS_ARRAY = "isArray";

    private final Graph graph;
    private final GraphTraversalSource gTraversal;

    public GraphResourceDefStore(Graph graph) {
        this.graph = graph;
        this.gTraversal = graph.traversal();
    }

    @Override
    public Optional<ResourceDef> getResourceDef(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting resource definition '{}'", resourceDefRef);

        return gTraversal.V()
                .has(LABEL_RESOURCE_DEF, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(resourceDefMap -> LOGGER.debug("- Found resource definition '{}'", resourceDefMap.get(PROPERTY_RESOURCE_DEF_REF)))
                .map(resourceDefMap -> new ResourceDef(
                        (ResourceDefRef) resourceDefMap.get(PROPERTY_RESOURCE_DEF_REF),
                        (String) resourceDefMap.get(PROPERTY_DESCRIPTION),
                        getPropertyDefs(resourceDefRef),
                        getAssociationDefs(resourceDefRef)
                ))
                .findFirst();
    }

    private List<PropertyDef> getPropertyDefs(ResourceDefRef resourceDefRef) {
        return gTraversal.V()
                .has(LABEL_RESOURCE_DEF, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .out(LABEL_HAS_PROPERTY_DEF)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(propertyDefMap -> LOGGER.debug("- Found property definition '{}'", propertyDefMap.get(PROPERTY_NAME)))
                .map(propertyDefMap -> new PropertyDef(
                        (String) propertyDefMap.get(PROPERTY_NAME),
                        (String) propertyDefMap.get(PROPERTY_DESCRIPTION),
                        (PropertyType) propertyDefMap.get(PROPERTY_TYPE),
                        (Boolean) propertyDefMap.get(PROPERTY_IS_ARRAY)
                ))
                .collect(Collectors.toList());
    }

    private List<AssociationDef> getAssociationDefs(ResourceDefRef resourceDefRef) {
        return gTraversal.V()
                .has(LABEL_RESOURCE_DEF, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .out(LABEL_HAS_ASSOCIATION_DEF)
                .valueMap()
                .by(unfold())
                .toStream()
                .peek(associationDefMap -> LOGGER.debug("- Found association definition '{}'", associationDefMap.get(PROPERTY_NAME)))
                .map(associatinoDefMap -> new AssociationDef(
                        (String) associatinoDefMap.get(PROPERTY_NAME),
                        (String) associatinoDefMap.get(PROPERTY_DESCRIPTION),
                        (ResourceDefRef) associatinoDefMap.get(PROPERTY_RESOURCE_DEF_REF)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceDef> getResourceDefs() {
        return null;
    }

    @Override
    public void addResourceDef(ResourceDef resourceDef) {
        LOGGER.debug("Adding resource definition '{}'", resourceDef.getRef());

        ResourceDefRef resourceDefRef = resourceDef.getRef();

        Vertex resourceVertex = gTraversal.addV(LABEL_RESOURCE_DEF)
                .property(PROPERTY_PROVIDER_NAME, resourceDefRef.getProviderName())
                .property(PROPERTY_GROUP_NAME, resourceDefRef.getGroupName())
                .property(PROPERTY_NAME, resourceDefRef.getResourceName())
                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .property(PROPERTY_DESCRIPTION, resourceDef.getDescription())
                .next();

        // add property definitions
        addPropertyDefs(resourceDef.getProperties())
                .forEach(propertyVertex -> {
                    gTraversal.addE(LABEL_HAS_PROPERTY_DEF)
                            .from(resourceVertex)
                            .to(propertyVertex)
                            .iterate();
                });

        // add association definitions
        addAssociationDefs(resourceDef.getAssociations())
                .forEach(associationVertex -> {
                    gTraversal.addE(LABEL_HAS_ASSOCIATION_DEF)
                            .from(resourceVertex)
                            .to(associationVertex)
                            .iterate();
                });
    }

    private List<Vertex> addPropertyDefs(List<PropertyDef> propertyDefs) {
        LOGGER.debug("- Found {} property definition(s)", propertyDefs.size());

        return propertyDefs
                .stream()
                .map(this::addPropertyDef)
                .collect(Collectors.toList());
    }

    private Vertex addPropertyDef(PropertyDef propertyDef) {
        LOGGER.debug("- Adding property definition '{}'", propertyDef);

        return gTraversal.addV(LABEL_PROPERTY_DEF)
                .property(PROPERTY_NAME, propertyDef.getName())
                .property(PROPERTY_DESCRIPTION, propertyDef.getDescription())
                .property(PROPERTY_TYPE, propertyDef.getPropertyType())
                .property(PROPERTY_IS_ARRAY, propertyDef.isArray())
                .next();
    }

    private List<Vertex> addAssociationDefs(List<AssociationDef> associationDefs) {
        LOGGER.debug("- Found {} association definition(s)", associationDefs.size());

        return associationDefs
                .stream()
                .map(this::addAssociationDef)
                .collect(Collectors.toList());
    }

    private Vertex addAssociationDef(AssociationDef associationDef) {
        LOGGER.debug("- Adding association definition '{}'", associationDef);

        return gTraversal.addV(LABEL_ASSOCIATION_DEF)
                .property(PROPERTY_NAME, associationDef.getName())
                .property(PROPERTY_DESCRIPTION, associationDef.getDescription())
                .next();
    }
}
