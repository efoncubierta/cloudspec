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

import arrow.core.None
import arrow.core.Some
import cloudspec.annotation.ResourceReflectionUtil.toAssociations
import cloudspec.annotation.ResourceReflectionUtil.toProperties
import cloudspec.annotation.ResourceReflectionUtil.toResource
import cloudspec.annotation.ResourceReflectionUtil.toResourceId
import cloudspec.util.ModelTestUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ResourceReflectionUtilTest {
    @Test
    fun shouldProduceResourceId() {
        val resourceIdOpt = toResourceId(ModelTestUtils.TEST_RESOURCE)
        assertTrue(resourceIdOpt is Some<String>)
        assertEquals(ModelTestUtils.RESOURCE_ID, resourceIdOpt.t)
    }

    @Test
    fun shouldNotProduceResourceId() {
        val resourceIdOpt = toResourceId(MyResource("test"))
        assertTrue(resourceIdOpt is None)
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
        val resourceOpt = toResource(ModelTestUtils.TEST_RESOURCE)
        assertTrue(resourceOpt is Some<Resource>)
        assertEquals(ModelTestUtils.RESOURCE, resourceOpt.t)
    }

    @Test
    fun shouldNotProduceResource() {
        val resourceOpt = toResource(MyResource("test"))
        assertTrue(resourceOpt is None)
    }

    private inner class MyResource(val id: String)
}
