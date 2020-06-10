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
import cloudspec.aws.ec2.nested.EC2NetworkAclEntry
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "network_acl",
        description = "Network ACL"
)
data class EC2NetworkAcl(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:AssociationDefinition(
                name = "subnets",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @property:PropertyDefinition(
                name = "entries",
                description = "One or more entries (rules) in the network ACL"
        )
        val entries: List<EC2NetworkAclEntry>?,

        @property:PropertyDefinition(
                name = "is_default",
                description = "Indicates whether this is the default network ACL for the VPC"
        )
        val isDefault: Boolean?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "network_acl_id",
                description = "The ID of the network ACL"
        )
        val networkAclId: String,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the network ACL"
        )
        val tags: List<KeyValue>?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The VPC for the network ACL",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the network ACL"
        )
        val ownerId: String?
) : EC2Resource(region)
