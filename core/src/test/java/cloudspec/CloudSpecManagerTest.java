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

import cloudspec.model.ProviderTest;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import cloudspec.util.TestProvider;
import cloudspec.validator.ResourceValidator;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CloudSpecManagerTest {
    public static final ProvidersRegistry PROVIDERS_REGISTRY = mock(ProvidersRegistry.class);
    public static final ResourceDefStore RESOURCE_DEF_STORE = mock(ResourceDefStore.class);
    public static final ResourceStore RESOURCE_STORE = mock(ResourceStore.class);
    public static final ResourceValidator RESOURCE_VALIDATOR = mock(ResourceValidator.class);

    static {
        when(PROVIDERS_REGISTRY.getProviders()).thenReturn(Collections.singletonList(new TestProvider()));
    }

    @Test
    public void shouldInit() {
        ProvidersRegistry registry = new ProvidersRegistry();

        registry.register(ProviderTest.TEST_PROVIDER);

        new CloudSpecManager(registry, RESOURCE_DEF_STORE, RESOURCE_STORE, RESOURCE_VALIDATOR);

        // TODO add assertions
    }
}
