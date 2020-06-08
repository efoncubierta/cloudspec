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
 * Define a property definition.
 *
 * Properties must be defined so resources integrity can be validated.
 */
data class PropertyDef (
        /**
         * Property name.
         */
        override val name: String,

        /**
         * Property description.
         */
        override val description: String,

        /**
         * Property type.
         */
        val propertyType: PropertyType,

        /**
         * Indicate whether the property is multi-valued.
         */
        val isMultiValued: Boolean,

        /**
         * Example values, for documentation.
         */
        val exampleValues: String,

        /**
         * Nested properties.
         */
        override val properties: Set<PropertyDef> = emptySet(),

        /**
         * Nested associations.
         */
        override val associations: Set<AssociationDef> = emptySet()
) : MemberDef, MemberDefsContainer
