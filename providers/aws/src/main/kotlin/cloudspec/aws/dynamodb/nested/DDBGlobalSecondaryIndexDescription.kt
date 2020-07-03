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
import software.amazon.awssdk.services.dynamodb.model.IndexStatus

data class DDBGlobalSecondaryIndexDescription(
        @property:PropertyDefinition(
                name = PROP_INDEX_NAME,
                description = PROP_INDEX_NAME_D
        )
        val indexName: String?,

        @property:PropertyDefinition(
                name = PROP_KEY_SCHEMA,
                description = PROP_KEY_SCHEMA_D
        )
        val keySchema: List<DDBKeySchemaElement>?,

        @property:PropertyDefinition(
                name = PROP_PROJECTION,
                description = PROP_PROJECTION_D
        )
        val projection: DDBProjection?,

        @property:PropertyDefinition(
                name = PROP_INDEX_STATUS,
                description = PROP_INDEX_STATUS_D
        )
        val indexStatus: IndexStatus?,

        @property:PropertyDefinition(
                name = PROP_BACKFILLING,
                description = PROP_BACKFILLING_D
        )
        val backfilling: Boolean?,

        @property:PropertyDefinition(
                name = PROP_PROVISIONED_THROUGHPUT,
                description = PROP_PROVISIONED_THROUGHPUT_D
        )
        val provisionedThroughput: DDBProvisionedThroughputDescription?,

        @property:PropertyDefinition(
                name = PROP_INDEX_SIZE,
                description = PROP_INDEX_SIZE_D
        )
        val indexSizeBytes: Long?,

        @property:PropertyDefinition(
                name = PROP_ITEM_COUNT,
                description = PROP_ITEM_COUNT_D
        )
        val itemCount: Long?,

        @property:PropertyDefinition(
                name = PROP_INDEX_ARN,
                description = PROP_INDEX_ARN_D
        )
        val indexArn: String?
) {
    companion object {
        const val PROP_INDEX_NAME = "index_name"
        const val PROP_INDEX_NAME_D = "The name of the global secondary index"
        const val PROP_KEY_SCHEMA = "key_schema"
        const val PROP_KEY_SCHEMA_D = "The complete key schema for a global secondary index, which consists of one or more pairs of attribute names and key types"
        const val PROP_PROJECTION = "projection"
        const val PROP_PROJECTION_D = "Represents attributes that are copied (projected) from the table into the global secondary index"
        const val PROP_INDEX_STATUS = "index_status"
        const val PROP_INDEX_STATUS_D = "The current state of the global secondary index"
        const val PROP_BACKFILLING = "backfilling"
        const val PROP_BACKFILLING_D = "Indicates whether the index is currently backfilling"
        const val PROP_PROVISIONED_THROUGHPUT = "provisioned_throughput"
        const val PROP_PROVISIONED_THROUGHPUT_D = "Represents the provisioned throughput settings for the specified global secondary index"
        const val PROP_INDEX_SIZE = "index_size"
        const val PROP_INDEX_SIZE_D = "The total size of the specified index, in bytes"
        const val PROP_ITEM_COUNT = "item_count"
        const val PROP_ITEM_COUNT_D = "The number of items in the specified index"
        const val PROP_INDEX_ARN = "index_arn"
        const val PROP_INDEX_ARN_D = "The Amazon Resource Name (ARN) that uniquely identifies the index"
    }
}
