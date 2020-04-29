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
import cloudspec.model.Provider;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceFqn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service class to manage resources.
 * <p>
 * This service class imports all resource definitions from the providers registry.
 */
public class ResourceService {
    private final Map<ResourceFqn, ResourceDef> resourceDefMap = new HashMap<>();

    /**
     * Constructor.
     *
     * @param providersRegistry Provider's registry.
     */
    public ResourceService(ProvidersRegistry providersRegistry) {
        providersRegistry.getProviders().forEach(this::importProvider);
    }

    /**
     * Import provider.
     *
     * @param provider Provider.
     */
    private void importProvider(Provider provider) {
        provider.getResourceDefs().forEach(this::importResourceDef);
    }

    /**
     * Import resource definition.
     *
     * @param resourceDef Resource definition.
     */
    private void importResourceDef(ResourceDef resourceDef) {
        resourceDefMap.put(resourceDef.getFqn(), resourceDef);
    }

    /**
     * Get a resource definition of this provider.
     *
     * @param resourceFqn Resource fully-qualified name.
     * @return Optional resource definition.
     */
    public Optional<ResourceDef> getResourceDef(ResourceFqn resourceFqn) {
        return Optional.ofNullable(resourceDefMap.get(resourceFqn));
    }
}
