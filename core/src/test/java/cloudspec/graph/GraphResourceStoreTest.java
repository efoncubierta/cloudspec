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
import cloudspec.util.ModelGenerator;
import cloudspec.util.ModelTestUtils;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GraphResourceStoreTest {
    private final Graph graph = TinkerGraph.open();
    private final GraphResourceDefStore resourceDefStore = new GraphResourceDefStore(graph);
    private final GraphResourceStore resourceStore = new GraphResourceStore(graph);

    @Before
    public void before() {
        graph.traversal().V().drop().iterate();

        resourceDefStore.createResourceDef(ModelTestUtils.TARGET_RESOURCE_DEF);
        resourceDefStore.createResourceDef(ModelTestUtils.RESOURCE_DEF);
        resourceStore.createResource(
                ModelTestUtils.TARGET_RESOURCE_DEF_REF,
                ModelTestUtils.TARGET_RESOURCE_ID,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        );
        resourceStore.createResource(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID,
                ModelTestUtils.PROPERTIES,
                ModelTestUtils.ASSOCIATIONS
        );
    }

    @Test
    public void shouldNotExistRandomResourceId() {
        assertFalse(
                resourceStore.exists(
                        ModelGenerator.randomResourceDefRef(),
                        ModelGenerator.randomResourceId()
                )
        );
    }

    @Test
    public void shouldExistResource() {
        assertTrue(
                resourceStore.exists(
                        ModelTestUtils.RESOURCE_DEF_REF,
                        ModelTestUtils.RESOURCE_ID
                )
        );
    }

    @Test
    public void shouldNotGetRandomResource() {
        Optional<Resource> resourceOpt = resourceStore.getResource(
                ModelGenerator.randomResourceDefRef(),
                ModelGenerator.randomResourceId()
        );
        assertNotNull(resourceOpt);
        assertFalse(resourceOpt.isPresent());
    }

    @Test
    public void shouldGetResource() {
        Optional<Resource> resourceOpt = resourceStore.getResource(
                ModelTestUtils.RESOURCE.getResourceDefRef(),
                ModelTestUtils.RESOURCE.getResourceId()
        );
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.get());
    }

    @Test
    public void shouldNotGetPropertiesOfRandomResource() {
        Properties properties = resourceStore.getProperties(
                ModelGenerator.randomResourceDefRef(),
                ModelGenerator.randomResourceId()
        );
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void shouldGetPropertiesOfResource() {
        Properties properties = resourceStore.getProperties(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        );
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(ModelTestUtils.PROPERTIES.size(), properties.size());
        System.out.println(ModelTestUtils.PROPERTIES);
        System.out.println(properties);
        assertTrue(ModelTestUtils.PROPERTIES.containsAll(properties));
    }

    @Test
    public void shouldNotGetAssociationsOfRandomResource() {
        Associations associations = resourceStore.getAssociations(
                ModelGenerator.randomResourceDefRef(),
                ModelGenerator.randomResourceId()
        );
        assertNotNull(associations);
        assertTrue(associations.isEmpty());
    }

    @Test
    public void shouldGetAssociationsOfResource() {
        Associations associations = resourceStore.getAssociations(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        );
        assertNotNull(associations);
        assertFalse(associations.isEmpty());
        assertEquals(ModelTestUtils.ASSOCIATIONS, associations);
    }

    @Test
    public void shouldNotGetResourcesByRandomDefinition() {
        List<Resource> resources = resourceStore.getResourcesByDefinition(
                ModelGenerator.randomResourceDefRef()
        );
        assertNotNull(resources);
        assertTrue(resources.isEmpty());
    }

    @Test
    public void shouldGetResourcesByDefinition() {
        List<Resource> resources = resourceStore.getResourcesByDefinition(
                ModelTestUtils.RESOURCE_DEF_REF
        );
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        assertEquals(1, resources.size());
        assertEquals(ModelTestUtils.RESOURCE, resources.get(0));
    }

    @Test
    public void shouldNotSetRandomPropertyToRandomResource() {
        ResourceDefRef resourceDefRef = ModelGenerator.randomResourceDefRef();
        String resourceId = ModelGenerator.randomResourceId();
        Property property = ModelGenerator.randomProperty();

        resourceStore.setProperty(
                resourceDefRef,
                resourceId,
                property
        );

        Properties properties = resourceStore.getProperties(resourceDefRef, resourceId);
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void shouldNotSetRandomPropertyToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Property property = ModelGenerator.randomProperty();

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setProperty(resource.getResourceDefRef(), resource.getResourceId(), property);

        Properties properties = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void shouldSetPropertyToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Property property = resource.getProperties().get(0);

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setProperty(resource.getResourceDefRef(), resource.getResourceId(), property);

        Properties properties = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(1, properties.size());
        assertEquals(property, resource.getProperties().get(0));
    }

    @Test
    public void shouldSetMultiplePropertiesToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setProperties(resource.getResourceDefRef(), resource.getResourceId(), resource.getProperties());

        Properties properties = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(resource.getProperties(), properties);
    }

    @Test
    public void shouldNotSetRandomAssociationToRandomResource() {
        ResourceDefRef resourceDefRef = ModelGenerator.randomResourceDefRef();
        String resourceId = ModelGenerator.randomResourceId();
        Association association = ModelGenerator.randomAssociation();

        resourceStore.setAssociation(
                resourceDefRef,
                resourceId,
                association
        );

        Associations associations = resourceStore.getAssociations(resourceDefRef, resourceId);
        assertNotNull(associations);
        assertTrue(associations.isEmpty());
    }

    @Test
    public void shouldNotSetRandomAssociationToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = ModelGenerator.randomAssociation();

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setAssociation(
                resource.getResourceDefRef(),
                resource.getResourceId(),
                association
        );

        Associations associations = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associations);
        assertTrue(associations.isEmpty());
    }

    @Test
    public void shouldNotSetAssociationToRandomTargetResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = resource.getAssociations().get(0);

        resourceDefStore.createResourceDef(resourceDef);

        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setAssociation(resource.getResourceDefRef(), resource.getResourceId(), association);

        Associations associations = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associations);
        assertTrue(associations.isEmpty());
    }

    @Test
    public void shouldSetAssociationToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = resource.getAssociations().get(0);

        resourceDefStore.createResourceDef(resourceDef);

        // create associated resources
        resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef(association.getResourceDefRef()));
        resourceStore.createResource(association.getResourceDefRef(), association.getResourceId());


        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setAssociations(resource.getResourceDefRef(), resource.getResourceId(), resource.getAssociations());

        Associations associations = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associations);
        assertFalse(associations.isEmpty());
        assertEquals(1, associations.size());
        assertEquals(association, associations.get(0));
    }

    @Test
    public void shouldSetMultipleAssociationsToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);

        resourceDefStore.createResourceDef(resourceDef);

        // create associated resources
        resource.getAssociations().forEach(association -> {
            resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef(association.getResourceDefRef()));
            resourceStore.createResource(association.getResourceDefRef(), association.getResourceId());
        });

        resourceStore.createResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.setAssociations(resource.getResourceDefRef(), resource.getResourceId(), resource.getAssociations());

        Associations associations = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associations);
        assertFalse(associations.isEmpty());
        assertEquals(resource.getAssociations(), associations);
    }
}
