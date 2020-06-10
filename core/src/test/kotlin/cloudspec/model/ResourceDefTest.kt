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
import cloudspec.util.ModelGenerator.randomName
import cloudspec.util.ModelTestUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ResourceDefTest {
    @Test
    fun shouldNotGetPropertyByPath() {
        val path = listOf(ModelTestUtils.PROP_NESTED_NAME, randomName())
        val propertyDefOpt = ModelTestUtils.RESOURCE_DEF.propertyByPath(path)
        assertTrue(propertyDefOpt is None)
    }

    @Test
    fun shouldGetPropertyByPath() {
        val path = listOf(ModelTestUtils.PROP_NESTED_NAME, ModelTestUtils.PROP_NUMBER_NAME)
        val propertyDefOpt = ModelTestUtils.RESOURCE_DEF.propertyByPath(path)
        assertTrue(propertyDefOpt is Some<PropertyDef>)
        assertEquals(ModelTestUtils.PROP_NUMBER_DEF, propertyDefOpt.t)
    }
}
