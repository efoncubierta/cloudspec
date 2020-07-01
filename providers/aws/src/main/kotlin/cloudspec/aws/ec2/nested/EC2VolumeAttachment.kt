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
import cloudspec.aws.ec2.EC2Instance
import cloudspec.aws.ec2.EC2Volume
import java.time.Instant

data class EC2VolumeAttachment(
        @property:PropertyDefinition(
                name = PROP_ATTACH_TIME,
                description = PROP_ATTACH_TIME_D
        )
        val attachTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_DEVICE,
                description = PROP_DEVICE_D
        )
        val device: String?,

        @property:AssociationDefinition(
                name = ASSOC_INSTANCE,
                description = ASSOC_INSTANCE_D,
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D,
                exampleValues = "attaching | attached | detaching | detached | busy"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = ASSOC_VOLUME,
                description = ASSOC_VOLUME_D,
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @property:PropertyDefinition(
                name = PROP_DELETE_ON_TERMINATION,
                description = PROP_DELETE_ON_TERMINATION_D
        )
        val deleteOnTermination: Boolean?
) {
    companion object {
        const val PROP_ATTACH_TIME = "attach_time"
        const val PROP_ATTACH_TIME_D = "The time stamp when the attachment initiated"
        const val PROP_DEVICE = "device"
        const val PROP_DEVICE_D = "The device name"
        const val ASSOC_INSTANCE = "instance"
        const val ASSOC_INSTANCE_D = "The instance"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The attachment state of the volume"
        const val ASSOC_VOLUME = "volume"
        const val ASSOC_VOLUME_D = "The volume"
        const val PROP_DELETE_ON_TERMINATION = "delete_on_termination"
        const val PROP_DELETE_ON_TERMINATION_D = "Indicates whether the EBS volume is deleted on instance termination"
    }
}
