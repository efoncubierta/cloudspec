/*-
 * #%L
 * CloudSpec Core Library
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
