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

        this.resourceLoader = new ResourceLoader(providersRegistry, resourceStore);
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

    private void mustBeInitiated() {
        if (!initiated) {
            throw new RuntimeException("CloudSpec is not initiated");
        }
    }

    public void loadResources(CloudSpec spec) {
        mustBeInitiated();

        resourceLoader.load(spec);
    }

    public CloudSpecValidatorResult validate(CloudSpec spec) {
        mustBeInitiated();

        return cloudSpecValidator.validate(spec);
    }
}
