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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.model.KeyValue
import java.util.*

@ResourceDefinition(
        provider = ProviderDataUtil.PROVIDER_NAME,
        group = ModelTestUtils.RESOURCE_GROUP,
        name = ModelTestUtils.RESOURCE_NAME,
        description = ModelTestUtils.RESOURCE_DESCRIPTION
)
data class TestResource(
        @property:IdDefinition
        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_ID_NAME,
                description = ModelTestUtils.PROP_ID_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_ID_EXAMPLE_VALUES
        )
        val id: String,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NUMBER_NAME,
                description = ModelTestUtils.PROP_NUMBER_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_NUMBER_EXAMPLE_VALUES
        )
        val numberProperty: Number?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NUMBER_LIST_NAME,
                description = ModelTestUtils.PROP_NUMBER_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_NUMBER_LIST_EXAMPLE_VALUES
        )
        val numberListProperty: List<Number>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_STRING_NAME,
                description = ModelTestUtils.PROP_STRING_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_STRING_EXAMPLE_VALUES
        )
        val stringProperty: String?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_STRING_LIST_NAME,
                description = ModelTestUtils.PROP_STRING_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_STRING_LIST_EXAMPLE_VALUES
        )
        val stringListProperty: List<String>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_ENUM_NAME,
                description = ModelTestUtils.PROP_ENUM_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_ENUM_EXAMPLE_VALUES
        )
        val enumProperty: TestEnum?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_ENUM_LIST_NAME,
                description = ModelTestUtils.PROP_ENUM_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_ENUM_LIST_EXAMPLE_VALUES
        )
        val enumListProperty: List<TestEnum>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_BOOLEAN_NAME,
                description = ModelTestUtils.PROP_BOOLEAN_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_BOOLEAN_EXAMPLE_VALUES
        )
        val booleanProperty: Boolean?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_BOOLEAN_LIST_NAME,
                description = ModelTestUtils.PROP_BOOLEAN_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_BOOLEAN_LIST_EXAMPLE_VALUES
        )
        val booleanListProperty: List<Boolean>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_DATE_NAME,
                description = ModelTestUtils.PROP_DATE_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_DATE_EXAMPLE_VALUES
        )
        val dateProperty: Date?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_DATE_LIST_NAME,
                description = ModelTestUtils.PROP_DATE_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_DATE_LIST_EXAMPLE_VALUES
        )
        val dateListProperty: List<Date>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_KEY_VALUE_NAME,
                description = ModelTestUtils.PROP_KEY_VALUE_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_KEY_VALUE_EXAMPLE_VALUES
        )
        val keyValueProperty: KeyValue?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_KEY_VALUE_LIST_NAME,
                description = ModelTestUtils.PROP_KEY_VALUE_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_KEY_VALUE_LIST_EXAMPLE_VALUES
        )
        val keyValueListProperty: List<KeyValue>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NESTED_NAME,
                description = ModelTestUtils.PROP_NESTED_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_NESTED_EXAMPLE_VALUES
        )
        val nestedProperty: TestNestedProperty?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NESTED_LIST_NAME,
                description = ModelTestUtils.PROP_NESTED_LIST_DESCRIPTION,
                exampleValues = ModelTestUtils.PROP_NESTED_LIST_EXAMPLE_VALUES
        )
        val nestedListProperty: List<TestNestedProperty>?,

        @property:AssociationDefinition(
                name = ModelTestUtils.ASSOC_NAME,
                description = ModelTestUtils.ASSOC_DESCRIPTION,
                targetClass = TestTargetResource::class
        )
        val associationId: String?,

        @property:AssociationDefinition(
                name = ModelTestUtils.ASSOC_NAME,
                description = ModelTestUtils.ASSOC_DESCRIPTION,
                targetClass = TestTargetResource::class
        )
        val associationId2: Int? = null
)
