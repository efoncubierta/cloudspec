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
import cloudspec.aws.ec2.resource.nested.EC2InternetGatewayAttachment;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.InternetGateway;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2InternetGatewayResource.RESOURCE_NAME,
        description = "Internet Gateway"
)
public class EC2InternetGatewayResource extends EC2Resource {
    public static final String RESOURCE_NAME = "internet_gateway";
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
            name = "attachments",
            description = "Any VPCs attached to the internet gateway"
    )
    private final List<EC2InternetGatewayAttachment> attachments;

    @IdDefinition
    @PropertyDefinition(
            name = "internet_gateway_id",
            description = "The ID of the internet gateway"
    )
    private final String internetGatewayId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the internet gateway"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the internet gateway"
    )
    private final List<KeyValue> tags;

    public EC2InternetGatewayResource(String region, List<EC2InternetGatewayAttachment> attachments,
                                      String internetGatewayId, String ownerId, List<KeyValue> tags) {
        this.region = region;
        this.attachments = attachments;
        this.internetGatewayId = internetGatewayId;
        this.ownerId = ownerId;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2InternetGatewayResource that = (EC2InternetGatewayResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(attachments, that.attachments) &&
                Objects.equals(internetGatewayId, that.internetGatewayId) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, attachments, internetGatewayId, ownerId, tags);
    }

    public static EC2InternetGatewayResource fromSdk(String regionName, InternetGateway internetGateway) {
        return Optional.ofNullable(internetGateway)
                .map(v ->
                        new EC2InternetGatewayResource(
                                regionName,
                                EC2InternetGatewayAttachment.fromSdk(v.attachments()),
                                v.internetGatewayId(),
                                v.ownerId(),
                                tagsFromSdk(v.tags())
                        )
                )
                .orElse(null);
    }
}
