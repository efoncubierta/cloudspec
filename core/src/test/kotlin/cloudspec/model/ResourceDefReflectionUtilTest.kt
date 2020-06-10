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
        val resourceDefRefOpt = toResourceDefRef(TestResource::class)
        assertTrue(resourceDefRefOpt is Some<ResourceDefRef>)
        assertEquals(ModelTestUtils.RESOURCE_DEF_REF, resourceDefRefOpt.t)
    }

    @Test
    fun shouldNotProduceResourceDefRef() {
        val resourceDefRefOpt = toResourceDefRef(MyResource::class)
        assertTrue(resourceDefRefOpt is None)
    }

    @Test
    fun shouldProduceResourceDescription() {
        val resourceDescriptionOpt = toResourceDescription(TestResource::class)
        assertTrue(resourceDescriptionOpt is Some<String>)
        assertEquals(ModelTestUtils.RESOURCE_DESCRIPTION, resourceDescriptionOpt.t)
    }

    @Test
    fun shouldNotProduceResourceDescription() {
        val resourceDescriptionOpt = toResourceDescription(MyResource::class)
        assertTrue(resourceDescriptionOpt is None)
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
        val resourceDefOpt = toResourceDef(TestResource::class)
        assertTrue(resourceDefOpt is Some<ResourceDef>)
        assertEquals(ModelTestUtils.RESOURCE_DEF, resourceDefOpt.t)
    }

    @Test
    fun shouldNotProduceResourceDef() {
        val resourceDefOpt = toResourceDef(MyResource::class)
        assertTrue(resourceDefOpt is None)
    }

    private data class MyResource(val id: String)
}
