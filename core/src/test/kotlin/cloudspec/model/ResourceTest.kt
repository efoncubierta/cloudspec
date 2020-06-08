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

import cloudspec.util.ModelTestUtils
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ResourceTest {
    @Test
    fun shouldGetPropertyByPath() {
        val path = Stack<String>()
        path.add(ModelTestUtils.PROP_NESTED_NAME)
        path.add(ModelTestUtils.PROP_STRING_NAME)

        val propertyDef = ModelTestUtils.RESOURCE_DEF.propertyByPath(path)
        assertNotNull(propertyDef)
        assertEquals(ModelTestUtils.PROP_STRING_DEF, propertyDef)
    }
}
