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
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class ModelTestUtils {
    public static final String PROVIDER_NAME = "myprovider";
    public static final String PROVIDER_DESCRIPTION = "My provider";

    public static final String RESOURCE_GROUP = "mygroup";
    public static final String RESOURCE_NAME = "myresource";
    public static final ResourceFqn RESOURCE_FQN = new ResourceFqn(ProviderDataUtil.PROVIDER_NAME, RESOURCE_GROUP, RESOURCE_NAME);
    public static final String RESOURCE_DESCRIPTION = "My resource";

    public static final String RESOURCE_ID = UUID.randomUUID().toString();

    public static final String PROP_INTEGER_NAME = "integer_property";
    public static final String PROP_INTEGER_DESCRIPTION = "Integer property";
    public static final PropertyType PROP_INTEGER_TYPE = PropertyType.INTEGER;
    public static final Integer PROP_INTEGER_VALUE = 1;
    public static final PropertyDef PROP_INTEGER_DEF = new PropertyDef(
            PROP_INTEGER_NAME,
            PROP_INTEGER_DESCRIPTION,
            PROP_INTEGER_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_INTEGER = new Property(PROP_INTEGER_NAME, PROP_INTEGER_VALUE);

    public static final String PROP_STRING_NAME = "string_property";
    public static final String PROP_STRING_DESCRIPTION = "String property";
    public static final PropertyType PROP_STRING_TYPE = PropertyType.STRING;
    public static final String PROP_STRING_VALUE = "foo";
    public static final PropertyDef PROP_STRING_DEF = new PropertyDef(
            PROP_STRING_NAME,
            PROP_STRING_DESCRIPTION,
            PROP_STRING_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_STRING = new Property(PROP_STRING_NAME, PROP_STRING_VALUE);

    public static final String PROP_BOOLEAN_NAME = "boolean_property";
    public static final String PROP_BOOLEAN_DESCRIPTION = "Boolean property";
    public static final PropertyType PROP_BOOLEAN_TYPE = PropertyType.BOOLEAN;
    public static final Boolean PROP_BOOLEAN_VALUE = Boolean.TRUE;
    public static final PropertyDef PROP_BOOLEAN_DEF = new PropertyDef(
            PROP_BOOLEAN_NAME,
            PROP_BOOLEAN_DESCRIPTION,
            PROP_BOOLEAN_TYPE,
            Boolean.FALSE
    );
    public static final Property PROP_BOOLEAN = new Property(PROP_BOOLEAN_NAME, PROP_BOOLEAN_VALUE);

    // test model objects
    public static final Provider PROVIDER = new MyProvider();
    public static final ResourceDef RESOURCE_DEF = new ResourceDef(
            RESOURCE_FQN,
            RESOURCE_DESCRIPTION,
            Arrays.asList(PROP_INTEGER_DEF, PROP_STRING_DEF, PROP_BOOLEAN_DEF),
            Collections.emptyList()
    );
    public static final Resource RESOURCE = new Resource(
            RESOURCE_FQN,
            RESOURCE_ID,
            Arrays.asList(PROP_INTEGER, PROP_STRING, PROP_BOOLEAN),
            Collections.emptyList()
    );

    public static void compareResourceDefs(ResourceDef resourceDef1, ResourceDef resourceDef2) {
        assertEquals(resourceDef1.getResourceFqn(), resourceDef2.getResourceFqn());

        assertNotNull(resourceDef1.getProperties());
        assertNotNull(resourceDef2.getProperties());
        assertEquals(resourceDef1.getProperties().size(), resourceDef2.getProperties().size());

        resourceDef1.getProperties().forEach(propertyDef1 -> {
            Optional<PropertyDef> propertyDef2Opt = resourceDef1.getProperty(propertyDef1.getName());
            assertNotNull(propertyDef2Opt);
            assertTrue(propertyDef2Opt.isPresent());
            assertEquals(propertyDef1.getName(), propertyDef2Opt.get().getName());
            assertEquals(propertyDef1.getDescription(), propertyDef2Opt.get().getDescription());
            assertEquals(propertyDef1.getPropertyType(), propertyDef2Opt.get().getPropertyType());
            assertEquals(propertyDef1.isArray(), propertyDef2Opt.get().isArray());
        });

        assertNotNull(resourceDef1.getFunctions());
        assertNotNull(resourceDef2.getFunctions());
        assertEquals(resourceDef1.getFunctions().size(), resourceDef2.getFunctions().size());

        resourceDef1.getFunctions().forEach(functionDef1 -> {
            Optional<FunctionDef> functionDef2Opt = resourceDef1.getFunction(functionDef1.getName());
            assertNotNull(functionDef2Opt);
            assertTrue(functionDef2Opt.isPresent());
            assertEquals(functionDef1.getName(), functionDef2Opt.get().getName());
            assertEquals(functionDef1.getDescription(), functionDef2Opt.get().getDescription());
        });
    }

    public static void compareResources(Resource resource1, Resource resource2) {
        assertEquals(resource1.getFqn(), resource2.getFqn());

        assertNotNull(resource1.getProperties());
        assertNotNull(resource2.getProperties());
        assertEquals(resource1.getProperties().size(), resource2.getProperties().size());

        resource1.getProperties().forEach(property1 -> {
            Optional<Property> property2Opt = resource1.getProperty(property1.getName());
            assertNotNull(property2Opt);
            assertTrue(property2Opt.isPresent());
            assertEquals(property1.getName(), property2Opt.get().getName());
            assertEquals(property1.getValue(), property2Opt.get().getValue());
        });

        assertNotNull(resource1.getFunctions());
        assertNotNull(resource2.getFunctions());
        assertEquals(resource1.getFunctions().size(), resource2.getFunctions().size());

        resource1.getFunctions().forEach(function1 -> {
            Optional<Function> function2Opt = resource1.getFunction(function1.getName());
            assertNotNull(function2Opt);
            assertTrue(function2Opt.isPresent());
            assertEquals(function1.getName(), function2Opt.get().getName());
        });
    }
}
