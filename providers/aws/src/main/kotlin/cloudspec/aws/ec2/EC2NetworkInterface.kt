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
