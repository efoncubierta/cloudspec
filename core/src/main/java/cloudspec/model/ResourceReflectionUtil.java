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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceReflectionUtil {
    public static List<Property> resourceToProperties(Resource resource) {
        return toProperties(resource);
    }

    private static List<Property> toProperties(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PropertyDefinition.class))
                .map(field -> {
                    PropertyDefinition propertyDefAnnotation = field.getAnnotation(PropertyDefinition.class);

                    try {
                        String name = propertyDefAnnotation.name();

                        field.setAccessible(true);
                        Object value = field.get(obj);
                        field.setAccessible(true);

                        if (value instanceof String) {
                            return new StringProperty(name, (String) value);
                        } else if (value instanceof Integer) {
                            return new IntegerProperty(name, (Integer) value);
                        } else if (value instanceof Boolean) {
                            return new BooleanProperty(name, (Boolean) value);
                        } else {
                            List<Property> properties = toProperties(value);
                            if (properties.size() > 0) {
                                Map<String, Property> propertiesMap = new HashMap<String, Property>();
                                properties.forEach(property -> {
                                    propertiesMap.put(property.getName(), property);
                                });

                                return new MapProperty(name, propertiesMap);
                            }
                        }

                        throw new RuntimeException(
                                String.format(
                                        "Type of property %s in class %s is not supported, or does not have field annotated with @PropertyDefinition",
                                        field.getName(),
                                        obj.getClass().getCanonicalName()
                                )
                        );
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(
                                String.format(
                                        "Could not obtain value from property %s in class %s",
                                        field.getName(),
                                        obj.getClass().getCanonicalName()
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

        List<PropertyDef> properties = toPropertydefs(resourceClass);
        List<FunctionDef> functions = toFunctionDefs(resourceClass);

        return new ResourceDef(new ResourceFqn(
                resourceDefinition.provider(),
                resourceDefinition.group(),
                resourceDefinition.name()
        ), properties, functions);
    }

    private static List<PropertyDef> toPropertydefs(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PropertyDefinition.class))
                .map(field -> toPropertyDef(clazz, field))
                .collect(Collectors.toList());
    }

    private static PropertyDef toPropertyDef(Class<?> resourceClass, Field field) {
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
        } else {
            // check if type has @PropertyDefinition annotations
            List<PropertyDef> properties = toPropertydefs(type);
            if (properties.size() > 0) {
                return new PropertyDef(
                        propertyDefAnnotation.name(),
                        propertyDefAnnotation.description(),
                        PropertyType.MAP,
                        Boolean.FALSE,
                        properties
                );
            }
        }

        throw new RuntimeException(
                String.format(
                        "Type of property %s in class %s is not supported, or does not have field annotated with @PropertyDefinition",
                        field.getName(),
                        resourceClass.getCanonicalName()
                )
        );
    }

    private static List<FunctionDef> toFunctionDefs(Class<?> clazz) {
        return Collections.emptyList();
    }
}
