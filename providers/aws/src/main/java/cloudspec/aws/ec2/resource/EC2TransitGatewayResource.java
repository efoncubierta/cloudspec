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

import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2TransitGatewayOptions;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.TransitGateway;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2TransitGatewayResource.RESOURCE_NAME,
        description = "Transit Gateway"
)
public class EC2TransitGatewayResource extends EC2Resource {
    public static final String RESOURCE_NAME = "transit_gateway";
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
            name = "transit_gateway_id",
            description = "The ID of the transit gateway"
    )
    private final String transitGatewayId;

    @PropertyDefinition(
            name = "transit_gateway_arn",
            description = "The Amazon Resource Name (ARN) of the transit gateway"
    )
    private final String transitGatewayArn;

    @PropertyDefinition(
            name = "state",
            description = "The state of the transit gateway",
            exampleValues = "pending | available | modifying | deleting | deleted"
    )
    private final String state;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account ID that owns the transit gateway"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "creation_time",
            description = "The creation time"
    )
    private final Date creationTime;

    @PropertyDefinition(
            name = "options",
            description = "The transit gateway options"
    )
    private final EC2TransitGatewayOptions options;

    @PropertyDefinition(
            name = "tags",
            description = "The tags for the transit gateway"
    )
    private final List<KeyValue> tags;

    public EC2TransitGatewayResource(String region, String transitGatewayId, String transitGatewayArn, String state,
                                     String ownerId, Date creationTime, EC2TransitGatewayOptions options,
                                     List<KeyValue> tags) {
        this.region = region;
        this.transitGatewayId = transitGatewayId;
        this.transitGatewayArn = transitGatewayArn;
        this.state = state;
        this.ownerId = ownerId;
        this.creationTime = creationTime;
        this.options = options;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof TransitGateway))) {
            return false;
        }

        if (o instanceof TransitGateway) {
            return sdkEquals((TransitGateway) o);
        }

        EC2TransitGatewayResource that = (EC2TransitGatewayResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(transitGatewayId, that.transitGatewayId) &&
                Objects.equals(transitGatewayArn, that.transitGatewayArn) &&
                Objects.equals(state, that.state) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(creationTime, that.creationTime) &&
                Objects.equals(options, that.options) &&
                Objects.equals(tags, that.tags);
    }

    private boolean sdkEquals(TransitGateway that) {
        return Objects.equals(transitGatewayId, that.transitGatewayId()) &&
                Objects.equals(transitGatewayArn, that.transitGatewayArn()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(creationTime, dateFromSdk(that.creationTime())) &&
                Objects.equals(options, that.options()) &&
                Objects.equals(tags, tagsFromSdk(that.tags()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, transitGatewayId, transitGatewayArn, state, ownerId, creationTime, options, tags);
    }

    public static EC2TransitGatewayResource fromSdk(String regionName, TransitGateway transitGateway) {
        return Optional.ofNullable(transitGateway)
                       .map(v -> new EC2TransitGatewayResource(
                               regionName,
                               v.transitGatewayId(),
                               v.transitGatewayArn(),
                               v.stateAsString(),
                               v.ownerId(),
                               dateFromSdk(v.creationTime()),
                               EC2TransitGatewayOptions.fromSdk(v.options()),
                               tagsFromSdk(v.tags())
                       ))
                       .orElse(null);
    }
}
