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
import cloudspec.aws.AWSProvider
import cloudspec.aws.ec2.nested.EC2VpcCidrBlockAssociation
import cloudspec.aws.ec2.nested.EC2VpcIpv6CidrBlockAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2Vpc.RESOURCE_NAME,
        description = EC2Vpc.RESOURCE_DESCRIPTION
)
data class EC2Vpc(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_CIDR_BLOCK,
                description = PROP_CIDR_BLOCK_D
        )
        val cidrBlock: String?,

        @property:AssociationDefinition(
                name = ASSOC_DHCP_OPTIONS,
                description = ASSOC_DHCP_OPTIONS_D,
                targetClass = EC2DhcpOptions::class
        )
        val dhcpOptionsId: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_VPC_ID,
                description = PROP_VPC_ID_D
        )
        val vpcId: String,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_TENANCY,
                description = PROP_INSTANCE_TENANCY_D,
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS,
                description = PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS_D
        )
        val ipv6CidrBlockAssociations: List<EC2VpcIpv6CidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = PROP_CIDR_BLOCK_ASSOCIATIONS,
                description = PROP_CIDR_BLOCK_ASSOCIATIONS_D
        )
        val cidrBlockAssociations: List<EC2VpcCidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = PROP_IS_DEFAULT,
                description = PROP_IS_DEFAULT_D
        )
        val isDefault: Boolean?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "vpc"
        const val RESOURCE_DESCRIPTION = "Virtual Private Cloud"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_CIDR_BLOCK = "cidr_block"
        const val PROP_CIDR_BLOCK_D = "The primary IPv4 CIDR block for the VPC"
        const val ASSOC_DHCP_OPTIONS = "dhcp_options"
        const val ASSOC_DHCP_OPTIONS_D = "The ID of the set of DHCP options you've associated with the VPC"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The current state of the VPC"
        const val PROP_VPC_ID = "vpc_id"
        const val PROP_VPC_ID_D = "The ID of the VPC"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the VPC"
        const val PROP_INSTANCE_TENANCY = "instance_tenancy"
        const val PROP_INSTANCE_TENANCY_D = "The allowed tenancy of instances launched into the VPC"
        const val PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS = "ipv6_cidr_block_associations"
        const val PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS_D = "Information about the IPv6 CIDR blocks associated with the VPC"
        const val PROP_CIDR_BLOCK_ASSOCIATIONS = "cidr_block_associations"
        const val PROP_CIDR_BLOCK_ASSOCIATIONS_D = "Information about the IPv4 CIDR blocks associated with the VPC"
        const val PROP_IS_DEFAULT = "is_default"
        const val PROP_IS_DEFAULT_D = "Indicates whether the VPC is the default VPC"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the VPC"
    }
}
