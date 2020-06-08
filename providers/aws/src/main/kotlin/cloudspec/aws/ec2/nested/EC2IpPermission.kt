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
                name = "from_port",
                description = "The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number"
        )
        val fromPort: Int?,

        @property:PropertyDefinition(
                name = "ip_protocol",
                description = "The IP protocol name or number"
        )
        val ipProtocol: String?,

        @property:PropertyDefinition(
                name = "ip_ranges",
                description = "The IPv4 ranges"
        )
        val ipRanges: List<EC2IpRange>?,

        @property:PropertyDefinition(
                name = "ipv6_ranges",
                description = "[VPC only] The IPv6 ranges"
        )
        val ipv6Ranges: List<EC2Ipv6Range>?
        ,
        @property:PropertyDefinition(
                name = "prefix_list_ids",
                description = "[VPC only] The prefix list IDs for an AWS service"
        )
        val prefixListIds: List<String>?,

        @property:PropertyDefinition(
                name = "to_port",
                description = "The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code"
        )
        val toPort: Int?,

        @property:PropertyDefinition(
                name = "user_groups",
                description = "The security group and AWS account ID pairs"
        )
        val userIdGroupPairs: List<EC2UserIdGroupPair>?
)
