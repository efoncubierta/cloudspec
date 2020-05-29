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

import cloudspec.annotation.PropertyDefinition;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnectionOptionsDescription;

import java.util.Objects;
import java.util.Optional;

public class EC2VpcPeeringConnectionOptionsDescription {
    @PropertyDefinition(
            name = "allow_dns_resolution_from_remote_vpc",
            description = "Indicates whether a local VPC can resolve public DNS hostnames to private IP addresses when queried from instances in a peer VPC"
    )
    private final Boolean allowDnsResolutionFromRemoteVpc;

    @PropertyDefinition(
            name = "allow_egress_from_local_classic_link_to_remote_vpc",
            description = "Indicates whether a local ClassicLink connection can communicate with the peer VPC over the VPC peering connection"
    )
    private final Boolean allowEgressFromLocalClassicLinkToRemoteVpc;

    @PropertyDefinition(
            name = "allow_egress_from_local_vpc_to_remote_classic_link",
            description = "Indicates whether a local VPC can communicate with a ClassicLink connection in the peer VPC over the VPC peering connection"
    )
    private final Boolean allowEgressFromLocalVpcToRemoteClassicLink;

    public EC2VpcPeeringConnectionOptionsDescription(Boolean allowDnsResolutionFromRemoteVpc,
                                                     Boolean allowEgressFromLocalClassicLinkToRemoteVpc,
                                                     Boolean allowEgressFromLocalVpcToRemoteClassicLink) {
        this.allowDnsResolutionFromRemoteVpc = allowDnsResolutionFromRemoteVpc;
        this.allowEgressFromLocalClassicLinkToRemoteVpc = allowEgressFromLocalClassicLinkToRemoteVpc;
        this.allowEgressFromLocalVpcToRemoteClassicLink = allowEgressFromLocalVpcToRemoteClassicLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2VpcPeeringConnectionOptionsDescription that = (EC2VpcPeeringConnectionOptionsDescription) o;
        return Objects.equals(allowDnsResolutionFromRemoteVpc, that.allowDnsResolutionFromRemoteVpc) &&
                Objects.equals(allowEgressFromLocalClassicLinkToRemoteVpc, that.allowEgressFromLocalClassicLinkToRemoteVpc) &&
                Objects.equals(allowEgressFromLocalVpcToRemoteClassicLink, that.allowEgressFromLocalVpcToRemoteClassicLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowDnsResolutionFromRemoteVpc, allowEgressFromLocalClassicLinkToRemoteVpc,
                allowEgressFromLocalVpcToRemoteClassicLink);
    }

    public static EC2VpcPeeringConnectionOptionsDescription fromSdk(VpcPeeringConnectionOptionsDescription vpcPeeringConnectionOptionsDescription) {
        return Optional.ofNullable(vpcPeeringConnectionOptionsDescription)
                .map(v ->
                        new EC2VpcPeeringConnectionOptionsDescription(
                                v.allowDnsResolutionFromRemoteVpc(),
                                v.allowEgressFromLocalClassicLinkToRemoteVpc(),
                                v.allowEgressFromLocalVpcToRemoteClassicLink()
                        )
                )
                .orElse(null);
    }
}
