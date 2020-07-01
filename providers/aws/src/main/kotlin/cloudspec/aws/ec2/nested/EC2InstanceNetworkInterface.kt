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
import cloudspec.aws.ec2.EC2SecurityGroup
import cloudspec.aws.ec2.EC2Subnet
import cloudspec.aws.ec2.EC2Vpc

data class EC2InstanceNetworkInterface(
        @property:AssociationDefinition(
                name = ASSOC_GROUPS,
                description = ASSOC_GROUPS_D,
                targetClass = EC2SecurityGroup::class
        )
        val groupIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_IPV6_ADDRESSES,
                description = PROP_IPV6_ADDRESSES_D
        )
        val ipv6Addresses: List<String>?,

        @property:AssociationDefinition(
                name = ASSOC_NETWORK_INTERFACE,
                description = ASSOC_NETWORK_INTERFACE_D,
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_DNS_NAME,
                description = PROP_PRIVATE_DNS_NAME_D
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESS,
                description = PROP_PRIVATE_IP_ADDRESS_D
        )
        val privateIpAddress: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESSES,
                description = PROP_PRIVATE_IP_ADDRESSES_D
        )
        val privateIpAddresses: List<EC2InstancePrivateIpAddress>?,

        @property:PropertyDefinition(
                name = PROP_SOURCE_DEST_CHECK,
                description = PROP_SOURCE_DEST_CHECK_D
        )
        val sourceDestCheck: Boolean?,

        @property:PropertyDefinition(
                name = PROP_STATUS,
                description = PROP_STATUS_D,
                exampleValues = "available | associated | attached | in-use | detaching"
        )
        val status: String?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNET,
                description = ASSOC_SUBNET_D,
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_INTERFACE_TYPE,
                description = PROP_INTERFACE_TYPE_D,
                exampleValues = "interface | efa"
        )
        val interfaceType: String?
) {
    companion object {
        const val ASSOC_GROUPS = "groups"
        const val ASSOC_GROUPS_D = "One or more security groups"
        const val PROP_IPV6_ADDRESSES = "ipv6_addresses"
        const val PROP_IPV6_ADDRESSES_D = "One or more IPv6 addresses associated with the network interface"
        const val ASSOC_NETWORK_INTERFACE = "network_interface"
        const val ASSOC_NETWORK_INTERFACE_D = "The network interface"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that created the network interface"
        const val PROP_PRIVATE_DNS_NAME = "private_dns_name"
        const val PROP_PRIVATE_DNS_NAME_D = "The private DNS name"
        const val PROP_PRIVATE_IP_ADDRESS = "private_ip_address"
        const val PROP_PRIVATE_IP_ADDRESS_D = "The IPv4 address of the network interface within the subnet"
        const val PROP_PRIVATE_IP_ADDRESSES = "private_ip_addresses"
        const val PROP_PRIVATE_IP_ADDRESSES_D = "One or more private IPv4 addresses associated with the network interface"
        const val PROP_SOURCE_DEST_CHECK = "source_dest_check"
        const val PROP_SOURCE_DEST_CHECK_D = "Indicates whether to validate network traffic to or from this network interface"
        const val PROP_STATUS = "status"
        const val PROP_STATUS_D = "The status of the network interface"
        const val ASSOC_SUBNET = "subnet"
        const val ASSOC_SUBNET_D = "The subnet"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC"
        const val PROP_INTERFACE_TYPE = "interface_type"
        const val PROP_INTERFACE_TYPE_D = "Describes the type of network interface"
    }
}
