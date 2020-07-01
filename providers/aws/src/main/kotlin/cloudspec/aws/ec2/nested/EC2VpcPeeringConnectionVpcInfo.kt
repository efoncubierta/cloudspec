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
        @property:PropertyDefinition(
                name = PROP_CIDR_BLOCK,
                description = PROP_CIDR_BLOCK_D
        )
        val cidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCKS,
                description = PROP_IPV6_CIDR_BLOCKS_D
        )
        val ipv6CidrBlocks: List<String>?,

        @property:PropertyDefinition(
                name = PROP_CIDR_BLOCKS,
                description = PROP_CIDR_BLOCKS_D
        )
        val cidrBlocks: List<String>?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_PEERING_OPTIONS,
                description = PROP_PEERING_OPTIONS_D
        )
        val peeringOptions: EC2VpcPeeringConnectionOptionsDescription?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val region: String?
) {
    companion object {
        const val PROP_CIDR_BLOCK = "cidr_block"
        const val PROP_CIDR_BLOCK_D = "The IPv4 CIDR block for the VPC"
        const val PROP_IPV6_CIDR_BLOCKS = "ipv6_cidr_blocks"
        const val PROP_IPV6_CIDR_BLOCKS_D = "The IPv6 CIDR block for the VPC"
        const val PROP_CIDR_BLOCKS = "cidr_blocks"
        const val PROP_CIDR_BLOCKS_D = "Information about the IPv4 CIDR blocks for the VPC"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The AWS account ID of the VPC owner"
        const val PROP_PEERING_OPTIONS = "peering_options"
        const val PROP_PEERING_OPTIONS_D = "Information about the VPC peering connection options for the accepter or requester VPC"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC"
        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The Region in which the VPC is located"
    }
}
