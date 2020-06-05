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
package cloudspec.validator;

import cloudspec.lang.Statement;
import cloudspec.model.ResourceDefRef;

import java.util.List;
import java.util.Optional;

/**
 * Define a resource validator.
 */
public interface ResourceValidator {
    /**
     * Check whether a resource exists by its id.
     *
     * @param resourceDefRef Resource definition reference.
     * @param resourceId     Resource id.
     * @return True if resource exists. False otherwise.
     */
    Boolean existById(ResourceDefRef resourceDefRef, String resourceId);

    /**
     * Check whether any resource exist.
     *
     * @param resourceDefRef   Resource definition reference.
     * @param filterStatements List of statements for filtering.
     * @return True if at least one resource exists. False otherwise.
     */
    Boolean existAny(ResourceDefRef resourceDefRef, List<Statement> filterStatements);

    /**
     * Validate an individual resource.
     *
     * @param resourceDefRef   Resource definition reference.
     * @param resourceId       Resource id.
     * @param filterStatements List of statements for filtering.
     * @param assertStatements List of statements for asserting.
     * @return Resource validator result.
     */
    Optional<ResourceValidationResult> validateById(ResourceDefRef resourceDefRef,
                                                    String resourceId,
                                                    List<Statement> filterStatements,
                                                    List<Statement> assertStatements);

    /**
     * Validate all resources of a kind.
     *
     * @param resourceDefRef   Resource definition reference.
     * @param filterStatements List of statements for filtering.
     * @param assertStatements List of statements for asserting.
     * @return List of resource validator results.
     */
    List<ResourceValidationResult> validateAll(ResourceDefRef resourceDefRef,
                                               List<Statement> filterStatements,
                                               List<Statement> assertStatements);
}
