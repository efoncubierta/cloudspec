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
import java.time.Instant

data class EC2NetworkInterfaceAttachment(
        @PropertyDefinition(
                name = "attach_time",
                description = "The timestamp indicating when the attachment initiated"
        )
        val attachTime: Instant?,

        //    @AssociationDefinition(
        //            name = "attachment_id",
        //            description = ""
        //    )
        //    private final String attachmentId;

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Indicates whether the network interface is deleted when the instance is terminated"
        )
        val deleteOnTermination: Boolean?,

        @AssociationDefinition(
                name = "instance",
                description = "The instance",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @PropertyDefinition(
                name = "instance_owner_id",
                description = "The AWS account ID of the owner of the instance"
        )
        val instanceOwnerId: String?,

        @PropertyDefinition(
                name = "status",
                description = "The attachment state"
        )
        val status: String?
)
