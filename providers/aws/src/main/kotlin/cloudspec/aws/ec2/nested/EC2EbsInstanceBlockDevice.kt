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
import cloudspec.aws.ec2.EC2Volume
import java.time.Instant

data class EC2EbsInstanceBlockDevice(
        @PropertyDefinition(
                name = "attach_time",
                description = "The time stamp when the attachment initiated"
        )
        val attachTime: Instant?,

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the volume is deleted on instance termination"
        )
        val deleteOnTermination: Boolean?,

        @PropertyDefinition(
                name = "status",
                description = "The attachment state",
                exampleValues = "attaching | attached | detaching | detached"
        )
        val status: String?,

        @AssociationDefinition(
                name = "volume",
                description = "The EBS volume",
                targetClass = EC2Volume::class
        )
        val volumeId: String?
)
