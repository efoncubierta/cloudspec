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
package cloudspec.annotation;

import cloudspec.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Util to extract resource information out of annotated classes.
 */
public class ResourceReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceReflectionUtil.class);

    /**
     * Extract resource out of an object of a class annotated with @ResourceDefinition.
     *
     * @param obj Object.
     * @return Optional resource.
     */
    public static Optional<Resource> toResource(Object obj) {
        return ResourceDefReflectionUtil.toResourceDefRef(obj.getClass())
                .flatMap(resourceDefRef -> toResourceId(obj)
                        .map(resourceId -> new Resource(
                                resourceDefRef,
                                resourceId,
                                ResourceReflectionUtil.toProperties(obj),
                                ResourceReflectionUtil.toAssociations(obj)
                        )));
    }

    /**
     * Extract resource id out of an object with a class annotated with @ResourceDefinition.
     *
     * @param obj Object.
     * @return Optional resource id.
     */
    public static Optional<String> toResourceId(Object obj) {
        return Stream.of(obj.getClass().getDeclaredFields())
                .peek(field -> {
                    if (!field.isAnnotationPresent(IdDefinition.class)) {
                        LOGGER.debug(
                                "Cannot produce a resource id from field '{}' of object of class '{}' " +
                                        "because it does not have a field annotated with @IdDefinition",
                                field.getName(),
                                obj.getClass().getCanonicalName()
                        );
                    }
                })
                .filter(field -> field.isAnnotationPresent(IdDefinition.class))
                .filter(field -> field.getType().isAssignableFrom(String.class))
                .map(field -> {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Found @IdDefinition annotation in field '{}' of object of class '{}'",
                                field.getName(),
                                obj.getClass().getCanonicalName()
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

    /**
     * Extract all properties out of fields annotated with @PropertyDefinition.
     *
     * @param obj Object.
     * @return List of properties.
     */
    public static Properties toProperties(Object obj) {
        return new Properties(
                Stream.of(obj.getClass().getDeclaredFields())
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
                                field.setAccessible(true);
                                Object value = field.get(obj);
                                Boolean multiValued = ResourceDefReflectionUtil.guessMultiValued(field);
                                field.setAccessible(true);

                                switch (ResourceDefReflectionUtil.guessPropertyType(field)) {
                                    case INTEGER:
                                    case DOUBLE:
                                    case STRING:
                                    case BOOLEAN:
                                    case KEY_VALUE:
                                        return Stream.of(new Property(propertyDefAnnotation.name(), value));
                                    case NESTED:
                                    default:
                                        if (multiValued) {
                                            return Stream.of(
                                                    new Property(
                                                            propertyDefAnnotation.name(),
                                                            ((List<?>) value)
                                                                    .stream()
                                                                    .map(ResourceReflectionUtil::toProperties)
                                                                    .collect(Collectors.toList())
                                                    )
                                            );
                                        }

                                        Properties properties = toProperties(value);
                                        Associations associations = toAssociations(value);
                                        if (properties.size() > 0 || associations.size() > 0) {
                                            return Stream.of(
                                                    new Property(
                                                            propertyDefAnnotation.name(),
                                                            new Properties(properties, associations)
                                                    )
                                            );
                                        }
                                }

                                LOGGER.warn("Type {} of property '{}' in class {} is not supported",
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
                        })
        );
    }

    /**
     * Extract all associations out of fields annotated with @AssociationDefinition.
     *
     * @param obj Object.
     * @return List of associations.
     */
    public static Associations toAssociations(Object obj) {
        return new Associations(
                Stream.of(obj.getClass().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(AssociationDefinition.class))
                        .flatMap(field -> {
                            // validate field is a string
                            if (!field.getType().isAssignableFrom(String.class)) {
                                LOGGER.warn("Type {} of association '{}' in class {} is not supported",
                                        field.getType().getCanonicalName(),
                                        field.getName(),
                                        obj.getClass().getCanonicalName()
                                );
                                return Stream.empty();
                            }

                            AssociationDefinition associationDefinitionAnnotation = field.getAnnotation(AssociationDefinition.class);

                            String associationName = associationDefinitionAnnotation.name();
                            Optional<ResourceDefRef> resourceDefRefOptional = ResourceDefReflectionUtil.toResourceDefRef(associationDefinitionAnnotation.targetClass());

                            // TODO add validation for association name and resourceDefRef
                            if (!resourceDefRefOptional.isPresent()) {
                                return Stream.empty();
                            }

                            try {
                                field.setAccessible(true);
                                String id = (String) field.get(obj);
                                field.setAccessible(true);

                                return Stream.of(new Association(
                                        associationName,
                                        resourceDefRefOptional.get(),
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
                        })
        );
    }
}
