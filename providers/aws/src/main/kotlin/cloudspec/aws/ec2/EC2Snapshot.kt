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
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "snapshot",
        description = "Snapshot"
)
data class EC2Snapshot(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        // private final String dataEncryptionKeyId;

        @property:PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the snapshot is encrypted"
        )
        val encrypted: Boolean?,

        // private final String kmsKeyId;

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the EBS snapshot owner"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "progress",
                description = "The progress of the snapshot, as a percentage"
        )
        val progress: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "snapshotId",
                description = "The ID of the snapshot. Each snapshot receives a unique identifier when it is created"
        )
        val snapshotId: String,

        @property:PropertyDefinition(
                name = "start_time",
                description = "The time stamp when the snapshot was initiated"
        )
        val startTime: Instant?,

        @property:PropertyDefinition(
                name = "state",
                description = "The snapshot state"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = "volume",
                description = "The volume that was used to create the snapshot",
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @property:PropertyDefinition(
                name = "volume_size",
                description = " The size of the volume, in GiB"
        )
        val volumeSize: Int?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the snapshot"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
