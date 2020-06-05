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
        @AssociationDefinition(
                name = "groups",
                description = "One or more security groups",
                targetClass = EC2SecurityGroup::class
        )
        val groupIds: List<String>?,

        @PropertyDefinition(
                name = "ipv6_addresses",
                description = "One or more IPv6 addresses associated with the network interface"
        )
        val ipv6Addresses: List<String>?,

        @AssociationDefinition(
                name = "network_interface",
                description = "The network interface",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that created the network interface"
        )

        val ownerId: String?,

        @PropertyDefinition(
                name = "private_dns_name",
                description = "The private DNS name"
        )
        val privateDnsName: String?,

        @PropertyDefinition(
                name = "private_ip_address",
                description = "The IPv4 address of the network interface within the subnet"
        )
        val privateIpAddress: String?,

        @PropertyDefinition(
                name = "private_ip_addresses",
                description = "One or more private IPv4 addresses associated with the network interface"
        )
        val privateIpAddresses: List<EC2InstancePrivateIpAddress>?,

        @PropertyDefinition(
                name = "source_dest_check",
                description = "Indicates whether to validate network traffic to or from this network interface"
        )
        val sourceDestCheck: Boolean?,

        @PropertyDefinition(
                name = "status",
                description = "The status of the network interface",
                exampleValues = "available | associated | attached | in-use | detaching"
        )
        val status: String?,

        @AssociationDefinition(
                name = "subnet",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "interface_type",
                description = "Describes the type of network interface",
                exampleValues = "interface | efa"
        )
        val interfaceType: String?
)
