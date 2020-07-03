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

import cloudspec.aws.dynamodb.nested.*
import cloudspec.model.KeyValue
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.model.BackupDescription
import software.amazon.awssdk.services.dynamodb.model.GlobalTableDescription
import software.amazon.awssdk.services.dynamodb.model.TableDescription

fun BackupDescription.toDDBBackup(region: Region, tags: List<KeyValue>): DDBBackup {
    return DDBBackup(
            region.id(),
            backupDetails().backupArn(),
            backupDetails().backupName(),
            backupDetails().backupSizeBytes(),
            backupDetails().backupStatus(),
            backupDetails().backupType(),
            backupDetails().backupCreationDateTime(),
            backupDetails().backupExpiryDateTime(),
            sourceTableDetails().tableArn(),
            tags
    )
}

fun TableDescription.toDDBTable(region: Region, tags: List<KeyValue>): DDBTable {
    return DDBTable(
            region.id(),
            tableName(),
            keySchema().toDDBKeySchemaElements(),
            tableStatus(),
            creationDateTime(),
            provisionedThroughput()?.toDDBProvisionedThroughputDescription(),
            tableSizeBytes(),
            itemCount(),
            tableArn(),
            tableId(),
            billingModeSummary()?.toDDBBillingModeSummary(),
            localSecondaryIndexes()?.toDDBLocalSecondaryIndexDescriptions(),
            globalSecondaryIndexes()?.toDDBGlobalSecondaryIndexDescriptions(),
            streamSpecification()?.toDDBStreamSpecification(),
            latestStreamLabel(),
            latestStreamArn(),
            globalTableVersion(),
            replicas()?.toDDBReplicaDescriptions(),
            restoreSummary()?.toDDBRestoreSummary(),
            sseDescription()?.toDDBSSEDescription(),
            archivalSummary()?.toDDBArchivalSummary(),
            tags
    )
}

fun GlobalTableDescription.toDDBGlobalTable(tags: List<KeyValue>): DDBGlobalTable {
    return DDBGlobalTable(
            replicationGroup()?.toDDBReplicaDescriptions(),
            globalTableArn(),
            creationDateTime(),
            globalTableStatus(),
            globalTableName(),
            tags
    )
}
