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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.KeyValue;

import java.util.List;

@ResourceDefinition(
        provider = ProviderDataUtil.PROVIDER_NAME,
        group = ModelTestUtils.RESOURCE_GROUP,
        name = ModelTestUtils.RESOURCE_NAME,
        description = ModelTestUtils.RESOURCE_DESCRIPTION
)
public class TestResource {
    @IdDefinition
    @PropertyDefinition(
            name = ModelTestUtils.PROP_ID_NAME,
            description = ModelTestUtils.PROP_ID_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_ID_EXAMPLE_VALUES
    )
    private final String id;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_INTEGER_NAME,
            description = ModelTestUtils.PROP_INTEGER_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_INTEGER_EXAMPLE_VALUES
    )
    private final Integer integerProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_INTEGER_LIST_NAME,
            description = ModelTestUtils.PROP_INTEGER_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_INTEGER_LIST_EXAMPLE_VALUES
    )
    private final List<Integer> integerListProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_DOUBLE_NAME,
            description = ModelTestUtils.PROP_DOUBLE_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_DOUBLE_EXAMPLE_VALUES
    )
    private final Double doubleProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_DOUBLE_LIST_NAME,
            description = ModelTestUtils.PROP_DOUBLE_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_DOUBLE_LIST_EXAMPLE_VALUES
    )
    private final List<Double> doubleListProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_STRING_NAME,
            description = ModelTestUtils.PROP_STRING_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_STRING_EXAMPLE_VALUES
    )
    private final String stringProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_STRING_LIST_NAME,
            description = ModelTestUtils.PROP_STRING_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_STRING_LIST_EXAMPLE_VALUES
    )
    private final List<String> stringListProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_BOOLEAN_NAME,
            description = ModelTestUtils.PROP_BOOLEAN_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_BOOLEAN_EXAMPLE_VALUES
    )
    private final Boolean booleanProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_BOOLEAN_LIST_NAME,
            description = ModelTestUtils.PROP_BOOLEAN_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_BOOLEAN_LIST_EXAMPLE_VALUES
    )
    private final List<Boolean> booleanListProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_KEY_VALUE_NAME,
            description = ModelTestUtils.PROP_KEY_VALUE_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_KEY_VALUE_EXAMPLE_VALUES
    )
    private final KeyValue keyValueProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_KEY_VALUE_LIST_NAME,
            description = ModelTestUtils.PROP_KEY_VALUE_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_KEY_VALUE_LIST_EXAMPLE_VALUES
    )
    private final List<KeyValue> keyValueListProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_NESTED_NAME,
            description = ModelTestUtils.PROP_NESTED_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_NESTED_EXAMPLE_VALUES
    )
    private final TestNestedProperty nestedProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_NESTED_LIST_NAME,
            description = ModelTestUtils.PROP_NESTED_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_NESTED_LIST_EXAMPLE_VALUES
    )
    private final List<TestNestedProperty> nestedListProperty;

    @AssociationDefinition(
            name = ModelTestUtils.ASSOC_NAME,
            description = ModelTestUtils.ASSOC_DESCRIPTION,
            targetClass = TestTargetResource.class
    )
    private final String associationId;

    public TestResource(String id,
                        Integer integerProperty, List<Integer> integerListProperty,
                        Double doubleProperty, List<Double> doubleListProperty,
                        String stringProperty, List<String> stringListProperty,
                        Boolean booleanProperty, List<Boolean> booleanListProperty,
                        KeyValue keyValueProperty, List<KeyValue> keyValueListProperty,
                        TestNestedProperty nestedProperty, List<TestNestedProperty> nestedListProperty,
                        String associationId) {
        this.id = id;
        this.integerProperty = integerProperty;
        this.integerListProperty = integerListProperty;
        this.doubleProperty = doubleProperty;
        this.doubleListProperty = doubleListProperty;
        this.stringProperty = stringProperty;
        this.stringListProperty = stringListProperty;
        this.booleanProperty = booleanProperty;
        this.booleanListProperty = booleanListProperty;
        this.keyValueProperty = keyValueProperty;
        this.keyValueListProperty = keyValueListProperty;
        this.nestedProperty = nestedProperty;
        this.nestedListProperty = nestedListProperty;
        this.associationId = associationId;
    }
}
