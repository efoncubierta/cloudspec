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
