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
package cloudspec.validator

import arrow.core.Option
import cloudspec.lang.Statement
import cloudspec.model.ResourceDefRef
import cloudspec.model.ResourceRef

/**
 * Define a resource validator.
 */
interface ResourceValidator {
    /**
     * Check whether a resource exists by its id.
     *
     * @param ref Resource definition reference.
     * @param id  Resource id.
     * @return True if resource exists. False otherwise.
     */
    fun exist(ref: ResourceRef): Boolean

    /**
     * Check whether any resource exist.
     *
     * @param defRef   Resource definition reference.
     * @param statements List of statements for filtering.
     * @return True if at least one resource exists. False otherwise.
     */
    fun existAny(defRef: ResourceDefRef, statements: List<Statement>): Boolean

    /**
     * Validate an individual resource.
     *
     * @param ref   Resource reference.
     * @param filterStatements List of statements for filtering.
     * @param assertStatements List of statements for asserting.
     * @return Resource validator result or null.
     */
    fun validate(ref: ResourceRef,
                 filterStatements: List<Statement>,
                 assertStatements: List<Statement>): Option<ResourceValidationResult>

    /**
     * Validate all resources of a kind.
     *
     * @param defRef   Resource definition reference.
     * @param filterStatements List of statements for filtering.
     * @param assertStatements List of statements for asserting.
     * @return List of resource validator results.
     */
    fun validateAll(defRef: ResourceDefRef,
                    filterStatements: List<Statement>,
                    assertStatements: List<Statement>): List<ResourceValidationResult>
}
