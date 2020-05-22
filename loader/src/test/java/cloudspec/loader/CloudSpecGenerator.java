/*-
 * #%L
 * CloudSpec Loader Library
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
package cloudspec.loader;

import cloudspec.lang.*;
import cloudspec.model.PropertyType;
import com.github.javafaker.Faker;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        statements.add(randomEqualPredicateStatement());
        statements.add(randomNotEqualPredicateStatement());
        statements.add(randomWithinPredicateStatement());
        statements.add(randomNotWithinPredicateStatement());
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

    public static PropertyStatement randomEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.eq(randomValue())
        );
    }

    public static PropertyStatement randomNotEqualPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.neq(randomValue())
        );
    }

    public static PropertyStatement randomWithinPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.within(randomValues())
        );
    }

    public static PropertyStatement randomNotWithinPredicateStatement() {
        return new PropertyStatement(
                faker.lorem().word(),
                P.without(randomValues())
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
                PropertyType.INTEGER,
                PropertyType.DOUBLE,
                PropertyType.STRING,
                PropertyType.BOOLEAN
        ).get(faker.random().nextInt(0, 3));
    }

    public static Object randomValue() {
        return randomValue(randomPropertyType());
    }

    public static Object randomValue(PropertyType propertyType) {
        switch (propertyType) {
            case INTEGER:
                return faker.random().nextInt(0, 1000);
            case DOUBLE:
                return faker.random().nextDouble();
            case BOOLEAN:
                return faker.random().nextBoolean();
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
}
