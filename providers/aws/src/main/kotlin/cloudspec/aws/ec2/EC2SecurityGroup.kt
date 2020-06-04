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
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2IpPermission
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.SecurityGroup

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "security_group",
        description = "Security Group"
)
data class EC2SecurityGroup(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "group_name",
                description = "The name of the security group"
        )
        val groupName: String?,

        @PropertyDefinition(
                name = "ip_permissions",
                description = "The inbound rules associated with the security group"
        )
        val ipPermissions: List<EC2IpPermission>?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the owner of the security group"
        )
        val ownerId: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "group_id",
                description = "The ID of the security group"
        )
        val groupId: String?,

        @PropertyDefinition(
                name = "ip_permissions_egress",
                description = "[VPC only] The outbound rules associated with the security group"
        )
        val ipPermissionsEgress: List<EC2IpPermission>?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the security group"
        )
        val tags: List<KeyValue>?,

        @AssociationDefinition(
                name = "vpc",
                description = "[VPC only] The VPC for the security group",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, securityGroup: SecurityGroup): EC2SecurityGroup {
            return securityGroup.toEC2SecurityGroup(region)
        }
    }
}
