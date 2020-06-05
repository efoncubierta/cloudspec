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
package cloudspec.model;

import java.util.List;
import java.util.Optional;

/**
 * Interface for classes that manage associations definitions.
 */
public interface AssociationDefsContainer {
    /**
     * Get an association definition by name.
     *
     * @param associationName Association name.
     * @return Optional association definition.
     */
    default Optional<AssociationDef> getAssociation(String associationName) {
        return getAssociations().stream().filter(def -> def.getName().equals(associationName)).findFirst();
    }

    /**
     * Get list of association definitions.
     *
     * @return List of association definitions.
     */
    List<AssociationDef> getAssociations();
}
