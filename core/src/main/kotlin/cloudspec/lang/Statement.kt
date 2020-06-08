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
package cloudspec.lang

import cloudspec.lang.predicate.DateCompare
import cloudspec.lang.predicate.IPAddress
import org.apache.commons.lang.text.StrBuilder
import org.apache.tinkerpop.gremlin.process.traversal.Compare
import org.apache.tinkerpop.gremlin.process.traversal.Contains
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.Text
import org.apache.tinkerpop.gremlin.process.traversal.util.AndP
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

sealed class Statement : CloudSpecSyntaxProducer

/**
 * Statement for association.
 */
data class AssociationStatement(
        val associationName: String,
        val statements: List<Statement>
) : Statement() {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()
        sb.appendln("${" ".repeat(spaces)}>${associationName} ( ")
        sb.appendln(
                statements.joinToString(" And \n") { statement ->
                    statement.toCloudSpecSyntax(spaces + 4)
                }
        )
        sb.appendln("${" ".repeat(spaces)})")
        return sb.toString()
    }
}

/**
 * Statement for property.
 */
data class PropertyStatement(
        val propertyName: String,
        val predicate: P<*>
) : Statement() {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()
        sb.append("${" ".repeat(spaces)}${propertyName} ")
        sb.append(predicateToCloudSpecSyntax(predicate))
        return sb.toString()
    }
}

/**
 * Statement for key values.
 */
data class KeyValueStatement(
        val propertyName: String,
        val key: String,
        val predicate: P<*>
) : Statement() {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()
        sb.append("${" ".repeat(spaces)}${propertyName}[\"${key}\"] ")
        sb.append(predicateToCloudSpecSyntax(predicate))
        return sb.toString()
    }
}

/**
 * Statement for nested properties.
 */
data class NestedStatement(
        val propertyName: String,
        val statements: List<Statement>
) : Statement() {
    override fun toCloudSpecSyntax(spaces: Int): String {
        val sb = StringBuilder()
        sb.appendln("${" ".repeat(spaces)}${propertyName} ( ")
        sb.appendln(
                statements.joinToString(" and \n") { statement ->
                    statement.toCloudSpecSyntax(spaces + 4)
                }
        )
        sb.appendln("${" ".repeat(spaces)})")
        return sb.toString()
    }
}

fun predicateToCloudSpecSyntax(predicate: P<*>): String {
    val biPredicate = predicate.biPredicate
    val originalValue = predicate.originalValue

    if (biPredicate == Compare.eq) {
        return when {
            originalValue == null -> {
                "is null"
            }
            originalValue === "" -> {
                "is empty"
            }
            else -> {
                "is equal to " + valueToCloudSpecSyntax(originalValue)
            }
        }
    } else if (biPredicate == Compare.neq) {
        return when {
            originalValue == null -> {
                "is not null"
            }
            originalValue === "" -> {
                "is not empty"
            }
            else -> {
                "is not equal to " + valueToCloudSpecSyntax(originalValue)
            }
        }
    } else if (biPredicate == Compare.lt) {
        return "is less than " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Compare.lte) {
        return "is less than or equal to " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Compare.gt) {
        return "is greater than " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Compare.gte) {
        return "is greater than or equal to " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Contains.within) {
        return "is within " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Contains.without) {
        return "is not within " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.startingWith) {
        return "is starting with " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.notStartingWith) {
        return "is not starting with " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.endingWith) {
        return "is ending with " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.notEndingWith) {
        return "is not ending with " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.containing) {
        return "is containing " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == Text.notContaining) {
        return "is not containing " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.eq) {
        return "is equal to ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.neq) {
        return "is not equal to ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.lt) {
        return "is less than ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.lte) {
        return "is less than or equal to ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.gt) {
        return "is greater than ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.gte) {
        return "is greater than or equal to ip address " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.withinNetwork) {
        return "is within network cidr " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.withoutNetwork) {
        return "is not within network cidr " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == IPAddress.isIpv4) {
        return "is ipv4"
    } else if (biPredicate == IPAddress.isIpv6) {
        return "is ipv6"
    } else if (biPredicate == DateCompare.before) {
        return "is before " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == DateCompare.notBefore) {
        return "is not before " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == DateCompare.after) {
        return "is after " + valueToCloudSpecSyntax(originalValue)
    } else if (biPredicate == DateCompare.notAfter) {
        return "is not after " + valueToCloudSpecSyntax(originalValue)
    } else if (predicate is AndP<*>) {
        val andP = predicate
        val leftPredicate = andP.predicates[0]
        val rightPredicate = andP.predicates[1]
        if ((leftPredicate.biPredicate
                        == Compare.gte) && (rightPredicate.biPredicate
                        == Compare.lt) ||
                (leftPredicate.biPredicate
                        == DateCompare.notBefore) && (rightPredicate.biPredicate
                        == DateCompare.before)) {
            return "is between " + valueToCloudSpecSyntax(leftPredicate.originalValue) +
                    " and " + valueToCloudSpecSyntax(rightPredicate.originalValue)
        }
    }
    return "UNKNOWN"
}

fun valueToCloudSpecSyntax(value: Any?): String {
    return when (value) {
        is List<*> -> {
            val values = value.joinToString(", ") { v ->
                valueToCloudSpecSyntax(v)
            }
            "[${values}]"
        }
        is String -> {
            "\"${value}\""
        }
        is Date -> {
            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            "\"${df.format(value)}\""
        }
        is Double -> {
            val format = DecimalFormat("0.0000000000000000000")
            format.format(value)
        }
        else -> {
            value.toString()
        }
    }
}
