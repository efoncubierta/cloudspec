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
import software.amazon.awssdk.services.ec2.model.NetworkInterfacePrivateIpAddress;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2NetworkInterfacePrivateIpAddress {
    @PropertyDefinition(
            name = "association",
            description = "The association information for an Elastic IP address (IPv4) associated with the network interface"
    )
    private final EC2NetworkInterfaceAssociation association;

    @PropertyDefinition(
            name = "primary",
            description = "Indicates whether this IPv4 address is the primary private IPv4 address of the network interface"
    )
    private final Boolean primary;

    @PropertyDefinition(
            name = "private_dns_name",
            description = "The private DNS name"
    )
    private final String privateDnsName;

    @PropertyDefinition(
            name = "private_ip_address",
            description = "The private IPv4 address"
    )
    private final String privateIpAddress;

    public EC2NetworkInterfacePrivateIpAddress(EC2NetworkInterfaceAssociation association, Boolean primary,
                                               String privateDnsName, String privateIpAddress) {
        this.association = association;
        this.primary = primary;
        this.privateDnsName = privateDnsName;
        this.privateIpAddress = privateIpAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2NetworkInterfacePrivateIpAddress that = (EC2NetworkInterfacePrivateIpAddress) o;
        return Objects.equals(association, that.association) &&
                Objects.equals(primary, that.primary) &&
                Objects.equals(privateDnsName, that.privateDnsName) &&
                Objects.equals(privateIpAddress, that.privateIpAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(association, primary, privateDnsName, privateIpAddress);
    }

    public static List<EC2NetworkInterfacePrivateIpAddress> fromSdk(List<NetworkInterfacePrivateIpAddress> networkInterfacePrivateIpAddresses) {
        return Optional.ofNullable(networkInterfacePrivateIpAddresses)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2NetworkInterfacePrivateIpAddress::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2NetworkInterfacePrivateIpAddress fromSdk(NetworkInterfacePrivateIpAddress networkInterfacePrivateIpAddress) {
        return Optional.ofNullable(networkInterfacePrivateIpAddress)
                .map(v -> new EC2NetworkInterfacePrivateIpAddress(
                                EC2NetworkInterfaceAssociation.fromSdk(v.association()),
                                v.primary(),
                                v.privateDnsName(),
                                v.privateIpAddress()
                        )
                )
                .orElse(null);
    }
}
