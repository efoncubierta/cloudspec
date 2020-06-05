/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
