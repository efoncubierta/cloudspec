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
import cloudspec.aws.ec2.nested.EC2VpcCidrBlockAssociation
import cloudspec.aws.ec2.nested.EC2VpcIpv6CidrBlockAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "vpc",
        description = "Virtual Private Cloud"
)
data class EC2Vpc(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "cidr_block",
                description = "The primary IPv4 CIDR block for the VPC"
        )
        val cidrBlock: String?,

        @AssociationDefinition(
                name = "dhcp_options",
                description = "The ID of the set of DHCP options you've associated with the VPC",
                targetClass = EC2DhcpOptions::class
        )
        val dhcpOptionsId: String?,

        @PropertyDefinition(
                name = "state",
                description = "The current state of the VPC"
        )
        val state: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "vpc_id",
                description = "The ID of the VPC"
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the VPC"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "instance_tenancy",
                description = "The allowed tenancy of instances launched into the VPC",
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @PropertyDefinition(
                name = "ipv6_cidr_block_associations",
                description = "Information about the IPv6 CIDR blocks associated with the VPC"
        )
        val ipv6CidrBlockAssociations: List<EC2VpcIpv6CidrBlockAssociation>?,

        @PropertyDefinition(
                name = "cidr_block_associations",
                description = "Information about the IPv4 CIDR blocks associated with the VPC"
        )
        val cidrBlockAssociations: List<EC2VpcCidrBlockAssociation>?,

        @PropertyDefinition(
                name = "is_default",
                description = "Indicates whether the VPC is the default VPC"
        )
        val isDefault: Boolean?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the VPC"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
