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
import cloudspec.util.ProviderDataUtil
import cloudspec.util.TestProvider
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProviderTest {
    @Test
    fun shouldBuildProviderFromAnnotation() {
        assertNotNull(TEST_PROVIDER.name)
        assertEquals(ProviderDataUtil.PROVIDER_NAME, TEST_PROVIDER.name)
        assertNotNull(TEST_PROVIDER.description)
        assertEquals(ProviderDataUtil.PROVIDER_DESCRIPTION, TEST_PROVIDER.description)
        assertNotNull(TEST_PROVIDER.resourceDefs)
        assertEquals(1, TEST_PROVIDER.resourceDefs.size)
        assertEquals(ModelTestUtils.RESOURCE_DEF, TEST_PROVIDER.resourceDefs[0])
    }

    companion object {
        val TEST_PROVIDER: Provider = TestProvider()
    }
}
