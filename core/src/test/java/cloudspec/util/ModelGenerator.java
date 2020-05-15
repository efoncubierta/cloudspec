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
package cloudspec.util;

import cloudspec.model.*;
import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ModelGenerator {
    private static final Faker faker = new Faker();

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

    public static PropertyType randomPropertyType() {
        return randomPropertyType(Boolean.FALSE);
    }

    public static PropertyType randomPropertyType(Boolean excludeNested) {
        return Arrays.asList(
                PropertyType.INTEGER,
                PropertyType.STRING,
                PropertyType.BOOLEAN,
                PropertyType.KEY_VALUE,
                PropertyType.NESTED
        ).get(faker.random().nextInt(0, excludeNested ? 3 : 4));
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
                        randomPropertyDefs(3, Boolean.TRUE)
                );
            case KEY_VALUE:
            default:
                return new PropertyDef(
                        randomName(),
                        randomDescription(),
                        propertyType,
                        faker.random().nextBoolean()
                );
        }
    }

    public static List<AssociationDef> randomAssociationDefs(Integer n) {
        return IntStream.range(0, n)
                .mapToObj(i -> randomAssociationDef())
                .collect(Collectors.toList());
    }

    public static AssociationDef randomAssociationDef() {
        return new AssociationDef(
                randomName(),
                randomDescription(),
                randomResourceDefRef()
        );
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
                return new Properties(
                        propertyDef.getProperties()
                                .stream()
                                .map(ModelGenerator::randomProperty)
                );
            case KEY_VALUE:
                return new KeyValue(faker.lorem().word(), faker.lorem().word());
            case INTEGER:
                return faker.random().nextInt(Integer.MAX_VALUE);
            case BOOLEAN:
                return faker.random().nextBoolean();
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

    public static Property randomProperty() {
        return randomProperty(randomPropertyDef());
    }

    public static Property randomProperty(PropertyDef propertyDef) {
        return new Property(
                propertyDef.getName(),
                propertyDef.isMultiValued() ?
                        randomPropertyValues(
                                faker.random().nextInt(3, 5),
                                propertyDef
                        ) :
                        randomPropertyValue(propertyDef)
        );
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

    public static Association randomAssociation() {
        return randomAssociation(randomAssociationDef());
    }

    public static Association randomAssociation(AssociationDef associationDef) {
        return new Association(
                associationDef.getName(),
                associationDef.getResourceDefRef(),
                randomResourceId()
        );
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
