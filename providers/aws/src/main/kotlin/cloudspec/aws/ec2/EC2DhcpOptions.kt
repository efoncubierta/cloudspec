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
import cloudspec.aws.ec2.nested.EC2DhcpConfiguration
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "dhcp_options",
        description = "DHCP Options"
)
data class EC2DhcpOptions(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "dhcp_configurations",
                description = "One or more DHCP options in the set"
        )
        val dhcpConfigurations: List<EC2DhcpConfiguration>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "dhcp_options_id",
                description = "The ID of the set of DHCP options"
        )
        val dhcpOptionsId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the Capacity Reservation"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the DHCP options set"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
