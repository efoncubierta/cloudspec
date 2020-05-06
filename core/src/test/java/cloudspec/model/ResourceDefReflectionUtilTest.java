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
package cloudspec.model;

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
        assertNotNull(resourceDefOpt);
        assertTrue(resourceDefOpt.isPresent());
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
