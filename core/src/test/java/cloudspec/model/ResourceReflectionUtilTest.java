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

import cloudspec.annotation.ResourceReflectionUtil;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

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

    @Test
    public void shouldProduceProperties() {
        Properties properties = ResourceReflectionUtil.toProperties(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(ModelTestUtils.PROPERTIES, properties);
    }

    @Test
    public void shouldNotProduceProperties() {
        Properties properties = ResourceReflectionUtil.toProperties(new MyResource("test"));
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    @Test
    public void shouldProduceAssociations() {
        Associations associations = ResourceReflectionUtil.toAssociations(ModelTestUtils.TEST_RESOURCE);
        assertNotNull(associations);
        assertFalse(associations.isEmpty());
        assertEquals(ModelTestUtils.ASSOCIATIONS, associations);
    }

    @Test
    public void shouldNotProduceAssociations() {
        Associations associations = ResourceReflectionUtil.toAssociations(new MyResource("test"));
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
