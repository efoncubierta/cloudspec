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
import cloudspec.aws.AWSProvider
import cloudspec.aws.ec2.nested.EC2VpcPeeringConnectionVpcInfo
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VpcPeeringConnection.RESOURCE_NAME,
        description = EC2VpcPeeringConnection.RESOURCE_DESCRIPTION
)
data class EC2VpcPeeringConnection(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ACCEPTER_VPC_INFO,
                description = PROP_ACCEPTER_VPC_INFO_D
        )
        val accepterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @property:PropertyDefinition(
                name = PROP_EXPIRATION_TIME,
                description = PROP_EXPIRATION_TIME_D
        )
        val expirationTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_REQUESTER_VPC_INFO,
                description = PROP_REQUESTER_VPC_INFO_D
        )
        val requesterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @property:PropertyDefinition(
                name = PROP_STATUS,
                description = PROP_STATUS_D,
                exampleValues = "initiating-request | pending-acceptance | active | deleted | rejected | failed | expired | provisioning | deleting"
        )
        val status: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_VPC_PEERING_CONNECTION_ID,
                description = PROP_VPC_PEERING_CONNECTION_ID_D
        )
        val vpcPeeringConnectionId: String
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "vpc_peering_connection"
        const val RESOURCE_DESCRIPTION = "VPC Peering Connection"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ACCEPTER_VPC_INFO = "accepter_vpc_info"
        const val PROP_ACCEPTER_VPC_INFO_D = "Information about the accepter VP"
        const val PROP_EXPIRATION_TIME = "expiration_time"
        const val PROP_EXPIRATION_TIME_D = "The time that an unaccepted VPC peering connection will expire"
        const val PROP_REQUESTER_VPC_INFO = "requester_vpc_info"
        const val PROP_REQUESTER_VPC_INFO_D = "Information about the requester VPC"
        const val PROP_STATUS = "status"
        const val PROP_STATUS_D = "The status of the VPC peering connection"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the resource"
        const val PROP_VPC_PEERING_CONNECTION_ID = "vpc_peering_connection_id"
        const val PROP_VPC_PEERING_CONNECTION_ID_D = "The ID of the VPC peering connection"
    }
}
