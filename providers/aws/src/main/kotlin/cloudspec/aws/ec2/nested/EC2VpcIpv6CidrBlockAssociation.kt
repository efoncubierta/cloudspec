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

data class EC2VpcIpv6CidrBlockAssociation(
//    @property:AssociationDefinition(
        //            name = "association",
        //            description = "The association for the IPv6 CIDR block"
        //    )
        //    private final String associationId;
        @property:PropertyDefinition(
                name = "ipv6_cidr_block",
                description = "The IPv6 CIDR block"
        )
        val ipv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = "ipv6_cidr_block_state",
                description = "Information about the state of the CIDR block"
        )
        val ipv6CidrBlockState: String?,

        @property:PropertyDefinition(
                name = "network_border_group",
                description = "The name of the location from which we advertise the IPV6 CIDR block"
        )
        val networkBorderGroup: String?,

        @property:PropertyDefinition(
                name = "ipv6_pool",
                description = "The ID of the IPv6 address pool from which the IPv6 CIDR block is allocated"
        )
        val ipv6Pool: String?
)
