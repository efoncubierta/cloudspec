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
import cloudspec.aws.ec2.EC2NetworkInterface

data class EC2NatGatewayAddress(
        @property:PropertyDefinition(
                name = "allocation_id",
                description = "The allocation ID of the Elastic IP address that's associated with the NAT gateway")
        val allocationId: String?,

        @property:AssociationDefinition(
                name = "network_interface",
                description = "The ID of the network interface associated with the NAT gateway",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @property:PropertyDefinition(
                name = "private_ip",
                description = "The private IP address associated with the Elastic IP address"
        )
        val privateIp: String?,

        @property:PropertyDefinition(
                name = "public_ip",
                description = "The Elastic IP address associated with the NAT gateway"
        )
        val publicIp: String?
)
