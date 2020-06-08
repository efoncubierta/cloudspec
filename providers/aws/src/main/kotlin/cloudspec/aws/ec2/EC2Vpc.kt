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
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "cidr_block",
                description = "The primary IPv4 CIDR block for the VPC"
        )
        val cidrBlock: String?,

        @property:AssociationDefinition(
                name = "dhcp_options",
                description = "The ID of the set of DHCP options you've associated with the VPC",
                targetClass = EC2DhcpOptions::class
        )
        val dhcpOptionsId: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The current state of the VPC"
        )
        val state: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "vpc_id",
                description = "The ID of the VPC"
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the VPC"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "instance_tenancy",
                description = "The allowed tenancy of instances launched into the VPC",
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @property:PropertyDefinition(
                name = "ipv6_cidr_block_associations",
                description = "Information about the IPv6 CIDR blocks associated with the VPC"
        )
        val ipv6CidrBlockAssociations: List<EC2VpcIpv6CidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = "cidr_block_associations",
                description = "Information about the IPv4 CIDR blocks associated with the VPC"
        )
        val cidrBlockAssociations: List<EC2VpcCidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = "is_default",
                description = "Indicates whether the VPC is the default VPC"
        )
        val isDefault: Boolean?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the VPC"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
