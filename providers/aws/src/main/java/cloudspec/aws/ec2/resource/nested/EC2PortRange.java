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
import software.amazon.awssdk.services.ec2.model.PortRange;

import java.util.Objects;
import java.util.Optional;

public class EC2PortRange {
    @PropertyDefinition(
            name = "from",
            description = "The first port in the range"
    )
    private final Integer from;

    @PropertyDefinition(
            name = "to",
            description = "The last port in the range"
    )
    private final Integer to;

    public EC2PortRange(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof PortRange))) {
            return false;
        }

        if (o instanceof PortRange) {
            return sdkEquals((PortRange) o);
        }

        EC2PortRange that = (EC2PortRange) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    private boolean sdkEquals(PortRange that) {
        return Objects.equals(from, that.from()) &&
                Objects.equals(to, that.to());
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public static EC2PortRange fromSdk(PortRange portRange) {
        return Optional.ofNullable(portRange)
                       .map(v -> new EC2PortRange(
                               v.from(),
                               v.to()
                       ))
                       .orElse(null);
    }
}
