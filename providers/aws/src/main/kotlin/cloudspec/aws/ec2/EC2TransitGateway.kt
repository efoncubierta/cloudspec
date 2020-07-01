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
import cloudspec.aws.ec2.nested.EC2TransitGatewayOptions
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2TransitGateway.RESOURCE_NAME,
        description = EC2TransitGateway.RESOURCE_DESCRIPTION
)
data class EC2TransitGateway(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_TRANSIT_GATEWAY_ID,
                description = PROP_TRANSIT_GATEWAY_ID_D
        )
        val transitGatewayId: String,

        @property:PropertyDefinition(
                name = PROP_TRANSIT_GATEWAY_ARN,
                description = PROP_TRANSIT_GATEWAY_ARN_D
        )
        val transitGatewayArn: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D,
                exampleValues = "pending | available | modifying | deleting | deleted"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_CREATION_TIME,
                description = PROP_CREATION_TIME_D
        )
        val creationTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_OPTIONS,
                description = PROP_OPTIONS_D
        )
        val options: EC2TransitGatewayOptions?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "transit_gateway"
        const val RESOURCE_DESCRIPTION = "Transit Gateway"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_TRANSIT_GATEWAY_ID = "transit_gateway_id"
        const val PROP_TRANSIT_GATEWAY_ID_D = "The ID of the transit gateway"
        const val PROP_TRANSIT_GATEWAY_ARN = "transit_gateway_arn"
        const val PROP_TRANSIT_GATEWAY_ARN_D = "The Amazon Resource Name (ARN) of the transit gateway"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the transit gateway"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account ID that owns the transit gateway"
        const val PROP_CREATION_TIME = "creation_time"
        const val PROP_CREATION_TIME_D = "The creation time"
        const val PROP_OPTIONS = "options"
        const val PROP_OPTIONS_D = "The transit gateway options"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags for the transit gateway"
    }
}
