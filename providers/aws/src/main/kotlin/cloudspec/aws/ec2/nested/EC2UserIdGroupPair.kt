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
                name = "group",
                description = "The security group",
                targetClass = EC2SecurityGroup::class
        )
        val groupId: String?,

        @property:PropertyDefinition(
                name = "group_name",
                description = "The name of the security group"
        )
        val groupName: String?,

        @property:PropertyDefinition(
                name = "peering_status",
                description = "The status of a VPC peering connection, if applicable"
        )
        val peeringStatus: String?,

        @property:PropertyDefinition(
                name = "userId",
                description = "The ID of an AWS account"
        )
        val userId: String?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The VPC for the referenced security group, if applicable",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:AssociationDefinition(
                name = "vpc_peering_connection",
                description = "The VPC peering connection, if applicable",
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
)
