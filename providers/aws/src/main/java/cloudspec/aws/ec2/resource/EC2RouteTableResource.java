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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2Route;
import cloudspec.aws.ec2.resource.nested.EC2RouteTableAssociation;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.PropagatingVgw;
import software.amazon.awssdk.services.ec2.model.RouteTable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2RouteTableResource.RESOURCE_NAME,
        description = "Route Table"
)
public class EC2RouteTableResource extends EC2Resource {
    public static final String RESOURCE_NAME = "route_table";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "associations",
            description = "The associations between the route table and one or more subnets or a gateway"
    )
    private final List<EC2RouteTableAssociation> associations;

    @PropertyDefinition(
            name = "propagating_gateway",
            description = "Any virtual private gateway (VGW) propagating routes"
    )
    private final List<String> propagatingGatewayIds;

    @IdDefinition
    @PropertyDefinition(
            name = "route_table_id",
            description = "The ID of the route table"
    )
    private final String routeTableId;

    @PropertyDefinition(
            name = "routes",
            description = "The routes in the route table"
    )
    private final List<EC2Route> routes;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the route table"
    )
    private final List<KeyValue> tags;

    @AssociationDefinition(
            name = "vpc",
            description = "The ID of the VPC",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the route table"
    )
    private final String ownerId;

    public EC2RouteTableResource(String region, List<EC2RouteTableAssociation> associations,
                                 List<String> propagatingGatewayIds, String routeTableId, List<EC2Route> routes,
                                 List<KeyValue> tags, String vpcId, String ownerId) {
        this.region = region;
        this.associations = associations;
        this.propagatingGatewayIds = propagatingGatewayIds;
        this.routeTableId = routeTableId;
        this.routes = routes;
        this.tags = tags;
        this.vpcId = vpcId;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2RouteTableResource that = (EC2RouteTableResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(associations, that.associations) &&
                Objects.equals(propagatingGatewayIds, that.propagatingGatewayIds) &&
                Objects.equals(routeTableId, that.routeTableId) &&
                Objects.equals(routes, that.routes) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, associations, propagatingGatewayIds, routeTableId, routes, tags, vpcId, ownerId);
    }

    public static EC2RouteTableResource fromSdk(String regionName, RouteTable routeTable) {
        return Optional.ofNullable(routeTable)
                .map(v -> new EC2RouteTableResource(
                        regionName,
                        EC2RouteTableAssociation.fromSdk(v.associations()),
                        propagatingGatewayIdsFromSdk(v.propagatingVgws()),
                        v.routeTableId(),
                        EC2Route.fromSdk(v.routes()),
                        tagsFromSdk(v.tags()),
                        v.vpcId(),
                        v.ownerId()
                ))
                .orElse(null);
    }

    public static List<String> propagatingGatewayIdsFromSdk(List<PropagatingVgw> propagatingVgws) {
        return Optional.ofNullable(propagatingVgws)
                .orElse(Collections.emptyList())
                .stream()
                .map(PropagatingVgw::gatewayId)
                .collect(Collectors.toList());
    }
}
