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
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.aws.ec2.resource.EC2InternetGatewayResource;
import cloudspec.aws.ec2.resource.EC2RouteTableResource;
import cloudspec.aws.ec2.resource.EC2SubnetResource;
import software.amazon.awssdk.services.ec2.model.RouteTableAssociation;
import software.amazon.awssdk.services.ec2.model.RouteTableAssociationState;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2RouteTableAssociation {
    @PropertyDefinition(
            name = "main",
            description = "Indicates whether this is the main route table"
    )
    private final Boolean main;

    @IdDefinition
    @PropertyDefinition(
            name = "main",
            description = "The ID of the association"
    )
    private final String routeTableAssociationId;

    @AssociationDefinition(
            name = "route_table",
            description = "The route table",
            targetClass = EC2RouteTableResource.class
    )
    private final String routeTableId;

    @AssociationDefinition(
            name = "subnet",
            description = "The subnet",
            targetClass = EC2SubnetResource.class
    )
    private final String subnetId;

    @AssociationDefinition(
            name = "gateway",
            description = "The internet gateway or virtual private gateway",
            targetClass = EC2InternetGatewayResource.class
    )
    private final String gatewayId;
    // TODO support VPG

    @PropertyDefinition(
            name = "state",
            description = "The state of the association",
            exampleValues = "associating | associated | disassociating | disassociated | failed"
    )
    private final String associationState;

    public EC2RouteTableAssociation(Boolean main, String routeTableAssociationId, String routeTableId, String subnetId,
                                    String gatewayId, String associationState) {
        this.main = main;
        this.routeTableAssociationId = routeTableAssociationId;
        this.routeTableId = routeTableId;
        this.subnetId = subnetId;
        this.gatewayId = gatewayId;
        this.associationState = associationState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2RouteTableAssociation that = (EC2RouteTableAssociation) o;
        return Objects.equals(main, that.main) &&
                Objects.equals(routeTableAssociationId, that.routeTableAssociationId) &&
                Objects.equals(routeTableId, that.routeTableId) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(gatewayId, that.gatewayId) &&
                Objects.equals(associationState, that.associationState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(main, routeTableAssociationId, routeTableId, subnetId, gatewayId, associationState);
    }

    public static List<EC2RouteTableAssociation> fromSdk(List<RouteTableAssociation> routeTableAssociations) {
        return Optional.ofNullable(routeTableAssociations)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2RouteTableAssociation::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2RouteTableAssociation fromSdk(RouteTableAssociation routeTableAssociation) {
        return Optional.ofNullable(routeTableAssociation)
                .map(v -> new EC2RouteTableAssociation(
                        v.main(),
                        v.routeTableAssociationId(),
                        v.routeTableId(),
                        v.subnetId(),
                        v.gatewayId(),
                        associationStateFromSdk(v.associationState())
                ))
                .orElse(null);
    }

    public static String associationStateFromSdk(RouteTableAssociationState routeTableAssociationState) {
        return Optional.ofNullable(routeTableAssociationState)
                .map(RouteTableAssociationState::stateAsString)
                .orElse(null);
    }
}
