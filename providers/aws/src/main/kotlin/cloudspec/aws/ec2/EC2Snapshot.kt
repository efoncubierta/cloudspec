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
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.aws.kms.KMSKey
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.ec2.model.SnapshotState
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2Snapshot.RESOURCE_NAME,
        description = EC2Snapshot.RESOURCE_DESCRIPTION
)
data class EC2Snapshot(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        // private final String dataEncryptionKeyId;

        @property:PropertyDefinition(
                name = PROP_ENCRYPTED,
                description = PROP_ENCRYPTED_D
        )
        val encrypted: Boolean?,

        @property:AssociationDefinition(
                name = PROP_KMS_KEY,
                description = PROP_KMS_KEY_D,
                targetClass = KMSKey::class
        )
        val kmsKeyId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_PROGRESS,
                description = PROP_PROGRESS_D
        )
        val progress: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_SNAPSHOT_ID,
                description = PROP_SNAPSHOT_ID_D
        )
        val snapshotId: String,

        @property:PropertyDefinition(
                name = PROP_START_TIME,
                description = PROP_START_TIME_D
        )
        val startTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: SnapshotState?,

        @property:AssociationDefinition(
                name = ASSOC_VOLUME,
                description = ASSOC_VOLUME_D,
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @property:PropertyDefinition(
                name = PROP_VOLUME_SIZE,
                description = PROP_VOLUME_SIZE_D
        )
        val volumeSize: Int?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "snapshot"
        const val RESOURCE_DESCRIPTION = "Snapshot"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ENCRYPTED = "encrypted"
        const val PROP_ENCRYPTED_D = "Indicates whether the snapshot is encrypted"
        const val PROP_KMS_KEY = "kms_key"
        const val PROP_KMS_KEY_D = "The AWS Key Management Service (AWS KMS) customer master key (CMK)"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The AWS account ID of the EBS snapshot owner"
        const val PROP_PROGRESS = "progress"
        const val PROP_PROGRESS_D = "The progress of the snapshot, as a percentage"
        const val PROP_SNAPSHOT_ID = "snapshotId"
        const val PROP_SNAPSHOT_ID_D = "The ID of the snapshot. Each snapshot receives a unique identifier when it is created"
        const val PROP_START_TIME = "start_time"
        const val PROP_START_TIME_D = "The time stamp when the snapshot was initiated"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The snapshot state"
        const val ASSOC_VOLUME = "volume"
        const val ASSOC_VOLUME_D = "The volume that was used to create the snapshot"
        const val PROP_VOLUME_SIZE = "volume_size"
        const val PROP_VOLUME_SIZE_D = "The size of the volume, in GiB"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the snapshot"
    }
}
