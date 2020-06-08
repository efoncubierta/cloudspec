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
                name = "attach_time",
                description = "The time stamp when the attachment initiated"
        )
        val attachTime: Instant?,

        @property:PropertyDefinition(
                name = "device",
                description = "The device name"
        )
        val device: String?,

        @property:AssociationDefinition(
                name = "instance",
                description = "The instance",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The attachment state of the volume",
                exampleValues = "attaching | attached | detaching | detached | busy"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = "volume",
                description = "The volume",
                targetClass = EC2Volume::class
        )
        val volumeId: String?,

        @property:PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the EBS volume is deleted on instance termination"
        )
        val deleteOnTermination: Boolean?
)
