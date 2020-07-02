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
import cloudspec.aws.ec2.nested.EC2VolumeAttachment
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.VolumeState
import software.amazon.awssdk.services.ec2.model.VolumeType
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2Volume.RESOURCE_NAME,
        description = EC2Volume.RESOURCE_DESCRIPTION
)
data class EC2Volume(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ATTACHMENTS,
                description = PROP_ATTACHMENTS_D
        )
        val attachments: List<EC2VolumeAttachment>?,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_CREATE_TIME,
                description = PROP_CREATE_TIME_D
        )
        val createTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_ENCRYPTED,
                description = PROP_ENCRYPTED_D
        )
        val encrypted: Boolean?,

        //    @property:AssociationDefinition(
        //            name = ASSOC_KMS_KEY,
        //            description = "The Amazon Resource Name (ARN) of the AWS Key Management Service (AWS KMS) customer master key (CMK)"
        //    )
        val kmsKeyId: String?,

        @property:PropertyDefinition(
                name = PROP_OUTPOST_ARN,
                description = PROP_OUTPOST_ARN_D
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = PROP_SIZE,
                description = PROP_SIZE_D
        )
        val size: Int?,

        @property:AssociationDefinition(
                name = ASSOC_SNAPSHOT,
                description = ASSOC_SNAPSHOT_D,
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: VolumeState?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_VOLUME_ID,
                description = PROP_VOLUME_ID_D
        )
        val volumeId: String,

        @property:PropertyDefinition(
                name = PROP_IOPS,
                description = PROP_IOPS_D
        )
        val iops: Int?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = PROP_VOLUME_TYPE,
                description = PROP_VOLUME_TYPE_D
        )
        val volumeType: VolumeType?,

        @property:PropertyDefinition(
                name = PROP_FAST_RESTORED,
                description = PROP_FAST_RESTORED_D
        )
        val fastRestored: Boolean?,

        @property:PropertyDefinition(
                name = PROP_MULTI_ATTACH_ENABLED,
                description = PROP_MULTI_ATTACH_ENABLED_D
        )
        val multiAttachEnabled: Boolean?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "volume"
        const val RESOURCE_DESCRIPTION = "Volume"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ATTACHMENTS = "attachments"
        const val PROP_ATTACHMENTS_D = "Information about the volume attachments"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone for the volume"
        const val PROP_CREATE_TIME = "create_time"
        const val PROP_CREATE_TIME_D = "The time stamp when volume creation was initiated"
        const val PROP_ENCRYPTED = "encrypted"
        const val PROP_ENCRYPTED_D = "Indicates whether the volume is encrypted"
        const val ASSOC_KMS_KEY = "kms_key"
        const val ASSOC_KMS_KEY_D = "The Amazon Resource Name (ARN) of the AWS Key Management Service (AWS KMS) customer master key (CMK)"
        const val PROP_OUTPOST_ARN = "outpost_arn"
        const val PROP_OUTPOST_ARN_D = "The Amazon Resource Name (ARN) of the Outpost"
        const val PROP_SIZE = "size"
        const val PROP_SIZE_D = "The size of the volume, in GiBs"
        const val ASSOC_SNAPSHOT = "snapshot"
        const val ASSOC_SNAPSHOT_D = "The snapshot from which the volume was created, if applicable"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The volume state"
        const val PROP_VOLUME_ID = "volume_id"
        const val PROP_VOLUME_ID_D = "The ID of the volume"
        const val PROP_IOPS = "iops"
        const val PROP_IOPS_D = "The number of I/O operations per second (IOPS) that the volume supports"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the volume"
        const val PROP_VOLUME_TYPE = "volume_type"
        const val PROP_VOLUME_TYPE_D = "The volume type"
        const val PROP_FAST_RESTORED = "fast_restored"
        const val PROP_FAST_RESTORED_D = "Indicates whether the volume was created using fast snapshot restore"
        const val PROP_MULTI_ATTACH_ENABLED = "multi_attach_enabled"
        const val PROP_MULTI_ATTACH_ENABLED_D = "Indicates whether Amazon EBS Multi-Attach is enabled"
    }
}
