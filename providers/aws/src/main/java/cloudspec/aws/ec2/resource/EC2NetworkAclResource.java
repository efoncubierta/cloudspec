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
import cloudspec.aws.ec2.resource.nested.EC2NetworkAclEntry;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.NetworkAcl;
import software.amazon.awssdk.services.ec2.model.NetworkAclAssociation;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NetworkAclResource.RESOURCE_NAME,
        description = "Network ACL"
)
public class EC2NetworkAclResource extends EC2Resource {
    public static final String RESOURCE_NAME = "network_acl";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @AssociationDefinition(
            name = "subnets",
            description = "The subnet",
            targetClass = EC2SubnetResource.class
    )
    private final List<String> subnetIds;

    @PropertyDefinition(
            name = "entries",
            description = "One or more entries (rules) in the network ACL"
    )
    private final List<EC2NetworkAclEntry> entries;

    @PropertyDefinition(
            name = "is_default",
            description = "Indicates whether this is the default network ACL for the VPC"
    )
    private final Boolean isDefault;

    @IdDefinition
    @PropertyDefinition(
            name = "network_acl_id",
            description = "The ID of the network ACL"
    )
    private final String networkAclId;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the network ACL"
    )
    private final List<KeyValue> tags;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC for the network ACL",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the network ACL"
    )
    private final String ownerId;

    public EC2NetworkAclResource(String region, List<String> subnetIds, List<EC2NetworkAclEntry> entries,
                                 Boolean isDefault, String networkAclId, List<KeyValue> tags, String vpcId,
                                 String ownerId) {
        this.region = region;
        this.subnetIds = subnetIds;
        this.entries = entries;
        this.isDefault = isDefault;
        this.networkAclId = networkAclId;
        this.tags = tags;
        this.vpcId = vpcId;
        this.ownerId = ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof NetworkAcl))) {
            return false;
        }

        if (o instanceof NetworkAcl) {
            return sdkEquals((NetworkAcl) o);
        }

        EC2NetworkAclResource that = (EC2NetworkAclResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(subnetIds, that.subnetIds) &&
                Objects.equals(entries, that.entries) &&
                Objects.equals(isDefault, that.isDefault) &&
                Objects.equals(networkAclId, that.networkAclId) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(ownerId, that.ownerId);
    }

    private boolean sdkEquals(NetworkAcl that) {
        return Objects.equals(subnetIds, subnetIdsFromSdk(that.associations())) &&
                Objects.equals(entries, that.entries()) &&
                Objects.equals(isDefault, that.isDefault()) &&
                Objects.equals(networkAclId, that.networkAclId()) &&
                Objects.equals(tags, tagsFromSdk(that.tags())) &&
                Objects.equals(vpcId, that.vpcId()) &&
                Objects.equals(ownerId, that.ownerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, subnetIds, entries, isDefault, networkAclId, tags, vpcId, ownerId);
    }

    public static EC2NetworkAclResource fromSdk(String regionName, NetworkAcl networkAcl) {
        return Optional.ofNullable(networkAcl)
                       .map(v -> new EC2NetworkAclResource(
                               regionName,
                               subnetIdsFromSdk(v.associations()),
                               EC2NetworkAclEntry.fromSdk(v.entries()),
                               v.isDefault(),
                               v.networkAclId(),
                               tagsFromSdk(v.tags()),
                               v.vpcId(),
                               v.ownerId()
                       ))
                       .orElse(null);
    }

    public static List<String> subnetIdsFromSdk(List<NetworkAclAssociation> networkAclAssociation) {
        return Optional.ofNullable(networkAclAssociation)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(NetworkAclAssociation::subnetId)
                       .collect(Collectors.toList());
    }
}
