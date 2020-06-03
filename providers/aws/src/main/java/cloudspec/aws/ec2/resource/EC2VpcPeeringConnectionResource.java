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

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2VpcPeeringConnectionVpcInfo;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnectionStateReason;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VolumeResource.RESOURCE_NAME,
        description = "VPC Peering Connection"
)
public class EC2VpcPeeringConnectionResource extends EC2Resource {
    public static final String RESOURCE_NAME = "vpc_peering_connection";
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
            name = "accepter_vpc_info",
            description = "Information about the accepter VP"
    )
    private final EC2VpcPeeringConnectionVpcInfo accepterVpcInfo;

    @PropertyDefinition(
            name = "expiration_time",
            description = "The time that an unaccepted VPC peering connection will expire"
    )
    private final Instant expirationTime;

    @PropertyDefinition(
            name = "requester_vpc_info",
            description = "Information about the requester VPC"
    )
    private final EC2VpcPeeringConnectionVpcInfo requesterVpcInfo;

    @PropertyDefinition(
            name = "status",
            description = "The status of the VPC peering connection",
            exampleValues = "initiating-request | pending-acceptance | active | deleted | rejected | failed | expired | provisioning | deleting"
    )
    private final String status;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the resource"
    )
    private final List<KeyValue> tags;

    @IdDefinition
    @PropertyDefinition(
            name = "vpc_peering_connection_id",
            description = "The ID of the VPC peering connection"
    )
    private final String vpcPeeringConnectionId;

    public EC2VpcPeeringConnectionResource(String region, EC2VpcPeeringConnectionVpcInfo accepterVpcInfo,
                                           Instant expirationTime, EC2VpcPeeringConnectionVpcInfo requesterVpcInfo,
                                           String status, List<KeyValue> tags, String vpcPeeringConnectionId) {
        this.region = region;
        this.accepterVpcInfo = accepterVpcInfo;
        this.expirationTime = expirationTime;
        this.requesterVpcInfo = requesterVpcInfo;
        this.status = status;
        this.tags = tags;
        this.vpcPeeringConnectionId = vpcPeeringConnectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof VpcPeeringConnection))) {
            return false;
        }

        if (o instanceof VpcPeeringConnection) {
            return sdkEquals((VpcPeeringConnection) o);
        }

        EC2VpcPeeringConnectionResource that = (EC2VpcPeeringConnectionResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(accepterVpcInfo, that.accepterVpcInfo) &&
                Objects.equals(expirationTime, that.expirationTime) &&
                Objects.equals(requesterVpcInfo, that.requesterVpcInfo) &&
                Objects.equals(status, that.status) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(vpcPeeringConnectionId, that.vpcPeeringConnectionId);
    }

    private boolean sdkEquals(VpcPeeringConnection that) {
        return Objects.equals(accepterVpcInfo, that.accepterVpcInfo()) &&
                Objects.equals(expirationTime, that.expirationTime()) &&
                Objects.equals(requesterVpcInfo, that.requesterVpcInfo()) &&
                Objects.equals(status, statusFromSdk(that.status())) &&
                Objects.equals(tags, tagsFromSdk(that.tags())) &&
                Objects.equals(vpcPeeringConnectionId, that.vpcPeeringConnectionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, accepterVpcInfo, expirationTime, requesterVpcInfo, status,
                tags, vpcPeeringConnectionId);
    }

    public static EC2VpcPeeringConnectionResource fromSdk(String regionName, VpcPeeringConnection vpcPeeringConnection) {
        return Optional.ofNullable(vpcPeeringConnection)
                       .map(v ->
                               new EC2VpcPeeringConnectionResource(
                                       regionName,
                                       EC2VpcPeeringConnectionVpcInfo.fromSdk(v.accepterVpcInfo()),
                                       v.expirationTime(),
                                       EC2VpcPeeringConnectionVpcInfo.fromSdk(v.requesterVpcInfo()),
                                       statusFromSdk(v.status()),
                                       tagsFromSdk(v.tags()),
                                       v.vpcPeeringConnectionId()
                               )
                       )
                       .orElse(null);
    }

    public static String statusFromSdk(VpcPeeringConnectionStateReason vpcPeeringConnectionStateReason) {
        return Optional.ofNullable(vpcPeeringConnectionStateReason)
                       .map(VpcPeeringConnectionStateReason::codeAsString)
                       .orElse(null);
    }
}
