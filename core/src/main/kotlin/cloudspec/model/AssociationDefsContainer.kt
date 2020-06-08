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

/**
 * Interface for classes that manage associations definitions.
 */
interface AssociationDefsContainer {
    /**
     * List of association definitions.
     */
    val associations: AssociationDefs

    /**
     * Get an association definition by name.
     *
     * @param name Association name.
     * @return Association definition or null.
     */
    fun associationByName(name: String): AssociationDef? {
        return associations.firstOrNull { it.name == name }
    }
}
