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

data class EC2Route(
        @property:PropertyDefinition(
                name = "destination_cidr_block",
                description = "The IPv4 CIDR block used for the destination match"
        )
        val destinationCidrBlock: String?,

        @property:PropertyDefinition(
                name = "destination_ipv6_cidr_block",
                description = "The IPv6 CIDR block used for the destination match"
        )
        val destinationIpv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = "destination_prefix_list_id",
                description = "The prefix of the AWS service"
        )
        val destinationPrefixListId: String?,

        @property:AssociationDefinition(
                name = "egress_only_internet_gateway",
                description = "The egress-only internet gateway",
                targetClass = EC2InternetGateway::class
        )
        val egressOnlyInternetGatewayId: String?,

        @property:AssociationDefinition(
                name = "gateway",
                description = "The gateway attached to your VPC",
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support vpwg

        @property:AssociationDefinition(
                name = "instance",
                description = "The ID of a NAT instance in your VPC",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = "instance_owner-Id",
                description = "The AWS account ID of the owner of the instance"
        )
        val instanceOwnerId: String?,

        @property:AssociationDefinition(
                name = "nat_gateway",
                description = "The NAT gateway",
                targetClass = EC2NatGateway::class
        )
        val natGatewayId: String?,

        @property:AssociationDefinition(
                name = "transit_gateway",
                description = "The transit gateway",
                targetClass = EC2TransitGateway::class
        )
        val transitGatewayId: String?,

        @property:AssociationDefinition(
                name = "local_gateway",
                description = "The local gateway",
                targetClass = EC2LocalGateway::class
        )
        val localGatewayId: String?,

        @property:AssociationDefinition(
                name = "network_interface",
                description = "The network interface",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @property:PropertyDefinition(
                name = "origin",
                description = "Describes how the route was created",
                exampleValues = "CreateRouteTable | CreateRoute | EnableVgwRoutePropagation"
        )
        val origin: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The state of the route",
                exampleValues = "active | blackhole"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = "vpc_peering_connection",
                description = "The VPC peering connection",
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
)
