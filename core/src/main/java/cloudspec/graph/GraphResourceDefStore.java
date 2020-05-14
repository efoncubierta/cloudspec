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

import cloudspec.model.AssociationDef;
import cloudspec.model.PropertyDef;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Graph-based implementation of resource definition store.
 * <p>
 * This class implement {@link ResourceDefStore} using Tinkerpop graphs.
 * <p>
 * Graph model:
 * <ul>
 * <li>
 *     Resource definitions are <strong>resourceDef</strong> vertices, with the following properties:
 *     <ul>
 *         <li><strong>resourceDefRef</strong></li>
 *         <li><strong>description</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     Property definitions are <strong>propertyDef</strong> vertices, with the following properties:
 *     <ul>
 *         <li><strong>name</strong></li>
 *         <li><strong>description</strong></li>
 *         <li><strong>type</strong></li>
 *         <li><strong>isArray</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     Association definitions are <strong>associationDef</strong> vertices, with the following properties:
 *     <ul>
 *         <li><strong>name</strong></li>
 *         <li><strong>description</strong></li>
 *         <li><strong>resourceDefRef</strong></li>
 *     </ul>
 * </li>
 * <li>
 *     <strong>resourceDef</strong> vertices has the following out edges:
 *     <ul>
 *         <li>resourceDef --- [hasPropertyDef] ---> propertyDef</li>
 *         <li>resourceDef --- [hasAssociationDef] ---> associationDef</li>
 *     </ul>
 * </li>
 * <li>
 *     <strong>propertyDef</strong> vertices has the following out edges:
 *     <ul>
 *         <li>propertyDef --- [hasPropertyDef] ---> propertyDef</li>
 *     </ul>
 * </li>
 * </ul>
 */
public class GraphResourceDefStore implements ResourceDefStore {
    private final Logger LOGGER = LoggerFactory.getLogger(GraphResourceDefStore.class);

    public static final String LABEL_RESOURCE_DEF = "resourceDef";
    public static final String LABEL_PROPERTY_DEF = "propertyDef";
    public static final String LABEL_ASSOCIATION_DEF = "associationDef";
    public static final String LABEL_HAS_PROPERTY_DEF = "hasPropertyDef";
    public static final String LABEL_HAS_ASSOCIATION_DEF = "hasAssociationDef";
    public static final String PROPERTY_RESOURCE_DEF_REF = "resourceDefRef";
    public static final String PROPERTY_PROVIDER_NAME = "providerName";
    public static final String PROPERTY_GROUP_NAME = "groupName";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_IS_ARRAY = "isArray";

    private final GraphTraversalSource graphTraversal;

    /**
     * Constructor.
     *
     * @param graph TinkerPop Graph.
     */
    public GraphResourceDefStore(Graph graph) {
        this.graphTraversal = graph.traversal();
    }

    @Override
    public Optional<ResourceDef> getResourceDef(ResourceDefRef resourceDefRef) {
        LOGGER.debug("Getting resource definition '{}'", resourceDefRef);

        return graphTraversal
                .V()
                .has(LABEL_RESOURCE_DEF, PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .toStream()
                .peek(resourceDefV -> LOGGER.debug("- Found resource definition '{}'",
                        (Object) resourceDefV.value(PROPERTY_RESOURCE_DEF_REF))
                )
                .map(resourceDefV -> new ResourceDef(
                        resourceDefV.value(PROPERTY_RESOURCE_DEF_REF),
                        resourceDefV.value(PROPERTY_DESCRIPTION),
                        getPropertyDefs(resourceDefV),
                        getAssociationDefs(resourceDefV)
                ))
                .findFirst();
    }

    private List<PropertyDef> getPropertyDefs(Vertex sourceV) {
        return graphTraversal
                .V(sourceV.id())
                .out(LABEL_HAS_PROPERTY_DEF)
                .toStream()
                .peek(propertyDefV -> LOGGER.debug("- Found property definition '{}'",
                        (Object) propertyDefV.value(PROPERTY_NAME))
                )
                .map(propertyDefV -> new PropertyDef(
                        propertyDefV.value(PROPERTY_NAME),
                        propertyDefV.value(PROPERTY_DESCRIPTION),
                        propertyDefV.value(PROPERTY_TYPE),
                        propertyDefV.value(PROPERTY_IS_ARRAY),
                        getPropertyDefs(propertyDefV)
                ))
                .collect(Collectors.toList());
    }

    private List<AssociationDef> getAssociationDefs(Vertex sourceV) {
        return graphTraversal
                .V(sourceV.id())
                .out(LABEL_HAS_ASSOCIATION_DEF)
                .toStream()
                .peek(associationDefV -> LOGGER.debug("- Found association definition '{}'",
                        (Object) associationDefV.value(PROPERTY_NAME))
                )
                .map(associationDefV -> new AssociationDef(
                        associationDefV.value(PROPERTY_NAME),
                        associationDefV.value(PROPERTY_DESCRIPTION),
                        associationDefV.value(PROPERTY_RESOURCE_DEF_REF)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceDef> getResourceDefs() {
        LOGGER.debug("Getting all resource definitions");

        return graphTraversal
                .V()
                .hasLabel(LABEL_RESOURCE_DEF)
                .toStream()
                .peek(resourceDefV ->
                        LOGGER.debug(
                                "- Found resource definition '{}'",
                                (Object) resourceDefV.value(PROPERTY_RESOURCE_DEF_REF)
                        )
                )
                .map(resourceDefV -> new ResourceDef(
                        resourceDefV.value(PROPERTY_RESOURCE_DEF_REF),
                        resourceDefV.value(PROPERTY_DESCRIPTION),
                        getPropertyDefs(resourceDefV),
                        getAssociationDefs(resourceDefV)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void createResourceDef(ResourceDef resourceDef) {
        LOGGER.debug("Adding resource definition '{}'", resourceDef.getRef());

        ResourceDefRef resourceDefRef = resourceDef.getRef();

        // create resource definition vertex
        Vertex resourceV = graphTraversal
                .addV(LABEL_RESOURCE_DEF)
                .property(PROPERTY_PROVIDER_NAME, resourceDefRef.getProviderName())
                .property(PROPERTY_GROUP_NAME, resourceDefRef.getGroupName())
                .property(PROPERTY_NAME, resourceDefRef.getResourceName())
                .property(PROPERTY_RESOURCE_DEF_REF, resourceDefRef)
                .property(PROPERTY_DESCRIPTION, resourceDef.getDescription())
                .next();

        // add property definitions
        addPropertyDefs(resourceV, resourceDef.getProperties());

        // add association definitions
        addAssociationDefs(resourceV, resourceDef.getAssociations());
    }

    private void addPropertyDefs(Vertex sourceV, List<PropertyDef> propertyDefs) {
        propertyDefs.forEach(propertyDef -> addPropertyDef(sourceV, propertyDef));
    }

    private void addPropertyDef(Vertex sourceV, PropertyDef propertyDef) {
        LOGGER.debug("- Adding property definition '{}' with value {}",
                getPropertyPath(sourceV, propertyDef.getName()), propertyDef);

        // create property definition vertex
        Vertex propertyFromV = graphTraversal
                .addV(LABEL_PROPERTY_DEF)
                .property(PROPERTY_NAME, propertyDef.getName())
                .property(PROPERTY_DESCRIPTION, propertyDef.getDescription())
                .property(PROPERTY_TYPE, propertyDef.getPropertyType())
                .property(PROPERTY_IS_ARRAY, propertyDef.isArray())
                .next();

        // link property definition to source vertex
        graphTraversal
                .addE(LABEL_HAS_PROPERTY_DEF)
                .from(sourceV)
                .to(propertyFromV)
                .next();

        // add nested property definitions
        addPropertyDefs(propertyFromV, propertyDef.getProperties());
    }

    private void addAssociationDefs(Vertex sourceV, List<AssociationDef> associationDefs) {
        associationDefs.forEach(associationDef -> addAssociationDef(sourceV, associationDef));
    }

    private void addAssociationDef(Vertex sourceV, AssociationDef associationDef) {
        LOGGER.debug("- Adding association definition '{}' with value {}",
                getPropertyPath(sourceV, associationDef.getName()), associationDef);

        // create association definition vertex
        Vertex associationDefV = graphTraversal
                .addV(LABEL_ASSOCIATION_DEF)
                .property(PROPERTY_NAME, associationDef.getName())
                .property(PROPERTY_DESCRIPTION, associationDef.getDescription())
                .property(PROPERTY_RESOURCE_DEF_REF, associationDef.getResourceDefRef())
                .next();

        // link association definition to source vertex
        graphTraversal
                .addE(LABEL_HAS_ASSOCIATION_DEF)
                .from(sourceV)
                .to(associationDefV)
                .next();
    }

    private String getPropertyPath(Vertex sourceV, String memberName) {
        if (sourceV.label().equals(LABEL_RESOURCE_DEF)) {
            return memberName;
        } else {
            return String.format(
                    "%s.%s",
                    getPropertyPath(graphTraversal.V(sourceV.id())
                                    .in(LABEL_HAS_PROPERTY_DEF)
                                    .next(),
                            sourceV.value(PROPERTY_NAME)
                    ),
                    memberName
            );
        }
    }
}
