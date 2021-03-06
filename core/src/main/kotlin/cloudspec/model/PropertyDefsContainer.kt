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
import arrow.core.firstOrNone

/**
 * Interface for classes that manage property definitions.
 */
interface PropertyDefsContainer {
    /**
     * List of property definitions.
     */
    val properties: PropertyDefs

    /**
     * Get a property definition by name.
     *
     * @param name Property name.
     * @return Property definition or null.
     */
    fun propertyByName(name: String): Option<PropertyDef> {
        return properties.firstOrNone { it.name == name }
    }
}
