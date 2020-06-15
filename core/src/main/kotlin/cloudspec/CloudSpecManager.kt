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

import cloudspec.lang.CloudSpec
import cloudspec.loader.ResourceLoader
import cloudspec.preflight.CloudSpecPreflight
import cloudspec.store.ResourceDefStore
import cloudspec.store.ResourceStore
import cloudspec.validator.CloudSpecValidator
import cloudspec.validator.CloudSpecValidatorResult
import cloudspec.validator.ResourceValidator
import org.slf4j.LoggerFactory

class CloudSpecManager(
        private val providersRegistry: ProvidersRegistry,
        private val resourceDefStore: ResourceDefStore,
        resourceStore: ResourceStore,
        resourceValidator: ResourceValidator
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val resourceLoader: ResourceLoader = ResourceLoader(providersRegistry, resourceStore)
    private val cloudSpecPreflight: CloudSpecPreflight = CloudSpecPreflight(providersRegistry, resourceDefStore)
    private val cloudSpecValidator: CloudSpecValidator = CloudSpecValidator(resourceValidator)
    private var initiated = false

    fun init() {
        if (initiated) {
            logger.error("CloudSpec already initiated")
            return
        }

        logger.info("Initiating CloudSpec")

        providersRegistry.getProviders()
                .onEach {
                    logger.debug("Importing provider '{}'", it.name)
                    logger.debug("- Found {} resource definitions", it.resourceDefs.size)
                }
                .flatMap { it.resourceDefs }
                .forEach { resourceDefStore.saveResourceDef(it) }

        initiated = true
    }

    fun preflight(spec: CloudSpec) {
        mustBeInitiated()
        cloudSpecPreflight.preflight(spec)
    }

    private fun mustBeInitiated() {
        if (!initiated) {
            throw RuntimeException("CloudSpec is not initiated")
        }
    }

    fun loadResources(spec: CloudSpec) {
        mustBeInitiated()
        resourceLoader.load(spec)
    }

    fun validate(spec: CloudSpec): CloudSpecValidatorResult {
        mustBeInitiated()
        return cloudSpecValidator.validate(spec)
    }

}
