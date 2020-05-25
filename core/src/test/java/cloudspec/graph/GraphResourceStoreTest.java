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
        resourceStore.saveResource(
                ModelTestUtils.TARGET_RESOURCE_DEF_REF,
                ModelTestUtils.TARGET_RESOURCE_ID,
                ModelTestUtils.TARGET_PROPERTIES,
                ModelTestUtils.TARGET_ASSOCIATIONS
        );
        resourceStore.saveResource(
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
        Optional<Properties> propertiesOpt = resourceStore.getProperties(
                ModelGenerator.randomResourceDefRef(),
                ModelGenerator.randomResourceId()
        );
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isEmpty());
    }

    @Test
    public void shouldGetPropertiesOfResource() {
        Optional<Properties> propertiesOpt = resourceStore.getProperties(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        );
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isPresent());
        assertEquals(ModelTestUtils.PROPERTIES.size(), propertiesOpt.get().size());
        assertTrue(ModelTestUtils.PROPERTIES.containsAll(propertiesOpt.get()));
    }

    @Test
    public void shouldNotGetAssociationsOfRandomResource() {
        Optional<Associations> associationsOpt = resourceStore.getAssociations(
                ModelGenerator.randomResourceDefRef(),
                ModelGenerator.randomResourceId()
        );
        assertNotNull(associationsOpt);
        assertTrue(associationsOpt.isEmpty());
    }

    @Test
    public void shouldGetAssociationsOfResource() {
        Optional<Associations> associationsOpt = resourceStore.getAssociations(
                ModelTestUtils.RESOURCE_DEF_REF,
                ModelTestUtils.RESOURCE_ID
        );
        assertNotNull(associationsOpt);
        assertTrue(associationsOpt.isPresent());
        assertEquals(ModelTestUtils.ASSOCIATIONS, associationsOpt.get());
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

        resourceStore.saveProperty(
                resourceDefRef,
                resourceId,
                property
        );

        Optional<Properties> propertiesOpt = resourceStore.getProperties(resourceDefRef, resourceId);
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isEmpty());
    }

    @Test
    public void shouldNotSetRandomPropertyToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Property<?> property = ModelGenerator.randomProperty();

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        assertThrows(RuntimeException.class, () -> resourceStore.saveProperty(resource.getResourceDefRef(), resource.getResourceId(), property));

        Optional<Properties> propertiesOpt = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isPresent());
        assertTrue(propertiesOpt.get().isEmpty());
    }

    @Test
    public void shouldSetPropertyToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Property<?> property = resource.getProperties().get(0);

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.saveProperty(resource.getResourceDefRef(), resource.getResourceId(), property);

        Optional<Properties> propertiesOpt = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isPresent());
        assertEquals(1, propertiesOpt.get().size());
        assertEquals(property, propertiesOpt.get().get(0));
    }

    @Test
    public void shouldSetMultiplePropertiesToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.saveProperties(resource.getResourceDefRef(), resource.getResourceId(), resource.getProperties());

        Optional<Properties> propertiesOpt = resourceStore.getProperties(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(propertiesOpt);
        assertTrue(propertiesOpt.isPresent());
        assertEquals(resource.getProperties(), propertiesOpt.get());
    }

    @Test
    public void shouldNotSetRandomAssociationToRandomResource() {
        ResourceDefRef resourceDefRef = ModelGenerator.randomResourceDefRef();
        String resourceId = ModelGenerator.randomResourceId();
        Association association = ModelGenerator.randomAssociation();

        resourceStore.saveAssociation(
                resourceDefRef,
                resourceId,
                association
        );

        Optional<Associations> associationsOpt = resourceStore.getAssociations(resourceDefRef, resourceId);
        assertNotNull(associationsOpt);
        assertTrue(associationsOpt.isEmpty());
    }

    @Test
    public void shouldNotSetRandomAssociationToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = ModelGenerator.randomAssociation();

        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        assertThrows(RuntimeException.class, () -> resourceStore.saveAssociation(
                resource.getResourceDefRef(),
                resource.getResourceId(),
                association
        ));

        Optional<Associations> associationsOpt = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associationsOpt);
        assertTrue(associationsOpt.isPresent());
        assertTrue(associationsOpt.get().isEmpty());
    }

    @Test
    public void shouldNotSetAssociationToRandomTargetResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = resource.getAssociations().get(0);

        resourceDefStore.createResourceDef(resourceDef);

        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        assertThrows(RuntimeException.class, () -> resourceStore.saveAssociation(resource.getResourceDefRef(), resource.getResourceId(), association));

        Optional<Associations> associationsOpt = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associationsOpt);
        assertTrue(associationsOpt.isPresent());
        assertTrue(associationsOpt.get().isEmpty());
    }

    @Test
    public void shouldSetAssociationToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);
        Association association = resource.getAssociations().get(0);
        ResourceDef targetResourceDef = ModelGenerator.randomResourceDef(association.getResourceDefRef());

        // create target resource
        resourceDefStore.createResourceDef(targetResourceDef);
        resourceStore.saveResource(association.getResourceDefRef(), association.getResourceId());

        // create resource
        resourceDefStore.createResourceDef(resourceDef);
        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.saveAssociation(resource.getResourceDefRef(), resource.getResourceId(), association);

        Optional<Associations> associationsOpt = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associationsOpt);
        assertFalse(associationsOpt.isEmpty());
        assertEquals(1, associationsOpt.get().size());
        assertEquals(association, associationsOpt.get().get(0));
    }

    @Test
    public void shouldSetMultipleAssociationsToResource() {
        ResourceDef resourceDef = ModelGenerator.randomResourceDef();
        Resource resource = ModelGenerator.randomResource(resourceDef);

        resourceDefStore.createResourceDef(resourceDef);

        // create associated resources
        resource.getAssociations().forEach(association -> {
            resourceDefStore.createResourceDef(ModelGenerator.randomResourceDef(association.getResourceDefRef()));
            resourceStore.saveResource(association.getResourceDefRef(), association.getResourceId());
        });

        resourceStore.saveResource(resource.getResourceDefRef(), resource.getResourceId());
        resourceStore.saveAssociations(resource.getResourceDefRef(), resource.getResourceId(), resource.getAssociations());

        Optional<Associations> associationsOpt = resourceStore.getAssociations(resource.getResourceDefRef(), resource.getResourceId());
        assertNotNull(associationsOpt);
        assertFalse(associationsOpt.isEmpty());
        assertEquals(resource.getAssociations(), associationsOpt.get());
    }
}
