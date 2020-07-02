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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition
import software.amazon.awssdk.services.ec2.model.VpcCidrBlockStateCode

data class EC2VpcIpv6CidrBlockAssociation(
        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCK,
                description = PROP_IPV6_CIDR_BLOCK_D
        )
        val ipv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCK_STATE,
                description = PROP_IPV6_CIDR_BLOCK_STATE_D
        )
        val ipv6CidrBlockState: VpcCidrBlockStateCode?,

        @property:PropertyDefinition(
                name = PROP_NETWORK_BORDER_GROUP,
                description = PROP_NETWORK_BORDER_GROUP_D
        )
        val networkBorderGroup: String?,

        @property:PropertyDefinition(
                name = PROP_IPV6_POOL,
                description = PROP_IPV6_POOL_D
        )
        val ipv6Pool: String?
) {
    companion object {
        const val PROP_IPV6_CIDR_BLOCK = "ipv6_cidr_block"
        const val PROP_IPV6_CIDR_BLOCK_D = "The IPv6 CIDR block"
        const val PROP_IPV6_CIDR_BLOCK_STATE = "ipv6_cidr_block_state"
        const val PROP_IPV6_CIDR_BLOCK_STATE_D = "Information about the state of the CIDR block"
        const val PROP_NETWORK_BORDER_GROUP = "network_border_group"
        const val PROP_NETWORK_BORDER_GROUP_D = "The name of the location from which we advertise the IPV6 CIDR block"
        const val PROP_IPV6_POOL = "ipv6_pool"
        const val PROP_IPV6_POOL_D = "The ID of the IPv6 address pool from which the IPv6 CIDR block is allocated"
    }
}
