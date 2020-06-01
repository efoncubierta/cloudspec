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
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.aws.ec2.resource.EC2SecurityGroupResource;
import cloudspec.aws.ec2.resource.EC2VpcPeeringConnectionResource;
import cloudspec.aws.ec2.resource.EC2VpcResource;
import software.amazon.awssdk.services.ec2.model.UserIdGroupPair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2UserIdGroupPair {
    @AssociationDefinition(
            name = "group",
            description = "The security group",
            targetClass = EC2SecurityGroupResource.class
    )
    private final String groupId;

    @PropertyDefinition(
            name = "group_name",
            description = "The name of the security group"
    )
    private final String groupName;

    @PropertyDefinition(
            name = "peering_status",
            description = "The status of a VPC peering connection, if applicable"
    )
    private final String peeringStatus;

    @PropertyDefinition(
            name = "userId",
            description = "The ID of an AWS account"
    )
    private final String userId;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC for the referenced security group, if applicable",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @AssociationDefinition(
            name = "vpc_peering_connection",
            description = "The VPC peering connection, if applicable",
            targetClass = EC2VpcPeeringConnectionResource.class
    )
    private final String vpcPeeringConnectionId;

    public EC2UserIdGroupPair(String groupId, String groupName, String peeringStatus, String userId, String vpcId,
                              String vpcPeeringConnectionId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.peeringStatus = peeringStatus;
        this.userId = userId;
        this.vpcId = vpcId;
        this.vpcPeeringConnectionId = vpcPeeringConnectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof UserIdGroupPair))) {
            return false;
        }

        if (o instanceof UserIdGroupPair) {
            return sdkEquals((UserIdGroupPair) o);
        }

        EC2UserIdGroupPair that = (EC2UserIdGroupPair) o;
        return Objects.equals(groupId, that.groupId) &&
                Objects.equals(groupName, that.groupName) &&
                Objects.equals(peeringStatus, that.peeringStatus) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(vpcPeeringConnectionId, that.vpcPeeringConnectionId);
    }

    private boolean sdkEquals(UserIdGroupPair that) {
        return Objects.equals(groupId, that.groupId()) &&
                Objects.equals(groupName, that.groupName()) &&
                Objects.equals(peeringStatus, that.peeringStatus()) &&
                Objects.equals(userId, that.userId()) &&
                Objects.equals(vpcId, that.vpcId()) &&
                Objects.equals(vpcPeeringConnectionId, that.vpcPeeringConnectionId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, groupName, peeringStatus, userId, vpcId, vpcPeeringConnectionId);
    }

    public static List<EC2UserIdGroupPair> fromSdk(List<UserIdGroupPair> userIdGroupPairs) {
        return Optional.ofNullable(userIdGroupPairs)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(EC2UserIdGroupPair::fromSdk)
                       .collect(Collectors.toList());
    }

    public static EC2UserIdGroupPair fromSdk(UserIdGroupPair userIdGroupPair) {
        return Optional.ofNullable(userIdGroupPair)
                       .map(v -> new EC2UserIdGroupPair(
                                       v.groupId(),
                                       v.groupName(),
                                       v.peeringStatus(),
                                       v.userId(),
                                       v.vpcId(),
                                       v.vpcPeeringConnectionId()
                               )
                       )
                       .orElse(null);
    }
}
