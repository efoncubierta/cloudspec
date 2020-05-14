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
            description = ModelTestUtils.PROP_ID_DESCRIPTION
    )
    private final String id;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_INTEGER_NAME,
            description = ModelTestUtils.PROP_INTEGER_DESCRIPTION
    )
    private final Integer integerProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_STRING_NAME,
            description = ModelTestUtils.PROP_STRING_DESCRIPTION
    )
    private final String stringProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_BOOLEAN_NAME,
            description = ModelTestUtils.PROP_BOOLEAN_DESCRIPTION
    )
    private final Boolean booleanProperty;

    @PropertyDefinition(
            name = ModelTestUtils.PROP_KEY_VALUE_NAME,
            description = ModelTestUtils.PROP_KEY_VALUE_DESCRIPTION
    )
    private final KeyValue keyValueProperty;

    public TestTargetResource(String id, Integer integerProperty, String stringProperty,
                              Boolean booleanProperty, KeyValue keyValueProperty) {
        this.id = id;
        this.integerProperty = integerProperty;
        this.stringProperty = stringProperty;
        this.booleanProperty = booleanProperty;
        this.keyValueProperty = keyValueProperty;
    }

    public String getId() {
        return id;
    }

    public Integer getIntegerProperty() {
        return integerProperty;
    }

    public String getStringProperty() {
        return stringProperty;
    }

    public Boolean getBooleanProperty() {
        return booleanProperty;
    }

    public KeyValue getKeyValueProperty() {
        return keyValueProperty;
    }
}
