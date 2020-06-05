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
package cloudspec.util;

import cloudspec.model.*;
import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModelGenerator {
    private static final Faker faker = new Faker();

    public static PropertyType randomPropertyType() {
        return randomPropertyType(Boolean.FALSE);
    }

    public static PropertyType randomPropertyType(Boolean excludeNested) {
        return Arrays.asList(
                PropertyType.NUMBER,
                PropertyType.STRING,
                PropertyType.BOOLEAN,
                PropertyType.DATE,
                PropertyType.KEY_VALUE,
                PropertyType.NESTED
        ).get(faker.random().nextInt(0, excludeNested ? 4 : 5));
    }

    public static List<PropertyDef> randomPropertyDefs(Integer n) {
        return randomPropertyDefs(n, Boolean.FALSE);
    }

    public static List<PropertyDef> randomPropertyDefs(Integer n, Boolean excludeNested) {
        return IntStream.range(0, n)
                        .mapToObj(i -> randomPropertyDef(excludeNested))
                        .collect(Collectors.toList());
    }

    public static PropertyDef randomPropertyDef() {
        return randomPropertyDef(Boolean.FALSE);
    }

    public static PropertyDef randomPropertyDef(Boolean excludeNested) {
        PropertyType propertyType = randomPropertyType(excludeNested);
        switch (propertyType) {
            case NESTED:
                return new PropertyDef(
                        randomName(),
                        randomDescription(),
                        PropertyType.NESTED,
                        faker.random().nextBoolean(),
                        faker.lorem().word(),
                        randomPropertyDefs(3, Boolean.TRUE),
                        randomAssociationDefs(3)
                );
            case KEY_VALUE:
            default:
                return new PropertyDef(
                        randomName(),
                        randomDescription(),
                        propertyType,
                        faker.random().nextBoolean(),
                        faker.lorem().word()
                );
        }
    }

    public static ResourceDef randomResourceDef() {
        return randomResourceDef(randomResourceDefRef());
    }

    public static ResourceDef randomResourceDef(ResourceDefRef resourceDefRef) {
        return new ResourceDef(
                resourceDefRef,
                randomDescription(),
                randomPropertyDefs(faker.random().nextInt(5, 10)),
                randomAssociationDefs(faker.random().nextInt(2, 5))
        );
    }

    public static List<Object> randomPropertyValues(Integer n) {
        return IntStream.range(0, n)
                        .mapToObj(i -> randomPropertyValue())
                        .collect(Collectors.toList());
    }

    public static List<Object> randomPropertyValues(Integer n, PropertyDef propertyDef) {
        return IntStream.range(0, n)
                        .mapToObj(i -> randomPropertyValue(propertyDef))
                        .collect(Collectors.toList());
    }

    public static Object randomPropertyValue() {
        return randomPropertyValue(randomPropertyDef());
    }

    public static Object randomPropertyValue(PropertyDef propertyDef) {
        switch (propertyDef.getPropertyType()) {
            case NESTED:
                return new NestedPropertyValue(
                        new Properties(
                                propertyDef.getProperties()
                                           .stream()
                                           .map(ModelGenerator::randomProperty)
                        ),
                        new Associations()
                );
            case KEY_VALUE:
                return new KeyValue(faker.lorem().word(), faker.lorem().word());
            case NUMBER:
                return faker.random().nextBoolean() ?
                        faker.random().nextInt(Integer.MAX_VALUE) :
                        faker.random().nextDouble();
            case BOOLEAN:
                return faker.random().nextBoolean();
            case DATE:
                return faker.date().past(1000, TimeUnit.DAYS);
            case STRING:
            default:
                return faker.lorem().sentence();
        }
    }

    public static Properties randomProperties(Integer n) {
        return randomProperties(randomPropertyDefs(n));
    }

    public static Properties randomProperties(List<PropertyDef> propertyDefs) {
        return new Properties(
                propertyDefs.stream()
                            .map(ModelGenerator::randomProperty)
        );
    }

    public static Property<?> randomProperty() {
        return randomProperty(randomPropertyDef());
    }

    public static Property<?> randomProperty(PropertyDef propertyDef) {
        switch (propertyDef.getPropertyType()) {
            case NUMBER:
                return new NumberProperty(
                        propertyDef.getName(),
                        (Number) randomPropertyValue(propertyDef)
                );
            case BOOLEAN:
                return new BooleanProperty(
                        propertyDef.getName(),
                        (Boolean) randomPropertyValue(propertyDef)
                );
            case DATE:
                return new DateProperty(
                        propertyDef.getName(),
                        (Date) randomPropertyValue(propertyDef)
                );
            case KEY_VALUE:
                return new KeyValueProperty(
                        propertyDef.getName(),
                        (KeyValue) randomPropertyValue(propertyDef)
                );
            case NESTED:
                return new NestedProperty(
                        propertyDef.getName(),
                        (NestedPropertyValue) randomPropertyValue(propertyDef)
                );
            case STRING:
            default:
                return new StringProperty(
                        propertyDef.getName(),
                        (String) randomPropertyValue(propertyDef)
                );
        }

    }

    public static Associations randomAssociations(Integer n) {
        return randomAssociations(randomAssociationDefs(n));
    }

    public static Associations randomAssociations(List<AssociationDef> associationDefs) {
        return new Associations(
                associationDefs
                        .stream()
                        .map(ModelGenerator::randomAssociation)
        );
    }

    public static List<AssociationDef> randomAssociationDefs(Integer n) {
        return IntStream.range(0, n)
                        .mapToObj(i -> randomAssociationDef())
                        .collect(Collectors.toList());
    }

    public static Association randomAssociation(AssociationDef associationDef) {
        return new Association(
                associationDef.getName(),
                associationDef.getResourceDefRef(),
                randomResourceId()
        );
    }

    public static AssociationDef randomAssociationDef() {
        return new AssociationDef(
                randomName(),
                randomDescription(),
                randomResourceDefRef(),
                faker.random().nextBoolean()
        );
    }

    public static String randomResourceId() {
        return faker.idNumber().valid();
    }

    public static String randomName() {
        return faker.lorem().characters(5, 10).toLowerCase();
    }

    public static String randomDescription() {
        return faker.lorem().sentence();
    }

    public static ResourceDefRef randomResourceDefRef() {
        return new ResourceDefRef(
                randomName(),
                randomName(),
                randomName()
        );
    }

    public static Association randomAssociation() {
        return randomAssociation(randomAssociationDef());
    }

    public static Resource randomResource() {
        return randomResource(randomResourceDef());
    }

    public static Resource randomResource(ResourceDef resourceDef) {
        return new Resource(
                resourceDef.getRef(),
                randomResourceId(),
                randomProperties(resourceDef.getProperties()),
                randomAssociations(resourceDef.getAssociations())
        );
    }
}
