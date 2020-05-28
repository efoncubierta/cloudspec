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
import software.amazon.awssdk.services.ec2.model.Ipv6Range;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EC2Ipv6Range {
    @PropertyDefinition(
            name = "cidr_ipv6",
            description = "The IPv6 CIDR range"
    )
    private final String cidrIpv6;

    public EC2Ipv6Range(String cidrIpv6) {
        this.cidrIpv6 = cidrIpv6;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2Ipv6Range that = (EC2Ipv6Range) o;
        return Objects.equals(cidrIpv6, that.cidrIpv6);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidrIpv6);
    }

    public static List<EC2Ipv6Range> fromSdk(List<Ipv6Range> ipv6Ranges) {
        if (Objects.isNull(ipv6Ranges)) {
            return Collections.emptyList();
        }

        return ipv6Ranges.stream()
                .map(EC2Ipv6Range::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2Ipv6Range fromSdk(Ipv6Range ipRange) {
        if (Objects.isNull(ipRange)) {
            return null;
        }

        return new EC2Ipv6Range(
                ipRange.cidrIpv6()
        );
    }
}
