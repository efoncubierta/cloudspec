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
import cloudspec.aws.ec2.*
import software.amazon.awssdk.services.ec2.model.RouteOrigin
import software.amazon.awssdk.services.ec2.model.RouteState

data class EC2Route(
        @property:PropertyDefinition(
                name = PROP_DESTINATION_CIDR_BLOCK,
                description = PROP_DESTINATION_CIDR_BLOCK_D
        )
        val destinationCidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_DESTINATION_IPV6_CIDR_BLOCK,
                description = PROP_DESTINATION_IPV6_CIDR_BLOCK_D
        )
        val destinationIpv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_DESTINATION_PREFIX_LIST_ID,
                description = PROP_DESTINATION_PREFIX_LIST_ID_D
        )
        val destinationPrefixListId: String?,

        @property:AssociationDefinition(
                name = ASSOC_EGRESS_ONLY_INTERNET_GATEWAY,
                description = ASSOC_EGRESS_ONLY_INTERNET_GATEWAY_D,
                targetClass = EC2InternetGateway::class
        )
        val egressOnlyInternetGatewayId: String?,

        @property:AssociationDefinition(
                name = ASSOC_GATEWAY,
                description = ASSOC_GATEWAY_D,
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support vpwg

        @property:AssociationDefinition(
                name = ASSOC_INSTANCE,
                description = ASSOC_INSTANCE_D,
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_OWNER_ID,
                description = PROP_INSTANCE_OWNER_ID_D
        )
        val instanceOwnerId: String?,

        @property:AssociationDefinition(
                name = ASSOC_NAT_GATEWAY,
                description = ASSOC_NAT_GATEWAY_D,
                targetClass = EC2NatGateway::class
        )
        val natGatewayId: String?,

        @property:AssociationDefinition(
                name = ASSOC_TRANSIT_GATEWAY,
                description = ASSOC_TRANSIT_GATEWAY_D,
                targetClass = EC2TransitGateway::class
        )
        val transitGatewayId: String?,

        @property:AssociationDefinition(
                name = ASSOC_LOCAL_GATEWAY,
                description = ASSOC_LOCAL_GATEWAY_D,
                targetClass = EC2LocalGateway::class
        )
        val localGatewayId: String?,

        @property:AssociationDefinition(
                name = ASSOC_NETWORK_INTERFACE,
                description = ASSOC_NETWORK_INTERFACE_D,
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @property:PropertyDefinition(
                name = PROP_ORIGIN,
                description = PROP_ORIGIN_D
        )
        val origin: RouteOrigin?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: RouteState?,

        @property:AssociationDefinition(
                name = ASSOC_VPC_PEERING_CONNECTION,
                description = ASSOC_VPC_PEERING_CONNECTION_D,
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
) {
    companion object {
        const val PROP_DESTINATION_CIDR_BLOCK = "destination_cidr_block"
        const val PROP_DESTINATION_CIDR_BLOCK_D = "The IPv4 CIDR block used for the destination match"
        const val PROP_DESTINATION_IPV6_CIDR_BLOCK = "destination_ipv6_cidr_block"
        const val PROP_DESTINATION_IPV6_CIDR_BLOCK_D = "The IPv6 CIDR block used for the destination match"
        const val PROP_DESTINATION_PREFIX_LIST_ID = "destination_prefix_list_id"
        const val PROP_DESTINATION_PREFIX_LIST_ID_D = "The prefix of the AWS service"
        const val ASSOC_EGRESS_ONLY_INTERNET_GATEWAY = "egress_only_internet_gateway"
        const val ASSOC_EGRESS_ONLY_INTERNET_GATEWAY_D = "The egress-only internet gateway"
        const val ASSOC_GATEWAY = "gateway"
        const val ASSOC_GATEWAY_D = "The gateway attached to your VPC"
        const val ASSOC_INSTANCE = "instance"
        const val ASSOC_INSTANCE_D = "The ID of a NAT instance in your VPC"
        const val PROP_INSTANCE_OWNER_ID = "instance_owner_id"
        const val PROP_INSTANCE_OWNER_ID_D = "The AWS account ID of the owner of the instance"
        const val ASSOC_NAT_GATEWAY = "nat_gateway"
        const val ASSOC_NAT_GATEWAY_D = "The NAT gateway"
        const val ASSOC_TRANSIT_GATEWAY = "transit_gateway"
        const val ASSOC_TRANSIT_GATEWAY_D = "The transit gateway"
        const val ASSOC_LOCAL_GATEWAY = "local_gateway"
        const val ASSOC_LOCAL_GATEWAY_D = "The local gateway"
        const val ASSOC_NETWORK_INTERFACE = "network_interface"
        const val ASSOC_NETWORK_INTERFACE_D = "The network interface"
        const val PROP_ORIGIN = "origin"
        const val PROP_ORIGIN_D = "Describes how the route was created"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the route"
        const val ASSOC_VPC_PEERING_CONNECTION = "vpc_peering_connection"
        const val ASSOC_VPC_PEERING_CONNECTION_D = "The VPC peering connection"
    }
}
