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
        return faker.lorem().word().toLowerCase();
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

    public static PropertyType randomPropertyType(Boolean excludeMap) {
        return Arrays.asList(
                PropertyType.INTEGER,
                PropertyType.STRING,
                PropertyType.BOOLEAN,
                PropertyType.MAP
        ).get(faker.random().nextInt(0, excludeMap ? 2 : 3));
    }

    public static List<PropertyDef> randomPropertyDefs(Integer n) {
        return randomPropertyDefs(n, Boolean.FALSE);
    }

    public static List<PropertyDef> randomPropertyDefs(Integer n, Boolean excludeMap) {
        return IntStream.range(0, n)
                .mapToObj(i -> ModelGenerator.randomPropertyDef(excludeMap))
                .collect(Collectors.toList());
    }

    public static PropertyDef randomPropertyDef() {
        return randomPropertyDef(Boolean.FALSE);
    }

    public static PropertyDef randomPropertyDef(Boolean excludeMap) {
        PropertyType propertyType = randomPropertyType(excludeMap);
        if (propertyType.equals(PropertyType.MAP)) {
            return new PropertyDef(
                    randomName(),
                    randomDescription(),
                    PropertyType.MAP,
                    Boolean.FALSE,
                    randomPropertyDefs(3, Boolean.TRUE)
            );
        } else {
            return new PropertyDef(
                    randomName(),
                    randomDescription(),
                    propertyType,
                    Boolean.FALSE
            );
        }
    }

    public static List<AssociationDef> randomAssociationDefs(Integer n) {
        return IntStream.range(0, n)
                .mapToObj(i -> ModelGenerator.randomAssociationDef())
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

    public static Object randomPropertyValue() {
        return randomPropertyValue(randomPropertyDef());
    }

    public static Object randomPropertyValue(PropertyDef propertyDef) {
        if (propertyDef.getPropertyType().equals(PropertyType.INTEGER)) {
            return faker.random().nextInt(Integer.MAX_VALUE);
        } else if (propertyDef.getPropertyType().equals(PropertyType.STRING)) {
            return faker.lorem().sentence();
        } else if (propertyDef.getPropertyType().equals(PropertyType.BOOLEAN)) {
            return faker.random().nextBoolean();
        }

        return propertyDef.getProperties()
                .stream()
                .map(ModelGenerator::randomProperty)
                .collect(Collectors.toList());
    }

    public static List<Property> randomProperties(Integer n) {
        return randomProperties(randomPropertyDefs(n));
    }

    public static List<Property> randomProperties(List<PropertyDef> propertyDefs) {
        return propertyDefs.stream()
                .map(ModelGenerator::randomProperty)
                .collect(Collectors.toList());
    }

    public static Property randomProperty() {
        return randomProperty(randomPropertyDef());
    }

    public static Property randomProperty(PropertyDef propertyDef) {
        return new Property(
                propertyDef.getName(),
                randomPropertyValue(propertyDef)
        );
    }

    public static List<Association> randomAssociations(Integer n) {
        return randomAssociations(randomAssociationDefs(n));
    }

    public static List<Association> randomAssociations(List<AssociationDef> associationDefs) {
        return associationDefs
                .stream()
                .map(ModelGenerator::randomAssociation)
                .collect(Collectors.toList());
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
