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

import cloudspec.annotation.ResourceDefReflectionUtil.toAssociationDefs
import cloudspec.annotation.ResourceDefReflectionUtil.toPropertyDefs
import cloudspec.annotation.ResourceDefReflectionUtil.toResourceDef
import cloudspec.annotation.ResourceDefReflectionUtil.toResourceDefRef
import cloudspec.annotation.ResourceDefReflectionUtil.toResourceDescription
import cloudspec.util.ModelTestUtils
import cloudspec.util.ModelTestUtils.compareAssociationDefs
import cloudspec.util.ModelTestUtils.comparePropertyDefs
import cloudspec.util.TestResource
import org.junit.Test
import kotlin.test.*

class ResourceDefReflectionUtilTest {
    @Test
    fun shouldProduceResourceDefRef() {
        val resourceDefRef = toResourceDefRef(TestResource::class)
        assertNotNull(resourceDefRef)
        assertEquals(ModelTestUtils.RESOURCE_DEF_REF, resourceDefRef)
    }

    @Test
    fun shouldNotProduceResourceDefRef() {
        val resourceDefRef = toResourceDefRef(MyResource::class)
        assertNull(resourceDefRef)
    }

    @Test
    fun shouldProduceResourceDescription() {
        val resourceDescription = toResourceDescription(TestResource::class)
        assertNotNull(resourceDescription)
        assertEquals(ModelTestUtils.RESOURCE_DESCRIPTION, resourceDescription)
    }

    @Test
    fun shouldNotProduceResourceDescription() {
        val resourceDescription = toResourceDescription(MyResource::class)
        assertNull(resourceDescription)
    }

    @Test
    fun shouldProducePropertyDefs() {
        val propertyDefs = toPropertyDefs(TestResource::class)
        assertNotNull(propertyDefs)
        assertFalse(propertyDefs.isEmpty())
        comparePropertyDefs(ModelTestUtils.PROPERTY_DEFS, propertyDefs)
    }

    @Test
    fun shouldNotProducePropertyDefs() {
        val propertyDefs = toPropertyDefs(MyResource::class)
        assertNotNull(propertyDefs)
        assertTrue(propertyDefs.isEmpty())
    }

    @Test
    fun shouldProduceAssociationDefs() {
        val associationDefs = toAssociationDefs(TestResource::class)
        assertNotNull(associationDefs)
        assertFalse(associationDefs.isEmpty())
        compareAssociationDefs(ModelTestUtils.ASSOCIATION_DEFS, associationDefs)
    }

    @Test
    fun shouldNotProduceAssociationDefs() {
        val associationDefs = toAssociationDefs(MyResource::class)
        assertNotNull(associationDefs)
        assertTrue(associationDefs.isEmpty())
    }

    @Test
    fun shouldProduceResourceDef() {
        val resourceDef = toResourceDef(TestResource::class)
        assertNotNull(resourceDef)
        assertEquals(ModelTestUtils.RESOURCE_DEF, resourceDef)
    }

    @Test
    fun shouldNotProduceResourceDef() {
        val resourceDef = toResourceDef(MyResource::class)
        assertNull(resourceDef)
    }

    private data class MyResource(val id: String)
}
