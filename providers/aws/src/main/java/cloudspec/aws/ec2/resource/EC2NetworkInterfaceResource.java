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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2NetworkInterfaceAssociation;
import cloudspec.aws.ec2.resource.nested.EC2NetworkInterfaceAttachment;
import cloudspec.aws.ec2.resource.nested.EC2NetworkInterfacePrivateIpAddress;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.GroupIdentifier;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceIpv6Address;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NetworkInterfaceResource.RESOURCE_NAME,
        description = "Network Interface"
)
public class EC2NetworkInterfaceResource extends EC2Resource {
    public static final String RESOURCE_NAME = "network_interface";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "association",
            description = "The association information for an Elastic IP address (IPv4) associated with the network interface"
    )
    private final EC2NetworkInterfaceAssociation association;

    @PropertyDefinition(
            name = "attachment",
            description = "The network interface attachment"
    )
    private final EC2NetworkInterfaceAttachment attachment;

    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone"
    )
    private final String availabilityZone;

    @AssociationDefinition(
            name = "groups",
            description = "Any security groups for the network interface",
            targetClass = EC2SecurityGroupResource.class
    )
    private final List<String> groupIds;

    @PropertyDefinition(
            name = "interface_type",
            description = "The type of network interface"
    )
    private final String interfaceType;

    @PropertyDefinition(
            name = "ipv6_addresses",
            description = "The IPv6 addresses associated with the network interface"
    )
    private final List<String> ipv6Addresses;

    @IdDefinition
    @PropertyDefinition(
            name = "network_interface_id",
            description = "The ID of the network interface"
    )
    private final String networkInterfaceId;

    @PropertyDefinition(
            name = "outpost_arn",
            description = "The Amazon Resource Name (ARN) of the Outpost"
    )
    private final String outpostArn;

    @PropertyDefinition(
            name = "owner_id",
            description = "The AWS account ID of the owner of the network interface"
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
            description = "The private IPv4 addresses associated with the network interface"
    )
    private final List<EC2NetworkInterfacePrivateIpAddress> privateIpAddresses;

//    @AssociationDefinition(
//            name = "requester",
//            description = ""
//    )
//    private final String requesterId;
//    private final Boolean requesterManaged;

    @PropertyDefinition(
            name = "source_dest_check",
            description = "Indicates whether traffic to or from the instance is validated"
    )
    private final Boolean sourceDestCheck;

    @PropertyDefinition(
            name = "status",
            description = "The status of the network interface",
            exampleValues = "available | associated | attaching | in-use | detaching"
    )
    private final String status;

    @AssociationDefinition(
            name = "subnet",
            description = "The subnet",
            targetClass = EC2SubnetResource.class
    )
    private final String subnetId;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the network interface"
    )
    private final List<KeyValue> tagSet;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    public EC2NetworkInterfaceResource(String region, EC2NetworkInterfaceAssociation association,
                                       EC2NetworkInterfaceAttachment attachment, String availabilityZone,
                                       List<String> groupIds, String interfaceType, List<String> ipv6Addresses,
                                       String networkInterfaceId, String outpostArn, String ownerId, String privateDnsName,
                                       String privateIpAddress, List<EC2NetworkInterfacePrivateIpAddress> privateIpAddresses,
                                       Boolean sourceDestCheck, String status, String subnetId, List<KeyValue> tagSet,
                                       String vpcId) {
        this.region = region;
        this.association = association;
        this.attachment = attachment;
        this.availabilityZone = availabilityZone;
        this.groupIds = groupIds;
        this.interfaceType = interfaceType;
        this.ipv6Addresses = ipv6Addresses;
        this.networkInterfaceId = networkInterfaceId;
        this.outpostArn = outpostArn;
        this.ownerId = ownerId;
        this.privateDnsName = privateDnsName;
        this.privateIpAddress = privateIpAddress;
        this.privateIpAddresses = privateIpAddresses;
        this.sourceDestCheck = sourceDestCheck;
        this.status = status;
        this.subnetId = subnetId;
        this.tagSet = tagSet;
        this.vpcId = vpcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2NetworkInterfaceResource that = (EC2NetworkInterfaceResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(association, that.association) &&
                Objects.equals(attachment, that.attachment) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(groupIds, that.groupIds) &&
                Objects.equals(interfaceType, that.interfaceType) &&
                Objects.equals(ipv6Addresses, that.ipv6Addresses) &&
                Objects.equals(networkInterfaceId, that.networkInterfaceId) &&
                Objects.equals(outpostArn, that.outpostArn) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(privateDnsName, that.privateDnsName) &&
                Objects.equals(privateIpAddress, that.privateIpAddress) &&
                Objects.equals(privateIpAddresses, that.privateIpAddresses) &&
                Objects.equals(sourceDestCheck, that.sourceDestCheck) &&
                Objects.equals(status, that.status) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(tagSet, that.tagSet) &&
                Objects.equals(vpcId, that.vpcId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, association, attachment, availabilityZone, groupIds, interfaceType, ipv6Addresses,
                networkInterfaceId, outpostArn, ownerId, privateDnsName, privateIpAddress, privateIpAddresses,
                sourceDestCheck, status, subnetId, tagSet, vpcId);
    }

    public static EC2NetworkInterfaceResource fromSdk(String regionName, NetworkInterface networkInterface) {
        return Optional.ofNullable(networkInterface)
                .map(v -> new EC2NetworkInterfaceResource(
                                regionName,
                                EC2NetworkInterfaceAssociation.fromSdk(v.association()),
                                EC2NetworkInterfaceAttachment.fromSdk(v.attachment()),
                                v.availabilityZone(),
                                securityGroupIdsFromSdk(v.groups()),
                                v.interfaceTypeAsString(),
                                ipv6AddressesFromSdk(v.ipv6Addresses()),
                                v.networkInterfaceId(),
                                v.outpostArn(),
                                v.ownerId(),
                                v.privateDnsName(),
                                v.privateIpAddress(),
                                EC2NetworkInterfacePrivateIpAddress.fromSdk(v.privateIpAddresses()),
                                v.sourceDestCheck(),
                                v.statusAsString(),
                                v.subnetId(),
                                tagsFromSdk(v.tagSet()),
                                v.vpcId()
                        )
                )
                .orElse(null);
    }

    public static List<String> securityGroupIdsFromSdk(List<GroupIdentifier> groupIdentifiers) {
        return Optional.ofNullable(groupIdentifiers)
                .orElse(Collections.emptyList())
                .stream()
                .map(GroupIdentifier::groupId)
                .collect(Collectors.toList());
    }

    public static List<String> ipv6AddressesFromSdk(List<NetworkInterfaceIpv6Address> networkInterfaceIpv6Addresses) {
        return Optional.ofNullable(networkInterfaceIpv6Addresses)
                .orElse(Collections.emptyList())
                .stream()
                .map(NetworkInterfaceIpv6Address::ipv6Address)
                .collect(Collectors.toList());
    }
}
