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
import software.amazon.awssdk.services.ec2.model.VpcCidrBlockState;
import software.amazon.awssdk.services.ec2.model.VpcIpv6CidrBlockAssociation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2VpcIpv6CidrBlockAssociation {
//    @AssociationDefinition(
//            name = "association",
//            description = "The association for the IPv6 CIDR block"
//    )
//    private final String associationId;

    @PropertyDefinition(
            name = "ipv6_cidr_block",
            description = "The IPv6 CIDR block"
    )
    private final String ipv6CidrBlock;

    @PropertyDefinition(
            name = "ipv6_cidr_block_state",
            description = "Information about the state of the CIDR block"
    )
    private final String ipv6CidrBlockState;

    @PropertyDefinition(
            name = "network_border_group",
            description = "The name of the location from which we advertise the IPV6 CIDR block"
    )
    private final String networkBorderGroup;

    @PropertyDefinition(
            name = "ipv6_pool",
            description = "The ID of the IPv6 address pool from which the IPv6 CIDR block is allocated"
    )
    private final String ipv6Pool;

    public EC2VpcIpv6CidrBlockAssociation(String ipv6CidrBlock, String ipv6CidrBlockState, String networkBorderGroup, String ipv6Pool) {
        this.ipv6CidrBlock = ipv6CidrBlock;
        this.ipv6CidrBlockState = ipv6CidrBlockState;
        this.networkBorderGroup = networkBorderGroup;
        this.ipv6Pool = ipv6Pool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof VpcIpv6CidrBlockAssociation))) {
            return false;
        }

        if (o instanceof VpcIpv6CidrBlockAssociation) {
            return sdkEquals((VpcIpv6CidrBlockAssociation) o);
        }

        EC2VpcIpv6CidrBlockAssociation that = (EC2VpcIpv6CidrBlockAssociation) o;
        return Objects.equals(ipv6CidrBlock, that.ipv6CidrBlock) &&
                Objects.equals(ipv6CidrBlockState, that.ipv6CidrBlockState) &&
                Objects.equals(networkBorderGroup, that.networkBorderGroup) &&
                Objects.equals(ipv6Pool, that.ipv6Pool);
    }

    public boolean sdkEquals(VpcIpv6CidrBlockAssociation that) {
        return Objects.equals(ipv6CidrBlock, that.ipv6CidrBlock()) &&
                Objects.equals(ipv6CidrBlockState, ipv6CidrBlockStateFromSdk(that.ipv6CidrBlockState())) &&
                Objects.equals(networkBorderGroup, that.networkBorderGroup()) &&
                Objects.equals(ipv6Pool, that.ipv6Pool());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipv6CidrBlock, ipv6CidrBlockState, networkBorderGroup, ipv6Pool);
    }

    public static List<EC2VpcIpv6CidrBlockAssociation> fromSdk(List<VpcIpv6CidrBlockAssociation> vpcIpv6CidrBlockAssociations) {
        return Optional.ofNullable(vpcIpv6CidrBlockAssociations)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2VpcIpv6CidrBlockAssociation::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2VpcIpv6CidrBlockAssociation fromSdk(VpcIpv6CidrBlockAssociation vpcIpv6CidrBlockAssociation) {
        return Optional.ofNullable(vpcIpv6CidrBlockAssociation)
                       .map(v ->
                               new EC2VpcIpv6CidrBlockAssociation(
                                       v.ipv6CidrBlock(),
                                       ipv6CidrBlockStateFromSdk(v.ipv6CidrBlockState()),
                                       v.networkBorderGroup(),
                                       v.ipv6Pool()
                               )
                       )
                       .orElse(null);
    }

    public static String ipv6CidrBlockStateFromSdk(VpcCidrBlockState vpcCidrBlockState) {
        return Optional.ofNullable(vpcCidrBlockState)
                       .map(VpcCidrBlockState::stateAsString)
                       .orElse(null);
    }
}
