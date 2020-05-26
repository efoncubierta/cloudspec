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

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;

import java.util.function.BiPredicate;

public enum IPAddress implements BiPredicate<String, String> {
    eq {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) == 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return neq;
        }
    },
    neq {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) != 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return eq;
        }
    },
    lt {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) < 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return gte;
        }
    },
    lte {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) <= 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return gt;
        }
    },
    gt {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) > 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return lte;
        }
    },
    gte {
        public boolean test(final String baseIpAddressStr, final String targetIpAddressStr) {
            try {
                inet.ipaddr.IPAddress baseIpAddress = new IPAddressString(baseIpAddressStr).toAddress();
                inet.ipaddr.IPAddress targetIpAddress = new IPAddressString(targetIpAddressStr).toAddress();
                return baseIpAddress.compareTo(targetIpAddress) >= 0;
            } catch (AddressStringException ignored) {
            }
            return false;
        }

        public IPAddress negate() {
            return lt;
        }
    },
    withinNetwork {
        public boolean test(final String ipAddressStr, final String networkCidrStr) {
            IPAddressString network = new IPAddressString(networkCidrStr);
            IPAddressString ipAddress = new IPAddressString(ipAddressStr);
            return network.isValid() && ipAddress.isValid() && network.contains(ipAddress);
        }

        public IPAddress negate() {
            return withoutNetwork;
        }
    },
    withoutNetwork {
        public boolean test(final String ipAddressStr, final String networkCidrStr) {
            IPAddressString network = new IPAddressString(networkCidrStr);
            IPAddressString ipAddress = new IPAddressString(ipAddressStr);
            return network.isValid() && ipAddress.isValid() && !network.contains(ipAddress);
        }

        public IPAddress negate() {
            return withinNetwork;
        }
    },
    isIpv4 {
        public boolean test(final String ipAddressStr, final String ignored) {
            IPAddressString ipAddress = new IPAddressString(ipAddressStr);
            return ipAddress.isValid() && ipAddress.isIPv4();
        }

        public IPAddress negate() {
            return neq;
        }
    },
    isIpv6 {
        public boolean test(final String ipAddressStr, final String ignored) {
            IPAddressString ipAddress = new IPAddressString(ipAddressStr);
            return ipAddress.isValid() && ipAddress.isIPv6();
        }

        public IPAddress negate() {
            return neq;
        }
    }
}
