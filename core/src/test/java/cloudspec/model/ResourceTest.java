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
package cloudspec.model;

import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.Optional;
import java.util.Stack;

import static org.junit.Assert.*;

public class ResourceTest {
    @Test
    public void shouldGetPropertyByPath() {
        Stack<String> path = new Stack<>();
        path.add(ModelTestUtils.PROP_NESTED_NAME);
        path.add(ModelTestUtils.PROP_STRING_NAME);
        Optional<PropertyDef> propertyDefOpt = ModelTestUtils.RESOURCE_DEF.getPropertyByPath(path);

        assertNotNull(propertyDefOpt);
        assertTrue(propertyDefOpt.isPresent());
        assertEquals(ModelTestUtils.PROP_STRING_DEF, propertyDefOpt.get());
    }
//    @Test
//    public void shouldBuildResourceFromAnnotation() {
//        BaseResource resource = new MyResource(MyResource.PROP_INTEGER_VALUE, MyResource.PROP_STRING_VALUE, MyResource.PROP_BOOLEAN_VALUE);
//
//        assertNotNull(resource.getFqn());
//        assertEquals(MyResource.RESOURCE_FQN, resource.getFqn());
//
//        assertNotNull(resource.getProperties());
//        assertEquals(6, resource.getProperties().size());
//
//        Optional<Property> integerProperty = resource.getProperty(MyResource.PROP_INTEGER_NAME);
//        assertNotNull(integerProperty);
//        assertTrue(integerProperty.isPresent());
//        assertEquals(MyResource.PROP_INTEGER_VALUE, integerProperty.get().getValue());
//
//        Optional<Property> stringProperty = resource.getProperty(MyResource.PROP_STRING_NAME);
//        assertNotNull(stringProperty);
//        assertTrue(stringProperty.isPresent());
//        assertEquals(MyResource.PROP_STRING_VALUE, stringProperty.get().getValue());
//
//        Optional<Property> booleanProperty = resource.getProperty(MyResource.PROP_BOOLEAN_NAME);
//        assertNotNull(booleanProperty);
//        assertTrue(booleanProperty.isPresent());
//        assertEquals(MyResource.PROP_BOOLEAN_VALUE, booleanProperty.get().getValue());;
//
//        assertNotNull(resource.getFunctions());
//        assertEquals(0, resource.getFunctions().size());
//    }


}
