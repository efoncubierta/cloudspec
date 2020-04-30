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
package cloudspec.service;

import cloudspec.ProvidersRegistry;
import cloudspec.model.Resource;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceFqn;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service class to manage resources.
 * <p>
 * This service class imports all resource definitions from the providers registry.
 */
public class ResourceService {
    private final ProvidersRegistry providersRegistry;

    /**
     * Constructor.
     *
     * @param providersRegistry Provider's registry.
     */
    public ResourceService(ProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    /**
     * Get a resource definition of this provider.
     *
     * @param resourceFqn Resource fully-qualified name.
     * @return Optional resource definition.
     */
    public Optional<ResourceDef> getResourceDef(ResourceFqn resourceFqn) {
        return providersRegistry.getProvider(resourceFqn.getProviderName())
                .flatMap(provider -> provider.getResourceDef(resourceFqn));
    }

    /**
     * Get resources of a particular type.
     *
     * @param resourceFqn Resource FQN.
     * @return List of resources.
     */
    public List<Resource> getResources(ResourceFqn resourceFqn) {
        return providersRegistry.getProvider(resourceFqn.getProviderName())
                .map(provider -> provider.getResources(resourceFqn))
                .orElse(Collections.emptyList());
    }
}
