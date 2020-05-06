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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceReflectionUtil.class);

    public static Optional<Resource> toResource(Object obj) {
        return toResourceFqn(obj.getClass())
                .flatMap(resourceFqn -> toResourceId(obj)
                        .map(resourceId -> new Resource(
                                resourceFqn,
                                resourceId,
                                ResourceReflectionUtil.toProperties(obj),
                                ResourceReflectionUtil.toAssociations(obj)
                        )));
    }

    public static Optional<ResourceFqn> toResourceFqn(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ResourceDefinition.class)) {
            LOGGER.warn(
                    "Cannot obtain FQN of class '{}' because it is not annotated with @ResourceDefinition",
                    clazz.getCanonicalName()
            );
            return Optional.empty();
        }

        ResourceDefinition resourceDefAnnotation = clazz.getAnnotation(ResourceDefinition.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Found @ResourceDefinition annotation in class '{}'. " +
                            "Obtained provider={}, group={}, name={}",
                    clazz.getCanonicalName(),
                    resourceDefAnnotation.provider(),
                    resourceDefAnnotation.group(),
                    resourceDefAnnotation.name()
            );
        }

        return Optional.of(new ResourceFqn(
                resourceDefAnnotation.provider(),
                resourceDefAnnotation.group(),
                resourceDefAnnotation.name()
        ));
    }

    public static Optional<String> toResourceDescription(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ResourceDefinition.class)) {
            LOGGER.warn(
                    "Cannot obtain description of class '{}' because it is not annotated with @ResourceDefinition",
                    clazz.getCanonicalName()
            );
            return Optional.empty();
        }

        ResourceDefinition resourceDefAnnotation = clazz.getAnnotation(ResourceDefinition.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Found @ResourceDefinition annotation in class '{}'. " +
                            "Obtained description={}",
                    clazz.getCanonicalName(),
                    resourceDefAnnotation.description()
            );
        }

        return Optional.of(resourceDefAnnotation.description());
    }

    public static Optional<String> toResourceId(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .peek(field -> {
                    if (!field.isAnnotationPresent(IdDefinition.class)) {
                        LOGGER.warn(
                                "Cannot obtain id of an object of class '{}' because it does not have a field annotated with @IdDefinition",
                                obj.getClass().getCanonicalName()
                        );
                    }
                })
                .filter(field -> field.isAnnotationPresent(IdDefinition.class))
                .filter(field -> field.getType().isAssignableFrom(String.class))
                .map(field -> {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Found @IdDefinition annotation in class '{}'",
                                field.getType().getCanonicalName()
                        );
                    }

                    try {
                        field.setAccessible(true);
                        String id = (String) field.get(obj);
                        field.setAccessible(true);

                        return id;
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(
                                String.format(
                                        "Could not obtain ID from field '%s' in class '%s'",
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
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Found @PropertyDefinition annotation in field '{}' of class '{}'",
                                field.getName(),
                                field.getType().getCanonicalName()
                        );
                    }

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

                        LOGGER.warn("Field type '{}' of property '{}' in class '{}' is not supported.",
                                field.getType().getCanonicalName(),
                                field.getName(),
                                obj.getClass().getCanonicalName()
                        );
                        return Stream.empty();
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

    public static List<Association> toAssociations(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AssociationDefinition.class))
                .flatMap(field -> {
                    // validate field is a string
                    if (!field.getType().isAssignableFrom(String.class)) {
                        LOGGER.warn("Field type '{}' of association '{}' in class '{}' is not supported.",
                                field.getType().getCanonicalName(),
                                field.getName(),
                                obj.getClass().getCanonicalName()
                        );
                        return Stream.empty();
                    }

                    AssociationDefinition associationDefinitionAnnotation = field.getAnnotation(AssociationDefinition.class);

                    String associationName = associationDefinitionAnnotation.name();
                    Optional<ResourceFqn> resourceFqn = toResourceFqn(associationDefinitionAnnotation.targetClass());

                    // TODO add validation for association name and resourceFqn
                    if (!resourceFqn.isPresent()) {
                        return Stream.empty();
                    }

                    try {
                        field.setAccessible(true);
                        String id = (String) field.get(obj);
                        field.setAccessible(true);

                        return Stream.of(new Association(
                                associationName,
                                resourceFqn.get(),
                                id)
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

    public static Optional<ResourceDef> toResourceDef(Class<?> resourceClass) {
        return toResourceFqn(resourceClass).flatMap(resourceFqn ->
                toResourceDescription(resourceClass).map(resourceDescription ->
                        new ResourceDef(resourceFqn, resourceDescription, toPropertyDefs(resourceClass), toAssociationDefs(resourceClass))
                )
        );

    }

    private static List<PropertyDef> toPropertyDefs(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(field -> toPropertyDef(clazz, field))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<PropertyDef> toPropertyDef(Class<?> resourceClass, Field field) {
        if (!field.isAnnotationPresent(PropertyDefinition.class)) {
            return Optional.empty();
        }

        PropertyDefinition propertyDefAnnotation = field.getAnnotation(PropertyDefinition.class);

        Class<?> type = field.getType();

        if (type.isAssignableFrom(String.class)) {
            return Optional.of(
                    new PropertyDef(
                            propertyDefAnnotation.name(),
                            propertyDefAnnotation.description(),
                            PropertyType.STRING,
                            Boolean.FALSE)
            );
        } else if (type.isAssignableFrom(Integer.class)) {
            return Optional.of(
                    new PropertyDef(
                            propertyDefAnnotation.name(),
                            propertyDefAnnotation.description(),
                            PropertyType.INTEGER,
                            Boolean.FALSE)
            );
        } else if (type.isAssignableFrom(Boolean.class)) {
            return Optional.of(
                    new PropertyDef(
                            propertyDefAnnotation.name(),
                            propertyDefAnnotation.description(),
                            PropertyType.BOOLEAN,
                            Boolean.FALSE)
            );
        }

        LOGGER.warn("Type of property '{}' in class '{}' is not supported",
                field.getName(),
                resourceClass.getCanonicalName()
        );

        return Optional.empty();
    }

    private static List<AssociationDef> toAssociationDefs(Class<?> clazz) {
        return Collections.emptyList();
    }
}
