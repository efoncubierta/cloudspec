/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2IpPermission
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "security_group",
        description = "Security Group"
)
data class EC2SecurityGroup(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "group_name",
                description = "The name of the security group"
        )
        val groupName: String?,

        @property:PropertyDefinition(
                name = "ip_permissions",
                description = "The inbound rules associated with the security group"
        )
        val ipPermissions: List<EC2IpPermission>?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the owner of the security group"
        )
        val ownerId: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "group_id",
                description = "The ID of the security group"
        )
        val groupId: String,

        @property:PropertyDefinition(
                name = "ip_permissions_egress",
                description = "[VPC only] The outbound rules associated with the security group"
        )
        val ipPermissionsEgress: List<EC2IpPermission>?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the security group"
        )
        val tags: List<KeyValue>?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "[VPC only] The VPC for the security group",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) : EC2Resource(region)
