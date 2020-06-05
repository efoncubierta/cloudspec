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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2SecurityGroup
import cloudspec.aws.ec2.EC2Vpc
import cloudspec.aws.ec2.EC2VpcPeeringConnection

data class EC2UserIdGroupPair(
        @AssociationDefinition(
                name = "group",
                description = "The security group",
                targetClass = EC2SecurityGroup::class
        )
        val groupId: String?,

        @PropertyDefinition(
                name = "group_name",
                description = "The name of the security group"
        )
        val groupName: String?,

        @PropertyDefinition(
                name = "peering_status",
                description = "The status of a VPC peering connection, if applicable"
        )
        val peeringStatus: String?,

        @PropertyDefinition(
                name = "userId",
                description = "The ID of an AWS account"
        )
        val userId: String?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC for the referenced security group, if applicable",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @AssociationDefinition(
                name = "vpc_peering_connection",
                description = "The VPC peering connection, if applicable",
                targetClass = EC2VpcPeeringConnection::class
        )
        val vpcPeeringConnectionId: String?
)
