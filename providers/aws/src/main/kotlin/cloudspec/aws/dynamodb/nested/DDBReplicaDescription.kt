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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.kms.KMSKey
import software.amazon.awssdk.services.dynamodb.model.ReplicaStatus

data class DDBReplicaDescription(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val regionName: String?,

        @property:PropertyDefinition(
                name = PROP_REPLICA_STATUS,
                description = PROP_REPLICA_STATUS_D
        )
        val replicaStatus: ReplicaStatus?,

        @property:PropertyDefinition(
                name = PROP_REPLICA_PROGRESS,
                description = PROP_REPLICA_PROGRESS_D
        )
        val replicaStatusPercentProgress: String?,

        @property:AssociationDefinition(
                name = PROP_KMS_KEY,
                description = PROP_KMS_KEY_D,
                targetClass = KMSKey::class
        )
        val kmsMasterKeyId: String?,

        @property:PropertyDefinition(
                name = PROP_PROVISIONED_THROUGHPUT_OVERRIDE,
                description = PROP_PROVISIONED_THROUGHPUT_OVERRIDE_D
        )
        val provisionedThroughputOverride: DDBProvisionedThroughputOverride?,

        @property:PropertyDefinition(
                name = PROP_GLOBAL_SECONDARY_INDEXES,
                description = PROP_GLOBAL_SECONDARY_INDEXES_D
        )
        val globalSecondaryIndices: List<DDBReplicaGlobalSecondaryIndexDescription>?
) {
    companion object {
        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The name of the Region"
        const val PROP_REPLICA_STATUS = "replica_status"
        const val PROP_REPLICA_STATUS_D = "The current state of the replica"
        const val PROP_REPLICA_PROGRESS = "replica_progress"
        const val PROP_REPLICA_PROGRESS_D = "Specifies the progress of a Create, Update, or Delete action on the replica as a percentage"
        const val PROP_KMS_KEY = "kms_key"
        const val PROP_KMS_KEY_D = "The AWS KMS customer master key (CMK) of the replica that will be used for AWS KMS encryption"
        const val PROP_PROVISIONED_THROUGHPUT_OVERRIDE = "provisioned_throughput_override"
        const val PROP_PROVISIONED_THROUGHPUT_OVERRIDE_D = "Replica-specific provisioned throughput"
        const val PROP_GLOBAL_SECONDARY_INDEXES = "global_secondary_indexes"
        const val PROP_GLOBAL_SECONDARY_INDEXES_D = "Replica-specific global secondary index settings"
    }
}
