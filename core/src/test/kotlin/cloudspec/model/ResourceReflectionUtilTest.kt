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
package cloudspec.model

import cloudspec.annotation.ResourceReflectionUtil.toAssociations
import cloudspec.annotation.ResourceReflectionUtil.toProperties
import cloudspec.annotation.ResourceReflectionUtil.toResource
import cloudspec.annotation.ResourceReflectionUtil.toResourceId
import cloudspec.util.ModelTestUtils
import org.junit.Test
import kotlin.test.*

class ResourceReflectionUtilTest {
    @Test
    fun shouldProduceResourceId() {
        val resourceId = toResourceId(ModelTestUtils.TEST_RESOURCE)
        assertNotNull(resourceId)
        assertEquals(ModelTestUtils.RESOURCE_ID, resourceId)
    }

    @Test
    fun shouldNotProduceResourceId() {
        val resourceId = toResourceId(MyResource("test"))
        assertNull(resourceId)
    }

    @Test
    fun shouldProduceProperties() {
        val properties = toProperties(ModelTestUtils.TEST_RESOURCE)
        assertNotNull(properties)
        assertFalse(properties.isEmpty())
        assertEquals(ModelTestUtils.PROPERTIES, properties)
    }

    @Test
    fun shouldNotProduceProperties() {
        val properties = toProperties(MyResource("test"))
        assertNotNull(properties)
        assertTrue(properties.isEmpty())
    }

    @Test
    fun shouldProduceAssociations() {
        val associations = toAssociations(ModelTestUtils.TEST_RESOURCE)
        assertNotNull(associations)
        assertFalse(associations.isEmpty())
        assertEquals(ModelTestUtils.ASSOCIATIONS, associations)
    }

    @Test
    fun shouldNotProduceAssociations() {
        val associations = toAssociations(MyResource("test"))
        assertNotNull(associations)
        assertTrue(associations.isEmpty())
    }

    @Test
    fun shouldProduceResource() {
        val resource = toResource(ModelTestUtils.TEST_RESOURCE)
        assertNotNull(resource)
        assertEquals(ModelTestUtils.RESOURCE, resource)
    }

    @Test
    fun shouldNotProduceResource() {
        val resource = toResource(MyResource("test"))
        assertNull(resource)
    }

    private inner class MyResource(val id: String)
}
