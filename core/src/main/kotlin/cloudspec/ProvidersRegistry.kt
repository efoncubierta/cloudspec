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

import arrow.core.Option
import arrow.core.firstOrNone
import cloudspec.model.Provider
import cloudspec.model.ResourceDefRef

class ProvidersRegistry {
    private val providers = mutableListOf<Provider>()

    fun getProvider(resourceDefRef: ResourceDefRef): Option<Provider> {
        return getProvider(resourceDefRef.providerName)
    }

    fun getProvider(providerName: String): Option<Provider> {
        return getProviders().firstOrNone { it.name == providerName }
    }

    fun getProviders(): List<Provider> {
        return providers
    }

    fun register(provider: Provider) {
        // filter out existing provider
        providers.removeIf { provider.name == it.name }

        // register new provider
        providers.add(provider)
    }
}
