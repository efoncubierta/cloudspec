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

import cloudspec.annotation.ProviderDefinition;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProviderTest {
    public static final String PROVIDER_NAME = "myprovider";
    public static final String PROVIDER_DESCRIPTION = "My provider";

    @Test
    public void shouldBuildProviderFromAnnotation() {
        Provider provider = new MyProvider();

        assertNotNull(provider.getName());
        assertEquals(PROVIDER_NAME, provider.getName());

        assertNotNull(provider.getDescription());
        assertEquals(PROVIDER_DESCRIPTION, provider.getDescription());

        assertNotNull(provider.getResourceDefs());
        assertEquals(1, provider.getResourceDefs().size());

        ResourceDef resourceDef = provider.getResourceDefs().get(0);
        assertEquals(ResourceTest.RESOURCE_FQN, resourceDef.getResourceFqn());

        assertNotNull(resourceDef.getProperties());
        assertEquals(3, resourceDef.getProperties().size());

        Optional<PropertyDef> integerPropertyDef = resourceDef.getProperty(ResourceTest.PROP_INTEGER_NAME);
        assertNotNull(integerPropertyDef);
        assertTrue(integerPropertyDef.isPresent());
        assertEquals(ResourceTest.PROP_INTEGER_NAME, integerPropertyDef.get().getName());
        assertEquals(ResourceTest.PROP_INTEGER_DESCRIPTION, integerPropertyDef.get().getDescription());
        assertEquals(ResourceTest.PROP_INTEGER_TYPE, integerPropertyDef.get().getPropertyType());

        Optional<PropertyDef> stringPropertyDef = resourceDef.getProperty(ResourceTest.PROP_STRING_NAME);
        assertNotNull(stringPropertyDef);
        assertTrue(stringPropertyDef.isPresent());
        assertEquals(ResourceTest.PROP_STRING_NAME, stringPropertyDef.get().getName());
        assertEquals(ResourceTest.PROP_STRING_DESCRIPTION, stringPropertyDef.get().getDescription());
        assertEquals(ResourceTest.PROP_STRING_TYPE, stringPropertyDef.get().getPropertyType());

        Optional<PropertyDef> booleanPropertyDef = resourceDef.getProperty(ResourceTest.PROP_BOOLEAN_NAME);
        assertNotNull(booleanPropertyDef);
        assertTrue(booleanPropertyDef.isPresent());
        assertEquals(ResourceTest.PROP_BOOLEAN_NAME, booleanPropertyDef.get().getName());
        assertEquals(ResourceTest.PROP_BOOLEAN_DESCRIPTION, booleanPropertyDef.get().getDescription());
        assertEquals(ResourceTest.PROP_BOOLEAN_TYPE, booleanPropertyDef.get().getPropertyType());
    }

    @ProviderDefinition(
            name = PROVIDER_NAME,
            description = PROVIDER_DESCRIPTION,
            resources = {ResourceTest.MyResource.class}
    )
    private class MyProvider extends Provider {
        @Override
        public List<Resource> getResources(ResourceFqn resourceFqn) {
            return Collections.emptyList();
        }
    }
}
