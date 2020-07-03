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
 * Define a set value.
 */
sealed class SetValue<T> {
    /**
     * Config reference.
     */
    abstract val ref: ConfigRef

    /**
     * Value.
     */
    abstract val value: T
}

data class NumberSetValue(override val ref: ConfigRef,
                          override val value: Number) : SetValue<Number>()

data class NumberArraySetValue(override val ref: ConfigRef,
                               override val value: List<Number>) : SetValue<List<Number>>()

data class StringSetValue(override val ref: ConfigRef,
                          override val value: String) : SetValue<String>()

data class StringArraySetValue(override val ref: ConfigRef,
                               override val value: List<String>) : SetValue<List<String>>()

data class BooleanSetValue(override val ref: ConfigRef,
                           override val value: Boolean) : SetValue<Boolean>()

data class BooleanArraySetValue(override val ref: ConfigRef,
                                override val value: List<Boolean>) : SetValue<List<Boolean>>()
