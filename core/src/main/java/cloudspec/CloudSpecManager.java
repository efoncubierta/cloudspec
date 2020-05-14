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
package cloudspec;

import cloudspec.lang.CloudSpec;
import cloudspec.loader.ResourceLoader;
import cloudspec.preflight.CloudSpecPreflight;
import cloudspec.store.ResourceDefStore;
import cloudspec.store.ResourceStore;
import cloudspec.validator.CloudSpecValidator;
import cloudspec.validator.CloudSpecValidatorResult;
import cloudspec.validator.ResourceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloudSpecManager {
    private final Logger LOGGER = LoggerFactory.getLogger(CloudSpecManager.class);

    private final ProvidersRegistry providersRegistry;
    private final ResourceDefStore resourceDefStore;
    private final ResourceStore resourceStore;
    private final ResourceValidator resourceValidator;

    private final ResourceLoader resourceLoader;
    private final CloudSpecPreflight cloudSpecPreflight;
    private final CloudSpecValidator cloudSpecValidator;

    private Boolean initiated = Boolean.FALSE;

    public CloudSpecManager(ProvidersRegistry providersRegistry,
                            ResourceDefStore resourceDefStore,
                            ResourceStore resourceStore,
                            ResourceValidator resourceValidator) {
        this.providersRegistry = providersRegistry;
        this.resourceDefStore = resourceDefStore;
        this.resourceStore = resourceStore;
        this.resourceValidator = resourceValidator;

        this.resourceLoader = new ResourceLoader(providersRegistry, resourceDefStore, resourceStore);
        this.cloudSpecPreflight = new CloudSpecPreflight(resourceDefStore);
        this.cloudSpecValidator = new CloudSpecValidator(resourceValidator);
    }

    public void init() {
        if (initiated) {
            LOGGER.error("CloudSpec already initiated");
            return;
        }

        LOGGER.info("Initiating CloudSpec");

        providersRegistry.getProviders()
                .stream()
                .peek(provider -> {
                    LOGGER.debug("Importing provider '{}'", provider.getName());
                    LOGGER.debug("- Found {} resource definitions", provider.getResourceDefs().size());
                })
                .flatMap(provider -> provider.getResourceDefs().stream())
                .forEach(resourceDefStore::createResourceDef);

        initiated = Boolean.TRUE;
    }

    public void preflight(CloudSpec spec) {
        mustBeInitiated();

        cloudSpecPreflight.preflight(spec);
    }

    public void loadResources(CloudSpec spec) {
        mustBeInitiated();

        resourceLoader.load(spec);
    }

    public CloudSpecValidatorResult validate(CloudSpec spec) {
        mustBeInitiated();

        return cloudSpecValidator.validate(spec);
    }

    private void mustBeInitiated() {
        if (!initiated) {
            throw new RuntimeException("CloudSpec is not initiated");
        }
    }
}
