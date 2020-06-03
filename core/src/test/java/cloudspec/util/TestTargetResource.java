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

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.KeyValue;

import java.util.Date;
import java.util.List;

@ResourceDefinition(
        provider = ProviderDataUtil.PROVIDER_NAME,
        group = ModelTestUtils.RESOURCE_GROUP,
        name = ModelTestUtils.TARGET_RESOURCE_NAME,
        description = ModelTestUtils.TARGET_RESOURCE_DESCRIPTION
)
public class TestTargetResource {
    @IdDefinition
    @PropertyDefinition(
            name = ModelTestUtils.PROP_ID_NAME,
            description = ModelTestUtils.PROP_ID_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_ID_EXAMPLE_VALUES
    )
    private final String id;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_NUMBER_NAME,
            description = ModelTestUtils.PROP_NUMBER_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_NUMBER_EXAMPLE_VALUES
    )
    private final Integer integerProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_NUMBER_LIST_NAME,
            description = ModelTestUtils.PROP_NUMBER_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_NUMBER_LIST_EXAMPLE_VALUES
    )
    private final List<Integer> integerListProperty;

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
            name = ModelTestUtils.PROP_DATE_NAME,
            description = ModelTestUtils.PROP_DATE_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_DATE_EXAMPLE_VALUES
    )
    private final Date dateProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_DATE_LIST_NAME,
            description = ModelTestUtils.PROP_DATE_LIST_DESCRIPTION,
            exampleValues = ModelTestUtils.PROP_DATE_LIST_EXAMPLE_VALUES
    )
    private final List<Date> dateListProperty;

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

    public TestTargetResource(String id,
                              Integer integerProperty, List<Integer> integerListProperty,
                              String stringProperty, List<String> stringListProperty,
                              Boolean booleanProperty, List<Boolean> booleanListProperty,
                              Date dateProperty, List<Date> dateListProperty,
                              KeyValue keyValueProperty, List<KeyValue> keyValueListProperty) {
        this.id = id;
        this.integerProperty = integerProperty;
        this.integerListProperty = integerListProperty;
        this.stringProperty = stringProperty;
        this.stringListProperty = stringListProperty;
        this.booleanProperty = booleanProperty;
        this.booleanListProperty = booleanListProperty;
        this.dateProperty = dateProperty;
        this.dateListProperty = dateListProperty;
        this.keyValueProperty = keyValueProperty;
        this.keyValueListProperty = keyValueListProperty;
    }
}
