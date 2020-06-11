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
 * Define the reference of a resource.
 */
data class ResourceRef(
        val defRef: ResourceDefRef,
        val id: String
) {
    override fun toString(): String {
        return "${defRef.providerName}:${defRef.groupName}:${defRef.resourceName}:$id"
    }

    companion object {
        /**
         * Build a resource reference object from string.
         *
         * @param refString Resource reference as string.
         * @return Optional resource reference.
         */
        fun fromString(refString: String): Option<ResourceRef> {
            // TODO manage null or malformed strings
            val parts = refString.split(":".toRegex())

            return if (parts.size != 4) {
                none()
            } else {
                ResourceRef(ResourceDefRef(parts[0], parts[1], parts[2]), parts[3]).toOption()
            }
        }
    }
}
