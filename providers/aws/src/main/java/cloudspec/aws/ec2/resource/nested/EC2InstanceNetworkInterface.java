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
import cloudspec.aws.ec2.resource.EC2SecurityGroupResource;
import cloudspec.aws.ec2.resource.EC2SubnetResource;
import cloudspec.aws.ec2.resource.EC2VpcResource;
import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.InstanceIpv6Address;
import software.amazon.awssdk.services.ec2.model.InstanceNetworkInterface;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InstanceNetworkInterface {
    @AssociationDefinition(
            name = "groups",
            description = "One or more security groups",
            targetClass = EC2SecurityGroupResource.class
    )
    private final List<String> groupIds;

    @PropertyDefinition(
            name = "ipv6_addresses",
            description = "One or more IPv6 addresses associated with the network interface"
    )
    private final List<String> ipv6Addresses;

    @AssociationDefinition(
            name = "network_interface",
            description = "The network interface",
            targetClass = EC2NetworkInterfaceResource.class
    )
    private final String networkInterfaceId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that created the network interface"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "private_dns_name",
            description = "The private DNS name"
    )
    private final String privateDnsName;

    @PropertyDefinition(
            name = "private_ip_address",
            description = "The IPv4 address of the network interface within the subnet"
    )
    private final String privateIpAddress;

    @PropertyDefinition(
            name = "private_ip_addresses",
            description = "One or more private IPv4 addresses associated with the network interface"
    )
    private final List<EC2InstancePrivateIpAddress> privateIpAddresses;

    @PropertyDefinition(
            name = "source_dest_check",
            description = "Indicates whether to validate network traffic to or from this network interface"
    )
    private final Boolean sourceDestCheck;

    @PropertyDefinition(
            name = "status",
            description = "The status of the network interface",
            exampleValues = "available | associated | attached | in-use | detaching"
    )
    private final String status;

    @AssociationDefinition(
            name = "subnet",
            description = "The subnet",
            targetClass = EC2SubnetResource.class
    )
    private final String subnetId;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "interface_type",
            description = "Describes the type of network interface",
            exampleValues = "interface | efa"
    )
    private final String interfaceType;

    public EC2InstanceNetworkInterface(List<String> groupIds, List<String> ipv6Addresses,
                                       String networkInterfaceId, String ownerId, String privateDnsName,
                                       String privateIpAddress, List<EC2InstancePrivateIpAddress> privateIpAddresses,
                                       Boolean sourceDestCheck, String status, String subnetId, String vpcId,
                                       String interfaceType) {
        this.groupIds = groupIds;
        this.ipv6Addresses = ipv6Addresses;
        this.networkInterfaceId = networkInterfaceId;
        this.ownerId = ownerId;
        this.privateDnsName = privateDnsName;
        this.privateIpAddress = privateIpAddress;
        this.privateIpAddresses = privateIpAddresses;
        this.sourceDestCheck = sourceDestCheck;
        this.status = status;
        this.subnetId = subnetId;
        this.vpcId = vpcId;
        this.interfaceType = interfaceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2InstanceNetworkInterface that = (EC2InstanceNetworkInterface) o;
        return Objects.equals(groupIds, that.groupIds) &&
                Objects.equals(ipv6Addresses, that.ipv6Addresses) &&
                Objects.equals(networkInterfaceId, that.networkInterfaceId) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(privateDnsName, that.privateDnsName) &&
                Objects.equals(privateIpAddress, that.privateIpAddress) &&
                Objects.equals(privateIpAddresses, that.privateIpAddresses) &&
                Objects.equals(sourceDestCheck, that.sourceDestCheck) &&
                Objects.equals(status, that.status) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(interfaceType, that.interfaceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupIds, ipv6Addresses, networkInterfaceId, ownerId, privateDnsName,
                privateIpAddress, privateIpAddresses, sourceDestCheck, status, subnetId, vpcId, interfaceType);
    }

    public static List<EC2InstanceNetworkInterface> fromSdk(List<InstanceNetworkInterface> instanceNetworkInterfaces) {
        return Optional.ofNullable(instanceNetworkInterfaces)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2InstanceNetworkInterface::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2InstanceNetworkInterface fromSdk(InstanceNetworkInterface instanceNetworkInterface) {
        return new EC2InstanceNetworkInterface(
                securityGroupIdsFromSdk(instanceNetworkInterface.groups()),
                ipv6AddressesFromSdk(instanceNetworkInterface.ipv6Addresses()),
                instanceNetworkInterface.networkInterfaceId(),
                instanceNetworkInterface.ownerId(),
                instanceNetworkInterface.privateDnsName(),
                instanceNetworkInterface.privateIpAddress(),
                EC2InstancePrivateIpAddress.fromSdk(instanceNetworkInterface.privateIpAddresses()),
                instanceNetworkInterface.sourceDestCheck(),
                instanceNetworkInterface.statusAsString(),
                instanceNetworkInterface.subnetId(),
                instanceNetworkInterface.vpcId(),
                instanceNetworkInterface.interfaceType()
        );
    }

    public static List<String> securityGroupIdsFromSdk(List<GroupIdentifier> groupIdentifiers) {
        return Optional.ofNullable(groupIdentifiers)
                .orElse(Collections.emptyList())
                .stream()
                .map(GroupIdentifier::groupId)
                .collect(Collectors.toList());
    }

    public static List<String> ipv6AddressesFromSdk(List<InstanceIpv6Address> instanceIpv6Addresses) {
        return Optional.ofNullable(instanceIpv6Addresses)
                .orElse(Collections.emptyList())
                .stream()
                .map(InstanceIpv6Address::ipv6Address)
                .collect(Collectors.toList());
    }

}
