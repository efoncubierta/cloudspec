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

import cloudspec.annotation.ProviderDefinition;
import cloudspec.annotation.ResourceDefReflectionUtil;
import cloudspec.annotation.ResourceDefinition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for classes implementing {@link Provider}.
 */
public abstract class BaseProvider implements Provider {
    private final String name;
    private final String description;
    private final List<ResourceDef> resourceDefs;

    /**
     * Constructor.
     * <p>
     * When a instance of a class implementing this class is constructed, all
     * information related to the provider contained in the annotations are extracted.
     */
    public BaseProvider() {
        // check the class is annotated
        if (!this.getClass().isAnnotationPresent(ProviderDefinition.class)) {
            throw new RuntimeException(
                    String.format("Provider class %s is not annotated with @ProviderDefinition", this.getClass().getCanonicalName())
            );
        }

        ProviderDefinition providerDefinition = this.getClass().getAnnotation(ProviderDefinition.class);
        this.name = providerDefinition.name();
        this.description = providerDefinition.description();

        this.resourceDefs = Stream.of(providerDefinition.resources())
                .filter(resourceClass -> resourceClass.isAnnotationPresent(ResourceDefinition.class))
                .map(ResourceDefReflectionUtil::toResourceDef)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ResourceDef> getResourceDefs() {
        return resourceDefs;
    }
}
