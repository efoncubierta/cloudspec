/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2NetworkAclEntry(
        @PropertyDefinition(
                name = "cidr_block",
                description = "The IPv4 network range to allow or deny, in CIDR notation"
        )
        val cidrBlock: String?,

        @PropertyDefinition(
                name = "egress",
                description = "Indicates whether the rule is an egress rule"
        )
        val egress: Boolean?,

        @PropertyDefinition(
                name = "icmp_type_code",
                description = "The ICMP code. A value of -1 means all codes for the specified ICMP type"
        )
        val icmpTypeCode: Int?,

        @PropertyDefinition(
                name = "ipv6_cidr_block",
                description = "The IPv6 network range to allow or deny, in CIDR notation"
        )
        val ipv6CidrBlock: String?,

        @PropertyDefinition(
                name = "port_range",
                description = "TCP or UDP protocols: The range of ports the rule applies to"
        )
        val portRange: EC2PortRange?,

        @PropertyDefinition(
                name = "protocol",
                description = "The protocol number. A value of \"-1\" means all protocols"
        )
        val protocol: String?,

        @PropertyDefinition(
                name = "rule_action",
                description = "Indicates whether to allow or deny the traffic that matches the rule",
                exampleValues = "allow | deny"
        )
        val ruleAction: String
)
