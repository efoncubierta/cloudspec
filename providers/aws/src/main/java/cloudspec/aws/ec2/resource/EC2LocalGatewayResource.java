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
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.LocalGateway;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2LocalGatewayResource.RESOURCE_NAME,
        description = "Transit Gateway"
)
public class EC2LocalGatewayResource extends EC2Resource {
    public static final String RESOURCE_NAME = "local_gateway";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @IdDefinition
    @PropertyDefinition(
            name = "local_gateway_id",
            description = "The ID of the local gateway"
    )
    private final String localGatewayId;

    @PropertyDefinition(
            name = "outpost_arn",
            description = "The Amazon Resource Name (ARN) of the Outpost"
    )
    private final String outpostArn;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account ID that owns the local gateway"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "state",
            description = "The state of the local gateway"
    )
    private final String state;

    @PropertyDefinition(
            name = "tags",
            description = "The tags assigned to the local gateway"
    )
    private final List<KeyValue> tags;

    public EC2LocalGatewayResource(String region, String localGatewayId, String outpostArn, String ownerId,
                                   String state, List<KeyValue> tags) {
        this.region = region;
        this.localGatewayId = localGatewayId;
        this.outpostArn = outpostArn;
        this.ownerId = ownerId;
        this.state = state;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof LocalGateway))) {
            return false;
        }

        if (o instanceof LocalGateway) {
            return sdkEquals((LocalGateway) o);
        }

        EC2LocalGatewayResource that = (EC2LocalGatewayResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(localGatewayId, that.localGatewayId) &&
                Objects.equals(outpostArn, that.outpostArn) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(state, that.state) &&
                Objects.equals(tags, that.tags);
    }

    private boolean sdkEquals(LocalGateway that) {
        return Objects.equals(localGatewayId, that.localGatewayId()) &&
                Objects.equals(outpostArn, that.outpostArn()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(state, that.state()) &&
                Objects.equals(tags, tagsFromSdk(that.tags()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(localGatewayId, outpostArn, ownerId, state, tags);
    }

    public static EC2LocalGatewayResource fromSdk(String regionName, LocalGateway localGateway) {
        return Optional.ofNullable(localGateway)
                       .map(v -> new EC2LocalGatewayResource(
                               regionName,
                               v.localGatewayId(),
                               v.outpostArn(),
                               v.ownerId(),
                               v.state(),
                               tagsFromSdk(v.tags())
                       ))
                       .orElse(null);
    }
}
