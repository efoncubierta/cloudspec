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
import cloudspec.aws.ec2.nested.EC2DhcpConfiguration
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2DhcpOptions.RESOURCE_NAME,
        description = EC2DhcpOptions.RESOURCE_DESCRIPTION
)
data class EC2DhcpOptions(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_DHCP_CONFIGURATIONS,
                description = PROP_DHCP_CONFIGURATIONS_D
        )
        val dhcpConfigurations: List<EC2DhcpConfiguration>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_DHCP_OPTIONS_ID,
                description = PROP_DHCP_OPTIONS_ID_D
        )
        val dhcpOptionsId: String,

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
        const val RESOURCE_NAME = "dhcp_options"
        const val RESOURCE_DESCRIPTION = "DHCP Options"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_DHCP_CONFIGURATIONS = "dhcp_configurations"
        const val PROP_DHCP_CONFIGURATIONS_D = "One or more DHCP options in the set"
        const val PROP_DHCP_OPTIONS_ID = "dhcp_options_id"
        const val PROP_DHCP_OPTIONS_ID_D = "The ID of the set of DHCP options"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the Capacity Reservation"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the DHCP options set"
    }
}
