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
import arrow.core.none
import arrow.core.orElse
import arrow.core.toOption

/**
 * Interface for classes that manage properties and associations.
 */
interface MembersContainer : PropertiesContainer, AssociationsContainer {
    /**
     * Get a property by its path.
     *
     * @param path Property path
     * @return Optional property.
     */
    fun propertyByPath(path: List<String>): Option<Property<*>> {
        return getProperty(path[0]).flatMap { property ->
            if (path.size > 1 && property is NestedProperty) {
                return@flatMap property.value.toOption().flatMap {
                    it.propertyByPath(path.subList(1, path.size))
                }
            }
            property.toOption()
        }
    }

    /**
     * Get an association by its path.
     *
     * @param path Association path
     * @return Optional association.
     */
    fun getAssociationByPath(path: List<String>): Option<Association> {
        return getProperty(path[0]).flatMap { property ->
            if (path.size > 1 && property is NestedProperty) {
                return@flatMap property.value.toOption().flatMap {
                    it.getAssociationByPath(path.subList(1, path.size))
                }
            }
            none<Association>()
        }.orElse { associationByName(path[0]) }
    }
}
