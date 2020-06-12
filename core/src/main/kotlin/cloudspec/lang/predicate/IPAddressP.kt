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

import org.apache.tinkerpop.gremlin.process.traversal.P
import java.util.function.BiPredicate

class IPAddressP(biPredicate: BiPredicate<String?, String?>, value: String?) : P<String?>(biPredicate, value) {
    override fun equals(other: Any?): Boolean {
        return other is IPAddressP && super.equals(other)
    }

    override fun toString(): String {
        return if (originalValue == null) {
            biPredicate.toString()
        } else {
            "$biPredicate($originalValue)"
        }
    }

    override fun negate(): IPAddressP {
        return IPAddressP(biPredicate.negate(), originalValue)
    }

    override fun clone(): IPAddressP {
        return super.clone() as IPAddressP
    }

    companion object {
        fun eq(value: String?): IPAddressP {
            return IPAddressP(IPAddress.eq, value)
        }

        fun neq(value: String?): IPAddressP {
            return IPAddressP(IPAddress.neq, value)
        }

        fun lt(value: String?): IPAddressP {
            return IPAddressP(IPAddress.lt, value)
        }

        fun lte(value: String?): IPAddressP {
            return IPAddressP(IPAddress.lte, value)
        }

        fun gt(value: String?): IPAddressP {
            return IPAddressP(IPAddress.gt, value)
        }

        fun gte(value: String?): IPAddressP {
            return IPAddressP(IPAddress.gte, value)
        }

        fun withinNetwork(value: String?): IPAddressP {
            return IPAddressP(IPAddress.withinNetwork, value)
        }

        fun withoutNetwork(value: String?): IPAddressP {
            return IPAddressP(IPAddress.withoutNetwork, value)
        }

        val isIpv4: IPAddressP
            get() = IPAddressP(IPAddress.isIpv4, "")

        val isIpv6: IPAddressP
            get() = IPAddressP(IPAddress.isIpv6, "")
    }
}
