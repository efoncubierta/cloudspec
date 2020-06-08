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
package cloudspec.lang.predicate

import inet.ipaddr.AddressStringException
import inet.ipaddr.IPAddressString
import java.util.function.BiPredicate

enum class IPAddress : BiPredicate<String?, String?> {
    eq {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress.compareTo(targetIpAddress) == 0
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return neq
        }
    },
    neq {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress.compareTo(targetIpAddress) != 0
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return eq
        }
    },
    lt {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress < targetIpAddress
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return gte
        }
    },
    lte {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress <= targetIpAddress
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return gt
        }
    },
    gt {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress > targetIpAddress
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return lte
        }
    },
    gte {
        override fun test(baseIpAddressStr: String?, targetIpAddressStr: String?): Boolean {
            try {
                val baseIpAddress = IPAddressString(baseIpAddressStr).toAddress()
                val targetIpAddress = IPAddressString(targetIpAddressStr).toAddress()
                return baseIpAddress >= targetIpAddress
            } catch (ignored: AddressStringException) {
            }
            return false
        }

        override fun negate(): IPAddress {
            return lt
        }
    },
    withinNetwork {
        override fun test(ipAddressStr: String?, networkCidrStr: String?): Boolean {
            val network = IPAddressString(networkCidrStr)
            val ipAddress = IPAddressString(ipAddressStr)
            return network.isValid && ipAddress.isValid && network.contains(ipAddress)
        }

        override fun negate(): IPAddress {
            return withoutNetwork
        }
    },
    withoutNetwork {
        override fun test(ipAddressStr: String?, networkCidrStr: String?): Boolean {
            val network = IPAddressString(networkCidrStr)
            val ipAddress = IPAddressString(ipAddressStr)
            return network.isValid && ipAddress.isValid && !network.contains(ipAddress)
        }

        override fun negate(): IPAddress {
            return withinNetwork
        }
    },
    isIpv4 {
        override fun test(ipAddressStr: String?, ignored: String?): Boolean {
            val ipAddress = IPAddressString(ipAddressStr)
            return ipAddress.isValid && ipAddress.isIPv4
        }

        override fun negate(): IPAddress {
            return neq
        }
    },
    isIpv6 {
        override fun test(ipAddressStr: String?, ignored: String?): Boolean {
            val ipAddress = IPAddressString(ipAddressStr)
            return ipAddress.isValid && ipAddress.isIPv6
        }

        override fun negate(): IPAddress {
            return neq
        }
    }
}
