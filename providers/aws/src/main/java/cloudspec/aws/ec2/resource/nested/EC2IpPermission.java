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
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.PrefixListId;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2IpPermission {
    @PropertyDefinition(
            name = "from_port",
            description = "The start of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 type number"
    )
    private final Integer fromPort;

    @PropertyDefinition(
            name = "ip_protocol",
            description = "The IP protocol name or number"
    )
    private final String ipProtocol;

    @PropertyDefinition(
            name = "ip_ranges",
            description = "The IPv4 ranges"
    )
    private final List<EC2IpRange> ipRanges;

    @PropertyDefinition(
            name = "ipv6_ranges",
            description = "[VPC only] The IPv6 ranges"
    )
    private final List<EC2Ipv6Range> ipv6Ranges;

    @PropertyDefinition(
            name = "prefix_list_ids",
            description = "[VPC only] The prefix list IDs for an AWS service"
    )
    private final List<String> prefixListIds;

    @PropertyDefinition(
            name = "to_port",
            description = "The end of port range for the TCP and UDP protocols, or an ICMP/ICMPv6 code"
    )
    private final Integer toPort;

    @PropertyDefinition(
            name = "user_groups",
            description = "The security group and AWS account ID pairs"
    )
    private final List<EC2UserIdGroupPair> userIdGroupPairs;

    public EC2IpPermission(Integer fromPort, String ipProtocol, List<EC2IpRange> ipRanges, List<EC2Ipv6Range> ipv6Ranges,
                           List<String> prefixListIds, Integer toPort, List<EC2UserIdGroupPair> userIdGroupPairs) {
        this.fromPort = fromPort;
        this.ipProtocol = ipProtocol;
        this.ipRanges = ipRanges;
        this.ipv6Ranges = ipv6Ranges;
        this.prefixListIds = prefixListIds;
        this.toPort = toPort;
        this.userIdGroupPairs = userIdGroupPairs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2IpPermission that = (EC2IpPermission) o;
        return Objects.equals(fromPort, that.fromPort) &&
                Objects.equals(ipProtocol, that.ipProtocol) &&
                Objects.equals(ipRanges, that.ipRanges) &&
                Objects.equals(ipv6Ranges, that.ipv6Ranges) &&
                Objects.equals(prefixListIds, that.prefixListIds) &&
                Objects.equals(toPort, that.toPort) &&
                Objects.equals(userIdGroupPairs, that.userIdGroupPairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromPort, ipProtocol, ipRanges, ipv6Ranges, prefixListIds, toPort, userIdGroupPairs);
    }

    public static List<EC2IpPermission> fromSdk(List<IpPermission> ipPermissions) {
        return Optional.ofNullable(ipPermissions)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2IpPermission::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2IpPermission fromSdk(IpPermission ipPermission) {
        return Optional.ofNullable(ipPermission)
                .map(v -> new EC2IpPermission(
                                v.fromPort(),
                                v.ipProtocol(),
                                EC2IpRange.fromSdk(v.ipRanges()),
                                EC2Ipv6Range.fromSdk(v.ipv6Ranges()),
                                prefixListIdsFromSdk(v.prefixListIds()),
                                v.toPort(),
                                EC2UserIdGroupPair.fromSdk(v.userIdGroupPairs())
                        )
                )
                .orElse(null);
    }

    public static List<String> prefixListIdsFromSdk(List<PrefixListId> prefixListIds) {
        return Optional.ofNullable(prefixListIds)
                .orElse(Collections.emptyList())
                .stream()
                .map(PrefixListId::prefixListId)
                .collect(Collectors.toList());
    }
}
