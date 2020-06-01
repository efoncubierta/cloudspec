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
import cloudspec.aws.ec2.resource.EC2NetworkInterfaceResource;
import software.amazon.awssdk.services.ec2.model.NatGatewayAddress;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2NatGatewayAddress {
    @PropertyDefinition(
            name = "allocation_id",
            description = "The allocation ID of the Elastic IP address that's associated with the NAT gateway"
    )
    private final String allocationId;

    @AssociationDefinition(
            name = "network_interface",
            description = "The ID of the network interface associated with the NAT gateway",
            targetClass = EC2NetworkInterfaceResource.class
    )
    private final String networkInterfaceId;

    @PropertyDefinition(
            name = "private_ip",
            description = "The private IP address associated with the Elastic IP address"
    )
    private final String privateIp;

    @PropertyDefinition(
            name = "public_ip",
            description = "The Elastic IP address associated with the NAT gateway"
    )
    private final String publicIp;

    public EC2NatGatewayAddress(String allocationId, String networkInterfaceId, String privateIp, String publicIp) {
        this.allocationId = allocationId;
        this.networkInterfaceId = networkInterfaceId;
        this.privateIp = privateIp;
        this.publicIp = publicIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof NatGatewayAddress))) {
            return false;
        }

        if (o instanceof NatGatewayAddress) {
            return sdkEquals((NatGatewayAddress) o);
        }

        EC2NatGatewayAddress that = (EC2NatGatewayAddress) o;
        return Objects.equals(allocationId, that.allocationId) &&
                Objects.equals(networkInterfaceId, that.networkInterfaceId) &&
                Objects.equals(privateIp, that.privateIp) &&
                Objects.equals(publicIp, that.publicIp);
    }

    private boolean sdkEquals(NatGatewayAddress that) {
        return Objects.equals(allocationId, that.allocationId()) &&
                Objects.equals(networkInterfaceId, that.networkInterfaceId()) &&
                Objects.equals(privateIp, that.privateIp()) &&
                Objects.equals(publicIp, that.publicIp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(allocationId, networkInterfaceId, privateIp, publicIp);
    }

    public static List<EC2NatGatewayAddress> fromSdk(List<NatGatewayAddress> natGatewayAddresses) {
        return Optional.ofNullable(natGatewayAddresses)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2NatGatewayAddress::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2NatGatewayAddress fromSdk(NatGatewayAddress natGatewayAddress) {
        return Optional.ofNullable(natGatewayAddress)
                       .map(v -> new EC2NatGatewayAddress(
                               v.allocationId(),
                               v.networkInterfaceId(),
                               v.privateIp(),
                               v.publicIp()
                       ))
                       .orElse(null);
    }
}
