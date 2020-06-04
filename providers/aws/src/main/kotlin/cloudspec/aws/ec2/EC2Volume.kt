/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2VolumeAttachment
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.Volume
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "volume",
        description = "Volume"
)
data class EC2Volume(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "attachments",
                description = "Information about the volume attachments"
        )
        val attachments: List<EC2VolumeAttachment>?,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone for the volume"
        )
        val availabilityZone: String?,

        @PropertyDefinition(
                name = "create_time",
                description = "The time stamp when volume creation was initiated"
        )
        val createTime: Instant?,

        @PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the volume is encrypted"
        )
        val encrypted: Boolean?,

        //    @AssociationDefinition(
        //            name = "kms_key",
        //            description = "The Amazon Resource Name (ARN) of the AWS Key Management Service (AWS KMS) customer master key (CMK)"
        //    )
        val kmsKeyId: String?,

        @PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?,

        @PropertyDefinition(
                name = "size",
                description = "The size of the volume, in GiBs"
        )
        val size: Int?,

        @AssociationDefinition(
                name = "snapshot",
                description = "The snapshot from which the volume was created, if applicable",
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @PropertyDefinition(
                name = "state",
                description = "The volume state",
                exampleValues = "creating | available | in-use | deleting | deleted | error"
        )
        val state: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "volume_id",
                description = "The ID of the volume"
        )
        val volumeId: String?,

        @PropertyDefinition(
                name = "iops",
                description = "The number of I/O operations per second (IOPS) that the volume supports"
        )
        val iops: Int?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the volume"
        )
        val tags: List<KeyValue>?,

        @PropertyDefinition(
                name = "volume_type",
                description = "The volume type",
                exampleValues = "standard | io1 | gp2 | sc1 | st1"
        )
        val volumeType: String?,

        @PropertyDefinition(
                name = "fast_restored",
                description = "Indicates whether the volume was created using fast snapshot restore"
        )
        val fastRestored: Boolean?,

        @PropertyDefinition(
                name = "multi_attach_enabled",
                description = "Indicates whether Amazon EBS Multi-Attach is enabled"
        )
        val multiAttachEnabled: Boolean?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, volume: Volume): EC2Volume {
            return volume.toEC2Volume(region)
        }
    }
}
