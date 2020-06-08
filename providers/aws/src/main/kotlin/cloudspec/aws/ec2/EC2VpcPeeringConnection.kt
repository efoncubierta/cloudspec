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

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2VpcPeeringConnectionVpcInfo
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "vpc_peering_connection",
        description = "VPC Peering Connection"
)
data class EC2VpcPeeringConnection(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "accepter_vpc_info",
                description = "Information about the accepter VP"
        )
        val accepterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @property:PropertyDefinition(
                name = "expiration_time",
                description = "The time that an unaccepted VPC peering connection will expire"
        )
        val expirationTime: Instant?,

        @property:PropertyDefinition(
                name = "requester_vpc_info",
                description = "Information about the requester VPC"
        )
        val requesterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @property:PropertyDefinition(
                name = "status",
                description = "The status of the VPC peering connection",
                exampleValues = "initiating-request | pending-acceptance | active | deleted | rejected | failed | expired | provisioning | deleting"
        )
        val status: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the resource"
        )
        val tags: List<KeyValue>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "vpc_peering_connection_id",
                description = "The ID of the VPC peering connection"
        )
        val vpcPeeringConnectionId: String?
) : EC2Resource(region)
