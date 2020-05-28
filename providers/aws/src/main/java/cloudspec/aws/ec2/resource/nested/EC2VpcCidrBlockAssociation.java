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
import software.amazon.awssdk.services.ec2.model.VpcCidrBlockAssociation;
import software.amazon.awssdk.services.ec2.model.VpcCidrBlockState;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2VpcCidrBlockAssociation {
    //    @AssociationDefinition(
//            name = "association",
//            description = "The association ID for the IPv4 CIDR block"
//    )
//    private final String associationId;

    @PropertyDefinition(
            name = "cidr_block",
            description = "The IPv4 CIDR block"
    )
    private final String cidrBlock;

    @PropertyDefinition(
            name = "cidr_block_state",
            description = "Information about the state of the CIDR block"
    )
    private final String cidrBlockState;

    public EC2VpcCidrBlockAssociation(String cidrBlock, String cidrBlockState) {
        this.cidrBlock = cidrBlock;
        this.cidrBlockState = cidrBlockState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2VpcCidrBlockAssociation that = (EC2VpcCidrBlockAssociation) o;
        return Objects.equals(cidrBlock, that.cidrBlock) &&
                Objects.equals(cidrBlockState, that.cidrBlockState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidrBlock, cidrBlockState);
    }

    public static List<EC2VpcCidrBlockAssociation> fromSdk(List<VpcCidrBlockAssociation> vpcCidrBlockAssociations) {
        if (Objects.isNull(vpcCidrBlockAssociations)) {
            return Collections.emptyList();
        }

        return vpcCidrBlockAssociations.stream()
                .map(EC2VpcCidrBlockAssociation::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2VpcCidrBlockAssociation fromSdk(VpcCidrBlockAssociation vpcCidrBlockAssociation) {
        return Optional.ofNullable(vpcCidrBlockAssociation)
                .map(v ->
                        new EC2VpcCidrBlockAssociation(
                                v.cidrBlock(),
                                ipCidrBlockStateFromSdk(v.cidrBlockState())
                        )
                )
                .orElse(null);
    }

    public static String ipCidrBlockStateFromSdk(VpcCidrBlockState vpcCidrBlockState) {
        return Optional.ofNullable(vpcCidrBlockState)
                .map(VpcCidrBlockState::stateAsString)
                .orElse(null);
    }
}
