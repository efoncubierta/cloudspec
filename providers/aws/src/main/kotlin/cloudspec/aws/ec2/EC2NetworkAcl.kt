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
import cloudspec.aws.ec2.nested.EC2NetworkAclEntry
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NetworkAcl.RESOURCE_NAME,
        description = EC2NetworkAcl.RESOURCE_DESCRIPTION
)
data class EC2NetworkAcl(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNETS,
                description = ASSOC_SUBNETS_D,
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_ENTRIES,
                description = PROP_ENTRIES_D
        )
        val entries: List<EC2NetworkAclEntry>?,

        @property:PropertyDefinition(
                name = PROP_IS_DEFAULT,
                description = PROP_IS_DEFAULT_D
        )
        val isDefault: Boolean?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_NETWORK_ACL_ID,
                description = PROP_NETWORK_ACL_ID_D
        )
        val networkAclId: String,

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
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "network_acl"
        const val RESOURCE_DESCRIPTION = "Network ACL"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val ASSOC_SUBNETS = "subnets"
        const val ASSOC_SUBNETS_D = "The subnet"
        const val PROP_ENTRIES = "entries"
        const val PROP_ENTRIES_D = "One or more entries (rules) in the network ACL"
        const val PROP_IS_DEFAULT = "is_default"
        const val PROP_IS_DEFAULT_D = "Indicates whether this is the default network ACL for the VPC"
        const val PROP_NETWORK_ACL_ID = "network_acl_id"
        const val PROP_NETWORK_ACL_ID_D = "The ID of the network ACL"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the network ACL"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC for the network ACL"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the network ACL"
    }
}
