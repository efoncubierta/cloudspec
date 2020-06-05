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
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2NetworkInterfaceAssociation
import cloudspec.aws.ec2.nested.EC2NetworkInterfaceAttachment
import cloudspec.aws.ec2.nested.EC2NetworkInterfacePrivateIpAddress
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "network_interface",
        description = "Network Interface"
)
data class EC2NetworkInterface(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "association",
                description = "The association information for an Elastic IP address (IPv4) associated with the network interface"
        )
        val association: EC2NetworkInterfaceAssociation?,

        @PropertyDefinition(
                name = "attachment",
                description = "The network interface attachment"
        )
        val attachment: EC2NetworkInterfaceAttachment?,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone"
        )
        val availabilityZone: String?,

        @AssociationDefinition(
                name = "groups",
                description = "Any security groups for the network interface",
                targetClass = EC2SecurityGroup::class
        )
        val groupIds: List<String>?,

        @PropertyDefinition(
                name = "interface_type",
                description = "The type of network interface"
        )
        val interfaceType: String?,

        @PropertyDefinition(
                name = "ipv6_addresses",
                description = "The IPv6 addresses associated with the network interface"
        )
        val ipv6Addresses: List<String>?,

        @IdDefinition
        @PropertyDefinition(
                name = "network_interface_id",
                description = "The ID of the network interface"
        )
        val networkInterfaceId: String?,

        @PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the owner of the network interface"
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
                description = "The private IPv4 addresses associated with the network interface"
        )
        val privateIpAddresses: List<EC2NetworkInterfacePrivateIpAddress>?,

        //    @AssociationDefinition(
        //            name = "requester",
        //            description = ""
        //    )
        //    private final String requesterId;
        //    private final Boolean requesterManaged;

        @PropertyDefinition(
                name = "source_dest_check",
                description = "Indicates whether traffic to or from the instance is validated"
        )
        val sourceDestCheck: Boolean?,

        @PropertyDefinition(
                name = "status",
                description = "The status of the network interface",
                exampleValues = "available | associated | attaching | in-use | detaching"
        )
        val status: String?,

        @AssociationDefinition(
                name = "subnet",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the network interface"
        )
        val tagSet: List<KeyValue>?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) : EC2Resource(region)
