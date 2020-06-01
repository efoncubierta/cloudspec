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
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.aws.ec2.resource.*;
import software.amazon.awssdk.services.ec2.model.Route;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2Route {
    @PropertyDefinition(
            name = "destination_cidr_block",
            description = "The IPv4 CIDR block used for the destination match"
    )
    private final String destinationCidrBlock;

    @PropertyDefinition(
            name = "destination_ipv6_cidr_block",
            description = "The IPv6 CIDR block used for the destination match"
    )
    private final String destinationIpv6CidrBlock;

    @PropertyDefinition(
            name = "destination_prefix_list_id",
            description = "The prefix of the AWS service"
    )
    private final String destinationPrefixListId;

    @AssociationDefinition(
            name = "egress_only_internet_gateway",
            description = "The egress-only internet gateway",
            targetClass = EC2InternetGatewayResource.class
    )
    private final String egressOnlyInternetGatewayId;

    @AssociationDefinition(
            name = "gateway",
            description = "The gateway attached to your VPC",
            targetClass = EC2InternetGatewayResource.class
    )
    private final String gatewayId;
    // TODO support vpwg

    @AssociationDefinition(
            name = "instance",
            description = "The ID of a NAT instance in your VPC",
            targetClass = EC2InstanceResource.class
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "instance_owner-Id",
            description = "The AWS account ID of the owner of the instance"
    )
    private final String instanceOwnerId;

    @AssociationDefinition(
            name = "nat_gateway",
            description = "The NAT gateway",
            targetClass = EC2NatGatewayResource.class
    )
    private final String natGatewayId;

    @AssociationDefinition(
            name = "transit_gateway",
            description = "The transit gateway",
            targetClass = EC2TransitGatewayResource.class
    )
    private final String transitGatewayId;

    @AssociationDefinition(
            name = "local_gateway",
            description = "The local gateway",
            targetClass = EC2LocalGatewayResource.class
    )
    private final String localGatewayId;

    @AssociationDefinition(
            name = "network_interface",
            description = "The network interface",
            targetClass = EC2NetworkInterfaceResource.class
    )
    private final String networkInterfaceId;

    @PropertyDefinition(
            name = "origin",
            description = "Describes how the route was created",
            exampleValues = "CreateRouteTable | CreateRoute | EnableVgwRoutePropagation"
    )
    private final String origin;

    @PropertyDefinition(
            name = "state",
            description = "The state of the route",
            exampleValues = "active | blackhole"
    )
    private final String state;

    @AssociationDefinition(
            name = "vpc_peering_connection",
            description = "The VPC peering connection",
            targetClass = EC2VpcPeeringConnectionResource.class
    )
    private final String vpcPeeringConnectionId;

    public EC2Route(String destinationCidrBlock, String destinationIpv6CidrBlock, String destinationPrefixListId,
                    String egressOnlyInternetGatewayId, String gatewayId, String instanceId, String instanceOwnerId,
                    String natGatewayId, String transitGatewayId, String localGatewayId, String networkInterfaceId,
                    String origin, String state, String vpcPeeringConnectionId) {
        this.destinationCidrBlock = destinationCidrBlock;
        this.destinationIpv6CidrBlock = destinationIpv6CidrBlock;
        this.destinationPrefixListId = destinationPrefixListId;
        this.egressOnlyInternetGatewayId = egressOnlyInternetGatewayId;
        this.gatewayId = gatewayId;
        this.instanceId = instanceId;
        this.instanceOwnerId = instanceOwnerId;
        this.natGatewayId = natGatewayId;
        this.transitGatewayId = transitGatewayId;
        this.localGatewayId = localGatewayId;
        this.networkInterfaceId = networkInterfaceId;
        this.origin = origin;
        this.state = state;
        this.vpcPeeringConnectionId = vpcPeeringConnectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof Route))) {
            return false;
        }

        if (o instanceof Route) {
            return sdkEquals((Route) o);
        }

        EC2Route ec2Route = (EC2Route) o;
        return Objects.equals(destinationCidrBlock, ec2Route.destinationCidrBlock) &&
                Objects.equals(destinationIpv6CidrBlock, ec2Route.destinationIpv6CidrBlock) &&
                Objects.equals(destinationPrefixListId, ec2Route.destinationPrefixListId) &&
                Objects.equals(egressOnlyInternetGatewayId, ec2Route.egressOnlyInternetGatewayId) &&
                Objects.equals(gatewayId, ec2Route.gatewayId) &&
                Objects.equals(instanceId, ec2Route.instanceId) &&
                Objects.equals(instanceOwnerId, ec2Route.instanceOwnerId) &&
                Objects.equals(natGatewayId, ec2Route.natGatewayId) &&
                Objects.equals(transitGatewayId, ec2Route.transitGatewayId) &&
                Objects.equals(localGatewayId, ec2Route.localGatewayId) &&
                Objects.equals(networkInterfaceId, ec2Route.networkInterfaceId) &&
                Objects.equals(origin, ec2Route.origin) &&
                Objects.equals(state, ec2Route.state) &&
                Objects.equals(vpcPeeringConnectionId, ec2Route.vpcPeeringConnectionId);
    }

    private boolean sdkEquals(Route that) {
        return Objects.equals(destinationCidrBlock, that.destinationCidrBlock()) &&
                Objects.equals(destinationIpv6CidrBlock, that.destinationIpv6CidrBlock()) &&
                Objects.equals(destinationPrefixListId, that.destinationPrefixListId()) &&
                Objects.equals(egressOnlyInternetGatewayId, that.egressOnlyInternetGatewayId()) &&
                Objects.equals(gatewayId, that.gatewayId()) &&
                Objects.equals(instanceId, that.instanceId()) &&
                Objects.equals(instanceOwnerId, that.instanceOwnerId()) &&
                Objects.equals(natGatewayId, that.natGatewayId()) &&
                Objects.equals(transitGatewayId, that.transitGatewayId()) &&
                Objects.equals(localGatewayId, that.localGatewayId()) &&
                Objects.equals(networkInterfaceId, that.networkInterfaceId()) &&
                Objects.equals(origin, that.originAsString()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(vpcPeeringConnectionId, that.vpcPeeringConnectionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationCidrBlock, destinationIpv6CidrBlock, destinationPrefixListId,
                egressOnlyInternetGatewayId, gatewayId, instanceId, instanceOwnerId, natGatewayId, transitGatewayId,
                localGatewayId, networkInterfaceId, origin, state, vpcPeeringConnectionId);
    }

    public static List<EC2Route> fromSdk(List<Route> routes) {
        return Optional.ofNullable(routes)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2Route::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2Route fromSdk(Route route) {
        return Optional.ofNullable(route)
                       .map(v -> new EC2Route(
                               v.destinationCidrBlock(),
                               v.destinationIpv6CidrBlock(),
                               v.destinationPrefixListId(),
                               v.egressOnlyInternetGatewayId(),
                               v.gatewayId(),
                               v.instanceId(),
                               v.instanceOwnerId(),
                               v.natGatewayId(),
                               v.transitGatewayId(),
                               v.localGatewayId(),
                               v.networkInterfaceId(),
                               v.originAsString(),
                               v.stateAsString(),
                               v.vpcPeeringConnectionId()
                       ))
                       .orElse(null);
    }
}
