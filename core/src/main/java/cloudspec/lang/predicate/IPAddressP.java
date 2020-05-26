/*-
 * #%L
 * CloudSpec Core Library
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
package cloudspec.lang.predicate;

import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.function.BiPredicate;

public class IPAddressP extends P<String> {
    public IPAddressP(final BiPredicate<String, String> biPredicate, final String value) {
        super(biPredicate, value);
    }

    public boolean equals(final Object other) {
        return other instanceof IPAddressP && super.equals(other);
    }

    public String toString() {
        return null == this.originalValue ?
                this.biPredicate.toString() :
                this.biPredicate.toString() + "(" + this.originalValue + ")";
    }

    public IPAddressP negate() {
        return new IPAddressP(this.biPredicate.negate(), this.originalValue);
    }

    public IPAddressP clone() {
        return (IPAddressP) super.clone();
    }

    public static IPAddressP eq(final String value) {
        return new IPAddressP(IPAddress.eq, value);
    }

    public static IPAddressP neq(final String value) {
        return new IPAddressP(IPAddress.neq, value);
    }

    public static IPAddressP lt(final String value) {
        return new IPAddressP(IPAddress.lt, value);
    }

    public static IPAddressP lte(final String value) {
        return new IPAddressP(IPAddress.lte, value);
    }

    public static IPAddressP gt(final String value) {
        return new IPAddressP(IPAddress.gt, value);
    }

    public static IPAddressP gte(final String value) {
        return new IPAddressP(IPAddress.gte, value);
    }

    public static P<String> withinNetwork(final String value) {
        return new IPAddressP(IPAddress.withinNetwork, value);
    }

    public static P<String> withoutNetwork(final String value) {
        return new IPAddressP(IPAddress.withoutNetwork, value);
    }

    public static IPAddressP isIpv4() {
        return new IPAddressP(IPAddress.isIpv4, "");
    }

    public static IPAddressP isIpv6() {
        return new IPAddressP(IPAddress.isIpv6, "");
    }
}
