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
import arrow.core.none
import arrow.core.toOption

/**
 * Define the reference of a config.
 */
data class ConfigRef(
        val provider: String,
        val name: String
) {
    override fun toString(): String {
        return "${provider}:${name}"
    }

    companion object {
        /**
         * Build a config reference object from string.
         *
         * @param refString Config reference as string.
         * @return Optional config reference.
         */
        fun fromString(refString: String): Option<ConfigRef> {
            // TODO manage null or malformed strings
            val parts = refString.split(":".toRegex())

            return if (parts.size != 2) {
                none()
            } else {
                ConfigRef(parts[0], parts[1]).toOption()
            }
        }
    }
}
