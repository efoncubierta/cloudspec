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
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "local_gateway",
        description = "Transit Gateway"
)
data class EC2LocalGateway(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "local_gateway_id",
                description = "The ID of the local gateway"
        )
        val localGatewayId: String?,

        @PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account ID that owns the local gateway"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "state",
                description = "The state of the local gateway"
        )
        val state: String?,

        @PropertyDefinition(
                name = "tags",
                description = "The tags assigned to the local gateway"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
