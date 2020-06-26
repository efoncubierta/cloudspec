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

import arrow.core.Option
import arrow.core.firstOrNone

typealias SetValues = List<SetValue<*>>

fun SetValues.getNumber(ref: ConfigRef): Option<Number> {
    return this.getNumbers(ref).firstOrNone()
}

fun SetValues.getNumbers(ref: ConfigRef): List<Number> {
    return this.filter { it.ref == ref }
        .filterIsInstance<NumberSetValue>()
        .map { it.value }
}

fun SetValues.getString(ref: ConfigRef): Option<String> {
    return this.getStrings(ref).firstOrNone()
}

fun SetValues.getStrings(ref: ConfigRef): List<String> {
    return this.filter { it.ref == ref }
        .filterIsInstance<StringSetValue>()
        .map { it.value }
}

fun SetValues.getBoolean(ref: ConfigRef): Option<Boolean> {
    return this.getBooleans(ref).firstOrNone()
}

fun SetValues.getBooleans(ref: ConfigRef): List<Boolean> {
    return this.filter { it.ref == ref }
        .filterIsInstance<BooleanSetValue>()
        .map { it.value }
}
