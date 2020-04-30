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
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceTestUtils {
    public static final String TEST_RESOURCE_GROUP = "mygroup";
    public static final String TEST_RESOURCE_NAME = "myresource";
    public static final ResourceFqn TEST_RESOURCE_FQN = new ResourceFqn(ProviderTestUtils.TEST_PROVIDER_NAME, TEST_RESOURCE_GROUP, TEST_RESOURCE_NAME);
    public static final String TEST_INTEGER_PROPERTY_NAME = "integer_property";
    public static final String TEST_STRING_PROPERTY_NAME = "string_property";
    public static final String TEST_BOOLEAN_PROPERTY_NAME = "boolean_property";
    public static final String TEST_ARRAY_OF_INTEGER_PROPERTY_NAME = "array_of_integer_property";
    public static final String TEST_ARRAY_OF_STRING_PROPERTY_NAME = "array_of_string_property";
    public static final String TEST_ARRAY_OF_BOOLEAN_PROPERTY_NAME = "array_of_boolean_property";

    public static final ResourceDef TEST_RESOURCE_DEF = mock(ResourceDef.class);
    public static final PropertyDef TEST_INTEGER_PROPERTY_DEF = new PropertyDef(
            TEST_INTEGER_PROPERTY_NAME,
            TEST_INTEGER_PROPERTY_NAME,
            PropertyType.INTEGER,
            Boolean.FALSE
    );
    public static final PropertyDef TEST_STRING_PROPERTY_DEF = new PropertyDef(
            TEST_STRING_PROPERTY_NAME,
            TEST_STRING_PROPERTY_NAME,
            PropertyType.STRING,
            Boolean.FALSE
    );
    public static final PropertyDef TEST_BOOLEAN_PROPERTY_DEF = new PropertyDef(
            TEST_BOOLEAN_PROPERTY_NAME,
            TEST_BOOLEAN_PROPERTY_NAME,
            PropertyType.BOOLEAN,
            Boolean.FALSE
    );

// TODO uncomment when the support for arrays is implement
//
//    public static final PropertyDef TEST_ARRAY_OF_INTEGER_PROPERTY_DEF = new PropertyDef(
//            TEST_ARRAY_OF_INTEGER_PROPERTY_NAME,
//            TEST_ARRAY_OF_INTEGER_PROPERTY_NAME,
//            PropertyType.INTEGER,
//            Boolean.FALSE
//    );
//    public static final PropertyDef TEST_ARRAY_OF_STRING_PROPERTY_DEF = new PropertyDef(
//            TEST_ARRAY_OF_INTEGER_PROPERTY_NAME,
//            TEST_ARRAY_OF_INTEGER_PROPERTY_NAME,
//            PropertyType.INTEGER,
//            Boolean.TRUE
//    );
//    public static final PropertyDef TEST_ARRAY_OF_BOOLEAN_PROPERTY_DEF = new PropertyDef(
//            TEST_ARRAY_OF_STRING_PROPERTY_NAME,
//            TEST_ARRAY_OF_STRING_PROPERTY_NAME,
//            PropertyType.STRING,
//            Boolean.TRUE
//    );

    public static final List<PropertyDef> TEST_PROPERTY_DEFS = Arrays.asList(
            TEST_INTEGER_PROPERTY_DEF,
            TEST_STRING_PROPERTY_DEF,
            TEST_BOOLEAN_PROPERTY_DEF
    );
    public static final List<FunctionDef> TEST_FUNCTION_DEFS = Collections.emptyList();

    public static final Resource TEST_RESOURCE = mock(Resource.class);
    public static final IntegerProperty TEST_INTEGER_PROPERTY = new IntegerProperty(TEST_INTEGER_PROPERTY_NAME, 1);
    public static final StringProperty TEST_STRING_PROPERTY = new StringProperty(TEST_STRING_PROPERTY_NAME, "foo");
    public static final BooleanProperty TEST_BOOLEAN_PROPERTY = new BooleanProperty(TEST_BOOLEAN_PROPERTY_NAME, Boolean.TRUE);
    public static final List<Property> TEST_PROPERTIES = Arrays.asList(
            TEST_INTEGER_PROPERTY,
            TEST_STRING_PROPERTY,
            TEST_BOOLEAN_PROPERTY
    );
    public static final List<Function> TEST_FUNCTIONS = Collections.emptyList();
    public static final ResourceLoader TEST_RESOURCE_LOADER = mock(ResourceLoader.class);

    static {
        when(TEST_RESOURCE.getResourceFqn()).thenReturn(TEST_RESOURCE_FQN);
        when(TEST_RESOURCE.getProperty(TEST_INTEGER_PROPERTY_NAME)).thenReturn(Optional.of(TEST_INTEGER_PROPERTY));
        when(TEST_RESOURCE.getProperty(TEST_STRING_PROPERTY_NAME)).thenReturn(Optional.of(TEST_STRING_PROPERTY));
        when(TEST_RESOURCE.getProperty(TEST_BOOLEAN_PROPERTY_NAME)).thenReturn(Optional.of(TEST_BOOLEAN_PROPERTY));
        when(TEST_RESOURCE.getProperties()).thenReturn(TEST_PROPERTIES);
        when(TEST_RESOURCE.getFunctions()).thenReturn(TEST_FUNCTIONS);
    }

    static {
        when(TEST_RESOURCE_LOADER.load()).thenReturn(Collections.singletonList(TEST_RESOURCE));
    }

    static {
        when(TEST_RESOURCE_DEF.getFqn()).thenReturn(TEST_RESOURCE_FQN);
        when(TEST_RESOURCE_DEF.getPropertyDef(TEST_INTEGER_PROPERTY_NAME)).thenReturn(Optional.of(TEST_INTEGER_PROPERTY_DEF));
        when(TEST_RESOURCE_DEF.getPropertyDef(TEST_STRING_PROPERTY_NAME)).thenReturn(Optional.of(TEST_STRING_PROPERTY_DEF));
        when(TEST_RESOURCE_DEF.getPropertyDef(TEST_BOOLEAN_PROPERTY_NAME)).thenReturn(Optional.of(TEST_BOOLEAN_PROPERTY_DEF));
        when(TEST_RESOURCE_DEF.getPropertyDefs()).thenReturn(TEST_PROPERTY_DEFS);
        when(TEST_RESOURCE_DEF.getFunctionDefs()).thenReturn(TEST_FUNCTION_DEFS);
        when(TEST_RESOURCE_DEF.getResourceLoader()).thenReturn(TEST_RESOURCE_LOADER);
    }
}
