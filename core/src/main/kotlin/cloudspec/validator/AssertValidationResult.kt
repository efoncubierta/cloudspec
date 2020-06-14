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

sealed class Path {
    fun toPathString(): String {
        return when (this) {
            is PropertyPath -> ".${this.name}"
            is NestedPropertyPath -> ".${this.name}"
            is KeyValuePropertyPath -> ".${this.name}[${this.key}]"
            is AssociationPath -> when {
                this.id.isNullOrEmpty() -> ">${this.name}"
                else -> ">${this.name}[${this.id}]"
            }
        }
    }
}

fun List<Path>.toPathString(): String {
    return when {
        this.isNotEmpty() -> "${this[0].toPathString()}${this.drop(1).toPathString()}"
        else -> ""
    }
}

data class PropertyPath(val name: String) : Path()
data class NestedPropertyPath(val name: String) : Path()
data class KeyValuePropertyPath(val name: String, val key: String) : Path()
data class AssociationPath(val name: String, val id: String? = null) : Path()

data class AssertValidationResult(
        val path: List<Path>,
        val success: Boolean,
        val error: AssertError? = null
)
