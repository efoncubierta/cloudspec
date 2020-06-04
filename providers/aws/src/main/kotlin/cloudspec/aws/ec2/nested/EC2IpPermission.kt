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

data class EC2IpPermission(
        @PropertyDefinition(
                name = "from_port",
                description = "The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number"
        )
        val fromPort: Int?,

        @PropertyDefinition(
                name = "ip_protocol",
                description = "The IP protocol name or number"
        )
        val ipProtocol: String?,

        @PropertyDefinition(
                name = "ip_ranges",
                description = "The IPv4 ranges"
        )
        val ipRanges: List<EC2IpRange>?,

        @PropertyDefinition(
                name = "ipv6_ranges",
                description = "[VPC only] The IPv6 ranges"
        )
        val ipv6Ranges: List<EC2Ipv6Range>?
        ,
        @PropertyDefinition(
                name = "prefix_list_ids",
                description = "[VPC only] The prefix list IDs for an AWS service"
        )
        val prefixListIds: List<String>?,

        @PropertyDefinition(
                name = "to_port",
                description = "The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code"
        )
        val toPort: Int?,

        @PropertyDefinition(
                name = "user_groups",
                description = "The security group and AWS account ID pairs"
        )
        val userIdGroupPairs: List<EC2UserIdGroupPair>?
)
