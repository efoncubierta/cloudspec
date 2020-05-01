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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceReflectionUtil {
    public static List<Property> toProperties(Resource resource) {
        return Stream.of(resource.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PropertyDefinition.class))
                .map(field -> {
                    PropertyDefinition propertyDefAnnotation = field.getAnnotation(PropertyDefinition.class);

                    try {
                        String name = propertyDefAnnotation.name();

                        field.setAccessible(true);
                        Object value = field.get(resource);
                        field.setAccessible(true);

                        if (value instanceof String) {
                            return new StringProperty(name, (String) value);
                        } else if (value instanceof Integer) {
                            return new IntegerProperty(name, (Integer) value);
                        } else if (value instanceof Boolean) {
                            return new BooleanProperty(name, (Boolean) value);
                        }

                        throw new RuntimeException(
                                String.format(
                                        "Type of property %s in class %s is not supported",
                                        field.getName(),
                                        resource.getClass().getCanonicalName()
                                )
                        );
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(
                                String.format(
                                        "Could not obtain value from property %s in class %s",
                                        field.getName(),
                                        resource.getClass().getCanonicalName()
                                )
                        );
                    }
                }).collect(Collectors.toList());
    }

    public static List<Function> toFunctions(Resource resource) {
        return Collections.emptyList();
    }

    public static ResourceDef toResourceDef(Class<? extends Resource> resourceClass) {
        ResourceDefinition resourceDefinition = resourceClass.getAnnotation(ResourceDefinition.class);

        List<PropertyDef> properties = Stream.of(resourceClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PropertyDefinition.class))
                .map(field -> toPropertyDef(resourceClass, field))
                .collect(Collectors.toList());

        List<FunctionDef> functions = Collections.emptyList();

        return new ResourceDef(new ResourceFqn(
                resourceDefinition.provider(),
                resourceDefinition.group(),
                resourceDefinition.name()
        ), properties, functions);
    }

    private static PropertyDef toPropertyDef(Class<? extends Resource> resourceClass, Field field) {
        PropertyDefinition propertyDefAnnotation = field.getAnnotation(PropertyDefinition.class);

        Class<?> type = field.getType();

        if (type.isAssignableFrom(String.class)) {
            return new PropertyDef(
                    propertyDefAnnotation.name(),
                    propertyDefAnnotation.description(),
                    PropertyType.STRING,
                    Boolean.FALSE);
        } else if (type.isAssignableFrom(Integer.class)) {
            return new PropertyDef(
                    propertyDefAnnotation.name(),
                    propertyDefAnnotation.description(),
                    PropertyType.INTEGER,
                    Boolean.FALSE);
        } else if (type.isAssignableFrom(Boolean.class)) {
            return new PropertyDef(
                    propertyDefAnnotation.name(),
                    propertyDefAnnotation.description(),
                    PropertyType.BOOLEAN,
                    Boolean.FALSE);
        }

        throw new RuntimeException(
                String.format(
                        "Type of property %s in class %s is not supported",
                        field.getName(),
                        resourceClass.getCanonicalName()
                )
        );
    }
}
