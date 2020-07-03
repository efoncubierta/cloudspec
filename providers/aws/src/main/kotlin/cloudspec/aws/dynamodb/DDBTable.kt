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
package cloudspec.aws.dynamodb

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.aws.dynamodb.nested.*
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.dynamodb.model.TableStatus
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = DDBGroup.GROUP_NAME,
        name = DDBTable.RESOURCE_NAME,
        description = DDBTable.RESOURCE_DESCRIPTION
)
data class DDBTable(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val region: String?,

//  final List<AttributeDefinition> attributeDefinitions;

        @property:PropertyDefinition(
                name = PROP_TABLE_NAME,
                description = PROP_TABLE_NAME_D
        )
        val tableName: String?,

        @PropertyDefinition(
                name = PROP_KEY_SCHEMA,
                description = PROP_KEY_SCHEMA_D
        )
        val keySchema: List<DDBKeySchemaElement>,

        @property:PropertyDefinition(
                name = PROP_TABLE_STATUS,
                description = PROP_TABLE_STATUS_D
        )
        val tableStatus: TableStatus?,

        @property:PropertyDefinition(
                name = PROP_CREATION_DATE,
                description = PROP_CREATION_DATE_D
        )
        val creationDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_PROVISIONED_THROUGHPUT,
                description = PROP_PROVISIONED_THROUGHPUT_D
        )
        val provisionedThroughput: DDBProvisionedThroughputDescription?,

        @property:PropertyDefinition(
                name = PROP_TABLE_SIZE,
                description = PROP_TABLE_SIZE_D
        )
        val tableSizeBytes: Long?,

        @property:PropertyDefinition(
                name = PROP_ITEM_COUNT,
                description = PROP_ITEM_COUNT_D
        )
        val itemCount: Long?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_TABLE_ARN,
                description = PROP_TABLE_ARN_D
        )
        val tableArn: String,

        @property:PropertyDefinition(
                name = PROP_TABLE_ID,
                description = PROP_TABLE_ID_D
        )
        val tableId: String?,

        @property:PropertyDefinition(
                name = PROP_BILLING_MODE_SUMMARY,
                description = PROP_BILLING_MODE_SUMMARY_D
        )
        val billingModeSummary: DDBBillingModeSummary?,

        @property:PropertyDefinition(
                name = PROP_LOCAL_SECONDARY_INDEXES,
                description = PROP_LOCAL_SECONDARY_INDEXES_D
        )
        val localSecondaryIndices: List<DDBLocalSecondaryIndexDescription>?,

        @property:PropertyDefinition(
                name = PROP_GLOBAL_SECONDARY_INDEXES,
                description = PROP_GLOBAL_SECONDARY_INDEXES_D
        )
        val globalSecondaryIndices: List<DDBGlobalSecondaryIndexDescription>?,

        @property:PropertyDefinition(
                name = PROP_STREAM_SPECIFICATION,
                description = PROP_STREAM_SPECIFICATION_D
        )
        val streamSpecification: DDBStreamSpecification?,

        @property:PropertyDefinition(
                name = PROP_LATEST_STREAM_LABEL,
                description = PROP_LATEST_STREAM_LABEL_D
        )
        val latestStreamLabel: String?,

        @property:PropertyDefinition(
                name = PROP_LATEST_STREAM_ARN,
                description = PROP_LATEST_STREAM_ARN_D
        )
        val latestStreamArn: String?,

        @property:PropertyDefinition(
                name = PROP_GLOBAL_TABLE_VERSION,
                description = PROP_GLOBAL_TABLE_VERSION_D
        )
        val globalTableVersion: String?,

        @property:PropertyDefinition(
                name = PROP_REPLICAS,
                description = PROP_REPLICAS_D
        )
        val replicas: List<DDBReplicaDescription>?,

        @property:PropertyDefinition(
                name = PROP_RESTORE_SUMMARY,
                description = PROP_RESTORE_SUMMARY_D
        )
        val restoreSummary: DDBRestoreSummary?,

        @property:PropertyDefinition(
                name = PROP_SSE_DESCRIPTION,
                description = PROP_SSE_DESCRIPTION_D
        )
        val sseDescription: DDBSSEDescription?,

        @property:PropertyDefinition(
                name = PROP_ARCHIVAL_SUMMARY,
                description = PROP_ARCHIVAL_SUMMARY_D
        )
        val archivalSummary: DDBArchivalSummary?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : DDBResource() {
    companion object {
        const val RESOURCE_NAME = "table"
        const val RESOURCE_DESCRIPTION = "Table"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          DDBGroup.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_TABLE_NAME = "table_name"
        const val PROP_TABLE_NAME_D = "The name of the table"
        const val PROP_KEY_SCHEMA = "key_schema"
        const val PROP_KEY_SCHEMA_D = "The primary key structure for the table"
        const val PROP_TABLE_STATUS = "table_status"
        const val PROP_TABLE_STATUS_D = "The current state of the table"
        const val PROP_CREATION_DATE = "creation_date"
        const val PROP_CREATION_DATE_D = "The date and time when the table was created, in UNIX epoch time format."
        const val PROP_PROVISIONED_THROUGHPUT = "provisioned_throughput"
        const val PROP_PROVISIONED_THROUGHPUT_D = "The provisioned throughput settings for the table, consisting of read and write capacity units, along with data about increases and decreases"
        const val PROP_TABLE_SIZE = "table_size"
        const val PROP_TABLE_SIZE_D = "The total size of the specified table, in bytes"
        const val PROP_ITEM_COUNT = "item_count"
        const val PROP_ITEM_COUNT_D = "The number of items in the specified table"
        const val PROP_TABLE_ARN = "table_arn"
        const val PROP_TABLE_ARN_D = "The Amazon Resource Name (ARN) that uniquely identifies the table"
        const val PROP_TABLE_ID = "table_id"
        const val PROP_TABLE_ID_D = "Unique identifier for the table for which the backup was created"
        const val PROP_BILLING_MODE_SUMMARY = "billing_mode_summary"
        const val PROP_BILLING_MODE_SUMMARY_D = "Contains the details for the read/write capacity mode"
        const val PROP_LOCAL_SECONDARY_INDEXES = "local_secondary_indexes"
        const val PROP_LOCAL_SECONDARY_INDEXES_D = "Represents one or more local secondary indexes on the table"
        const val PROP_GLOBAL_SECONDARY_INDEXES = "global_secondary_indexes"
        const val PROP_GLOBAL_SECONDARY_INDEXES_D = "he global secondary indexes, if any, on the table"
        const val PROP_STREAM_SPECIFICATION = "stream_specification"
        const val PROP_STREAM_SPECIFICATION_D = "The current DynamoDB Streams configuration for the table"
        const val PROP_LATEST_STREAM_LABEL = "latest_stream_label"
        const val PROP_LATEST_STREAM_LABEL_D = "A timestamp, in ISO 8601 format, for this stream"
        const val PROP_LATEST_STREAM_ARN = "latest_stream_arn"
        const val PROP_LATEST_STREAM_ARN_D = "The Amazon Resource Name (ARN) that uniquely identifies the latest stream for this table"
        const val PROP_GLOBAL_TABLE_VERSION = "global_table_version"
        const val PROP_GLOBAL_TABLE_VERSION_D = "Represents the version of global tables in use, if the table is replicated across AWS Regions"
        const val PROP_REPLICAS = "replicas"
        const val PROP_REPLICAS_D = "Represents replicas of the table"
        const val PROP_RESTORE_SUMMARY = "restore_summary"
        const val PROP_RESTORE_SUMMARY_D = "Contains details for the restore"
        const val PROP_SSE_DESCRIPTION = "sse_description"
        const val PROP_SSE_DESCRIPTION_D = "The description of the server-side encryption status on the specified table"
        const val PROP_ARCHIVAL_SUMMARY = "archival_summary"
        const val PROP_ARCHIVAL_SUMMARY_D = "Contains information about the table archive"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags attached to the table"
    }
}
