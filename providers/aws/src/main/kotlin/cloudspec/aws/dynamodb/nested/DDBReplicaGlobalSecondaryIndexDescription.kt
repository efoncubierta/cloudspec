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
package cloudspec.aws.dynamodb.nested

import cloudspec.annotation.PropertyDefinition

data class DDBReplicaGlobalSecondaryIndexDescription(
        @property:PropertyDefinition(
                name = PROP_INDEX_NAME,
                description = PROP_INDEX_NAME_D
        )
        val indexName: String?,

        @property:PropertyDefinition(
                name = PROP_PROVISIONED_THROUGHPUT_OVERRIDE,
                description = PROP_PROVISIONED_THROUGHPUT_OVERRIDE_D
        )
        val provisionedThroughputOverride: DDBProvisionedThroughputOverride?
) {
    companion object {
        const val PROP_INDEX_NAME = "index_name"
        const val PROP_INDEX_NAME_D = "The name of the global secondary index"
        const val PROP_PROVISIONED_THROUGHPUT_OVERRIDE = "provisioned_throughput_override"
        const val PROP_PROVISIONED_THROUGHPUT_OVERRIDE_D = "If not described, uses the source table GSI's read capacity settings"
    }
}
