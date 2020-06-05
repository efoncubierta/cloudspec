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
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "snapshot",
        description = "Snapshot"
)
data class EC2Snapshot(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        // private final String dataEncryptionKeyId;

        @PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the snapshot is encrypted"
        )
        val encrypted: Boolean?,

        // private final String kmsKeyId;

        @PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the EBS snapshot owner"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "progress",
                description = "The progress of the snapshot, as a percentage"
        )
        val progress: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "snapshotId",
                description = "The ID of the snapshot. Each snapshot receives a unique identifier when it is created"
        )
        val snapshotId: String?,

        @PropertyDefinition(
                name = "start_time",
                description = "The time stamp when the snapshot was initiated"
        )
        val startTime: Instant?,

        @PropertyDefinition(
                name = "state",
                description = "The snapshot state"
        )
        val state: String?,

        @AssociationDefinition(
                name = "volume",
                description = "The volume that was used to create the snapshot",
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @PropertyDefinition(
                name = "volume_size",
                description = " The size of the volume, in GiB"
        )
        val volumeSize: Int?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the snapshot"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
