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
package cloudspec.model;

import java.util.List;
import java.util.Optional;

/**
 * Define a CloudSpec's provider.
 * <p>
 * A provider provides CloudSpec with resource definitions and resource loaders. Providers must implement
 * this class, that will be instantiated into the {@link cloudspec.ProvidersRegistry}.
 * <p>
 * Note to future: providers will also provide new expressions to CloudSpec.
 */
public interface Provider {
    /**
     * Get provider name.
     *
     * @return Provider name.
     */
    String getName();

    /**
     * Get provider description.
     *
     * @return Provider description.
     */
    String getDescription();

    /**
     * Get a resource definition by its reference.
     *
     * @param resourceDefRef Resource definition reference.
     * @return Optional resource definition.
     */
    default Optional<ResourceDef> getResourceDef(ResourceDefRef resourceDefRef) {
        return getResourceDefs()
                .stream()
                .filter(resourceDef -> resourceDef.getRef().equals(resourceDefRef))
                .findFirst();
    }

    /**
     * Get all resource definitions the provider provides.
     *
     * @return List of resource definitions.
     */
    List<ResourceDef> getResourceDefs();

    /**
     * Get all resources of a particular resource definition.
     *
     * @param resourceDefRef Resource definition reference.
     * @return List of resources.
     */
    List<?> getResources(ResourceDefRef resourceDefRef);

    /**
     * Get a resource by resource definition and id.
     *
     * @param resourceDefRef Resource definition reference.
     * @param resourceId     Resource id.
     * @return Optional resource.
     */
    Optional<?> getResource(ResourceDefRef resourceDefRef, String resourceId);
}
