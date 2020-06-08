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
import org.apache.tinkerpop.gremlin.process.traversal.util.AndP
import java.util.*
import java.util.function.BiPredicate

class DateP(biPredicate: BiPredicate<Date?, Date?>, value: Date?) : P<Date>(biPredicate, value) {
    override fun equals(other: Any?): Boolean {
        return other is DateP && super.equals(other)
    }

    override fun toString(): String {
        return if (originalValue == null) {
            biPredicate.toString()
        } else {
            "$biPredicate($originalValue)"
        }
    }

    override fun negate(): DateP {
        return DateP(biPredicate.negate(), originalValue)
    }

    override fun clone(): DateP {
        return super.clone() as DateP
    }

    companion object {
        @JvmStatic
        fun before(value: Date?): DateP {
            return DateP(DateCompare.before, value)
        }

        @JvmStatic
        fun notBefore(value: Date?): DateP {
            return DateP(DateCompare.notBefore, value)
        }

        @JvmStatic
        fun after(value: Date?): DateP {
            return DateP(DateCompare.after, value)
        }

        @JvmStatic
        fun notAfter(value: Date?): DateP {
            return DateP(DateCompare.notAfter, value)
        }

        @JvmStatic
        fun between(from: Date?, to: Date?): P<Date> {
            return AndP(listOf(DateP(DateCompare.notBefore, from),
                               DateP(DateCompare.before, to)))
        }
    }
}
