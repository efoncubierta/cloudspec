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
import cloudspec.aws.ec2.resource.nested.EC2VpcCidrBlockAssociation;
import cloudspec.aws.ec2.resource.nested.EC2VpcIpv6CidrBlockAssociation;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.Vpc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VpcResource.RESOURCE_NAME,
        description = "Virtual Private Cloud"
)
public class EC2VpcResource extends EC2Resource {
    public static final String RESOURCE_NAME = "vpc";
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
            name = "cidr_block",
            description = "The primary IPv4 CIDR block for the VPC"
    )
    private final String cidrBlock;

    @AssociationDefinition(
            name = "dhcp_options",
            description = "The ID of the set of DHCP options you've associated with the VPC",
            targetClass = EC2DhcpOptionsResource.class
    )
    private final String dhcpOptionsId;

    @PropertyDefinition(
            name = "state",
            description = "The current state of the VPC"
    )
    private final String state;

    @IdDefinition
    @PropertyDefinition(
            name = "vpc_id",
            description = "The ID of the VPC"
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the VPC"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "instance_tenancy",
            description = "The allowed tenancy of instances launched into the VPC",
            exampleValues = "default | dedicated | host"
    )
    private final String instanceTenancy;

    @PropertyDefinition(
            name = "ipv6_cidr_block_associations",
            description = "Information about the IPv6 CIDR blocks associated with the VPC"
    )
    private final List<EC2VpcIpv6CidrBlockAssociation> ipv6CidrBlockAssociations;

    @PropertyDefinition(
            name = "cidr_block_associations",
            description = "Information about the IPv4 CIDR blocks associated with the VPC"
    )
    private final List<EC2VpcCidrBlockAssociation> cidrBlockAssociations;

    @PropertyDefinition(
            name = "is_default",
            description = "Indicates whether the VPC is the default VPC"
    )
    private final Boolean isDefault;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the VPC"
    )
    private final List<KeyValue> tags;

    public EC2VpcResource(String region, String cidrBlock, String dhcpOptionsId, String state,
                          String vpcId, String ownerId, String instanceTenancy,
                          List<EC2VpcIpv6CidrBlockAssociation> ipv6CidrBlockAssociations,
                          List<EC2VpcCidrBlockAssociation> cidrBlockAssociations,
                          Boolean isDefault, List<KeyValue> tags) {
        this.region = region;
        this.cidrBlock = cidrBlock;
        this.dhcpOptionsId = dhcpOptionsId;
        this.state = state;
        this.vpcId = vpcId;
        this.ownerId = ownerId;
        this.instanceTenancy = instanceTenancy;
        this.ipv6CidrBlockAssociations = ipv6CidrBlockAssociations;
        this.cidrBlockAssociations = cidrBlockAssociations;
        this.isDefault = isDefault;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof Vpc))) {
            return false;
        }

        if (o instanceof Vpc) {
            return sdkEquals((Vpc) o);
        }

        EC2VpcResource that = (EC2VpcResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(cidrBlock, that.cidrBlock) &&
                Objects.equals(dhcpOptionsId, that.dhcpOptionsId) &&
                Objects.equals(state, that.state) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(instanceTenancy, that.instanceTenancy) &&
                Objects.equals(ipv6CidrBlockAssociations, that.ipv6CidrBlockAssociations) &&
                Objects.equals(cidrBlockAssociations, that.cidrBlockAssociations) &&
                Objects.equals(isDefault, that.isDefault) &&
                Objects.equals(tags, that.tags);
    }

    private boolean sdkEquals(Vpc that) {
        return Objects.equals(cidrBlock, that.cidrBlock()) &&
                Objects.equals(dhcpOptionsId, that.dhcpOptionsId()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(vpcId, that.vpcId()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(instanceTenancy, that.instanceTenancyAsString()) &&
                Objects.equals(ipv6CidrBlockAssociations, that.ipv6CidrBlockAssociationSet()) &&
                Objects.equals(cidrBlockAssociations, that.cidrBlockAssociationSet()) &&
                Objects.equals(isDefault, that.isDefault()) &&
                Objects.equals(tags, tagsFromSdk(that.tags()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, cidrBlock, dhcpOptionsId, state, vpcId, ownerId, instanceTenancy,
                ipv6CidrBlockAssociations, cidrBlockAssociations, isDefault, tags);
    }

    public static EC2VpcResource fromSdk(String regionName, Vpc vpc) {
        return Optional.ofNullable(vpc)
                       .map(v ->
                               new EC2VpcResource(
                                       regionName,
                                       v.cidrBlock(),
                                       v.dhcpOptionsId(),
                                       v.stateAsString(),
                                       v.vpcId(),
                                       v.ownerId(),
                                       v.instanceTenancyAsString(),
                                       EC2VpcIpv6CidrBlockAssociation.fromSdk(v.ipv6CidrBlockAssociationSet()),
                                       EC2VpcCidrBlockAssociation.fromSdk(v.cidrBlockAssociationSet()),
                                       v.isDefault(),
                                       tagsFromSdk(v.tags())
                               )
                       )
                       .orElse(null);
    }
}
