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

import java.time.Instant

/**
 * Define a resource property.
 */
sealed class Property<T> : Member {
    abstract override val name: String
    abstract val value: T?
}

data class NumberProperty(
        override val name: String,
        override val value: Number?
) : Property<Number>() {}

data class StringProperty(
        override val name: String,
        override val value: String?
) : Property<String>()

data class BooleanProperty(
        override val name: String,
        override val value: Boolean?
) : Property<Boolean>()

data class KeyValueProperty(
        override val name: String,
        override val value: KeyValue?
) : Property<KeyValue>()

data class InstantProperty(
        override val name: String,
        override val value: Instant?
) : Property<Instant>()

data class NestedProperty(
        override val name: String,
        override val value: NestedPropertyValue?
) : Property<NestedPropertyValue>()
