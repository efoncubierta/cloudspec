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
import cloudspec.aws.ec2.nested.EC2SubnetIpv6CidrBlockAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "subnet",
        description = "VPC Subnet"
)
data class EC2Subnet(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone of the subnet"
        )
        val availabilityZone: String?,

        @PropertyDefinition(
                name = "available_ip_address_count",
                description = "The number of unused private IPv4 addresses in the subnet"
        )
        val availableIpAddressCount: Int?,

        @PropertyDefinition(
                name = "cidr_block",
                description = "The IPv4 CIDR block assigned to the subnet"
        )
        val cidrBlock: String?,

        @PropertyDefinition(
                name = "default_for_az",
                description = "Indicates whether this is the default subnet for the Availability Zone"
        )
        val defaultForAz: Boolean?,

        @PropertyDefinition(
                name = "map_public_ip_on_launch",
                description = "Indicates whether instances launched in this subnet receive a public IPv4 address"
        )
        val mapPublicIpOnLaunch: Boolean?,

        @PropertyDefinition(
                name = "state",
                description = "The current state of the subnet",
                exampleValues = "pending | available"
        )
        val state: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "subnet_id",
                description = "The ID of the subnet"
        )
        val subnetId: String?,

        @AssociationDefinition(
                name = "vpc",
                description = "The ID of the VPC the subnet is in",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the subnet"
        )
        val ownerId: String?
        ,
        @PropertyDefinition(
                name = "assign_ipv6_address_on_creation",
                description = "Indicates whether a network interface created in this subnet receives an IPv6 address."
        )
        val assignIpv6AddressOnCreation: Boolean?,

        @PropertyDefinition(
                name = "ipv6_cidr_block_associations",
                description = "Information about the IPv6 CIDR blocks associated with the subnet"
        )
        val ipv6CidrBlockAssociations: List<EC2SubnetIpv6CidrBlockAssociation>?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the subnet"
        )
        val tags: List<KeyValue>?,

        @PropertyDefinition(
                name = "subnet_arn",
                description = "The Amazon Resource Name (ARN) of the subnet"
        )
        val subnetArn: String?,

        @PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?
) : EC2Resource(region)
