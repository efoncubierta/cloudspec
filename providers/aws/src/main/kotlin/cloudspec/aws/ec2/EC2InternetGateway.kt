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
import cloudspec.aws.ec2.nested.EC2InternetGatewayAttachment
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2InternetGateway.RESOURCE_NAME,
        description = EC2InternetGateway.RESOURCE_DESCRIPTION
)
data class EC2InternetGateway(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ATTACHMENTS,
                description = PROP_ATTACHMENTS_D
        )
        val attachments: List<EC2InternetGatewayAttachment>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_INTERNET_GATEWAY_ID,
                description = PROP_INTERNET_GATEWAY_ID_D
        )
        val internetGatewayId: String,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "internet_gateway"
        const val RESOURCE_DESCRIPTION = "Internet Gateway"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ATTACHMENTS = "attachments"
        const val PROP_ATTACHMENTS_D = "Any VPCs attached to the internet gateway"
        const val PROP_INTERNET_GATEWAY_ID = "internet_gateway_id"
        const val PROP_INTERNET_GATEWAY_ID_D = "The ID of the internet gateway"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the internet gateway"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the internet gateway"
    }
}
