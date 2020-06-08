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
import cloudspec.aws.ec2.nested.EC2VolumeAttachment
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "volume",
        description = "Volume"
)
data class EC2Volume(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "attachments",
                description = "Information about the volume attachments"
        )
        val attachments: List<EC2VolumeAttachment>?,

        @property:PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone for the volume"
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = "create_time",
                description = "The time stamp when volume creation was initiated"
        )
        val createTime: Instant?,

        @property:PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the volume is encrypted"
        )
        val encrypted: Boolean?,

        //    @property:AssociationDefinition(
        //            name = "kms_key",
        //            description = "The Amazon Resource Name (ARN) of the AWS Key Management Service (AWS KMS) customer master key (CMK)"
        //    )
        val kmsKeyId: String?,

        @property:PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = "size",
                description = "The size of the volume, in GiBs"
        )
        val size: Int?,

        @property:AssociationDefinition(
                name = "snapshot",
                description = "The snapshot from which the volume was created, if applicable",
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The volume state",
                exampleValues = "creating | available | in-use | deleting | deleted | error"
        )
        val state: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "volume_id",
                description = "The ID of the volume"
        )
        val volumeId: String?,

        @property:PropertyDefinition(
                name = "iops",
                description = "The number of I/O operations per second (IOPS) that the volume supports"
        )
        val iops: Int?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the volume"
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = "volume_type",
                description = "The volume type",
                exampleValues = "standard | io1 | gp2 | sc1 | st1"
        )
        val volumeType: String?,

        @property:PropertyDefinition(
                name = "fast_restored",
                description = "Indicates whether the volume was created using fast snapshot restore"
        )
        val fastRestored: Boolean?,

        @property:PropertyDefinition(
                name = "multi_attach_enabled",
                description = "Indicates whether Amazon EBS Multi-Attach is enabled"
        )
        val multiAttachEnabled: Boolean?
) : EC2Resource(region)
