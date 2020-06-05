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
package cloudspec.util;

import cloudspec.annotation.ProviderDefinition;
import cloudspec.model.BaseProvider;
import cloudspec.model.ResourceDefRef;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ProviderDefinition(
        name = ProviderDataUtil.PROVIDER_NAME,
        description = ProviderDataUtil.PROVIDER_DESCRIPTION,
        resources = {TestResource.class}
)
public class TestProvider extends BaseProvider {
    @Override
    public List<Object> getResources(ResourceDefRef resourceDefRef) {
        return Collections.emptyList();
    }

    @Override
    public Optional<?> getResource(ResourceDefRef resourceDefRef, String resourceId) {
        return Optional.empty();
    }
}
