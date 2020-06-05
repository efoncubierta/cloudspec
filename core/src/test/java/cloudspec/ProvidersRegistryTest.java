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
package cloudspec;

import cloudspec.model.Provider;
import cloudspec.model.ProviderTest;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProvidersRegistryTest {
    @Test
    public void shouldRegisterProvider() {
        ProvidersRegistry registry = new ProvidersRegistry();

        registry.register(ProviderTest.TEST_PROVIDER);

        Optional<Provider> providerOpt = registry.getProvider(ModelTestUtils.PROVIDER_NAME);
        assertTrue(providerOpt.isPresent());
        assertEquals(ModelTestUtils.PROVIDER_NAME, providerOpt.get().getName());

        List<Provider> providers = registry.getProviders();
        assertEquals(1, providers.size());
        assertEquals(ModelTestUtils.PROVIDER_NAME, providers.get(0).getName());
    }
}
