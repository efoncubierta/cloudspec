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
                name = PROP_CIDR_BLOCK,
                description = PROP_CIDR_BLOCK_D
        )
        val cidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_EGRESS,
                description = PROP_EGRESS_D
        )
        val egress: Boolean?,

        @property:PropertyDefinition(
                name = PROP_ICMP_TYPE_CODE,
                description = PROP_ICMP_TYPE_CODE_D
        )
        val icmpTypeCode: Int?,

        @property:PropertyDefinition(
                name = PROP_IPV6_CIDR_BLOCK,
                description = PROP_IPV6_CIDR_BLOCK_D
        )
        val ipv6CidrBlock: String?,

        @property:PropertyDefinition(
                name = PROP_PORT_RANGE,
                description = PROP_PORT_RANGE_D
        )
        val portRange: EC2PortRange?,

        @property:PropertyDefinition(
                name = PROP_PROTOCOL,
                description = PROP_PROTOCOL_D
        )
        val protocol: String?,

        @property:PropertyDefinition(
                name = PROP_RULE_ACTION,
                description = PROP_RULE_ACTION_D,
                exampleValues = "allow | deny"
        )
        val ruleAction: String
) {
    companion object {
        const val PROP_CIDR_BLOCK = "cidr_block"
        const val PROP_CIDR_BLOCK_D = "The IPv4 network range to allow or deny, in CIDR notation"
        const val PROP_EGRESS = "egress"
        const val PROP_EGRESS_D = "Indicates whether the rule is an egress rule"
        const val PROP_ICMP_TYPE_CODE = "icmp_type_code"
        const val PROP_ICMP_TYPE_CODE_D = "The ICMP code. A value of -1 means all codes for the specified ICMP type"
        const val PROP_IPV6_CIDR_BLOCK = "ipv6_cidr_block"
        const val PROP_IPV6_CIDR_BLOCK_D = "The IPv6 network range to allow or deny, in CIDR notation"
        const val PROP_PORT_RANGE = "port_range"
        const val PROP_PORT_RANGE_D = "TCP or UDP protocols: The range of ports the rule applies to"
        const val PROP_PROTOCOL = "protocol"
        const val PROP_PROTOCOL_D = "The protocol number. A value of \"-1\" means all protocols"
        const val PROP_RULE_ACTION = "rule_action"
        const val PROP_RULE_ACTION_D = "Indicates whether to allow or deny the traffic that matches the rule"
    }
}
