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
 * Define the reference of a resource definition.
 */
data class ResourceDefRef(
        val providerName: String,
        val groupName: String,
        val resourceName: String
) {
    override fun toString(): String {
        return "$providerName:$groupName:$resourceName"
    }

    companion object {
        /**
         * Build a resource definition reference object from string.
         *
         * @param refString Resource definition reference as string.
         * @return Optional resource definition reference.
         */
        fun fromString(refString: String): ResourceDefRef? {
            // TODO manage null or malformed strings
            val parts = refString.split(":".toRegex())

            return if (parts.size != 3) {
                null
            } else {
                ResourceDefRef(parts[0], parts[1], parts[2])
            }
        }
    }
}
