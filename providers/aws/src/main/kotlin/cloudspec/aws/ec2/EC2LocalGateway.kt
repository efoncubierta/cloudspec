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
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2LocalGateway.RESOURCE_NAME,
        description = EC2LocalGateway.RESOURCE_DESCRIPTION
)
data class EC2LocalGateway(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_LOCAL_GATEWAY_ID,
                description = PROP_LOCAL_GATEWAY_ID_D
        )
        val localGatewayId: String,

        @property:PropertyDefinition(
                name = PROP_OUTPOST_ARN,
                description = PROP_OUTPOST_ARN_D
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "local_gateway"
        const val RESOURCE_DESCRIPTION = "Local Gateway"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_LOCAL_GATEWAY_ID = "local_gateway_id"
        const val PROP_LOCAL_GATEWAY_ID_D = "The ID of the local gateway"
        const val PROP_OUTPOST_ARN = "outpost_arn"
        const val PROP_OUTPOST_ARN_D = "The Amazon Resource Name (ARN) of the Outpost"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account ID that owns the local gateway"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the local gateway"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags assigned to the local gateway"
    }
}
