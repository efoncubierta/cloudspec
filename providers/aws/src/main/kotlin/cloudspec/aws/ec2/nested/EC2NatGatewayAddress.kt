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
                name = PROP_ALLOCATION_ID,
                description = PROP_ALLOCATION_ID_D
        )
        val allocationId: String?,

        @property:AssociationDefinition(
                name = ASSOC_NETWORK_INTERFACE,
                description = ASSOC_NETWORK_INTERFACE_D,
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP,
                description = PROP_PRIVATE_IP_D
        )
        val privateIp: String?,

        @property:PropertyDefinition(
                name = PROP_PUBLIC_IP,
                description = PROP_PUBLIC_IP_D
        )
        val publicIp: String?
) {
    companion object {
        const val PROP_ALLOCATION_ID = "allocation_id"
        const val PROP_ALLOCATION_ID_D = "The allocation ID of the Elastic IP address that's associated with the NAT gateway"
        const val ASSOC_NETWORK_INTERFACE = "network_interface"
        const val ASSOC_NETWORK_INTERFACE_D = "The ID of the network interface associated with the NAT gateway"
        const val PROP_PRIVATE_IP = "private_ip"
        const val PROP_PRIVATE_IP_D = "The private IP address associated with the Elastic IP address"
        const val PROP_PUBLIC_IP = "public_ip"
        const val PROP_PUBLIC_IP_D = "The Elastic IP address associated with the NAT gateway"
    }
}
