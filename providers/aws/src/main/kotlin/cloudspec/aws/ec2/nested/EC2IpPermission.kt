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

data class EC2IpPermission(
        @property:PropertyDefinition(
                name = PROP_FROM_PORT,
                description = PROP_FROM_PORT_D
        )
        val fromPort: Int?,

        @property:PropertyDefinition(
                name = PROP_IP_PROTOCOL,
                description = PROP_IP_PROTOCOL_D
        )
        val ipProtocol: String?,

        @property:PropertyDefinition(
                name = PROP_IP_RANGES,
                description = PROP_IP_RANGES_D
        )
        val ipRanges: List<EC2IpRange>?,

        @property:PropertyDefinition(
                name = PROP_IPV6_RANGES,
                description = PROP_IPV6_RANGES_D
        )
        val ipv6Ranges: List<EC2Ipv6Range>?
        ,
        @property:PropertyDefinition(
                name = PROP_PREFIX_LIST_IDS,
                description = PROP_PREFIX_LIST_IDS_D
        )
        val prefixListIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_TO_PORT,
                description = PROP_TO_PORT_D
        )
        val toPort: Int?,

        @property:PropertyDefinition(
                name = PROP_USER_GROUPS,
                description = PROP_USER_GROUPS_D
        )
        val userIdGroupPairs: List<EC2UserIdGroupPair>?
) {
    companion object {
        const val PROP_FROM_PORT = "from_port"
        const val PROP_FROM_PORT_D = "The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number"
        const val PROP_IP_PROTOCOL = "ip_protocol"
        const val PROP_IP_PROTOCOL_D = "The IP protocol name or number"
        const val PROP_IP_RANGES = "ip_ranges"
        const val PROP_IP_RANGES_D = "The IPv4 ranges"
        const val PROP_IPV6_RANGES = "ipv6_ranges"
        const val PROP_IPV6_RANGES_D = "[VPC only] The IPv6 ranges"
        const val PROP_PREFIX_LIST_IDS = "prefix_list_ids"
        const val PROP_PREFIX_LIST_IDS_D = "[VPC only] The prefix list IDs for an AWS service"
        const val PROP_TO_PORT = "to_port"
        const val PROP_TO_PORT_D = "The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code"
        const val PROP_USER_GROUPS = "user_groups"
        const val PROP_USER_GROUPS_D = "The security group and AWS account ID pairs"
    }
}
