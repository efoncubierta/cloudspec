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
import cloudspec.aws.ec2.resource.EC2RouteTableResource;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Objects;
import java.util.Optional;

public class EC2TransitGatewayOptions {
    @PropertyDefinition(
            name = "auto_accept_shared_attachments",
            description = "Indicates whether attachment requests are automatically accepted"
    )
    private final Boolean autoAcceptSharedAttachments;

    @PropertyDefinition(
            name = "default_route_table_association",
            description = "Indicates whether resource attachments are automatically associated with the default association route table"
    )
    private final Boolean defaultRouteTableAssociation;

    @AssociationDefinition(
            name = "association_default_route_table",
            description = "The default association route table",
            targetClass = EC2RouteTableResource.class
    )
    private final String associationDefaultRouteTableId;

    @PropertyDefinition(
            name = "default_route_table_propagation",
            description = "Indicates whether resource attachments automatically propagate routes to the default propagation route table"
    )
    private final Boolean defaultRouteTablePropagation;

    @AssociationDefinition(
            name = "propagation_default_route_table",
            description = "The default propagation route table",
            targetClass = EC2RouteTableResource.class
    )
    private final String propagationDefaultRouteTableId;

    @PropertyDefinition(
            name = "vpn_ecmp_support",
            description = "Indicates whether Equal Cost Multipath Protocol support is enabled"
    )
    private final Boolean vpnEcmpSupport;

    @PropertyDefinition(
            name = "dns_support",
            description = "Indicates whether DNS support is enabled"
    )
    private final Boolean dnsSupport;

    @PropertyDefinition(
            name = "multicast_support",
            description = "Indicates whether multicast is enabled on the transit gateway"
    )
    private final Boolean multicastSupport;

    public EC2TransitGatewayOptions(Boolean autoAcceptSharedAttachments, Boolean defaultRouteTableAssociation,
                                    String associationDefaultRouteTableId, Boolean defaultRouteTablePropagation,
                                    String propagationDefaultRouteTableId, Boolean vpnEcmpSupport, Boolean dnsSupport,
                                    Boolean multicastSupport) {
        this.autoAcceptSharedAttachments = autoAcceptSharedAttachments;
        this.defaultRouteTableAssociation = defaultRouteTableAssociation;
        this.associationDefaultRouteTableId = associationDefaultRouteTableId;
        this.defaultRouteTablePropagation = defaultRouteTablePropagation;
        this.propagationDefaultRouteTableId = propagationDefaultRouteTableId;
        this.vpnEcmpSupport = vpnEcmpSupport;
        this.dnsSupport = dnsSupport;
        this.multicastSupport = multicastSupport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2TransitGatewayOptions that = (EC2TransitGatewayOptions) o;
        return Objects.equals(autoAcceptSharedAttachments, that.autoAcceptSharedAttachments) &&
                Objects.equals(defaultRouteTableAssociation, that.defaultRouteTableAssociation) &&
                Objects.equals(associationDefaultRouteTableId, that.associationDefaultRouteTableId) &&
                Objects.equals(defaultRouteTablePropagation, that.defaultRouteTablePropagation) &&
                Objects.equals(propagationDefaultRouteTableId, that.propagationDefaultRouteTableId) &&
                Objects.equals(vpnEcmpSupport, that.vpnEcmpSupport) &&
                Objects.equals(dnsSupport, that.dnsSupport) &&
                Objects.equals(multicastSupport, that.multicastSupport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autoAcceptSharedAttachments, defaultRouteTableAssociation, associationDefaultRouteTableId, defaultRouteTablePropagation, propagationDefaultRouteTableId, vpnEcmpSupport, dnsSupport, multicastSupport);
    }

    public static EC2TransitGatewayOptions fromSdk(TransitGatewayOptions transitGatewayOptions) {
        return Optional.ofNullable(transitGatewayOptions)
                .map(v -> new EC2TransitGatewayOptions(
                        v.autoAcceptSharedAttachments().equals(AutoAcceptSharedAttachmentsValue.ENABLE),
                        v.defaultRouteTableAssociation().equals(DefaultRouteTableAssociationValue.ENABLE),
                        v.associationDefaultRouteTableId(),
                        v.defaultRouteTablePropagation().equals(DefaultRouteTablePropagationValue.ENABLE),
                        v.propagationDefaultRouteTableId(),
                        v.vpnEcmpSupport().equals(VpnEcmpSupportValue.ENABLE),
                        v.dnsSupport().equals(DnsSupportValue.ENABLE),
                        v.multicastSupport().equals(MulticastSupportValue.ENABLE)
                ))
                .orElse(null);
    }
}
