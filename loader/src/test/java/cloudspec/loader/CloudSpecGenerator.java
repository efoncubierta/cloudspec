/*-
 * #%L
 * CloudSpec Loader Library
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
package cloudspec.loader;

import cloudspec.lang.*;
import cloudspec.lang.predicate.DateP;
import cloudspec.lang.predicate.IPAddressP;
import cloudspec.model.PropertyType;
import com.github.javafaker.Faker;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.TextP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CloudSpecGenerator {
    private static final Faker faker = new Faker();

    public static CloudSpec fullSpec() {
        return CloudSpec.builder()
                        .setName(faker.lorem().sentence())
                        .addGroups(fullGroup())
                        .build();
    }

    public static GroupExpr fullGroup() {
        return GroupExpr.builder()
                        .setName(faker.lorem().sentence())
                        .addRules(fullRule())
                        .build();
    }

    public static RuleExpr fullRule() {
        return RuleExpr.builder()
                       .setName(faker.lorem().sentence())
                       .setResourceDefRef(
                               String.format(
                                       "%s:%s:%s",
                                       faker.lorem().word(),
                                       faker.lorem().word(),
                                       faker.lorem().word()
                               )
                       )
                       .setWithExpr(fullWith())
                       .setAssertExp(fullAssert())
                       .build();
    }

    public static WithExpr fullWith() {
        return WithExpr.builder()
                       .setStatements(fullStatements(true, true))
                       .build();
    }

    public static AssertExpr fullAssert() {
        return AssertExpr.builder()
                         .setStatement(fullStatements(true, true))
                         .build();
    }

    public static List<Statement> fullStatements(Boolean includeNested, Boolean includeAssociation) {
        List<Statement> statements = new ArrayList<>();

        statements.add(randomValueNullPredicateStatement());
        statements.add(randomValueNotNullPredicateStatement());
        statements.add(randomValueEqualPredicateStatement());
        statements.add(randomValueNotEqualPredicateStatement());
        statements.add(randomValueWithinPredicateStatement());
        statements.add(randomValueNotWithinPredicateStatement());

        statements.add(randomNumberLessThanPredicateStatement());
        statements.add(randomNumberLessThanEqualPredicateStatement());
        statements.add(randomNumberGreaterThanPredicateStatement());
        statements.add(randomNumberGreaterThanEqualPredicateStatement());
        statements.add(randomNumberBetweenPredicateStatement());

        statements.add(randomStringEmptyPredicateStatement());
        statements.add(randomStringNotEmptyPredicateStatement());
        statements.add(randomStringStartingWithPredicateStatement());
        statements.add(randomStringNotStartingWithPredicateStatement());
        statements.add(randomStringEndingWithPredicateStatement());
        statements.add(randomStringNotEndingWithPredicateStatement());
        statements.add(randomStringContainingPredicateStatement());
        statements.add(randomStringNotContainingPredicateStatement());

        statements.add(randomIpAddressEqualPredicateStatement());
        statements.add(randomIpAddressNotEqualPredicateStatement());
        statements.add(randomIpAddressLessThanPredicateStatement());
        statements.add(randomIpAddressLessThanEqualPredicateStatement());
        statements.add(randomIpAddressGreaterThanPredicateStatement());
        statements.add(randomIpAddressGreaterThanEqualPredicateStatement());
        statements.add(randomIpWithinNetworkPredicateStatement());
        statements.add(randomNotIpWithinNetworkPredicateStatement());
        statements.add(randomIpIsIpv4PredicateStatement());
        statements.add(randomIpIsIpv6PredicateStatement());

        statements.add(randomDateBeforePredicateStatement());
        statements.add(randomDateNotBeforePredicateStatement());
        statements.add(randomDateAfterPredicateStatement());
        statements.add(randomDateNotAfterPredicateStatement());
        statements.add(randomDateBetweenPredicateStatement());

        statements.add(randomKeyValueEqualPredicateStatement());
        statements.add(randomKeyValueNotEqualPredicateStatement());
        statements.add(randomKeyValueWithinPredicateStatement());
        statements.add(randomKeyValueNotWithinPredicateStatement());

        if (includeNested) {
            statements.add(fullNestedStatement());
        }

        if (includeAssociation) {
            statements.add(fullAssociationStatement());
        }

        return statements;
    }

    public static PropertyStatement randomValueNullPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.eq(null)
        );
    }

    public static PropertyStatement randomValueNotNullPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.neq(null)
        );
    }

    public static PropertyStatement randomValueEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.eq(randomValue())
        );
    }

    public static PropertyStatement randomValueNotEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.neq(randomValue())
        );
    }

    public static PropertyStatement randomValueWithinPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.within(randomValues())
        );
    }

    public static PropertyStatement randomValueNotWithinPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.without(randomValues())
        );
    }

    public static PropertyStatement randomNumberLessThanPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.lt(randomNumber())
        );
    }

    public static PropertyStatement randomNumberLessThanEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.lte(randomNumber())
        );
    }

    public static PropertyStatement randomNumberGreaterThanPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.gt(randomNumber())
        );
    }

    public static PropertyStatement randomNumberGreaterThanEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.gte(randomNumber())
        );
    }

    public static PropertyStatement randomNumberBetweenPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.between(randomNumber(), randomNumber())
        );
    }

    public static PropertyStatement randomStringEmptyPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.eq("")
        );
    }

    public static PropertyStatement randomStringNotEmptyPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.neq("")
        );
    }

    public static PropertyStatement randomStringStartingWithPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.startingWith(randomString())
        );
    }

    public static PropertyStatement randomStringNotStartingWithPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.notStartingWith(randomString())
        );
    }

    public static PropertyStatement randomStringEndingWithPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.endingWith(randomString())
        );
    }

    public static PropertyStatement randomStringNotEndingWithPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.notEndingWith(randomString())
        );
    }

    public static PropertyStatement randomStringContainingPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.containing(randomString())
        );
    }

    public static PropertyStatement randomStringNotContainingPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                TextP.notContaining(randomString())
        );
    }

    public static PropertyStatement randomIpAddressEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.eq(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpAddressNotEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.neq(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpAddressLessThanPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.lt(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpAddressLessThanEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.lte(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpAddressGreaterThanPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.gt(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpAddressGreaterThanEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.gte(randomIpAddress())
        );
    }

    public static PropertyStatement randomIpWithinNetworkPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.withinNetwork(randomNetworkCidr())
        );
    }

    public static PropertyStatement randomNotIpWithinNetworkPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.withoutNetwork(randomNetworkCidr())
        );
    }

    public static PropertyStatement randomIpIsIpv4PredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.isIpv4()
        );
    }

    public static PropertyStatement randomIpIsIpv6PredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                IPAddressP.isIpv6()
        );
    }

    public static PropertyStatement randomDateBeforePredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                DateP.before(randomDate())
        );
    }

    public static PropertyStatement randomDateNotBeforePredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                DateP.notBefore(randomDate())
        );
    }

    public static PropertyStatement randomDateAfterPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                DateP.after(randomDate())
        );
    }

    public static PropertyStatement randomDateNotAfterPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                DateP.notAfter(randomDate())
        );
    }

    public static PropertyStatement randomDateBetweenPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                DateP.between(
                        randomDate(),
                        randomDate()
                )
        );
    }

    public static KeyValueStatement randomKeyValueEqualPredicateStatement() {
        return new KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.eq(randomValue())
        );
    }

    public static KeyValueStatement randomKeyValueNotEqualPredicateStatement() {
        return new KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.neq(randomValue())
        );
    }

    public static KeyValueStatement randomKeyValueWithinPredicateStatement() {
        return new KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.within(randomValues())
        );
    }

    public static KeyValueStatement randomKeyValueNotWithinPredicateStatement() {
        return new KeyValueStatement(
                faker.lorem().word(),
                faker.lorem().word(),
                P.without(randomValues())
        );
    }

    public static NestedStatement fullNestedStatement() {
        return new NestedStatement(
                faker.lorem().word(),
                fullStatements(false, false)
        );
    }

    public static AssociationStatement fullAssociationStatement() {
        return new AssociationStatement(
                faker.lorem().word(),
                fullStatements(false, false)
        );
    }

    public static PropertyType randomPropertyType() {
        return Arrays.asList(
                PropertyType.NUMBER,
                PropertyType.STRING,
                PropertyType.BOOLEAN
        ).get(faker.random().nextInt(0, 2));
    }

    public static Object randomValue() {
        return randomValue(randomPropertyType());
    }

    public static String randomString() {
        return faker.lorem().word();
    }

    public static String randomIpAddress() {
        return faker.random().nextBoolean() ?
                randomIpv4Address() :
                randomIpv6Address();
    }

    public static String randomIpv4Address() {
        return faker.internet().ipV4Address();
    }

    public static String randomIpv6Address() {
        return faker.internet().ipV6Address();
    }

    public static String randomNetworkCidr() {
        return faker.internet().ipV4Cidr();
    }

    public static Object randomNumber() {
        return randomValue(PropertyType.NUMBER);
    }

    public static Object randomValue(PropertyType propertyType) {
        switch (propertyType) {
            case NUMBER:
                return faker.random().nextBoolean() ?
                        faker.random().nextInt(0, 1000) :
                        faker.random().nextDouble();
            case BOOLEAN:
                return faker.random().nextBoolean();
            case DATE:
                return randomDate();
            case STRING:
            default:
                return faker.lorem().word();

        }
    }

    public static List<Object> randomValues() {
        return randomValues(randomPropertyType());
    }

    public static List<Object> randomValues(PropertyType propertyType) {
        return IntStream.range(0, 5)
                        .mapToObj(i -> randomValue(propertyType))
                        .collect(Collectors.toList());
    }

    public static Date randomDate() {
        // return a date rounded to seconds
        Date date = faker.date().past(1000, TimeUnit.DAYS);
        Long seconds = date.getTime() - (date.getTime() % 1000);
        return new Date(seconds);
    }
}
