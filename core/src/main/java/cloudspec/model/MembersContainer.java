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
 * Interface for classes that manage properties and associations.
 */
public interface MembersContainer extends PropertiesContainer, AssociationsContainer {
    /**
     * Get a property by its path.
     *
     * @param path Property path
     * @return Optional property.
     */
    default Optional<Property<?>> getPropertyByPath(List<String> path) {
        if (path.size() == 1) {
            return getProperty(path.get(0));
        } else if (path.size() > 1) {
            return getProperty(path.get(0))
                    .flatMap(property -> property.getPropertyByPath(path.subList(1, path.size())));
        }

        return Optional.empty();
    }

    /**
     * Get an association by its path.
     *
     * @param path Association path
     * @return Optional association.
     */
    default Optional<Association> getAssociationByPath(List<String> path) {
        if (path.size() == 1) {
            return getAssociation(path.get(0));
        } else if (path.size() > 1) {
            return getProperty(path.get(0))
                    .flatMap(property -> property.getAssociationByPath(path.subList(1, path.size())));
        }

        return Optional.empty();
    }
}
