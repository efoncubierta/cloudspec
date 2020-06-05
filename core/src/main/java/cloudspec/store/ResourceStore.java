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
package cloudspec.store;

import cloudspec.model.*;

import java.util.List;
import java.util.Optional;

public interface ResourceStore {
    void saveResource(ResourceDefRef resourceDefRef, String resourceId);

    void saveResource(ResourceDefRef resourceDefRef, String resourceId,
                      Properties properties, Associations associations);

    Boolean exists(ResourceDefRef resourceDefRef, String resourceId);

    Optional<Resource> getResource(ResourceDefRef resourceDefRef, String resourceId);

    List<Resource> getResourcesByDefinition(ResourceDefRef resourceDefRef);

    Optional<Properties> getProperties(ResourceDefRef resourceDefRef, String resourceId);

    void saveProperty(ResourceDefRef resourceDefRef, String resourceId, Property<?> property);

    void saveProperties(ResourceDefRef resourceDefRef, String resourceId, Properties properties);

    Optional<Associations> getAssociations(ResourceDefRef resourceDefRef, String resourceId);

    void saveAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association);

    void saveAssociations(ResourceDefRef resourceDefRef, String resourceId, Associations associations);
}
