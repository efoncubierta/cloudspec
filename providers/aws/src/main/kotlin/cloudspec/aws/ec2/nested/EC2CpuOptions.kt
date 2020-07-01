/*-
 * #%L
 * CloudSpec AWS Provider
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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2CpuOptions(
        @property:PropertyDefinition(
                name = PROP_CORE_COUNT,
                description = PROP_CORE_COUNT_D
        )
        val coreCount: Int?,

        @property:PropertyDefinition(
                name = PROP_THREADS_PER_CORE,
                description = PROP_THREADS_PER_CORE_D
        )
        val threadsPerCore: Int?
) {
    companion object {
        const val PROP_CORE_COUNT = "core_count"
        const val PROP_CORE_COUNT_D = "The number of CPU cores for the instance"
        const val PROP_THREADS_PER_CORE = "threads_per_core"
        const val PROP_THREADS_PER_CORE_D = "The number of threads per CPU core"
    }
}
