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
package cloudspec.model

import cloudspec.annotation.ProviderDefinition
import cloudspec.annotation.ResourceDefReflectionUtil
import cloudspec.annotation.ResourceDefinition
import kotlin.reflect.full.findAnnotation

/**
 * Define a CloudSpec's provider.
 *
 * A provider provides CloudSpec with resource definitions and resource loaders. Providers must implement
 * this class, that will be instantiated into the [cloudspec.ProvidersRegistry].
 *
 * Note to future: providers will also provide new expressions to CloudSpec.
 */
abstract class Provider {
    /**
     * Provider name.
     */
    val name: String

    /**
     * Provider description.
     */
    val description: String

    /**
     * Get all resource definitions the provider provides.
     *
     * @return List of resource definitions.
     */
    val resourceDefs: ResourceDefs

    /**
     * Constructor.
     *
     * When a instance of a class implementing this class is constructed, all
     * information related to the provider contained in the annotations are extracted.
     */
    init {
        val providerDefinition = this::class.findAnnotation<ProviderDefinition>()
                ?: throw RuntimeException("Provider class ${this::class.qualifiedName} " +
                                                  "is not annotated with @ProviderDefinition")
        // check the class is annotated

        name = providerDefinition.name
        description = providerDefinition.description
        resourceDefs = providerDefinition.resources
                .filter { it.annotations.any { annotation -> annotation is ResourceDefinition } }
                .mapNotNull { ResourceDefReflectionUtil.toResourceDef(it) }
    }

    /**
     * Get a resource definition by its reference.
     *
     * @param ref Resource definition reference.
     * @return Resource definition or null.
     */
    fun resourceDef(ref: ResourceDefRef): ResourceDef? {
        return resourceDefs.firstOrNull { it.ref == ref }
    }

    /**
     * Get all resources of a particular resource definition.
     *
     * @param ref Resource definition reference.
     * @return List of resources.
     */
    abstract fun resourcesByRef(ref: ResourceDefRef): List<Any>

    /**
     * Get a resource by resource definition and id.
     *
     * @param ref Resource definition reference.
     * @param id  Resource id.
     * @return Resource or null.
     */
    abstract fun resourceById(ref: ResourceDefRef, id: String): Any?
}
