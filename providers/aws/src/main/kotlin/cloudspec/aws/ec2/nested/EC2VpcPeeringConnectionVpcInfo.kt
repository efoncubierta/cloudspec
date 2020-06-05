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
