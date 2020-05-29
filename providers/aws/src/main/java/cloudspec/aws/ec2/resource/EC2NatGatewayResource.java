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
import cloudspec.aws.ec2.resource.nested.EC2NatGatewayAddress;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.NatGateway;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NatGatewayResource.RESOURCE_NAME,
        description = "NAT Gateway"
)
public class EC2NatGatewayResource extends EC2Resource {
    public static final String RESOURCE_NAME = "nat_gateway";
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
            name = "create_time",
            description = "The date and time the NAT gateway was created"
    )
    private final Date createTime;

    @PropertyDefinition(
            name = "delete_time",
            description = "The date and time the NAT gateway was deleted, if applicable"
    )
    private final Date deleteTime;

    @PropertyDefinition(
            name = "failure_code",
            description = "If the NAT gateway could not be created, specifies the error code for the failure."
    )
    private final String failureCode;

    @PropertyDefinition(
            name = "nat_gateway_addresses",
            description = "Information about the IP addresses and network interface associated with the NAT gateway"
    )
    private final List<EC2NatGatewayAddress> natGatewayAddresses;

    @IdDefinition
    @PropertyDefinition(
            name = "nat_gateway_id",
            description = "The ID of the NAT gateway"
    )
    private final String natGatewayId;

    @PropertyDefinition(
            name = "state",
            description = "The state of the NAT gateway",
            exampleValues = "pending | failed | available | deleting | deleted"
    )
    private final String state;

    @AssociationDefinition(
            name = "subnet",
            description = "The subnet in which the NAT gateway is located",
            targetClass = EC2SubnetResource.class
    )
    private final String subnetId;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC in which the NAT gateway is located",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "tags",
            description = "The tags for the NAT gateway"
    )
    private final List<KeyValue> tags;

    public EC2NatGatewayResource(String region, Date createTime, Date deleteTime, String failureCode,
                                 List<EC2NatGatewayAddress> natGatewayAddresses, String natGatewayId, String state,
                                 String subnetId, String vpcId, List<KeyValue> tags) {
        this.region = region;
        this.createTime = createTime;
        this.deleteTime = deleteTime;
        this.failureCode = failureCode;
        this.natGatewayAddresses = natGatewayAddresses;
        this.natGatewayId = natGatewayId;
        this.state = state;
        this.subnetId = subnetId;
        this.vpcId = vpcId;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2NatGatewayResource that = (EC2NatGatewayResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(deleteTime, that.deleteTime) &&
                Objects.equals(failureCode, that.failureCode) &&
                Objects.equals(natGatewayAddresses, that.natGatewayAddresses) &&
                Objects.equals(natGatewayId, that.natGatewayId) &&
                Objects.equals(state, that.state) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, createTime, deleteTime, failureCode, natGatewayAddresses,
                natGatewayId, state, subnetId, vpcId, tags);
    }

    public static EC2NatGatewayResource fromSdk(String regionName, NatGateway natGateway) {
        return Optional.ofNullable(natGateway)
                .map(v -> new EC2NatGatewayResource(
                        regionName,
                        dateFromSdk(v.createTime()),
                        dateFromSdk(v.deleteTime()),
                        v.failureCode(),
                        EC2NatGatewayAddress.fromSdk(v.natGatewayAddresses()),
                        v.natGatewayId(),
                        v.stateAsString(),
                        v.subnetId(),
                        v.vpcId(),
                        tagsFromSdk(v.tags())
                ))
                .orElse(null);
    }
}
