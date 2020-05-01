/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.model;

import cloudspec.annotation.ProviderDefinition;
import cloudspec.annotation.ResourceDefinition;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Define a CloudSpec's provider.
 * <p>
 * A provider provides CloudSpec with resource definitions and resource loaders. Providers must implement
 * this class, that will be instantiated into the {@link cloudspec.ProvidersRegistry}.
 * <p>
 * Note to future: providers will also provide new expressions and functions to CloudSpec.
 */
public abstract class BaseProvider implements Provider {
    private final String name;
    private final String description;
    private final List<ResourceDef> resourceDefs;

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
                .map(ResourceReflectionUtil::toResourceDef)
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