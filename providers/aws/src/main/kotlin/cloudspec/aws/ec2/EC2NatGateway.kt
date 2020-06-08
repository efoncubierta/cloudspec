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
import cloudspec.aws.ec2.nested.EC2NatGatewayAddress
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "nat_gateway",
        description = "NAT Gateway"
)
data class EC2NatGateway(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "create_time",
                description = "The date and time the NAT gateway was created"
        )
        val createTime: Instant?,

        @property:PropertyDefinition(
                name = "delete_time",
                description = "The date and time the NAT gateway was deleted, if applicable"
        )
        val deleteTime: Instant?,

        @property:PropertyDefinition(
                name = "failure_code",
                description = "If the NAT gateway could not be created, specifies the error code for the failure."
        )
        val failureCode: String?,

        @property:PropertyDefinition(
                name = "nat_gateway_addresses",
                description = "Information about the IP addresses and network interface associated with the NAT gateway"
        )
        val natGatewayAddresses: List<EC2NatGatewayAddress>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "nat_gateway_id",
                description = "The ID of the NAT gateway"
        )
        val natGatewayId: String?
        ,
        @property:PropertyDefinition(
                name = "state",
                description = "The state of the NAT gateway",
                exampleValues = "pending | failed | available | deleting | deleted"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = "subnet",
                description = "The subnet in which the NAT gateway is located",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The VPC in which the NAT gateway is located",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "The tags for the NAT gateway"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
