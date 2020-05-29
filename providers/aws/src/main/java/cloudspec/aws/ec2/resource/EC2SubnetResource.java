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
import cloudspec.aws.ec2.resource.nested.EC2SubnetIpv6CidrBlockAssociation;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.Subnet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2SubnetResource.RESOURCE_NAME,
        description = "VPC Subnet"
)
public class EC2SubnetResource extends EC2Resource {
    public static final String RESOURCE_NAME = "subnet";
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
            name = "availability_zone",
            description = "The Availability Zone of the subnet"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "available_ip_address_count",
            description = "The number of unused private IPv4 addresses in the subnet"
    )
    private final Integer availableIpAddressCount;

    @PropertyDefinition(
            name = "cidr_block",
            description = "The IPv4 CIDR block assigned to the subnet"
    )
    private final String cidrBlock;

    @PropertyDefinition(
            name = "default_for_az",
            description = "Indicates whether this is the default subnet for the Availability Zone"
    )
    private final Boolean defaultForAz;

    @PropertyDefinition(
            name = "map_public_ip_on_launch",
            description = "Indicates whether instances launched in this subnet receive a public IPv4 address"
    )
    private final Boolean mapPublicIpOnLaunch;

    @PropertyDefinition(
            name = "state",
            description = "The current state of the subnet",
            exampleValues = "pending | available"
    )
    private final String state;

    @IdDefinition
    @PropertyDefinition(
            name = "subnet_id",
            description = "The ID of the subnet"
    )
    private final String subnetId;

    @AssociationDefinition(
            name = "vpc",
            description = "The ID of the VPC the subnet is in",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the subnet"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "assign_ipv6_address_on_creation",
            description = "Indicates whether a network interface created in this subnet receives an IPv6 address."
    )
    private final Boolean assignIpv6AddressOnCreation;

    @PropertyDefinition(
            name = "ipv6_cidr_block_associations",
            description = "Information about the IPv6 CIDR blocks associated with the subnet"
    )
    private final List<EC2SubnetIpv6CidrBlockAssociation> ipv6CidrBlockAssociationSet;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the subnet"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "subnet_arn",
            description = "The Amazon Resource Name (ARN) of the subnet"
    )
    private final String subnetArn;

    @PropertyDefinition(
            name = "outpost_arn",
            description = "The Amazon Resource Name (ARN) of the Outpost"
    )
    private final String outpostArn;

    public EC2SubnetResource(String region, String availabilityZone, Integer availableIpAddressCount,
                             String cidrBlock, Boolean defaultForAz, Boolean mapPublicIpOnLaunch, String state,
                             String subnetId, String vpcId, String ownerId, Boolean assignIpv6AddressOnCreation,
                             List<EC2SubnetIpv6CidrBlockAssociation> ipv6CidrBlockAssociationSet, List<KeyValue> tags,
                             String subnetArn, String outpostArn) {
        this.region = region;
        this.availabilityZone = availabilityZone;
        this.availableIpAddressCount = availableIpAddressCount;
        this.cidrBlock = cidrBlock;
        this.defaultForAz = defaultForAz;
        this.mapPublicIpOnLaunch = mapPublicIpOnLaunch;
        this.state = state;
        this.subnetId = subnetId;
        this.vpcId = vpcId;
        this.ownerId = ownerId;
        this.assignIpv6AddressOnCreation = assignIpv6AddressOnCreation;
        this.ipv6CidrBlockAssociationSet = ipv6CidrBlockAssociationSet;
        this.tags = tags;
        this.subnetArn = subnetArn;
        this.outpostArn = outpostArn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2SubnetResource that = (EC2SubnetResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(availableIpAddressCount, that.availableIpAddressCount) &&
                Objects.equals(cidrBlock, that.cidrBlock) &&
                Objects.equals(defaultForAz, that.defaultForAz) &&
                Objects.equals(mapPublicIpOnLaunch, that.mapPublicIpOnLaunch) &&
                Objects.equals(state, that.state) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(assignIpv6AddressOnCreation, that.assignIpv6AddressOnCreation) &&
                Objects.equals(ipv6CidrBlockAssociationSet, that.ipv6CidrBlockAssociationSet) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(subnetArn, that.subnetArn) &&
                Objects.equals(outpostArn, that.outpostArn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, availabilityZone, availableIpAddressCount, cidrBlock, defaultForAz,
                mapPublicIpOnLaunch, state, subnetId, vpcId, ownerId, assignIpv6AddressOnCreation,
                ipv6CidrBlockAssociationSet, tags, subnetArn, outpostArn);
    }

    public static EC2SubnetResource fromSdk(String regionName, Subnet subnet) {
        return Optional.ofNullable(subnet)
                .map(v ->
                        new EC2SubnetResource(
                                regionName,
                                v.availabilityZone(),
                                v.availableIpAddressCount(),
                                v.cidrBlock(),
                                v.defaultForAz(),
                                v.mapPublicIpOnLaunch(),
                                v.stateAsString(),
                                v.subnetId(),
                                v.vpcId(),
                                v.ownerId(),
                                v.assignIpv6AddressOnCreation(),
                                EC2SubnetIpv6CidrBlockAssociation.fromSdk(v.ipv6CidrBlockAssociationSet()),
                                tagsFromSdk(v.tags()),
                                v.subnetArn(),
                                v.outpostArn()
                        )
                )
                .orElse(null);
    }
}
