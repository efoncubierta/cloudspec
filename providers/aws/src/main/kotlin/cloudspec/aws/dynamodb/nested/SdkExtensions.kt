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

import cloudspec.model.KeyValue
import software.amazon.awssdk.services.dynamodb.model.*

fun ArchivalSummary.toDDBArchivalSummary(): DDBArchivalSummary {
    return DDBArchivalSummary(
            archivalDateTime(),
            archivalReason(),
            archivalBackupArn()
    )
}

fun BillingModeSummary.toDDBBillingModeSummary(): DDBBillingModeSummary {
    return DDBBillingModeSummary(
            billingMode(),
            lastUpdateToPayPerRequestDateTime()
    )
}

fun List<GlobalSecondaryIndexDescription>.toDDBGlobalSecondaryIndexDescriptions(): List<DDBGlobalSecondaryIndexDescription> {
    return map { it.toDDBGlobalSecondaryIndexDescription() }
}

fun GlobalSecondaryIndexDescription.toDDBGlobalSecondaryIndexDescription(): DDBGlobalSecondaryIndexDescription {
    return DDBGlobalSecondaryIndexDescription(
            indexName(),
            keySchema()?.toDDBKeySchemaElements(),
            projection()?.toDDBProjection(),
            indexStatus(),
            backfilling(),
            provisionedThroughput()?.toDDBProvisionedThroughputDescription(),
            indexSizeBytes(),
            itemCount(),
            indexArn()
    )
}

fun List<KeySchemaElement>.toDDBKeySchemaElements(): List<DDBKeySchemaElement> {
    return map { it.toDDBKeySchemaElement() }
}

fun KeySchemaElement.toDDBKeySchemaElement(): DDBKeySchemaElement {
    return DDBKeySchemaElement(
            attributeName(),
            keyType()
    )
}

fun List<LocalSecondaryIndexDescription>.toDDBLocalSecondaryIndexDescriptions(): List<DDBLocalSecondaryIndexDescription> {
    return map { it.toDDBLocalSecondaryIndexDescription() }
}

fun LocalSecondaryIndexDescription.toDDBLocalSecondaryIndexDescription(): DDBLocalSecondaryIndexDescription {
    return DDBLocalSecondaryIndexDescription(
            indexName(),
            keySchema()?.toDDBKeySchemaElements(),
            projection()?.toDDBProjection(),
            indexSizeBytes(),
            itemCount(),
            indexArn()
    )
}

fun Projection.toDDBProjection(): DDBProjection {
    return DDBProjection(
            projectionType(),
            nonKeyAttributes()
    )
}

fun ProvisionedThroughputDescription.toDDBProvisionedThroughputDescription(): DDBProvisionedThroughputDescription {
    return DDBProvisionedThroughputDescription(
            lastIncreaseDateTime(),
            lastDecreaseDateTime(),
            numberOfDecreasesToday(),
            readCapacityUnits(),
            writeCapacityUnits()
    )
}

fun ProvisionedThroughputOverride.toDDBProvisionedThroughputOverride(): DDBProvisionedThroughputOverride {
    return DDBProvisionedThroughputOverride(
            readCapacityUnits()
    )
}

fun List<Replica>.toDDBReplicas(): List<DDBReplica> {
    return map { it.toDDBReplica() }
}

fun Replica.toDDBReplica(): DDBReplica {
    return DDBReplica(regionName())
}

fun List<ReplicaDescription>.toDDBReplicaDescriptions(): List<DDBReplicaDescription> {
    return map { it.toDDBReplicaDescription() }
}

fun ReplicaDescription.toDDBReplicaDescription(): DDBReplicaDescription {
    return DDBReplicaDescription(
            regionName(),
            replicaStatus(),
            replicaStatusPercentProgress(),
            provisionedThroughputOverride()?.toDDBProvisionedThroughputOverride(),
            globalSecondaryIndexes()?.toDDBReplicaGlobalSecondaryIndexDescriptions()
    )
}

fun List<ReplicaGlobalSecondaryIndexDescription>.toDDBReplicaGlobalSecondaryIndexDescriptions(): List<DDBReplicaGlobalSecondaryIndexDescription> {
    return map { it.toDDBReplicaGlobalSecondaryIndexDescription() }
}

fun ReplicaGlobalSecondaryIndexDescription.toDDBReplicaGlobalSecondaryIndexDescription(): DDBReplicaGlobalSecondaryIndexDescription {
    return DDBReplicaGlobalSecondaryIndexDescription(
            indexName(),
            provisionedThroughputOverride().toDDBProvisionedThroughputOverride()
    )
}

fun RestoreSummary.toDDBRestoreSummary(): DDBRestoreSummary {
    return DDBRestoreSummary(
            sourceBackupArn(),
            sourceTableArn(),
            restoreDateTime(),
            restoreInProgress()
    )
}

fun SSEDescription.toDDBSSEDescription(): DDBSSEDescription {
    return DDBSSEDescription(
            status(),
            sseType(),
            inaccessibleEncryptionDateTime()
    )
}

fun StreamSpecification.toDDBStreamSpecification(): DDBStreamSpecification {
    return DDBStreamSpecification(
            streamEnabled(),
            streamViewType()
    )
}

fun List<Tag>.toKeyValues(): List<KeyValue> {
    return map { it.toKeyValue() }
}

fun Tag.toKeyValue(): KeyValue {
    return KeyValue(key(), value())
}
