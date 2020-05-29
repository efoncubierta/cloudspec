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
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.PropertyDefinition;
import software.amazon.awssdk.services.ec2.model.IcmpTypeCode;
import software.amazon.awssdk.services.ec2.model.NetworkAclEntry;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2NetworkAclEntry {
    @PropertyDefinition(
            name = "cidr_block",
            description = "The IPv4 network range to allow or deny, in CIDR notation"
    )
    private final String cidrBlock;

    @PropertyDefinition(
            name = "egress",
            description = "Indicates whether the rule is an egress rule"
    )
    private final Boolean egress;

    @PropertyDefinition(
            name = "icmp_type_code",
            description = "The ICMP code. A value of -1 means all codes for the specified ICMP type"
    )
    private final Integer icmpTypeCode;

    @PropertyDefinition(
            name = "ipv6_cidr_block",
            description = "The IPv6 network range to allow or deny, in CIDR notation"
    )
    private final String ipv6CidrBlock;

    @PropertyDefinition(
            name = "port_range",
            description = "TCP or UDP protocols: The range of ports the rule applies to"
    )
    private final EC2PortRange portRange;

    @PropertyDefinition(
            name = "protocol",
            description = "The protocol number. A value of \"-1\" means all protocols"
    )
    private final String protocol;

    @PropertyDefinition(
            name = "rule_action",
            description = "Indicates whether to allow or deny the traffic that matches the rule",
            exampleValues = "allow | deny"
    )
    private final String ruleAction;

    public EC2NetworkAclEntry(String cidrBlock, Boolean egress, Integer icmpTypeCode, String ipv6CidrBlock,
                              EC2PortRange portRange, String protocol, String ruleAction) {
        this.cidrBlock = cidrBlock;
        this.egress = egress;
        this.icmpTypeCode = icmpTypeCode;
        this.ipv6CidrBlock = ipv6CidrBlock;
        this.portRange = portRange;
        this.protocol = protocol;
        this.ruleAction = ruleAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2NetworkAclEntry that = (EC2NetworkAclEntry) o;
        return Objects.equals(cidrBlock, that.cidrBlock) &&
                Objects.equals(egress, that.egress) &&
                Objects.equals(icmpTypeCode, that.icmpTypeCode) &&
                Objects.equals(ipv6CidrBlock, that.ipv6CidrBlock) &&
                Objects.equals(portRange, that.portRange) &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(ruleAction, that.ruleAction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidrBlock, egress, icmpTypeCode, ipv6CidrBlock, portRange, protocol, ruleAction);
    }

    public static List<EC2NetworkAclEntry> fromSdk(List<NetworkAclEntry> networkAclEntries) {
        return Optional.ofNullable(networkAclEntries)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2NetworkAclEntry::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2NetworkAclEntry fromSdk(NetworkAclEntry networkAclEntry) {
        return Optional.ofNullable(networkAclEntry)
                .map(v -> new EC2NetworkAclEntry(
                        v.cidrBlock(),
                        v.egress(),
                        icmpTypeCodeFromSdk(v.icmpTypeCode()),
                        v.ipv6CidrBlock(),
                        EC2PortRange.fromSdk(v.portRange()),
                        v.protocol(),
                        v.ruleActionAsString()
                ))
                .orElse(null);
    }

    public static Integer icmpTypeCodeFromSdk(IcmpTypeCode icmpTypeCode) {
        return Optional.ofNullable(icmpTypeCode)
                .map(IcmpTypeCode::code)
                .orElse(null);
    }
}
