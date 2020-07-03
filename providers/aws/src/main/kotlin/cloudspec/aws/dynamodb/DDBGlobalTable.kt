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
import cloudspec.aws.dynamodb.nested.DDBReplicaDescription
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.dynamodb.model.GlobalTableStatus
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = DDBGroup.GROUP_NAME,
        name = DDBGlobalTable.RESOURCE_NAME,
        description = DDBGlobalTable.RESOURCE_DESCRIPTION
)
data class DDBGlobalTable(
        @property:PropertyDefinition(
                name = PROP_REPLICAS,
                description = PROP_REPLICAS_D
        )
        val replicationGroup: List<DDBReplicaDescription>?,

        @IdDefinition
        @property:PropertyDefinition(
                name = PROP_TABLE_ARN,
                description = PROP_TABLE_ARN_D
        )
        val globalTableArn: String,

        @property:PropertyDefinition(
                name = PROP_CREATION_DATE,
                description = PROP_CREATION_DATE_D
        )
        val creationDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_TABLE_STATUS,
                description = PROP_TABLE_STATUS_D
        )
        val globalTableStatus: GlobalTableStatus?,

        @property:PropertyDefinition(
                name = PROP_TABLE_NAME,
                description = PROP_TABLE_NAME_D
        )
        val globalTableName: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : DDBResource() {
    companion object {
        const val RESOURCE_NAME = "global_table"
        const val RESOURCE_DESCRIPTION = "Global Table"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          DDBGroup.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REPLICAS = "replicas"
        const val PROP_REPLICAS_D = "The Regions where the global table has replicas"
        const val PROP_TABLE_ARN = "table_arn"
        const val PROP_TABLE_ARN_D = "The unique identifier of the global table"
        const val PROP_CREATION_DATE = "creation_date"
        const val PROP_CREATION_DATE_D = "The creation time of the global table"
        const val PROP_TABLE_STATUS = "table_status"
        const val PROP_TABLE_STATUS_D = "The current state of the global table"
        const val PROP_TABLE_NAME = "table_name"
        const val PROP_TABLE_NAME_D = "The global table name"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags attached to the table"
    }
}
