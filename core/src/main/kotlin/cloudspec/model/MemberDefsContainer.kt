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

/**
 * Interface for classes that manage properties and associations definitions.
 */
interface MemberDefsContainer : PropertyDefsContainer, AssociationDefsContainer {
    /**
     * Get a property by its path.
     *
     * @param path Property path
     * @return Property or null.
     */
    fun propertyByPath(path: List<String>): Option<PropertyDef> {
        val property = propertyByName(path[0])
        if (path.size > 1) {
            return property.flatMap { it.propertyByPath(path.subList(1, path.size)) }
        }
        return property
    }

    /**
     * Get an association by its path.
     *
     * @param path Association path
     * @return Association or null.
     */
    fun associationByPath(path: List<String>): Option<AssociationDef> {
        if (path.size == 1) {
            return associationByName(path[0])
        } else if (path.size > 1) {
            return propertyByName(path[0]).flatMap { it.associationByPath(path.subList(1, path.size)) }
        }
        return none()
    }
}
