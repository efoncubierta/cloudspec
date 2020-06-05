/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.model;

import cloudspec.annotation.ResourceDefReflectionUtil;
import cloudspec.util.ModelTestUtils;
import cloudspec.util.TestResource;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ResourceDefReflectionUtilTest {
    @Test
    public void shouldProduceResourceDefRef() {
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefReflectionUtil.toResourceDefRef(TestResource.class);
        assertNotNull(resourceDefRefOpt);
        assertTrue(resourceDefRefOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE_DEF_REF, resourceDefRefOpt.get());
    }

    @Test
    public void shouldNotProduceResourceDefRef() {
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefReflectionUtil.toResourceDefRef(MyResource.class);
        assertNotNull(resourceDefRefOpt);
        assertFalse(resourceDefRefOpt.isPresent());
    }

    @Test
    public void shouldProduceResourceDescription() {
        Optional<String> resourceDescriptionOpt = ResourceDefReflectionUtil.toResourceDescription(TestResource.class);
        assertNotNull(resourceDescriptionOpt);
        assertTrue(resourceDescriptionOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE_DESCRIPTION, resourceDescriptionOpt.get());
    }

    @Test
    public void shouldNotProduceResourceDescription() {
        Optional<String> resourceDescriptionOpt = ResourceDefReflectionUtil.toResourceDescription(MyResource.class);
        assertNotNull(resourceDescriptionOpt);
        assertFalse(resourceDescriptionOpt.isPresent());
    }

    @Test
    public void shouldProducePropertyDefs() {
        List<PropertyDef> propertyDefs = ResourceDefReflectionUtil.toPropertyDefs(TestResource.class);
        assertNotNull(propertyDefs);
        assertFalse(propertyDefs.isEmpty());
        ModelTestUtils.comparePropertyDefs(ModelTestUtils.PROPERTY_DEFS, propertyDefs);
    }

    @Test
    public void shouldNotProducePropertyDefs() {
        List<PropertyDef> propertyDefs = ResourceDefReflectionUtil.toPropertyDefs(MyResource.class);
        assertNotNull(propertyDefs);
        assertTrue(propertyDefs.isEmpty());
    }

    @Test
    public void shouldProduceAssociationDefs() {
        List<AssociationDef> associationDefs = ResourceDefReflectionUtil.toAssociationDefs(TestResource.class);
        assertNotNull(associationDefs);
        assertFalse(associationDefs.isEmpty());
        ModelTestUtils.compareAssociationDefs(ModelTestUtils.ASSOCIATION_DEFS, associationDefs);
    }

    @Test
    public void shouldNotProduceAssociationDefs() {
        List<AssociationDef> associationDefs = ResourceDefReflectionUtil.toAssociationDefs(MyResource.class);
        assertNotNull(associationDefs);
        assertTrue(associationDefs.isEmpty());
    }

    @Test
    public void shouldProduceResourceDef() {
        Optional<ResourceDef> resourceDefOpt = ResourceDefReflectionUtil.toResourceDef(TestResource.class);
        System.out.println(ModelTestUtils.RESOURCE_DEF);
        System.out.println(resourceDefOpt.get());
        assertEquals(ModelTestUtils.RESOURCE_DEF, resourceDefOpt.get());
    }

    @Test
    public void shouldNotProduceResourceDef() {
        Optional<ResourceDef> resourceDefOpt = ResourceDefReflectionUtil.toResourceDef(MyResource.class);
        assertNotNull(resourceDefOpt);
        assertFalse(resourceDefOpt.isPresent());
    }

    private class MyResource {
        private String id;

        public MyResource(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
