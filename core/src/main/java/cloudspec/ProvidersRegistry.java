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
import cloudspec.model.ResourceDefRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProvidersRegistry {
    private final List<Provider> providers = new ArrayList<>();

    public Optional<Provider> getProvider(ResourceDefRef resourceDefRef) {
        return getProvider(resourceDefRef.getProviderName());
    }

    public Optional<Provider> getProvider(String providerName) {
        return getProviders().stream().filter(provider -> provider.getName().equals(providerName)).findFirst();
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void register(Provider provider) {
        // filter out existing provider
        providers.removeIf(p -> provider.getName().equals(p.getName()));

        // register new provider
        providers.add(provider);
    }
}
