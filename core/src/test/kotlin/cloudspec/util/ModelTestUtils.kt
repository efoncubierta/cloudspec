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
package cloudspec.util

import cloudspec.model.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object ModelTestUtils {
    const val PROVIDER_NAME = "myprovider"
    const val PROVIDER_DESCRIPTION = "My provider"
    const val RESOURCE_GROUP = "mygroup"
    const val RESOURCE_NAME = "myresource"
    const val RESOURCE_DESCRIPTION = "My resource"
    val RESOURCE_DEF_REF = ResourceDefRef(ProviderDataUtil.PROVIDER_NAME, RESOURCE_GROUP, RESOURCE_NAME)
    const val TARGET_RESOURCE_NAME = "mytargetresource"
    const val TARGET_RESOURCE_DESCRIPTION = "My target resource"
    val TARGET_RESOURCE_DEF_REF = ResourceDefRef(ProviderDataUtil.PROVIDER_NAME, RESOURCE_GROUP, TARGET_RESOURCE_NAME)
    val RESOURCE_REF = ResourceRef(RESOURCE_DEF_REF, UUID.randomUUID().toString())
    val TARGET_RESOURCE_REF = ResourceRef(TARGET_RESOURCE_DEF_REF, UUID.randomUUID().toString())
    const val ASSOC_NAME = "myassociation"
    const val ASSOC_DESCRIPTION = "My association"
    val ASSOCIATION_DEF = AssociationDef(ASSOC_NAME, ASSOC_DESCRIPTION,
                                         TARGET_RESOURCE_DEF_REF, false)
    val ASSOCIATION = Association(ASSOC_NAME, TARGET_RESOURCE_REF)
    val ASSOCIATIONS: Associations = setOf(ASSOCIATION)
    val ASSOCIATION_DEFS = setOf(ASSOCIATION_DEF)
    val TARGET_ASSOCIATIONS: Associations = emptySet()
    val TARGET_ASSOCIATION_DEFS: Set<AssociationDef> = emptySet()
    const val PROP_ID_NAME = "id_property"
    const val PROP_ID_DESCRIPTION = "ID property"
    const val PROP_ID_EXAMPLE_VALUES = "The ID property"
    val PROP_ID_TYPE = PropertyType.STRING
    val PROP_ID_DEF = PropertyDef(
            PROP_ID_NAME,
            PROP_ID_DESCRIPTION,
            PROP_ID_TYPE,
            false,
            PROP_ID_EXAMPLE_VALUES
    )
    val PROP_ID = StringProperty(PROP_ID_NAME, RESOURCE_REF.id)
    const val PROP_NUMBER_NAME = "number_property"
    const val PROP_NUMBER_DESCRIPTION = "Number property"
    const val PROP_NUMBER_EXAMPLE_VALUES = "The number property"
    val PROP_NUMBER_TYPE = PropertyType.NUMBER
    const val PROP_NUMBER_VALUE = 1
    val PROP_NUMBER_DEF = PropertyDef(
            PROP_NUMBER_NAME,
            PROP_NUMBER_DESCRIPTION,
            PROP_NUMBER_TYPE,
            false,
            PROP_NUMBER_EXAMPLE_VALUES
    )
    val PROP_NUMBER = NumberProperty(PROP_NUMBER_NAME, PROP_NUMBER_VALUE)
    const val PROP_NUMBER_LIST_NAME = "number_list_property"
    const val PROP_NUMBER_LIST_DESCRIPTION = "Number list property"
    const val PROP_NUMBER_LIST_EXAMPLE_VALUES = "The list of integer property"
    val PROP_NUMBER_LIST_TYPE = PropertyType.NUMBER
    val PROP_NUMBER_LIST_VALUE = listOf(PROP_NUMBER_VALUE, PROP_NUMBER_VALUE)
    val PROP_NUMBER_LIST_DEF = PropertyDef(
            PROP_NUMBER_LIST_NAME,
            PROP_NUMBER_LIST_DESCRIPTION,
            PROP_NUMBER_LIST_TYPE,
            true,
            PROP_NUMBER_LIST_EXAMPLE_VALUES
    )
    val PROP_NUMBER_LIST = NumberProperty(PROP_NUMBER_LIST_NAME, PROP_NUMBER_VALUE)
    const val PROP_STRING_NAME = "string_property"
    const val PROP_STRING_DESCRIPTION = "String property"
    const val PROP_STRING_EXAMPLE_VALUES = "The string property"
    val PROP_STRING_TYPE = PropertyType.STRING
    const val PROP_STRING_VALUE = "foo"

    val PROP_STRING_DEF = PropertyDef(
            PROP_STRING_NAME,
            PROP_STRING_DESCRIPTION,
            PROP_STRING_TYPE,
            false,
            PROP_STRING_EXAMPLE_VALUES
    )
    val PROP_STRING = StringProperty(PROP_STRING_NAME, PROP_STRING_VALUE)
    const val PROP_STRING_LIST_NAME = "string_list_property"
    const val PROP_STRING_LIST_DESCRIPTION = "String list property"
    const val PROP_STRING_LIST_EXAMPLE_VALUES = "The list of strings property"
    val PROP_STRING_LIST_TYPE = PropertyType.STRING
    val PROP_STRING_LIST_VALUE = listOf(PROP_STRING_VALUE, PROP_STRING_VALUE)
    val PROP_STRING_LIST_DEF = PropertyDef(
            PROP_STRING_LIST_NAME,
            PROP_STRING_LIST_DESCRIPTION,
            PROP_STRING_LIST_TYPE,
            true,
            PROP_STRING_LIST_EXAMPLE_VALUES
    )
    val PROP_STRING_LIST = StringProperty(PROP_STRING_LIST_NAME, PROP_STRING_VALUE)

    const val PROP_ENUM_NAME = "enum_property"
    const val PROP_ENUM_DESCRIPTION = "Enum property"
    const val PROP_ENUM_EXAMPLE_VALUES = "The enum property"
    val PROP_ENUM_TYPE = PropertyType.STRING
    val PROP_ENUM_VALUE = TestEnum.TEST_1
    val PROP_ENUM_DEF = PropertyDef(
            PROP_ENUM_NAME,
            PROP_ENUM_DESCRIPTION,
            PROP_ENUM_TYPE,
            false,
            PROP_ENUM_EXAMPLE_VALUES
    )
    val PROP_ENUM = StringProperty(PROP_ENUM_NAME, PROP_ENUM_VALUE.toString())
    const val PROP_ENUM_LIST_NAME = "enum_list_property"
    const val PROP_ENUM_LIST_DESCRIPTION = "Enum list property"
    const val PROP_ENUM_LIST_EXAMPLE_VALUES = "The list of enums property"
    val PROP_ENUM_LIST_TYPE = PropertyType.STRING
    val PROP_ENUM_LIST_VALUE = listOf(PROP_ENUM_VALUE, PROP_ENUM_VALUE)
    val PROP_ENUM_LIST_DEF = PropertyDef(
            PROP_ENUM_LIST_NAME,
            PROP_ENUM_LIST_DESCRIPTION,
            PROP_ENUM_LIST_TYPE,
            true,
            PROP_ENUM_LIST_EXAMPLE_VALUES
    )
    val PROP_ENUM_LIST = StringProperty(PROP_ENUM_LIST_NAME, PROP_ENUM_VALUE.toString())

    const val PROP_BOOLEAN_NAME = "boolean_property"
    const val PROP_BOOLEAN_DESCRIPTION = "Boolean property"
    const val PROP_BOOLEAN_EXAMPLE_VALUES = "The boolean property"
    val PROP_BOOLEAN_TYPE = PropertyType.BOOLEAN
    val PROP_BOOLEAN_VALUE = true
    val PROP_BOOLEAN_DEF = PropertyDef(
            PROP_BOOLEAN_NAME,
            PROP_BOOLEAN_DESCRIPTION,
            PROP_BOOLEAN_TYPE,
            false,
            PROP_BOOLEAN_EXAMPLE_VALUES
    )
    val PROP_BOOLEAN = BooleanProperty(PROP_BOOLEAN_NAME, PROP_BOOLEAN_VALUE)
    const val PROP_BOOLEAN_LIST_NAME = "boolean_list_property"
    const val PROP_BOOLEAN_LIST_DESCRIPTION = "Boolean list property"
    const val PROP_BOOLEAN_LIST_EXAMPLE_VALUES = "The list of booleans property"
    val PROP_BOOLEAN_LIST_TYPE = PropertyType.BOOLEAN
    val PROP_BOOLEAN_LIST_VALUE = listOf(PROP_BOOLEAN_VALUE, PROP_BOOLEAN_VALUE)
    val PROP_BOOLEAN_LIST_DEF = PropertyDef(
            PROP_BOOLEAN_LIST_NAME,
            PROP_BOOLEAN_LIST_DESCRIPTION,
            PROP_BOOLEAN_LIST_TYPE,
            true,
            PROP_BOOLEAN_LIST_EXAMPLE_VALUES
    )
    val PROP_BOOLEAN_LIST = BooleanProperty(PROP_BOOLEAN_LIST_NAME, PROP_BOOLEAN_VALUE)
    const val PROP_DATE_NAME = "date_property"
    const val PROP_DATE_DESCRIPTION = "Date property"
    const val PROP_DATE_EXAMPLE_VALUES = "The date property"
    val PROP_DATE_TYPE = PropertyType.DATE
    val PROP_DATE_VALUE = Date()
    val PROP_DATE_DEF = PropertyDef(
            PROP_DATE_NAME,
            PROP_DATE_DESCRIPTION,
            PROP_DATE_TYPE,
            false,
            PROP_DATE_EXAMPLE_VALUES
    )
    val PROP_DATE = InstantProperty(PROP_DATE_NAME, PROP_DATE_VALUE.toInstant())
    const val PROP_DATE_LIST_NAME = "date_list_property"
    const val PROP_DATE_LIST_DESCRIPTION = "Date list property"
    const val PROP_DATE_LIST_EXAMPLE_VALUES = "The list of dates property"
    val PROP_DATE_LIST_TYPE = PropertyType.DATE
    val PROP_DATE_LIST_VALUE = listOf(PROP_DATE_VALUE, PROP_DATE_VALUE)
    val PROP_DATE_LIST_DEF = PropertyDef(
            PROP_DATE_LIST_NAME,
            PROP_DATE_LIST_DESCRIPTION,
            PROP_DATE_LIST_TYPE,
            true,
            PROP_DATE_LIST_EXAMPLE_VALUES
    )
    val PROP_DATE_LIST = InstantProperty(PROP_DATE_LIST_NAME, PROP_DATE_VALUE.toInstant())
    const val PROP_KEY_VALUE_NAME = "key_value_property"
    const val PROP_KEY_VALUE_DESCRIPTION = "KeyValue property"
    const val PROP_KEY_VALUE_EXAMPLE_VALUES = "The key value property"
    val PROP_KEY_VALUE_TYPE = PropertyType.KEY_VALUE

    val PROP_KEY_VALUE_VALUE = KeyValue(PROP_STRING_NAME, PROP_STRING_VALUE)
    val PROP_KEY_VALUE_DEF = PropertyDef(
            PROP_KEY_VALUE_NAME,
            PROP_KEY_VALUE_DESCRIPTION,
            PROP_KEY_VALUE_TYPE,
            false,
            PROP_KEY_VALUE_EXAMPLE_VALUES
    )
    val PROP_KEY_VALUE = KeyValueProperty(PROP_KEY_VALUE_NAME, PROP_KEY_VALUE_VALUE)
    const val PROP_KEY_VALUE_LIST_NAME = "key_value_list_property"
    const val PROP_KEY_VALUE_LIST_DESCRIPTION = "KeyValue list property"
    const val PROP_KEY_VALUE_LIST_EXAMPLE_VALUES = "The list of key values property"
    val PROP_KEY_VALUE_LIST_TYPE = PropertyType.KEY_VALUE
    val PROP_KEY_VALUE_LIST_VALUE = listOf(PROP_KEY_VALUE_VALUE, PROP_KEY_VALUE_VALUE)
    val PROP_KEY_VALUE_LIST_DEF = PropertyDef(
            PROP_KEY_VALUE_LIST_NAME,
            PROP_KEY_VALUE_LIST_DESCRIPTION,
            PROP_KEY_VALUE_LIST_TYPE,
            true,
            PROP_KEY_VALUE_LIST_EXAMPLE_VALUES
    )
    val PROP_KEY_VALUE_LIST = KeyValueProperty(PROP_KEY_VALUE_LIST_NAME, PROP_KEY_VALUE_VALUE)
    const val PROP_NESTED_NAME = "nested_property"
    const val PROP_NESTED_DESCRIPTION = "Nested property"
    const val PROP_NESTED_EXAMPLE_VALUES = "The nested property"
    val PROP_NESTED_TYPE = PropertyType.NESTED
    val PROP_NESTED_VALUE = NestedPropertyValue(
            setOf(
                    PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
                    PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
                    PROP_ENUM, PROP_ENUM_LIST, PROP_ENUM_LIST,
                    PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
                    PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
                    PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST
            ),
            setOf(
                    ASSOCIATION
            )
    )
    val PROP_NESTED_DEF = PropertyDef(
            PROP_NESTED_NAME,
            PROP_NESTED_DESCRIPTION,
            PROP_NESTED_TYPE,
            false,
            PROP_NESTED_EXAMPLE_VALUES,
            setOf(
                    PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
                    PROP_STRING_DEF, PROP_STRING_LIST_DEF,
                    PROP_ENUM_DEF, PROP_ENUM_LIST_DEF,
                    PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
                    PROP_DATE_DEF, PROP_DATE_LIST_DEF,
                    PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
            ),
            setOf(
                    ASSOCIATION_DEF
            )
    )
    val PROP_NESTED = NestedProperty(PROP_NESTED_NAME, PROP_NESTED_VALUE)
    const val PROP_NESTED_LIST_NAME = "nested_list_property"
    const val PROP_NESTED_LIST_DESCRIPTION = "Nested list property"
    const val PROP_NESTED_LIST_EXAMPLE_VALUES = "The list of nested property"
    val PROP_NESTED_LIST_TYPE = PropertyType.NESTED
    val PROP_NESTED_LIST_VALUE = listOf(PROP_NESTED_VALUE, PROP_NESTED_VALUE)
    val PROP_NESTED_LIST_DEF = PropertyDef(
            PROP_NESTED_LIST_NAME,
            PROP_NESTED_LIST_DESCRIPTION,
            PROP_NESTED_LIST_TYPE,
            true,
            PROP_NESTED_LIST_EXAMPLE_VALUES,
            setOf(
                    PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
                    PROP_STRING_DEF, PROP_STRING_LIST_DEF,
                    PROP_ENUM_DEF, PROP_ENUM_LIST_DEF,
                    PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
                    PROP_DATE_DEF, PROP_DATE_LIST_DEF,
                    PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
            ),
            setOf(
                    ASSOCIATION_DEF
            )
    )
    val PROP_NESTED_LIST = NestedProperty(PROP_NESTED_LIST_NAME, PROP_NESTED_VALUE)

    val PROPERTIES = setOf(
            PROP_ID,
            PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
            PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
            PROP_ENUM, PROP_ENUM_LIST, PROP_ENUM_LIST,
            PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
            PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
            PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST,
            PROP_NESTED, PROP_NESTED_LIST
    )

    val TARGET_PROPERTIES = setOf(
            PROP_ID,
            PROP_NUMBER, PROP_NUMBER_LIST, PROP_NUMBER_LIST,
            PROP_STRING, PROP_STRING_LIST, PROP_STRING_LIST,
            PROP_ENUM, PROP_ENUM_LIST, PROP_ENUM_LIST,
            PROP_BOOLEAN, PROP_BOOLEAN_LIST, PROP_BOOLEAN_LIST,
            PROP_DATE, PROP_DATE_LIST, PROP_DATE_LIST,
            PROP_KEY_VALUE, PROP_KEY_VALUE_LIST, PROP_KEY_VALUE_LIST
    )

    val PROPERTY_DEFS = setOf(
            PROP_ID_DEF,
            PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
            PROP_STRING_DEF, PROP_STRING_LIST_DEF,
            PROP_ENUM_DEF, PROP_ENUM_LIST_DEF,
            PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
            PROP_DATE_DEF, PROP_DATE_LIST_DEF,
            PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF,
            PROP_NESTED_DEF, PROP_NESTED_LIST_DEF
    )

    val TARGET_PROPERTY_DEFS = setOf(
            PROP_ID_DEF,
            PROP_NUMBER_DEF, PROP_NUMBER_LIST_DEF,
            PROP_STRING_DEF, PROP_STRING_LIST_DEF,
            PROP_ENUM_DEF, PROP_ENUM_LIST_DEF,
            PROP_BOOLEAN_DEF, PROP_BOOLEAN_LIST_DEF,
            PROP_DATE_DEF, PROP_DATE_LIST_DEF,
            PROP_KEY_VALUE_DEF, PROP_KEY_VALUE_LIST_DEF
    )

    // test model objects
    val RESOURCE_DEF = ResourceDef(
            RESOURCE_DEF_REF,
            RESOURCE_DESCRIPTION,
            PROPERTY_DEFS,
            ASSOCIATION_DEFS
    )

    val TARGET_RESOURCE_DEF = ResourceDef(
            TARGET_RESOURCE_DEF_REF,
            TARGET_RESOURCE_DESCRIPTION,
            TARGET_PROPERTY_DEFS,
            TARGET_ASSOCIATION_DEFS
    )

    val RESOURCE = Resource(
            RESOURCE_REF,
            PROPERTIES,
            ASSOCIATIONS
    )

    val TARGET_RESOURCE = Resource(
            TARGET_RESOURCE_REF,
            TARGET_PROPERTIES,
            TARGET_ASSOCIATIONS
    )

    val TEST_PROVIDER: Provider = TestProvider()

    val TEST_RESOURCE = TestResource(
            RESOURCE_REF.id,
            PROP_NUMBER_VALUE,
            PROP_NUMBER_LIST_VALUE,
            PROP_STRING_VALUE,
            PROP_STRING_LIST_VALUE,
            PROP_ENUM_VALUE,
            PROP_ENUM_LIST_VALUE,
            PROP_BOOLEAN_VALUE,
            PROP_BOOLEAN_LIST_VALUE,
            PROP_DATE_VALUE,
            PROP_DATE_LIST_VALUE,
            PROP_KEY_VALUE_VALUE,
            PROP_KEY_VALUE_LIST_VALUE,
            TestNestedProperty(
                    PROP_NUMBER_VALUE,
                    PROP_NUMBER_LIST_VALUE,
                    PROP_STRING_VALUE,
                    PROP_STRING_LIST_VALUE,
                    PROP_ENUM_VALUE,
                    PROP_ENUM_LIST_VALUE,
                    PROP_BOOLEAN_VALUE,
                    PROP_BOOLEAN_LIST_VALUE,
                    PROP_DATE_VALUE,
                    PROP_DATE_LIST_VALUE,
                    PROP_KEY_VALUE_VALUE,
                    PROP_KEY_VALUE_LIST_VALUE,
                    TARGET_RESOURCE_REF.id
            ),
            listOf(
                    TestNestedProperty(
                            PROP_NUMBER_VALUE,
                            PROP_NUMBER_LIST_VALUE,
                            PROP_STRING_VALUE,
                            PROP_STRING_LIST_VALUE,
                            PROP_ENUM_VALUE,
                            PROP_ENUM_LIST_VALUE,
                            PROP_BOOLEAN_VALUE,
                            PROP_BOOLEAN_LIST_VALUE,
                            PROP_DATE_VALUE,
                            PROP_DATE_LIST_VALUE,
                            PROP_KEY_VALUE_VALUE,
                            PROP_KEY_VALUE_LIST_VALUE,
                            TARGET_RESOURCE_REF.id
                    )
            ),
            TARGET_RESOURCE_REF.id
    )

    fun comparePropertyDefs(propertyDefs1: Set<PropertyDef>, propertyDefs2: Set<PropertyDef?>) {
        assertNotNull(propertyDefs1)
        assertNotNull(propertyDefs2)
        assertEquals(propertyDefs1.size, propertyDefs2.size)
        assertTrue(propertyDefs1.containsAll(propertyDefs2))
    }

    fun compareAssociationDefs(associationDefs1: Set<AssociationDef>, associationDefs2: Set<AssociationDef?>) {
        assertNotNull(associationDefs1)
        assertNotNull(associationDefs2)
        assertEquals(associationDefs1.size, associationDefs2.size)
        assertTrue(associationDefs1.containsAll(associationDefs2))
    }
}
