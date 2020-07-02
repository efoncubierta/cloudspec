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
import software.amazon.awssdk.services.ec2.model.VolumeType

data class EC2EbsBlockDevice(
        @property:PropertyDefinition(
                name = PROP_DELETE_ON_TERMINATION,
                description = PROP_DELETE_ON_TERMINATION_D
        )
        val deleteOnTermination: Boolean?,

        @property:PropertyDefinition(
                name = PROP_IOPS,
                description = PROP_IOPS_D
        )
        val iops: Int?,

        @property:AssociationDefinition(
                name = ASSOC_SNAPSHOT,
                description = ASSOC_SNAPSHOT_D,
                targetClass = EC2Snapshot::class
        )
        val snapshotId: String?,

        @property:PropertyDefinition(
                name = PROP_VOLUME_SIZE,
                description = PROP_VOLUME_SIZE_D
        )
        val volumeSize: Int?,

        @property:PropertyDefinition(
                name = PROP_VOLUME_TYPE,
                description = PROP_VOLUME_TYPE_D
        )
        val volumeType: VolumeType?,

        @property:PropertyDefinition(
                name = PROP_ENCRYPTED,
                description = PROP_ENCRYPTED_D
        )
        val encrypted: Boolean?
) {
    companion object {
        const val PROP_DELETE_ON_TERMINATION = "delete_on_termination"
        const val PROP_DELETE_ON_TERMINATION_D = "Indicates whether the EBS volume is deleted on instance termination"
        const val PROP_IOPS = "iops"
        const val PROP_IOPS_D = "The number of I/O operations per second (IOPS) that the volume supports"
        const val ASSOC_SNAPSHOT = "snapshot"
        const val ASSOC_SNAPSHOT_D = "The ID of the snapshot"
        const val PROP_VOLUME_SIZE = "volume_size"
        const val PROP_VOLUME_SIZE_D = "The size of the volume, in GiB"
        const val PROP_VOLUME_TYPE = "volume_type"
        const val PROP_VOLUME_TYPE_D = "The volume type"
        const val PROP_ENCRYPTED = "encrypted"
        const val PROP_ENCRYPTED_D = "Indicates whether the encryption state of an EBS volume is changed while being restored from a backing snapshot"
    }
}
