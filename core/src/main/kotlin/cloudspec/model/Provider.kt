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

import arrow.core.Option
import arrow.fx.IO

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
    abstract val name: String

    /**
     * Provider description.
     */
    abstract val description: String

    /**
     * Get all group definitions the provider provides.
     *
     * @return list of group definitions.
     */
    abstract val groupDefs: GroupDefs

    /**
     * Get all resource definitions the provider provides.
     *
     * @return List of resource definitions.
     */
    abstract val resourceDefs: ResourceDefs

    /**
     *  Get all configuration definitions the provider provides.
     *
     * @return List of configuration definitions.
     */
    abstract val configDefs: ConfigDefs

    /**
     * Get all resources of a particular resource definition.
     *
     * @param sets CloudSpec config.
     * @param defRef Resource definition reference.
     * @return List of resources.
     */
    abstract fun resourcesByDef(sets: SetValues, defRef: ResourceDefRef): IO<List<Resource>>

    /**
     * Get a resource by resource definition and id.
     *
     * @param sets CloudSpec config.
     * @param ref Resource reference.
     * @return Optional resource.
     */
    abstract fun resource(sets: SetValues, ref: ResourceRef): IO<Option<Resource>>
}
