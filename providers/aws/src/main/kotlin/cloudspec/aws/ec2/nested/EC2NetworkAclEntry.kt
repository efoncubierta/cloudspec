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

data class EC2NetworkAclEntry(
        @property:PropertyDefinition(
                name = "cidr_block",
                description = "The IPv4 network range to allow or deny, in CIDR notation"
        )
        val cidrBlock: String?,

        @property:PropertyDefinition(
                name = "egress",
                description = "Indicates whether the rule is an egress rule"
        )
        val egress: Boolean?,

        @property:PropertyDefinition(
                name = "icmp_type_code",
                description = "The ICMP code. A value of -1 means all codes for the specified ICMP type"
        )
        val icmpTypeCode: Int?,

        @property:PropertyDefinition(
                name = "ipv6_cidr_block",
                description = "The IPv6 network range to allow or deny, in CIDR notation"
        )
        val ipv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = "port_range",
                description = "TCP or UDP protocols: The range of ports the rule applies to"
        )
        val portRange: EC2PortRange?,

        @property:PropertyDefinition(
                name = "protocol",
                description = "The protocol number. A value of \"-1\" means all protocols"
        )
        val protocol: String?,

        @property:PropertyDefinition(
                name = "rule_action",
                description = "Indicates whether to allow or deny the traffic that matches the rule",
                exampleValues = "allow | deny"
        )
        val ruleAction: String
)
