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
import cloudspec.aws.ec2.nested.EC2SubnetIpv6CidrBlockAssociation
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.SubnetState

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2Subnet.RESOURCE_NAME,
        description = EC2Subnet.RESOURCE_DESCRIPTION
)
data class EC2Subnet(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_AVAILABLE_IP_ADDRESS_COUNT,
                description = PROP_AVAILABLE_IP_ADDRESS_COUNT_D
        )
        val availableIpAddressCount: Int?,

        @property:PropertyDefinition(
                name = PROP_CIDR_BLOCK,
                description = PROP_CIDR_BLOCK_D
        )
        val cidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_DEFAULT_FOR_AZ,
                description = PROP_DEFAULT_FOR_AZ_D
        )
        val defaultForAz: Boolean?,

        @property:PropertyDefinition(
                name = PROP_MAP_PUBLIC_IP_ON_LAUNCH,
                description = PROP_MAP_PUBLIC_IP_ON_LAUNCH_D
        )
        val mapPublicIpOnLaunch: Boolean?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: SubnetState?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_SUBNET_ID,
                description = PROP_SUBNET_ID_D
        )
        val subnetId: String,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?
        ,
        @property:PropertyDefinition(
                name = PROP_ASSIGN_IPV6_ADDRESS_ON_CREATION,
                description = PROP_ASSIGN_IPV6_ADDRESS_ON_CREATION_D
        )
        val assignIpv6AddressOnCreation: Boolean?,

        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS,
                description = PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS_D
        )
        val ipv6CidrBlockAssociations: List<EC2SubnetIpv6CidrBlockAssociation>?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = PROP_SUBNET_ARN,
                description = PROP_SUBNET_ARN_D
        )
        val subnetArn: String?,

        @property:PropertyDefinition(
                name = PROP_OUTPOST_ARN,
                description = PROP_OUTPOST_ARN_D
        )
        val outpostArn: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "subnet"
        const val RESOURCE_DESCRIPTION = "Subnet"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone of the subnet"
        const val PROP_AVAILABLE_IP_ADDRESS_COUNT = "available_ip_address_count"
        const val PROP_AVAILABLE_IP_ADDRESS_COUNT_D = "The number of unused private IPv4 addresses in the subnet"
        const val PROP_CIDR_BLOCK = "cidr_block"
        const val PROP_CIDR_BLOCK_D = "The IPv4 CIDR block assigned to the subnet"
        const val PROP_DEFAULT_FOR_AZ = "default_for_az"
        const val PROP_DEFAULT_FOR_AZ_D = "Indicates whether this is the default subnet for the Availability Zone"
        const val PROP_MAP_PUBLIC_IP_ON_LAUNCH = "map_public_ip_on_launch"
        const val PROP_MAP_PUBLIC_IP_ON_LAUNCH_D = "Indicates whether instances launched in this subnet receive a public IPv4 address"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The current state of the subnet"
        const val PROP_SUBNET_ID = "subnet_id"
        const val PROP_SUBNET_ID_D = "The ID of the subnet"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The ID of the VPC the subnet is in"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the subnet"
        const val PROP_ASSIGN_IPV6_ADDRESS_ON_CREATION = "assign_ipv6_address_on_creation"
        const val PROP_ASSIGN_IPV6_ADDRESS_ON_CREATION_D = "Indicates whether a network interface created in this subnet receives an IPv6 address."
        const val PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS = "ipv6_cidr_block_associations"
        const val PROP_IPV6_CIDR_BLOCK_ASSOCIATIONS_D = "Information about the IPv6 CIDR blocks associated with the subnet"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the subnet"
        const val PROP_SUBNET_ARN = "subnet_arn"
        const val PROP_SUBNET_ARN_D = "The Amazon Resource Name (ARN) of the subnet"
        const val PROP_OUTPOST_ARN = "outpost_arn"
        const val PROP_OUTPOST_ARN_D = "The Amazon Resource Name (ARN) of the Outpost"
    }
}
