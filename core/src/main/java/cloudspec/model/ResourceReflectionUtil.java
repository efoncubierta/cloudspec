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

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceReflectionUtil {
    public static Optional<Resource> toResource(Object obj) {
        return toResourceFqn(obj)
                .flatMap(resourceFqn -> toResourceId(obj)
                        .map(resourceId -> new Resource(
                                resourceFqn,
                                resourceId,
                                ResourceReflectionUtil.toProperties(obj),
                                ResourceReflectionUtil.toFunctions(obj)
                        )));
    }

    public static Optional<ResourceFqn> toResourceFqn(Object obj) {
        // check the class is annotated
        if (!obj.getClass().isAnnotationPresent(ResourceDefinition.class)) {
            return Optional.empty();
        }

        ResourceDefinition resourceDefAnnotation = obj.getClass().getAnnotation(ResourceDefinition.class);
        return Optional.of(new ResourceFqn(
                resourceDefAnnotation.provider(),
                resourceDefAnnotation.group(),
                resourceDefAnnotation.name()
        ));
    }

    public static Optional<String> toResourceId(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(IdDefinition.class))
                .filter(field -> field.getType().isAssignableFrom(String.class))
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        String id = (String) field.get(obj);
                        field.setAccessible(true);

                        return id;
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(
                                String.format(
                                        "Could not obtain ID from field %s in class %s",
                                        field.getName(),
                                        obj.getClass().getCanonicalName()
                                )
                        );
                    }
                }).findFirst();

    }

    public static List<Property> toProperties(Object obj) {
        return toProperties(obj, "");
    }

    private static List<Property> toProperties(Object obj, String prefix) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PropertyDefinition.class))
                .flatMap(field -> {
                    PropertyDefinition propertyDefAnnotation = field.getAnnotation(PropertyDefinition.class);

                    try {
                        String name = String.format("%s%s", prefix, propertyDefAnnotation.name());

                        field.setAccessible(true);
                        Object value = field.get(obj);
                        field.setAccessible(true);

                        if (value instanceof String || value instanceof Integer || value instanceof Boolean) {
                            return Stream.of(new Property(name, value));
                        } else {
                            List<Property> properties = toProperties(value, name + ".");
                            if (properties.size() > 0) {
                                return properties.stream();
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

    public static List<Function> toFunctions(Object object) {
        return Collections.emptyList();
    }

    public static ResourceDef toResourceDef(Class<?> resourceClass) {
        ResourceDefinition resourceDefinition = resourceClass.getAnnotation(ResourceDefinition.class);

        // TODO validate class is annotated

        List<PropertyDef> properties = toPropertydefs(resourceClass);
        List<FunctionDef> functions = toFunctionDefs(resourceClass);

        return new ResourceDef(new ResourceFqn(
                resourceDefinition.provider(),
                resourceDefinition.group(),
                resourceDefinition.name()
        ), resourceDefinition.description(), properties, functions);
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
