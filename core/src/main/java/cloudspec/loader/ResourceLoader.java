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
package cloudspec.loader;

import cloudspec.ProvidersRegistry;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceFqn;
import cloudspec.model.ResourceReflectionUtil;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.stream.Stream;

public class ResourceLoader {
    private final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

    private final ProvidersRegistry providersRegistry;
    private final ResourceDefStore resourceDefStore;
    private final ResourceStore resourceStore;

    public ResourceLoader(ProvidersRegistry providersRegistry,
                          ResourceDefStore resourceDefStore,
                          ResourceStore resourceStore) {
        this.providersRegistry = providersRegistry;
        this.resourceDefStore = resourceDefStore;
        this.resourceStore = resourceStore;
    }

    public void load() {
        resourceDefStore.getResourceDefs()
                .stream()
                .map(ResourceDef::getResourceFqn)
                .forEach(this::loadResourcesByType);
    }

    private void loadResourcesByType(ResourceFqn resourceFqn) {
        LOGGER.debug("Loading resources of type '{}'", resourceFqn);

        providersRegistry.getProvider(resourceFqn.getProviderName())
                .map(provider -> provider.getResources(resourceFqn).stream())
                .orElse(Stream.empty())
                .map(ResourceReflectionUtil::toResource)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(resourceStore::addResource);
    }
}
