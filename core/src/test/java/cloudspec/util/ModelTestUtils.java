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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ModelTestUtils {
    public static final String PROVIDER_NAME = "myprovider";
    public static final String PROVIDER_DESCRIPTION = "My provider";

    public static final String RESOURCE_GROUP = "mygroup";

    public static final String RESOURCE_NAME = "myresource";
    public static final String RESOURCE_DESCRIPTION = "My resource";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(ProviderDataUtil.PROVIDER_NAME, RESOURCE_GROUP, RESOURCE_NAME);

    public static final String TARGET_RESOURCE_NAME = "mytargetresource";
    public static final String TARGET_RESOURCE_DESCRIPTION = "My target resource";
    public static final ResourceDefRef TARGET_RESOURCE_DEF_REF = new ResourceDefRef(ProviderDataUtil.PROVIDER_NAME, RESOURCE_GROUP, TARGET_RESOURCE_NAME);

    public static final String RESOURCE_ID = UUID.randomUUID().toString();
    public static final String TARGET_RESOURCE_ID = UUID.randomUUID().toString();

    public static final String PROP_INTEGER_NAME = "integer_property";
    public static final String PROP_INTEGER_DESCRIPTION = "Integer property";
    public static final PropertyType PROP_INTEGER_TYPE = PropertyType.INTEGER;
    public static final Integer PROP_INTEGER_VALUE = 1;
    public static final PropertyDef PROP_INTEGER_DEF = new PropertyDef(
            PROP_INTEGER_NAME,
            PROP_INTEGER_DESCRIPTION,
            PROP_INTEGER_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_INTEGER = new Property(PROP_INTEGER_NAME, PROP_INTEGER_VALUE);

    public static final String PROP_STRING_NAME = "string_property";
    public static final String PROP_STRING_DESCRIPTION = "String property";
    public static final PropertyType PROP_STRING_TYPE = PropertyType.STRING;
    public static final String PROP_STRING_VALUE = "foo";
    public static final PropertyDef PROP_STRING_DEF = new PropertyDef(
            PROP_STRING_NAME,
            PROP_STRING_DESCRIPTION,
            PROP_STRING_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_STRING = new Property(PROP_STRING_NAME, PROP_STRING_VALUE);

    public static final String PROP_BOOLEAN_NAME = "boolean_property";
    public static final String PROP_BOOLEAN_DESCRIPTION = "Boolean property";
    public static final PropertyType PROP_BOOLEAN_TYPE = PropertyType.BOOLEAN;
    public static final Boolean PROP_BOOLEAN_VALUE = Boolean.TRUE;
    public static final PropertyDef PROP_BOOLEAN_DEF = new PropertyDef(
            PROP_BOOLEAN_NAME,
            PROP_BOOLEAN_DESCRIPTION,
            PROP_BOOLEAN_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_BOOLEAN = new Property(PROP_BOOLEAN_NAME, PROP_BOOLEAN_VALUE);

    public static final List<Property> PROPERTIES = Arrays.asList(PROP_INTEGER, PROP_STRING, PROP_BOOLEAN);
    public static final List<PropertyDef> PROPERTY_DEFS = Arrays.asList(PROP_INTEGER_DEF, PROP_STRING_DEF, PROP_BOOLEAN_DEF);


    public static final String ASSOC_NAME = "myassociation";
    public static final String ASSOC_DESCRIPTION = "My association";
    public static final AssociationDef ASSOCIATION_DEF = new AssociationDef(ASSOC_NAME, ASSOC_DESCRIPTION, TARGET_RESOURCE_DEF_REF);
    public static final Association ASSOCIATION = new Association(ASSOC_NAME, TARGET_RESOURCE_DEF_REF, TARGET_RESOURCE_ID);
    public static final List<Association> ASSOCIATIONS = Collections.singletonList(ASSOCIATION);
    public static final List<AssociationDef> ASSOCIATION_DEFS = Collections.singletonList(ASSOCIATION_DEF);

    // test model objects
    public static final ResourceDef RESOURCE_DEF = new ResourceDef(
            RESOURCE_DEF_REF,
            RESOURCE_DESCRIPTION,
            PROPERTY_DEFS,
            ASSOCIATION_DEFS
    );
    public static final Resource RESOURCE = new Resource(
            RESOURCE_DEF_REF,
            RESOURCE_ID,
            PROPERTIES,
            ASSOCIATIONS
    );
    public static final Resource TARGET_RESOURCE = new Resource(
            TARGET_RESOURCE_DEF_REF,
            TARGET_RESOURCE_ID,
            PROPERTIES,
            Collections.emptyList()
    );

    public static final Provider TEST_PROVIDER = new TestProvider();
    public static final TestResource TEST_RESOURCE = new TestResource(RESOURCE_ID, PROP_INTEGER_VALUE, PROP_STRING_VALUE, PROP_BOOLEAN_VALUE, TARGET_RESOURCE_ID);

    public static void compareProperties(List<Property> properties1, List<Property> properties2) {
        assertNotNull(properties1);
        assertNotNull(properties2);
        assertEquals(properties1.size(), properties2.size());

        assertTrue(
                properties1.stream().allMatch(property1 ->
                        properties2.stream().anyMatch(property1::equals))
        );
    }

    public static void compareAssociations(List<Association> associations1, List<Association> associations2) {
        assertNotNull(associations1);
        assertNotNull(associations2);
        assertEquals(associations1.size(), associations2.size());

        assertTrue(
                associations1.stream().allMatch(association1 ->
                        associations2.stream().anyMatch(association1::equals))
        );
    }

    public static void comparePropertyDefs(List<PropertyDef> propertyDefs1, List<PropertyDef> propertyDefs2) {
        assertNotNull(propertyDefs1);
        assertNotNull(propertyDefs2);
        assertEquals(propertyDefs1.size(), propertyDefs2.size());

        assertTrue(
                propertyDefs1.stream().allMatch(propertyDef1 ->
                        propertyDefs2.stream().anyMatch(propertyDef1::equals))
        );
    }

    public static void compareAssociationDefs(List<AssociationDef> associationDefs1, List<AssociationDef> associationDefs2) {
        assertNotNull(associationDefs1);
        assertNotNull(associationDefs2);
        assertEquals(associationDefs1.size(), associationDefs2.size());

        assertTrue(
                associationDefs1.stream().allMatch(associationDef1 ->
                        associationDefs2.stream().anyMatch(associationDef1::equals))
        );
    }
}
