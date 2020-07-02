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
import cloudspec.aws.AWSProvider
import cloudspec.aws.ec2.nested.EC2IpPermission
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2SecurityGroup.RESOURCE_NAME,
        description = EC2SecurityGroup.RESOURCE_DESCRIPTION
)
data class EC2SecurityGroup(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_GROUP_NAME,
                description = PROP_GROUP_NAME_D
        )
        val groupName: String?,

        @property:PropertyDefinition(
                name = PROP_IP_PERMISSIONS,
                description = PROP_IP_PERMISSIONS_D
        )
        val ipPermissions: List<EC2IpPermission>?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_GROUP_ID,
                description = PROP_GROUP_ID_D
        )
        val groupId: String,

        @property:PropertyDefinition(
                name = PROP_IP_PERMISSIONS_EGRESS,
                description = PROP_IP_PERMISSIONS_EGRESS_D
        )
        val ipPermissionsEgress: List<EC2IpPermission>?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "security_group"
        const val RESOURCE_DESCRIPTION = "Security Group"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_GROUP_NAME = "group_name"
        const val PROP_GROUP_NAME_D = "The name of the security group"
        const val PROP_IP_PERMISSIONS = "ip_permissions"
        const val PROP_IP_PERMISSIONS_D = "The inbound rules associated with the security group"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The AWS account ID of the owner of the security group"
        const val PROP_GROUP_ID = "group_id"
        const val PROP_GROUP_ID_D = "The ID of the security group"
        const val PROP_IP_PERMISSIONS_EGRESS = "ip_permissions_egress"
        const val PROP_IP_PERMISSIONS_EGRESS_D = "[VPC only] The outbound rules associated with the security group"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the security group"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "[VPC only] The VPC for the security group"
    }
}
