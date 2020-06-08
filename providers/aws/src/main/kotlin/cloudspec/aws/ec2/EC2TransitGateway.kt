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

import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2TransitGatewayOptions
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "transit_gateway",
        description = "Transit Gateway"
)
data class EC2TransitGateway(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "transit_gateway_id",
                description = "The ID of the transit gateway"
        )
        val transitGatewayId: String?,

        @property:PropertyDefinition(
                name = "transit_gateway_arn",
                description = "The Amazon Resource Name (ARN) of the transit gateway"
        )
        val transitGatewayArn: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The state of the transit gateway",
                exampleValues = "pending | available | modifying | deleting | deleted"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account ID that owns the transit gateway"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "creation_time",
                description = "The creation time"
        )
        val creationTime: Instant?,

        @property:PropertyDefinition(
                name = "options",
                description = "The transit gateway options"
        )
        val options: EC2TransitGatewayOptions?,

        @property:PropertyDefinition(
                name = "tags",
                description = "The tags for the transit gateway"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
