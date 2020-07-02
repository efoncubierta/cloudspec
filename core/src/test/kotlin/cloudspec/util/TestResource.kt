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
                description = ModelTestUtils.PROP_ID_DESCRIPTION
        )
        val id: String,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NUMBER_NAME,
                description = ModelTestUtils.PROP_NUMBER_DESCRIPTION
        )
        val numberProperty: Number?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NUMBER_LIST_NAME,
                description = ModelTestUtils.PROP_NUMBER_LIST_DESCRIPTION
        )
        val numberListProperty: List<Number>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_STRING_NAME,
                description = ModelTestUtils.PROP_STRING_DESCRIPTION
        )
        val stringProperty: String?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_STRING_LIST_NAME,
                description = ModelTestUtils.PROP_STRING_LIST_DESCRIPTION
        )
        val stringListProperty: List<String>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_ENUM_NAME,
                description = ModelTestUtils.PROP_ENUM_DESCRIPTION
        )
        val enumProperty: TestEnum?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_ENUM_LIST_NAME,
                description = ModelTestUtils.PROP_ENUM_LIST_DESCRIPTION
        )
        val enumListProperty: List<TestEnum>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_BOOLEAN_NAME,
                description = ModelTestUtils.PROP_BOOLEAN_DESCRIPTION
        )
        val booleanProperty: Boolean?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_BOOLEAN_LIST_NAME,
                description = ModelTestUtils.PROP_BOOLEAN_LIST_DESCRIPTION
        )
        val booleanListProperty: List<Boolean>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_DATE_NAME,
                description = ModelTestUtils.PROP_DATE_DESCRIPTION
        )
        val dateProperty: Date?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_DATE_LIST_NAME,
                description = ModelTestUtils.PROP_DATE_LIST_DESCRIPTION
        )
        val dateListProperty: List<Date>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_KEY_VALUE_NAME,
                description = ModelTestUtils.PROP_KEY_VALUE_DESCRIPTION
        )
        val keyValueProperty: KeyValue?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_KEY_VALUE_LIST_NAME,
                description = ModelTestUtils.PROP_KEY_VALUE_LIST_DESCRIPTION
        )
        val keyValueListProperty: List<KeyValue>?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NESTED_NAME,
                description = ModelTestUtils.PROP_NESTED_DESCRIPTION
        )
        val nestedProperty: TestNestedProperty?,

        @property:PropertyDefinition(
                name = ModelTestUtils.PROP_NESTED_LIST_NAME,
                description = ModelTestUtils.PROP_NESTED_LIST_DESCRIPTION
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
