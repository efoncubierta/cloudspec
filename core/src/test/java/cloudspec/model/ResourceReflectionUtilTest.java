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

import cloudspec.annotation.ResourceReflectionUtil;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ResourceReflectionUtilTest {
    @Test
    public void shouldProduceResourceId() {
        Optional<String> resourceIdOpt = ResourceReflectionUtil.toResourceId(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(resourceIdOpt);
        assertTrue(resourceIdOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE_ID, resourceIdOpt.get());
    }

    @Test
    public void shouldNotProduceResourceId() {
        Optional<String> resourceIdOpt = ResourceReflectionUtil.toResourceId(new MyResource("test"));
        assertNotNull(resourceIdOpt);
        assertFalse(resourceIdOpt.isPresent());
    }

    public void shouldProduceProperties() {
        List<Property> properties = ResourceReflectionUtil.toProperties(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        ModelTestUtils.compareProperties(ModelTestUtils.PROPERTIES, properties);
    }

    @Test
    public void shouldNotProduceProperties() {
        List<Property> properties = ResourceReflectionUtil.toProperties(new MyResource("test"));
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void shouldProduceAssociations() {
        List<Association> associations = ResourceReflectionUtil.toAssociations(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(associations);
        assertFalse(associations.isEmpty());
        ModelTestUtils.compareAssociations(ModelTestUtils.ASSOCIATIONS, associations);
    }

    @Test
    public void shouldNotProduceAssociations() {
        List<Association> associations = ResourceReflectionUtil.toAssociations(new MyResource("test"));
        assertNotNull(associations);
        assertTrue(associations.isEmpty());
    }

    @Test
    public void shouldProduceResource() {
        Optional<Resource> resourceOpt = ResourceReflectionUtil.toResource(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.get());
    }

    @Test
    public void shouldNotProduceResource() {
        Optional<Resource> resourceOpt = ResourceReflectionUtil.toResource(new MyResource("test"));
        assertNotNull(resourceOpt);
        assertFalse(resourceOpt.isPresent());
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
