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
import cloudspec.aws.ec2.EC2Vpc

data class EC2VpcPeeringConnectionVpcInfo(
        @PropertyDefinition(
                name = "cidr_block",
                description = "The IPv4 CIDR block for the VPC"
        )
        val cidrBlock: String?,

        @PropertyDefinition(
                name = "ipv6_cidr_blocks",
                description = "The IPv6 CIDR block for the VPC"
        )
        val ipv6CidrBlocks: List<String>?,

        @PropertyDefinition(
                name = "cidr_blocks",
                description = "Information about the IPv4 CIDR blocks for the VPC"
        )
        val cidrBlocks: List<String>?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the VPC owner"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "peering_options",
                description = "Information about the VPC peering connection options for the accepter or requester VPC"
        )
        val peeringOptions: EC2VpcPeeringConnectionOptionsDescription?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "region",
                description = "The Region in which the VPC is located"
        )
        val region: String?
)
