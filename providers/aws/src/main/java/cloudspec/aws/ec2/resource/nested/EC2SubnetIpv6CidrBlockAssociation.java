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
import software.amazon.awssdk.services.ec2.model.SubnetCidrBlockState;
import software.amazon.awssdk.services.ec2.model.SubnetIpv6CidrBlockAssociation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2SubnetIpv6CidrBlockAssociation {
//    @AssociationDefinition(
//            name = "association",
//            description = ""
//    )
//    private final String associationId;

    @PropertyDefinition(
            name = "ipv6_cidr_block",
            description = "The IPv6 CIDR block"
    )
    private final String ipv6CidrBlock;

    @PropertyDefinition(
            name = "ipv6_cidr_block_state",
            description = "Information about the state of the CIDR block",
            exampleValues = "associating | associated | disassociating | disassociated | failing | failed"
    )
    private final String ipv6CidrBlockState;

    public EC2SubnetIpv6CidrBlockAssociation(String ipv6CidrBlock, String ipv6CidrBlockState) {
        this.ipv6CidrBlock = ipv6CidrBlock;
        this.ipv6CidrBlockState = ipv6CidrBlockState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2SubnetIpv6CidrBlockAssociation that = (EC2SubnetIpv6CidrBlockAssociation) o;
        return Objects.equals(ipv6CidrBlock, that.ipv6CidrBlock) &&
                Objects.equals(ipv6CidrBlockState, that.ipv6CidrBlockState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipv6CidrBlock, ipv6CidrBlockState);
    }

    public static List<EC2SubnetIpv6CidrBlockAssociation> fromSdk(List<SubnetIpv6CidrBlockAssociation> subnetIpv6CidrBlockAssociations) {
        if (Objects.isNull(subnetIpv6CidrBlockAssociations)) {
            return Collections.emptyList();
        }

        return subnetIpv6CidrBlockAssociations.stream()
                .map(EC2SubnetIpv6CidrBlockAssociation::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2SubnetIpv6CidrBlockAssociation fromSdk(SubnetIpv6CidrBlockAssociation subnetIpv6CidrBlockAssociation) {
        return Optional.ofNullable(subnetIpv6CidrBlockAssociation)
                .map(v ->
                        new EC2SubnetIpv6CidrBlockAssociation(
                                v.ipv6CidrBlock(),
                                ipv6CidrBlockStateFromSdk(v.ipv6CidrBlockState())
                        )
                )
                .orElse(null);
    }

    public static String ipv6CidrBlockStateFromSdk(SubnetCidrBlockState subnetCidrBlockState) {
        return Optional.ofNullable(subnetCidrBlockState)
                .map(SubnetCidrBlockState::stateAsString)
                .orElse(null);
    }
}
