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

import cloudspec.annotation.PropertyDefinition;

public class TestMapResource {
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

    public TestMapResource(Integer integerProperty, String stringProperty, Boolean booleanProperty) {
        this.integerProperty = integerProperty;
        this.stringProperty = stringProperty;
        this.booleanProperty = booleanProperty;
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
}
