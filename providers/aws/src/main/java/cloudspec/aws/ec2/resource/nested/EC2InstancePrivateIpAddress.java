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
import software.amazon.awssdk.services.ec2.model.InstancePrivateIpAddress;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InstancePrivateIpAddress {
    @PropertyDefinition(
            name = "primary",
            description = "Indicates whether this IPv4 address is the primary private IP address of the network interface"
    )
    private final Boolean primary;

    @PropertyDefinition(
            name = "private_dns_name",
            description = "The private IPv4 DNS name"
    )
    private final String privateDnsName;

    @PropertyDefinition(
            name = "private_ip_address",
            description = "The private IPv4 address of the network interface"
    )
    private final String privateIpAddress;

    public EC2InstancePrivateIpAddress(Boolean primary, String privateDnsName, String privateIpAddress) {
        this.primary = primary;
        this.privateDnsName = privateDnsName;
        this.privateIpAddress = privateIpAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof InstancePrivateIpAddress))) {
            return false;
        }

        if (o instanceof InstancePrivateIpAddress) {
            return sdkEquals((InstancePrivateIpAddress) o);
        }

        EC2InstancePrivateIpAddress that = (EC2InstancePrivateIpAddress) o;
        return Objects.equals(primary, that.primary) &&
                Objects.equals(privateDnsName, that.privateDnsName) &&
                Objects.equals(privateIpAddress, that.privateIpAddress);
    }

    private boolean sdkEquals(InstancePrivateIpAddress that) {
        return Objects.equals(primary, that.primary()) &&
                Objects.equals(privateDnsName, that.privateDnsName()) &&
                Objects.equals(privateIpAddress, that.privateIpAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(primary, privateDnsName, privateIpAddress);
    }

    public static List<EC2InstancePrivateIpAddress> fromSdk(List<InstancePrivateIpAddress> instancePrivateIpAddress) {
        return Optional.ofNullable(instancePrivateIpAddress)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2InstancePrivateIpAddress::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2InstancePrivateIpAddress fromSdk(InstancePrivateIpAddress instancePrivateIpAddress) {
        return Optional.ofNullable(instancePrivateIpAddress)
                       .map(v -> new EC2InstancePrivateIpAddress(
                                       v.primary(),
                                       v.privateDnsName(),
                                       v.privateIpAddress()
                               )
                       )
                       .orElse(null);
    }
}
