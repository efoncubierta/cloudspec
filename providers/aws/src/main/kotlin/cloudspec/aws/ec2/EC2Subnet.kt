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
import cloudspec.aws.ec2.nested.EC2SubnetIpv6CidrBlockAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "subnet",
        description = "VPC Subnet"
)
data class EC2Subnet(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone of the subnet"
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = "available_ip_address_count",
                description = "The number of unused private IPv4 addresses in the subnet"
        )
        val availableIpAddressCount: Int?,

        @property:PropertyDefinition(
                name = "cidr_block",
                description = "The IPv4 CIDR block assigned to the subnet"
        )
        val cidrBlock: String?,

        @property:PropertyDefinition(
                name = "default_for_az",
                description = "Indicates whether this is the default subnet for the Availability Zone"
        )
        val defaultForAz: Boolean?,

        @property:PropertyDefinition(
                name = "map_public_ip_on_launch",
                description = "Indicates whether instances launched in this subnet receive a public IPv4 address"
        )
        val mapPublicIpOnLaunch: Boolean?,

        @property:PropertyDefinition(
                name = "state",
                description = "The current state of the subnet",
                exampleValues = "pending | available"
        )
        val state: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "subnet_id",
                description = "The ID of the subnet"
        )
        val subnetId: String,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The ID of the VPC the subnet is in",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the subnet"
        )
        val ownerId: String?
        ,
        @property:PropertyDefinition(
                name = "assign_ipv6_address_on_creation",
                description = "Indicates whether a network interface created in this subnet receives an IPv6 address."
        )
        val assignIpv6AddressOnCreation: Boolean?,

        @property:PropertyDefinition(
                name = "ipv6_cidr_block_associations",
                description = "Information about the IPv6 CIDR blocks associated with the subnet"
        )
        val ipv6CidrBlockAssociations: List<EC2SubnetIpv6CidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the subnet"
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = "subnet_arn",
                description = "The Amazon Resource Name (ARN) of the subnet"
        )
        val subnetArn: String?,

        @property:PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?
) : EC2Resource(region)
