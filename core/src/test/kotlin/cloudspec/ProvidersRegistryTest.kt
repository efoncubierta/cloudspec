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
package cloudspec

import cloudspec.model.ProviderTest
import cloudspec.util.ModelTestUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProvidersRegistryTest {
    @Test
    fun shouldRegisterProvider() {
        val registry = ProvidersRegistry()

        registry.register(ProviderTest.TEST_PROVIDER)
        val provider = registry.getProvider(ModelTestUtils.PROVIDER_NAME)
        assertNotNull(provider)
        assertEquals(ModelTestUtils.PROVIDER_NAME, provider.name)
        val providers = registry.getProviders()
        assertEquals(1, providers.size)
        assertEquals(ModelTestUtils.PROVIDER_NAME, providers[0].name)
    }
}
