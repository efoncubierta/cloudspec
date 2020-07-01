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
import cloudspec.aws.ec2.nested.EC2NetworkInterfaceAssociation
import cloudspec.aws.ec2.nested.EC2NetworkInterfaceAttachment
import cloudspec.aws.ec2.nested.EC2NetworkInterfacePrivateIpAddress
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NetworkInterface.RESOURCE_NAME,
        description = EC2NetworkInterface.RESOURCE_DESCRIPTION
)
data class EC2NetworkInterface(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ASSOCIATION,
                description = PROP_ASSOCIATION_D
        )
        val association: EC2NetworkInterfaceAssociation?,

        @property:PropertyDefinition(
                name = PROP_ATTACHMENT,
                description = PROP_ATTACHMENT_D
        )
        val attachment: EC2NetworkInterfaceAttachment?,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:AssociationDefinition(
                name = ASSOC_GROUPS,
                description = ASSOC_GROUPS_D,
                targetClass = EC2SecurityGroup::class
        )
        val groupIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_INTERFACE_TYPE,
                description = PROP_INTERFACE_TYPE_D
        )
        val interfaceType: String?,

        @property:PropertyDefinition(
                name = PROP_IPV6_ADDRESSES,
                description = PROP_IPV6_ADDRESSES_D
        )
        val ipv6Addresses: List<String>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_NETWORK_INTERFACE_ID,
                description = PROP_NETWORK_INTERFACE_ID_D
        )
        val networkInterfaceId: String,

        @property:PropertyDefinition(
                name = PROP_OUTPOST_ARN,
                description = PROP_OUTPOST_ARN_D
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_DNS_NAME,
                description = PROP_PRIVATE_DNS_NAME_D
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESS,
                description = PROP_PRIVATE_IP_ADDRESS_D
        )
        val privateIpAddress: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESSES,
                description = PROP_PRIVATE_IP_ADDRESSES_D
        )
        val privateIpAddresses: List<EC2NetworkInterfacePrivateIpAddress>?,

        //    @property:AssociationDefinition(
        //            name = ASSOC_REQUESTER,
        //            description = ASSOC_REQUESTER_D
        //    )
        //    private final String requesterId;
        //    private final Boolean requesterManaged;

        @property:PropertyDefinition(
                name = PROP_SOURCE_DEST_CHECK,
                description = PROP_SOURCE_DEST_CHECK_D
        )
        val sourceDestCheck: Boolean?,

        @property:PropertyDefinition(
                name = PROP_STATUS,
                description = PROP_STATUS_D,
                exampleValues = "available | associated | attaching | in-use | detaching"
        )
        val status: String?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNET,
                description = ASSOC_SUBNET_D,
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tagSet: List<KeyValue>?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "network_interface"
        const val RESOURCE_DESCRIPTION = "Network Interface"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ASSOCIATION = "association"
        const val PROP_ASSOCIATION_D = "The association information for an Elastic IP address (IPv4) associated with the network interface"
        const val PROP_ATTACHMENT = "attachment"
        const val PROP_ATTACHMENT_D = "The network interface attachment"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone"
        const val ASSOC_GROUPS = "groups"
        const val ASSOC_GROUPS_D = "Any security groups for the network interface"
        const val PROP_INTERFACE_TYPE = "interface_type"
        const val PROP_INTERFACE_TYPE_D = "The type of network interface"
        const val PROP_IPV6_ADDRESSES = "ipv6_addresses"
        const val PROP_IPV6_ADDRESSES_D = "The IPv6 addresses associated with the network interface"
        const val PROP_NETWORK_INTERFACE_ID = "network_interface_id"
        const val PROP_NETWORK_INTERFACE_ID_D = "The ID of the network interface"
        const val PROP_OUTPOST_ARN = "outpost_arn"
        const val PROP_OUTPOST_ARN_D = "The Amazon Resource Name (ARN) of the Outpost"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The AWS account ID of the owner of the network interface"
        const val PROP_PRIVATE_DNS_NAME = "private_dns_name"
        const val PROP_PRIVATE_DNS_NAME_D = "The private DNS name"
        const val PROP_PRIVATE_IP_ADDRESS = "private_ip_address"
        const val PROP_PRIVATE_IP_ADDRESS_D = "The IPv4 address of the network interface within the subnet"
        const val PROP_PRIVATE_IP_ADDRESSES = "private_ip_addresses"
        const val PROP_PRIVATE_IP_ADDRESSES_D = "The private IPv4 addresses associated with the network interface"
        const val ASSOC_REQUESTER = "requester"
        const val ASSOC_REQUESTER_D = ""
        const val PROP_SOURCE_DEST_CHECK = "source_dest_check"
        const val PROP_SOURCE_DEST_CHECK_D = "Indicates whether traffic to or from the instance is validated"
        const val PROP_STATUS = "status"
        const val PROP_STATUS_D = "The status of the network interface"
        const val ASSOC_SUBNET = "subnet"
        const val ASSOC_SUBNET_D = "The subnet"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the network interface"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC"
    }
}

