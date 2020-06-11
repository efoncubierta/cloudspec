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
 * Define an association definition.
 *
 * Associations must be defined so resources integrity can be validated.
 */
data class AssociationDef(
        /**
         * Association name.
         */
        override val name: String,

        /**
         * Association description.
         */
        override val description: String,

        /**
         * Resource definition.
         */
        val defRef: ResourceDefRef,

        /**
         * Indicates whether the association is to-many.
         */
        val isMany: Boolean
) : MemberDef
