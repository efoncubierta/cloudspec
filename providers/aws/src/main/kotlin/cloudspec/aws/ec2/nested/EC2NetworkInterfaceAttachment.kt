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
        @property:PropertyDefinition(
                name = PROP_ATTACH_TIME,
                description = PROP_ATTACH_TIME_D
        )
        val attachTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_DELETE_ON_TERMINATION,
                description = PROP_DELETE_ON_TERMINATION_D
        )
        val deleteOnTermination: Boolean?,

        @property:AssociationDefinition(
                name = PROP_INSTANCE,
                description = PROP_INSTANCE_D,
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_OWNER_ID,
                description = PROP_INSTANCE_OWNER_ID_D
        )
        val instanceOwnerId: String?,

        @property:PropertyDefinition(
                name = PROP_STATUS,
                description = PROP_STATUS_D
        )
        val status: String?
) {
    companion object {
        const val PROP_ATTACH_TIME = "attach_time"
        const val PROP_ATTACH_TIME_D = "The timestamp indicating when the attachment initiated"
        const val PROP_DELETE_ON_TERMINATION = "delete_on_termination"
        const val PROP_DELETE_ON_TERMINATION_D = "Indicates whether the network interface is deleted when the instance is terminated"
        const val PROP_INSTANCE = "instance"
        const val PROP_INSTANCE_D = "The instance"
        const val PROP_INSTANCE_OWNER_ID = "instance_owner_id"
        const val PROP_INSTANCE_OWNER_ID_D = "The AWS account ID of the owner of the instance"
        const val PROP_STATUS = "status"
        const val PROP_STATUS_D = "The attachment state"
    }
}
