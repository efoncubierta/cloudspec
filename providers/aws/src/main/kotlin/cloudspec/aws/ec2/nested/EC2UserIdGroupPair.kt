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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2SecurityGroup
import cloudspec.aws.ec2.EC2Vpc
import cloudspec.aws.ec2.EC2VpcPeeringConnection

data class EC2UserIdGroupPair(
        @property:AssociationDefinition(
                name = ASSOC_GROUP,
                description = ASSOC_GROUP_D,
                targetClass = EC2SecurityGroup::class
        )
        val groupId: String?,

        @property:PropertyDefinition(
                name = ASSOC_GROUP_NAME,
                description = ASSOC_GROUP_NAME_D
        )
        val groupName: String?,

        @property:PropertyDefinition(
                name = PROP_PEERING_STATUS,
                description = PROP_PEERING_STATUS_D
        )
        val peeringStatus: String?,

        @property:PropertyDefinition(
                name = PROP_USER_ID,
                description = PROP_USER_ID_D
        )
        val userId: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC_PEERING_CONNECTION,
                description = ASSOC_VPC_PEERING_CONNECTION_D,
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
) {
    companion object {
        const val ASSOC_GROUP = "group"
        const val ASSOC_GROUP_D = "The security group"
        const val ASSOC_GROUP_NAME = "group_name"
        const val ASSOC_GROUP_NAME_D = "The name of the security group"
        const val PROP_PEERING_STATUS = "peering_status"
        const val PROP_PEERING_STATUS_D = "The status of a VPC peering connection, if applicable"
        const val PROP_USER_ID = "userId"
        const val PROP_USER_ID_D = "The ID of an AWS account"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC for the referenced security group, if applicable"
        const val ASSOC_VPC_PEERING_CONNECTION = "vpc_peering_connection"
        const val ASSOC_VPC_PEERING_CONNECTION_D = "The VPC peering connection, if applicable"
    }
}
