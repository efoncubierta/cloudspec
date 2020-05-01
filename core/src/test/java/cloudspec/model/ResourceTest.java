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

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class ResourceTest {
    public static final Resource TEST_RESOURCE = new MyResource(MyResource.PROP_INTEGER_VALUE, MyResource.PROP_STRING_VALUE, MyResource.PROP_BOOLEAN_VALUE);

    @Test
    public void shouldBuildResourceFromAnnotation() {
        BaseResource resource = new MyResource(MyResource.PROP_INTEGER_VALUE, MyResource.PROP_STRING_VALUE, MyResource.PROP_BOOLEAN_VALUE);

        assertNotNull(resource.getResourceFqn());
        assertEquals(MyResource.RESOURCE_FQN, resource.getResourceFqn());

        assertNotNull(resource.getProperties());
        assertEquals(3, resource.getProperties().size());

        Optional<Property> integerProperty = resource.getProperty(MyResource.PROP_INTEGER_NAME);
        assertNotNull(integerProperty);
        assertTrue(integerProperty.isPresent());
        assertEquals(MyResource.PROP_INTEGER_VALUE, integerProperty.get().getValue());

        Optional<Property> stringProperty = resource.getProperty(MyResource.PROP_STRING_NAME);
        assertNotNull(stringProperty);
        assertTrue(stringProperty.isPresent());
        assertEquals(MyResource.PROP_STRING_VALUE, stringProperty.get().getValue());

        Optional<Property> booleanProperty = resource.getProperty(MyResource.PROP_BOOLEAN_NAME);
        assertNotNull(booleanProperty);
        assertTrue(booleanProperty.isPresent());
        assertEquals(MyResource.PROP_BOOLEAN_VALUE, booleanProperty.get().getValue());

        assertNotNull(resource.getFunctions());
        assertEquals(0, resource.getFunctions().size());
    }


}
