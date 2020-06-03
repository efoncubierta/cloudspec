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

import cloudspec.model.Properties;
import cloudspec.model.*;

import java.util.*;

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

    public static final String ASSOC_NAME = "myassociation";
    public static final String ASSOC_DESCRIPTION = "My association";
    public static final AssociationDef ASSOCIATION_DEF = new AssociationDef(ASSOC_NAME, ASSOC_DESCRIPTION,
            TARGET_RESOURCE_DEF_REF, Boolean.FALSE);
    public static final Association ASSOCIATION = new Association(ASSOC_NAME, TARGET_RESOURCE_DEF_REF, TARGET_RESOURCE_ID);
    public static final Associations ASSOCIATIONS = new Associations(ASSOCIATION);
    public static final List<AssociationDef> ASSOCIATION_DEFS = Collections.singletonList(ASSOCIATION_DEF);
    public static final Associations TARGET_ASSOCIATIONS = new Associations();
    public static final List<AssociationDef> TARGET_ASSOCIATION_DEFS = Collections.emptyList();

    public static final String PROP_ID_NAME = "id_property";
    public static final String PROP_ID_DESCRIPTION = "ID property";
    public static final String PROP_ID_EXAMPLE_VALUES = "The ID property";
    public static final PropertyType PROP_ID_TYPE = PropertyType.STRING;
    public static final PropertyDef PROP_ID_DEF = new PropertyDef(
            PROP_ID_NAME,
            PROP_ID_DESCRIPTION,
            PROP_ID_TYPE,
            Boolean.FALSE,
            PROP_ID_EXAMPLE_VALUES
    );
    public static final StringProperty PROP_ID = new StringProperty(PROP_ID_NAME, RESOURCE_ID);

    public static final String PROP_NUMBER_NAME = "number_property";
    public static final String PROP_NUMBER_DESCRIPTION = "Number property";
    public static final String PROP_NUMBER_EXAMPLE_VALUES = "The number property";
    public static final PropertyType PROP_NUMBER_TYPE = PropertyType.NUMBER;
    public static final Integer PROP_NUMBER_VALUE = 1;
    public static final PropertyDef PROP_NUMBER_DEF = new PropertyDef(
            PROP_NUMBER_NAME,
            PROP_NUMBER_DESCRIPTION,
            PROP_NUMBER_TYPE,
            Boolean.FALSE,
            PROP_NUMBER_EXAMPLE_VALUES
    );
    public static final NumberProperty PROP_NUMBER = new NumberProperty(PROP_NUMBER_NAME, PROP_NUMBER_VALUE);

    public static final String PROP_NUMBER_LIST_NAME = "integer_list_property";
    public static final String PROP_NUMBER_LIST_DESCRIPTION = "Integer list property";
    public static final String PROP_NUMBER_LIST_EXAMPLE_VALUES = "The list of integer property";
    public static final PropertyType PROP_NUMBER_LIST_TYPE = PropertyType.NUMBER;
    public static final List<Integer> PROP_NUMBER_LIST_VALUE = Arrays.asList(PROP_NUMBER_VALUE, PROP_NUMBER_VALUE);
    public static final PropertyDef PROP_NUMBER_LIST_DEF = new PropertyDef(
            PROP_NUMBER_LIST_NAME,
            PROP_NUMBER_LIST_DESCRIPTION,
            PROP_NUMBER_LIST_TYPE,
            Boolean.TRUE,
            PROP_NUMBER_LIST_EXAMPLE_VALUES
    );
    public static final NumberProperty PROP_NUMBER_LIST = new NumberProperty(PROP_NUMBER_LIST_NAME, PROP_NUMBER_VALUE);

    public static final String PROP_STRING_NAME = "string_property";
    public static final String PROP_STRING_DESCRIPTION = "String property";
    public static final String PROP_STRING_EXAMPLE_VALUES = "The string property";
    public static final PropertyType PROP_STRING_TYPE = PropertyType.STRING;
    public static final String PROP_STRING_VALUE = "foo";
    public static final PropertyDef PROP_STRING_DEF = new PropertyDef(
            PROP_STRING_NAME,
            PROP_STRING_DESCRIPTION,
            PROP_STRING_TYPE,
            Boolean.FALSE,
            PROP_STRING_EXAMPLE_VALUES
    );
    public static final StringProperty PROP_STRING = new StringProperty(PROP_STRING_NAME, PROP_STRING_VALUE);

    public static final String PROP_STRING_LIST_NAME = "string_list_property";
    public static final String PROP_STRING_LIST_DESCRIPTION = "String list property";
    public static final String PROP_STRING_LIST_EXAMPLE_VALUES = "The list of strings property";
    public static final PropertyType PROP_STRING_LIST_TYPE = PropertyType.STRING;
    public static final List<String> PROP_STRING_LIST_VALUE = Arrays.asList(PROP_STRING_VALUE, PROP_STRING_VALUE);
    public static final PropertyDef PROP_STRING_LIST_DEF = new PropertyDef(
            PROP_STRING_LIST_NAME,
            PROP_STRING_LIST_DESCRIPTION,
            PROP_STRING_LIST_TYPE,
            Boolean.TRUE,
            PROP_STRING_LIST_EXAMPLE_VALUES
    );
    public static final StringProperty PROP_STRING_LIST = new StringProperty(PROP_STRING_LIST_NAME, PROP_STRING_VALUE);

    public static final String PROP_BOOLEAN_NAME = "boolean_property";
    public static final String PROP_BOOLEAN_DESCRIPTION = "Boolean property";
    public static final String PROP_BOOLEAN_EXAMPLE_VALUES = "The boolean property";
    public static final PropertyType PROP_BOOLEAN_TYPE = PropertyType.BOOLEAN;
    public static final Boolean PROP_BOOLEAN_VALUE = Boolean.TRUE;
    public static final PropertyDef PROP_BOOLEAN_DEF = new PropertyDef(
            PROP_BOOLEAN_NAME,
            PROP_BOOLEAN_DESCRIPTION,
            PROP_BOOLEAN_TYPE,
            Boolean.FALSE,
            PROP_BOOLEAN_EXAMPLE_VALUES
    );
    public static final BooleanProperty PROP_BOOLEAN = new BooleanProperty(PROP_BOOLEAN_NAME, PROP_BOOLEAN_VALUE);

    public static final String PROP_BOOLEAN_LIST_NAME = "boolean_list_property";
    public static final String PROP_BOOLEAN_LIST_DESCRIPTION = "Boolean list property";
    public static final String PROP_BOOLEAN_LIST_EXAMPLE_VALUES = "The list of booleans property";
    public static final PropertyType PROP_BOOLEAN_LIST_TYPE = PropertyType.BOOLEAN;
    public static final List<Boolean> PROP_BOOLEAN_LIST_VALUE = Arrays.asList(PROP_BOOLEAN_VALUE, PROP_BOOLEAN_VALUE);
    public static final PropertyDef PROP_BOOLEAN_LIST_DEF = new PropertyDef(
            PROP_BOOLEAN_LIST_NAME,
            PROP_BOOLEAN_LIST_DESCRIPTION,
            PROP_BOOLEAN_LIST_TYPE,
            Boolean.TRUE,
            PROP_BOOLEAN_LIST_EXAMPLE_VALUES
    );
    public static final BooleanProperty PROP_BOOLEAN_LIST = new BooleanProperty(PROP_BOOLEAN_LIST_NAME, PROP_BOOLEAN_VALUE);

    public static final String PROP_DATE_NAME = "date_property";
    public static final String PROP_DATE_DESCRIPTION = "Date property";
    public static final String PROP_DATE_EXAMPLE_VALUES = "The date property";
    public static final PropertyType PROP_DATE_TYPE = PropertyType.DATE;
    public static final Date PROP_DATE_VALUE = new Date();
    public static final PropertyDef PROP_DATE_DEF = new PropertyDef(
            PROP_DATE_NAME,
            PROP_DATE_DESCRIPTION,
            PROP_DATE_TYPE,
            Boolean.FALSE,
            PROP_DATE_EXAMPLE_VALUES
    );
    public static final DateProperty PROP_DATE = new DateProperty(PROP_DATE_NAME, PROP_DATE_VALUE);

    public static final String PROP_DATE_LIST_NAME = "date_list_property";
    public static final String PROP_DATE_LIST_DESCRIPTION = "Date list property";
    public static final String PROP_DATE_LIST_EXAMPLE_VALUES = "The list of dates property";
    public static final PropertyType PROP_DATE_LIST_TYPE = PropertyType.DATE;
    public static final List<Date> PROP_DATE_LIST_VALUE = Arrays.asList(PROP_DATE_VALUE, PROP_DATE_VALUE);
    public static final PropertyDef PROP_DATE_LIST_DEF = new PropertyDef(
            PROP_DATE_LIST_NAME,
            PROP_DATE_LIST_DESCRIPTION,
            PROP_DATE_LIST_TYPE,
            Boolean.TRUE,
            PROP_DATE_LIST_EXAMPLE_VALUES
    );
    public static final DateProperty PROP_DATE_LIST = new DateProperty(PROP_DATE_LIST_NAME, PROP_DATE_VALUE);

    public static final String PROP_KEY_VALUE_NAME = "key_value_property";
    public static final String PROP_KEY_VALUE_DESCRIPTION = "KeyValue property";
    public static final String PROP_KEY_VALUE_EXAMPLE_VALUES = "The key value property";
    public static final PropertyType PROP_KEY_VALUE_TYPE = PropertyType.KEY_VALUE;
    public static final KeyValue PROP_KEY_VALUE_VALUE = new KeyValue(PROP_STRING_NAME, PROP_STRING_VALUE);
    public static final PropertyDef PROP_KEY_VALUE_DEF = new PropertyDef(
            PROP_KEY_VALUE_NAME,
            PROP_KEY_VALUE_DESCRIPTION,
            PROP_KEY_VALUE_TYPE,
            Boolean.FALSE,
            PROP_KEY_VALUE_EXAMPLE_VALUES
    );
    public static final KeyValueProperty PROP_KEY_VALUE = new KeyValueProperty(PROP_KEY_VALUE_NAME, PROP_KEY_VALUE_VALUE);

    public static final String PROP_KEY_VALUE_LIST_NAME = "key_value_list_property";
    public static final String PROP_KEY_VALUE_LIST_DESCRIPTION = "KeyValue list property";
    public static final String PROP_KEY_VALUE_LIST_EXAMPLE_VALUES = "The list of key values property";
    public static final PropertyType PROP_KEY_VALUE_LIST_TYPE = PropertyType.KEY_VALUE;
    public static final List<KeyValue> PROP_KEY_VALUE_LIST_VALUE = Arrays.asList(PROP_KEY_VALUE_VALUE, PROP_KEY_VALUE_VALUE);
    public static final PropertyDef PROP_KEY_VALUE_LIST_DEF = new PropertyDef(
            PROP_KEY_VALUE_LIST_NAME,
            PROP_KEY_VALUE_LIST_DESCRIPTION,
            PROP_KEY_VALUE_LIST_TYPE,
            Boolean.TRUE,
            PROP_KEY_VALUE_LIST_EXAMPLE_VALUES
    );
    public static final KeyValueProperty PROP_KEY_VALUE_LIST = new KeyValueProperty(PROP_KEY_VALUE_LIST_NAME, PROP_KEY_VALUE_VALUE);

    public static final String PROP_NESTED_NAME = "nested_property";
    public static final String PROP_NESTED_DESCRIPTION = "Nested property";
    public static final String PROP_NESTED_EXAMPLE_VALUES = "The nested property";
    public static final PropertyType PROP_NESTED_TYPE = PropertyType.NESTED;
    public static final NestedPropertyValue PROP_NESTED_VALUE = new NestedPropertyValue(
            new Properties(
                    PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
                    PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
                    PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
                    PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
                    PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST
            ),
            new Associations(
                    ASSOCIATION
            )
    );
    public static final PropertyDef PROP_NESTED_DEF = new PropertyDef(
            PROP_NESTED_NAME,
            PROP_NESTED_DESCRIPTION,
            PROP_NESTED_TYPE,
            Boolean.FALSE,
            PROP_NESTED_EXAMPLE_VALUES,
            Arrays.asList(
                    PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
                    PROP_STRING_DEF, PROP_STRING_LIST_DEF,
                    PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
                    PROP_DATE_DEF, PROP_DATE_LIST_DEF,
                    PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
            ),
            Collections.singletonList(ASSOCIATION_DEF)
    );
    public static final NestedProperty PROP_NESTED = new NestedProperty(PROP_NESTED_NAME, PROP_NESTED_VALUE);

    public static final String PROP_NESTED_LIST_NAME = "nested_list_property";
    public static final String PROP_NESTED_LIST_DESCRIPTION = "Nested list property";
    public static final String PROP_NESTED_LIST_EXAMPLE_VALUES = "The list of nested property";
    public static final PropertyType PROP_NESTED_LIST_TYPE = PropertyType.NESTED;
    public static final List<NestedPropertyValue> PROP_NESTED_LIST_VALUE = Arrays.asList(PROP_NESTED_VALUE, PROP_NESTED_VALUE);
    public static final PropertyDef PROP_NESTED_LIST_DEF = new PropertyDef(
            PROP_NESTED_LIST_NAME,
            PROP_NESTED_LIST_DESCRIPTION,
            PROP_NESTED_LIST_TYPE,
            Boolean.TRUE,
            PROP_NESTED_LIST_EXAMPLE_VALUES,
            Arrays.asList(
                    PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
                    PROP_STRING_DEF, PROP_STRING_LIST_DEF,
                    PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
                    PROP_DATE_DEF, PROP_DATE_LIST_DEF,
                    PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
            ),
            Collections.singletonList(ASSOCIATION_DEF)
    );
    public static final NestedProperty PROP_NESTED_LIST = new NestedProperty(PROP_NESTED_LIST_NAME, PROP_NESTED_VALUE);

    public static final Properties PROPERTIES = new Properties(
            PROP_ID,
            PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
            PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
            PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
            PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
            PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST,
            PROP_NESTED, PROP_NESTED_LIST
    );
    public static final Properties TARGET_PROPERTIES = new Properties(
            PROP_ID,
            PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
            PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
            PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
            PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
            PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST
    );
    public static final List<PropertyDef> PROPERTY_DEFS = Arrays.asList(
            PROP_ID_DEF,
            PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
            PROP_STRING_DEF, PROP_STRING_LIST_DEF,
            PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
            PROP_DATE_DEF, PROP_DATE_LIST_DEF,
            PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF,
            PROP_NESTED_DEF, PROP_NESTED_LIST_DEF
    );
    public static final List<PropertyDef> TARGET_PROPERTY_DEFS = Arrays.asList(
            PROP_ID_DEF,
            PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
            PROP_STRING_DEF, PROP_STRING_LIST_DEF,
            PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
            PROP_DATE_DEF, PROP_DATE_LIST_DEF,
            PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
    );

    // test model objects
    public static final ResourceDef RESOURCE_DEF = new ResourceDef(
            RESOURCE_DEF_REF,
            RESOURCE_DESCRIPTION,
            PROPERTY_DEFS,
            ASSOCIATION_DEFS
    );
    public static final ResourceDef TARGET_RESOURCE_DEF = new ResourceDef(
            TARGET_RESOURCE_DEF_REF,
            TARGET_RESOURCE_DESCRIPTION,
            TARGET_PROPERTY_DEFS,
            TARGET_ASSOCIATION_DEFS
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
            TARGET_PROPERTIES,
            TARGET_ASSOCIATIONS
    );

    public static final Provider TEST_PROVIDER = new TestProvider();
    public static final TestResource TEST_RESOURCE = new TestResource(
            RESOURCE_ID,
            PROP_NUMBER_VALUE,
            PROP_NUMBER_LIST_VALUE,
            PROP_STRING_VALUE,
            PROP_STRING_LIST_VALUE,
            PROP_BOOLEAN_VALUE,
            PROP_BOOLEAN_LIST_VALUE,
            PROP_DATE_VALUE,
            PROP_DATE_LIST_VALUE,
            PROP_KEY_VALUE_VALUE,
            PROP_KEY_VALUE_LIST_VALUE,
            new TestNestedProperty(
                    PROP_NUMBER_VALUE,
                    PROP_NUMBER_LIST_VALUE,
                    PROP_STRING_VALUE,
                    PROP_STRING_LIST_VALUE,
                    PROP_BOOLEAN_VALUE,
                    PROP_BOOLEAN_LIST_VALUE,
                    PROP_DATE_VALUE,
                    PROP_DATE_LIST_VALUE,
                    PROP_KEY_VALUE_VALUE,
                    PROP_KEY_VALUE_LIST_VALUE,
                    TARGET_RESOURCE_ID
            ),
            Collections.singletonList(
                    new TestNestedProperty(
                            PROP_NUMBER_VALUE,
                            PROP_NUMBER_LIST_VALUE,
                            PROP_STRING_VALUE,
                            PROP_STRING_LIST_VALUE,
                            PROP_BOOLEAN_VALUE,
                            PROP_BOOLEAN_LIST_VALUE,
                            PROP_DATE_VALUE,
                            PROP_DATE_LIST_VALUE,
                            PROP_KEY_VALUE_VALUE,
                            PROP_KEY_VALUE_LIST_VALUE,
                            TARGET_RESOURCE_ID
                    )
            ),
            TARGET_RESOURCE_ID
    );

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
