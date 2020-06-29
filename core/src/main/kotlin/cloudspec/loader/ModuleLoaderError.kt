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
package cloudspec.loader

sealed class ModuleLoaderError {
    abstract fun message(): String;

    data class ModuleDirectoryNotFound(val modulePath: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Module directory not found: $modulePath"
        }
    }

    data class ModuleDirectoryAccessDenied(val modulePath: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Not enough permissions to read from module directory: $modulePath"
        }
    }

    data class ModuleCyclicUseFound(val modulePath: String, val modulePathStack: List<String>) : ModuleLoaderError() {
        override fun message(): String {
            return "Cyclic use of modules found: ${modulePathStack.plus(modulePath).joinToString(" use ") }"
        }
    }

    data class SyntaxError(val message: String) : ModuleLoaderError() {
        override fun message(): String {
            return "Syntax error: $message"
        }
    }
}
