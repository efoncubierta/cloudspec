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
package cloudspec.loader

import cloudspec.lang.*
import cloudspec.lang.predicate.DateP.Companion.after
import cloudspec.lang.predicate.DateP.Companion.before
import cloudspec.lang.predicate.DateP.Companion.between
import cloudspec.lang.predicate.DateP.Companion.notAfter
import cloudspec.lang.predicate.DateP.Companion.notBefore
import cloudspec.lang.predicate.IPAddressP.Companion.eq
import cloudspec.lang.predicate.IPAddressP.Companion.gt
import cloudspec.lang.predicate.IPAddressP.Companion.gte
import cloudspec.lang.predicate.IPAddressP.Companion.isIpv4
import cloudspec.lang.predicate.IPAddressP.Companion.isIpv6
import cloudspec.lang.predicate.IPAddressP.Companion.lt
import cloudspec.lang.predicate.IPAddressP.Companion.lte
import cloudspec.lang.predicate.IPAddressP.Companion.neq
import cloudspec.lang.predicate.IPAddressP.Companion.withinNetwork
import cloudspec.lang.predicate.IPAddressP.Companion.withoutNetwork
import cloudspec.model.ConfigValueType
import cloudspec.model.PropertyType
import com.github.javafaker.Faker
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

object CloudSpecGenerator {
    private val faker = Faker()

    fun fullSpec(): CloudSpec {
        return CloudSpec.builder()
            .setName(faker.lorem().sentence())
            .addConfig(fullConfig(), fullConfig())
            .addGroups(fullGroup(), fullGroup())
            .build()
    }

    fun fullGroup(): GroupExpr {
        return GroupExpr.builder()
            .setName(faker.lorem().sentence())
            .addConfig(fullConfig(), fullConfig())
            .addRules(fullRule(), fullRule())
            .build()
    }

    fun fullRule(): RuleExpr {
        return RuleExpr.builder()
            .setName(faker.lorem().sentence())
            .setResourceDefRef("${faker.lorem().word()}:${faker.lorem().word()}:${faker.lorem().word()}")
            .addConfig(fullConfig(), fullConfig())
            .setWithExpr(fullWith())
            .setAssertExp(fullAssert())
            .build()
    }

    fun fullConfig(): ConfigExpr {
        return ConfigExpr(
                "${faker.lorem().word()}:${faker.lorem().word()}",
                randomConfigValue()
        )
    }

    fun fullWith(): WithExpr {
        return WithExpr.builder()
            .setStatements(fullStatements(includeNested = true, includeAssociation = true))
            .build()
    }

    fun fullAssert(): AssertExpr {
        return AssertExpr.builder()
            .setStatement(fullStatements(includeNested = true, includeAssociation = true))
            .build()
    }

    fun fullStatements(includeNested: Boolean, includeAssociation: Boolean): List<Statement> {
        val statements = mutableListOf<Statement>()
        statements.add(randomValueNullPredicateStatement())
        statements.add(randomValueNotNullPredicateStatement())
        statements.add(randomValueEqualPredicateStatement())
        statements.add(randomValueNotEqualPredicateStatement())
        statements.add(randomValueWithinPredicateStatement())
        statements.add(randomValueNotWithinPredicateStatement())
        statements.add(randomNumberLessThanPredicateStatement())
        statements.add(randomNumberLessThanEqualPredicateStatement())
        statements.add(randomNumberGreaterThanPredicateStatement())
        statements.add(randomNumberGreaterThanEqualPredicateStatement())
        statements.add(randomNumberBetweenPredicateStatement())
        statements.add(randomStringEmptyPredicateStatement())
        statements.add(randomStringNotEmptyPredicateStatement())
        statements.add(randomStringStartingWithPredicateStatement())
        statements.add(randomStringNotStartingWithPredicateStatement())
        statements.add(randomStringEndingWithPredicateStatement())
        statements.add(randomStringNotEndingWithPredicateStatement())
        statements.add(randomStringContainingPredicateStatement())
        statements.add(randomStringNotContainingPredicateStatement())
        statements.add(randomIpAddressEqualPredicateStatement())
        statements.add(randomIpAddressNotEqualPredicateStatement())
        statements.add(randomIpAddressLessThanPredicateStatement())
        statements.add(randomIpAddressLessThanEqualPredicateStatement())
        statements.add(randomIpAddressGreaterThanPredicateStatement())
        statements.add(randomIpAddressGreaterThanEqualPredicateStatement())
        statements.add(randomIpWithinNetworkPredicateStatement())
        statements.add(randomNotIpWithinNetworkPredicateStatement())
        statements.add(randomIpIsIpv4PredicateStatement())
        statements.add(randomIpIsIpv6PredicateStatement())
        statements.add(randomDateBeforePredicateStatement())
        statements.add(randomDateNotBeforePredicateStatement())
        statements.add(randomDateAfterPredicateStatement())
        statements.add(randomDateNotAfterPredicateStatement())
        statements.add(randomDateBetweenPredicateStatement())
        statements.add(randomKeyValueEqualPredicateStatement())
        statements.add(randomKeyValueNotEqualPredicateStatement())
        statements.add(randomKeyValueWithinPredicateStatement())
        statements.add(randomKeyValueNotWithinPredicateStatement())

        if (includeNested) {
            statements.add(fullNestedStatement())
        }

        if (includeAssociation) {
            statements.add(fullAssociationStatement())
        }

        return statements
    }

    fun randomValueNullPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.eq<Any?>(null)
        )
    }

    fun randomValueNotNullPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.neq<Any?>(null)
        )
    }

    fun randomValueEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.eq(randomValue())
        )
    }

    fun randomValueNotEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.neq(randomValue())
        )
    }

    fun randomValueWithinPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.within(randomValues())
        )
    }

    fun randomValueNotWithinPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.without(randomValues())
        )
    }

    fun randomNumberLessThanPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.lt(randomNumber())
        )
    }

    fun randomNumberLessThanEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.lte(randomNumber())
        )
    }

    fun randomNumberGreaterThanPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.gt(randomNumber())
        )
    }

    fun randomNumberGreaterThanEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.gte(randomNumber())
        )
    }

    fun randomNumberBetweenPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.between(randomNumber(), randomNumber())
        )
    }

    fun randomStringEmptyPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.eq("")
        )
    }

    fun randomStringNotEmptyPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                P.neq("")
        )
    }

    fun randomStringStartingWithPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.startingWith(randomString())
        )
    }

    fun randomStringNotStartingWithPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.notStartingWith(randomString())
        )
    }

    fun randomStringEndingWithPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.endingWith(randomString())
        )
    }

    fun randomStringNotEndingWithPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.notEndingWith(randomString())
        )
    }

    fun randomStringContainingPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.containing(randomString())
        )
    }

    fun randomStringNotContainingPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                TextP.notContaining(randomString())
        )
    }

    fun randomIpAddressEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                eq(randomIpAddress())
        )
    }

    fun randomIpAddressNotEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                neq(randomIpAddress())
        )
    }

    fun randomIpAddressLessThanPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                lt(randomIpAddress())
        )
    }

    fun randomIpAddressLessThanEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                lte(randomIpAddress())
        )
    }

    fun randomIpAddressGreaterThanPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                gt(randomIpAddress())
        )
    }

    fun randomIpAddressGreaterThanEqualPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                gte(randomIpAddress())
        )
    }

    fun randomIpWithinNetworkPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                withinNetwork(randomNetworkCidr())
        )
    }

    fun randomNotIpWithinNetworkPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                withoutNetwork(randomNetworkCidr())
        )
    }

    fun randomIpIsIpv4PredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                isIpv4
        )
    }

    fun randomIpIsIpv6PredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                isIpv6
        )
    }

    fun randomDateBeforePredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                before(randomInstant())
        )
    }

    fun randomDateNotBeforePredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                notBefore(randomInstant())
        )
    }

    fun randomDateAfterPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                after(randomInstant())
        )
    }

    fun randomDateNotAfterPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                notAfter(randomInstant())
        )
    }

    fun randomDateBetweenPredicateStatement(): PropertyStatement {
        return PropertyStatement(
                faker.lorem().word(),
                between(randomInstant(), randomInstant())
        )
    }

    fun randomKeyValueEqualPredicateStatement(): KeyValueStatement {
        return KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.eq(randomValue())
        )
    }

    fun randomKeyValueNotEqualPredicateStatement(): KeyValueStatement {
        return KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.neq(randomValue())
        )
    }

    fun randomKeyValueWithinPredicateStatement(): KeyValueStatement {
        return KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.within(randomValues())
        )
    }

    fun randomKeyValueNotWithinPredicateStatement(): KeyValueStatement {
        return KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.without(randomValues())
        )
    }

    fun fullNestedStatement(): NestedStatement {
        return NestedStatement(
                faker.lorem().word(),
                fullStatements(includeNested = false, includeAssociation = false)
        )
    }

    fun fullAssociationStatement(): AssociationStatement {
        return AssociationStatement(
                faker.lorem().word(),
                fullStatements(includeNested = false, includeAssociation = false)
        )
    }

    fun randomPropertyType(): PropertyType {
        return listOf(
                PropertyType.NUMBER,
                PropertyType.STRING,
                PropertyType.BOOLEAN
        )[faker.random().nextInt(0, 2)]
    }

    fun randomString(): String {
        return faker.lorem().word()
    }

    fun randomIpAddress(): String {
        return if (faker.random().nextBoolean()) {
            randomIpv4Address()
        } else {
            randomIpv6Address()
        }
    }

    fun randomIpv4Address(): String {
        return faker.internet().ipV4Address()
    }

    fun randomIpv6Address(): String {
        return faker.internet().ipV6Address()
    }

    fun randomNetworkCidr(): String {
        return faker.internet().ipV4Cidr()
    }

    fun randomNumber(): Any {
        return randomValue(PropertyType.NUMBER)
    }

    fun randomValue(propertyType: PropertyType = randomPropertyType()): Any {
        return when (propertyType) {
            PropertyType.NUMBER -> {
                if (faker.random().nextBoolean()) {
                    faker.random().nextInt(0, 1000)
                } else {
                    faker.random().nextDouble()
                }
            }
            PropertyType.BOOLEAN -> faker.random().nextBoolean()
            PropertyType.DATE -> randomDate()
            PropertyType.STRING -> faker.lorem().word()
            else -> faker.lorem().word()
        }
    }

    fun randomValues(propertyType: PropertyType = randomPropertyType()): List<Any> {
        return (0..5).map { randomValue(propertyType) }
    }

    fun randomConfigValueType(): ConfigValueType {
        return listOf(
                ConfigValueType.NUMBER,
                ConfigValueType.STRING,
                ConfigValueType.BOOLEAN
        )[faker.random().nextInt(0, 2)]
    }

    fun randomConfigValue(configValueType: ConfigValueType = randomConfigValueType()): Any {
        return when (configValueType) {
            ConfigValueType.NUMBER -> {
                if (faker.random().nextBoolean()) {
                    faker.random().nextInt(0, 1000)
                } else {
                    faker.random().nextDouble()
                }
            }
            ConfigValueType.BOOLEAN -> faker.random().nextBoolean()
            ConfigValueType.STRING -> faker.lorem().word()
        }
    }

    fun randomInstant(): Instant {
        return randomDate().toInstant()
    }

    fun randomDate(): Date {
        // return a date rounded to seconds
        val date = faker.date().past(1000, TimeUnit.DAYS)
        val seconds = date.time - date.time % 1000
        return Date(seconds)
    }
}
