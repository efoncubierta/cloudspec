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
import cloudspec.aws.ec2.*

data class EC2Route(
        @PropertyDefinition(
                name = "destination_cidr_block",
                description = "The IPv4 CIDR block used for the destination match"
        )
        val destinationCidrBlock: String?,

        @PropertyDefinition(
                name = "destination_ipv6_cidr_block",
                description = "The IPv6 CIDR block used for the destination match"
        )
        val destinationIpv6CidrBlock: String?,

        @PropertyDefinition(
                name = "destination_prefix_list_id",
                description = "The prefix of the AWS service"
        )
        val destinationPrefixListId: String?,

        @AssociationDefinition(
                name = "egress_only_internet_gateway",
                description = "The egress-only internet gateway",
                targetClass = EC2InternetGateway::class
        )
        val egressOnlyInternetGatewayId: String?,

        @AssociationDefinition(
                name = "gateway",
                description = "The gateway attached to your VPC",
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support vpwg

        @AssociationDefinition(
                name = "instance",
                description = "The ID of a NAT instance in your VPC",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @PropertyDefinition(
                name = "instance_owner-Id",
                description = "The AWS account ID of the owner of the instance"
        )
        val instanceOwnerId: String?,

        @AssociationDefinition(
                name = "nat_gateway",
                description = "The NAT gateway",
                targetClass = EC2NatGateway::class
        )
        val natGatewayId: String?,

        @AssociationDefinition(
                name = "transit_gateway",
                description = "The transit gateway",
                targetClass = EC2TransitGateway::class
        )
        val transitGatewayId: String?,

        @AssociationDefinition(
                name = "local_gateway",
                description = "The local gateway",
                targetClass = EC2LocalGateway::class
        )
        val localGatewayId: String?,

        @AssociationDefinition(
                name = "network_interface",
                description = "The network interface",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceId: String?,

        @PropertyDefinition(
                name = "origin",
                description = "Describes how the route was created",
                exampleValues = "CreateRouteTable | CreateRoute | EnableVgwRoutePropagation"
        )
        val origin: String?,

        @PropertyDefinition(
                name = "state",
                description = "The state of the route",
                exampleValues = "active | blackhole"
        )
        val state: String?,

        @AssociationDefinition(
                name = "vpc_peering_connection",
                description = "The VPC peering connection",
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
)
