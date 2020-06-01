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
import cloudspec.aws.ec2.resource.EC2VpcResource;
import software.amazon.awssdk.services.ec2.model.CidrBlock;
import software.amazon.awssdk.services.ec2.model.Ipv6CidrBlock;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnectionVpcInfo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2VpcPeeringConnectionVpcInfo {
    @PropertyDefinition(
            name = "cidr_block",
            description = "The IPv4 CIDR block for the VPC"
    )
    private final String cidrBlock;

    @PropertyDefinition(
            name = "ipv6_cidr_blocks",
            description = "The IPv6 CIDR block for the VPC"
    )
    private final List<String> ipv6CidrBlocks;

    @PropertyDefinition(
            name = "cidr_blocks",
            description = "Information about the IPv4 CIDR blocks for the VPC"
    )
    private final List<String> cidrBlocks;

    @PropertyDefinition(
            name = "owner_id",
            description = "The AWS account ID of the VPC owner"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "peering_options",
            description = "Information about the VPC peering connection options for the accepter or requester VPC"
    )
    private final EC2VpcPeeringConnectionOptionsDescription peeringOptions;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "region",
            description = "The Region in which the VPC is located"
    )
    private final String region;

    public EC2VpcPeeringConnectionVpcInfo(String cidrBlock, List<String> ipv6CidrBlocks, List<String> cidrBlocks,
                                          String ownerId, EC2VpcPeeringConnectionOptionsDescription peeringOptions,
                                          String vpcId, String region) {
        this.cidrBlock = cidrBlock;
        this.ipv6CidrBlocks = ipv6CidrBlocks;
        this.cidrBlocks = cidrBlocks;
        this.ownerId = ownerId;
        this.peeringOptions = peeringOptions;
        this.vpcId = vpcId;
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof VpcPeeringConnectionVpcInfo))) {
            return false;
        }

        if (o instanceof VpcPeeringConnectionVpcInfo) {
            return sdkEquals((VpcPeeringConnectionVpcInfo) o);
        }

        EC2VpcPeeringConnectionVpcInfo that = (EC2VpcPeeringConnectionVpcInfo) o;
        return Objects.equals(cidrBlock, that.cidrBlock) &&
                Objects.equals(ipv6CidrBlocks, that.ipv6CidrBlocks) &&
                Objects.equals(cidrBlocks, that.cidrBlocks) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(peeringOptions, that.peeringOptions) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(region, that.region);
    }

    private boolean sdkEquals(VpcPeeringConnectionVpcInfo that) {
        return Objects.equals(cidrBlock, that.cidrBlock()) &&
                Objects.equals(ipv6CidrBlocks, ipv6CidrBlocksFromSdk(that.ipv6CidrBlockSet())) &&
                Objects.equals(cidrBlocks, cidrBlocksFromSdk(that.cidrBlockSet())) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(peeringOptions, that.peeringOptions()) &&
                Objects.equals(vpcId, that.vpcId()) &&
                Objects.equals(region, that.region());
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidrBlock, ipv6CidrBlocks, cidrBlocks, ownerId, peeringOptions, vpcId, region);
    }

    public static EC2VpcPeeringConnectionVpcInfo fromSdk(VpcPeeringConnectionVpcInfo vpcPeeringConnectionVpcInfo) {
        return Optional.ofNullable(vpcPeeringConnectionVpcInfo)
                       .map(v ->
                               new EC2VpcPeeringConnectionVpcInfo(
                                       v.cidrBlock(),
                                       ipv6CidrBlocksFromSdk(v.ipv6CidrBlockSet()),
                                       cidrBlocksFromSdk(v.cidrBlockSet()),
                                       v.ownerId(),
                                       EC2VpcPeeringConnectionOptionsDescription.fromSdk(v.peeringOptions()),
                                       v.vpcId(),
                                       v.region()
                               )
                       )
                       .orElse(null);
    }

    public static List<String> ipv6CidrBlocksFromSdk(List<Ipv6CidrBlock> ipv6CidrBlocks) {
        return Optional.ofNullable(ipv6CidrBlocks)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(Ipv6CidrBlock::ipv6CidrBlock)
                       .collect(Collectors.toList());
    }

    public static List<String> cidrBlocksFromSdk(List<CidrBlock> cidrBlocks) {
        return Optional.ofNullable(cidrBlocks)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(CidrBlock::cidrBlock)
                       .collect(Collectors.toList());
    }
}
