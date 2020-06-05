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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2Snapshot

data class EC2EbsBlockDevice(
        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the EBS volume is deleted on instance termination"
        )
        val deleteOnTermination: Boolean?,

        @PropertyDefinition(
                name = "iops",
                description = "The number of I/O operations per second (IOPS) that the volume supports"
        )
        val iops: Int?,

        @AssociationDefinition(
                name = "snapshot",
                description = "The ID of the snapshot",
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @PropertyDefinition(
                name = "volume_size",
                description = "The size of the volume, in GiB"
        )
        val volumeSize: Int?,

        @PropertyDefinition(
                name = "volume_type",
                description = "The volume type",
                exampleValues = "gp2, st1, sc1, standard"
        )
        val volumeType: String?,

        @PropertyDefinition(
                name = "encrypted",
                description = "Indicates whether the encryption state of an EBS volume is changed while being restored from a backing snapshot"
        )
        val encrypted: Boolean?
)
