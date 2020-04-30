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
package cloudspec.model;

import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import org.junit.Test;

import java.util.Optional;

import static cloudspec.model.ProviderTest.PROVIDER_NAME;
import static org.junit.Assert.*;

public class ResourceTest {

    public static final String RESOURCE_GROUP = "mygroup";
    public static final String RESOURCE_NAME = "myresource";
    public static final ResourceFqn RESOURCE_FQN = new ResourceFqn(PROVIDER_NAME, RESOURCE_GROUP, RESOURCE_NAME);
    public static final String RESOURCE_DESCRIPTION = "My resource";

    public static final String PROP_INTEGER_NAME = "integer_property";
    public static final String PROP_INTEGER_DESCRIPTION = "Integer property";
    public static final PropertyType PROP_INTEGER_TYPE = PropertyType.INTEGER;
    public static final Integer PROP_INTEGER_VALUE = 1;

    public static final String PROP_STRING_NAME = "string_property";
    public static final String PROP_STRING_DESCRIPTION = "String property";
    public static final PropertyType PROP_STRING_TYPE = PropertyType.STRING;
    public static final String PROP_STRING_VALUE = "foo";

    public static final String PROP_BOOLEAN_NAME = "boolean_property";
    public static final String PROP_BOOLEAN_DESCRIPTION = "Boolean property";
    public static final PropertyType PROP_BOOLEAN_TYPE = PropertyType.BOOLEAN;
    public static final Boolean PROP_BOOLEAN_VALUE = Boolean.TRUE;

    @Test
    public void shouldBuildResourceFromAnnotation() {
        Resource resource = new MyResource(PROP_INTEGER_VALUE, PROP_STRING_VALUE, PROP_BOOLEAN_VALUE);

        assertNotNull(resource.getResourceFqn());
        assertEquals(RESOURCE_FQN, resource.getResourceFqn());

        assertNotNull(resource.getProperties());
        assertEquals(3, resource.getProperties().size());

        Optional<Property> integerProperty = resource.getProperty(PROP_INTEGER_NAME);
        assertNotNull(integerProperty);
        assertTrue(integerProperty.isPresent());
        assertEquals(PROP_INTEGER_VALUE, integerProperty.get().getValue());

        Optional<Property> stringProperty = resource.getProperty(PROP_STRING_NAME);
        assertNotNull(stringProperty);
        assertTrue(stringProperty.isPresent());
        assertEquals(PROP_STRING_VALUE, stringProperty.get().getValue());

        Optional<Property> booleanProperty = resource.getProperty(PROP_BOOLEAN_NAME);
        assertNotNull(booleanProperty);
        assertTrue(booleanProperty.isPresent());
        assertEquals(PROP_BOOLEAN_VALUE, booleanProperty.get().getValue());

        assertNotNull(resource.getFunctions());
        assertEquals(0, resource.getFunctions().size());
    }

    @ResourceDefinition(
            provider = PROVIDER_NAME,
            group = RESOURCE_GROUP,
            name = RESOURCE_NAME,
            description = RESOURCE_DESCRIPTION
    )
    public class MyResource extends Resource {
        @PropertyDefinition(
                name = PROP_INTEGER_NAME,
                description = PROP_INTEGER_DESCRIPTION
        )
        private final Integer integerProperty;

        @PropertyDefinition(
                name = PROP_STRING_NAME,
                description = PROP_STRING_DESCRIPTION
        )
        private final String stringProperty;

        @PropertyDefinition(
                name = PROP_BOOLEAN_NAME,
                description = PROP_BOOLEAN_DESCRIPTION
        )
        private final Boolean booleanProperty;

        public MyResource(Integer integerProperty, String stringProperty, Boolean booleanProperty) {
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
}
