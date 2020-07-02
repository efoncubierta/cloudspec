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
import cloudspec.aws.ec2.nested.EC2NatGatewayAddress
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.NatGatewayState
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2NatGateway.RESOURCE_NAME,
        description = EC2NatGateway.RESOURCE_DESCRIPTION
)
data class EC2NatGateway(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_CREATE_TIME,
                description = PROP_CREATE_TIME_D
        )
        val createTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_DELETE_TIME,
                description = PROP_DELETE_TIME_D
        )
        val deleteTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_FAILURE_CODE,
                description = PROP_FAILURE_CODE_D
        )
        val failureCode: String?,

        @property:PropertyDefinition(
                name = PROP_NAT_GATEWAY_ADDRESSES,
                description = PROP_NAT_GATEWAY_ADDRESSES_D
        )
        val natGatewayAddresses: List<EC2NatGatewayAddress>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_NAT_GATEWAY_ID,
                description = PROP_NAT_GATEWAY_ID_D
        )
        val natGatewayId: String,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: NatGatewayState?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNET,
                description = ASSOC_SUBNET_D,
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "nat_gateway"
        const val RESOURCE_DESCRIPTION = "NAT Gateway"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_CREATE_TIME = "create_time"
        const val PROP_CREATE_TIME_D = "The date and time the NAT gateway was created"
        const val PROP_DELETE_TIME = "delete_time"
        const val PROP_DELETE_TIME_D = "The date and time the NAT gateway was deleted, if applicable"
        const val PROP_FAILURE_CODE = "failure_code"
        const val PROP_FAILURE_CODE_D = "If the NAT gateway could not be created, specifies the error code for the failure."
        const val PROP_NAT_GATEWAY_ADDRESSES = "nat_gateway_addresses"
        const val PROP_NAT_GATEWAY_ADDRESSES_D = "Information about the IP addresses and network interface associated with the NAT gateway"
        const val PROP_NAT_GATEWAY_ID = "nat_gateway_id"
        const val PROP_NAT_GATEWAY_ID_D = "The ID of the NAT gateway"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the NAT gateway"
        const val ASSOC_SUBNET = "subnet"
        const val ASSOC_SUBNET_D = "The subnet in which the NAT gateway is located"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC in which the NAT gateway is located"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags for the NAT gateway"
    }
}
