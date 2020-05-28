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
import cloudspec.aws.ec2.resource.nested.EC2IpPermission;
import cloudspec.aws.ec2.resource.nested.EC2Resource;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.SecurityGroup;

import java.util.List;
import java.util.Objects;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2SecurityGroupResource.RESOURCE_NAME,
        description = "Security Group"
)
public class EC2SecurityGroupResource extends EC2Resource {
    public static final String RESOURCE_NAME = "security_group";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "group_name",
            description = "The name of the security group"
    )
    private final String groupName;

    @PropertyDefinition(
            name = "ip_permissions",
            description = "The inbound rules associated with the security group"
    )
    private final List<EC2IpPermission> ipPermissions;

    @IdDefinition
    @PropertyDefinition(
            name = "group_id",
            description = "The ID of the security group"
    )
    private final String groupId;

    @PropertyDefinition(
            name = "ip_permissions_egress",
            description = "[VPC only] The outbound rules associated with the security group"
    )
    private final List<EC2IpPermission> ipPermissionsEgress;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the security group"
    )
    private final List<KeyValue> tags;

    @AssociationDefinition(
            name = "vpc",
            description = "[VPC only] The VPC for the security group",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    public EC2SecurityGroupResource(String ownerId, String region, String groupName, List<EC2IpPermission> ipPermissions,
                                    String groupId, List<EC2IpPermission> ipPermissionsEgress, List<KeyValue> tags,
                                    String vpcId) {
        super(ownerId, region);
        this.groupName = groupName;
        this.ipPermissions = ipPermissions;
        this.groupId = groupId;
        this.ipPermissionsEgress = ipPermissionsEgress;
        this.tags = tags;
        this.vpcId = vpcId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2SecurityGroupResource that = (EC2SecurityGroupResource) o;
        return Objects.equals(groupName, that.groupName) &&
                Objects.equals(ipPermissions, that.ipPermissions) &&
                Objects.equals(groupId, that.groupId) &&
                Objects.equals(ipPermissionsEgress, that.ipPermissionsEgress) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(vpcId, that.vpcId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupName, ipPermissions, groupId, ipPermissionsEgress, tags, vpcId);
    }

    public static EC2SecurityGroupResource fromSdk(String regionName, SecurityGroup securityGroup) {
        if (Objects.isNull(securityGroup)) {
            return null;
        }

        return new EC2SecurityGroupResource(
                securityGroup.ownerId(),
                regionName,
                securityGroup.groupName(),
                EC2IpPermission.fromSdk(securityGroup.ipPermissions()),
                securityGroup.groupId(),
                EC2IpPermission.fromSdk(securityGroup.ipPermissionsEgress()),
                tagsFromSdk(securityGroup.tags()),
                securityGroup.vpcId()
        );
    }
}
