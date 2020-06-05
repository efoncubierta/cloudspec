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
package cloudspec.annotation;

import cloudspec.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Util to extract resource definition information out of annotated classes.
 */
public class ResourceDefReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceDefReflectionUtil.class);

    /**
     * Extract resource definition out of a class annotated with @ResourceDefinition.
     *
     * @param clazz Class.
     * @return Optional resource definition.
     */
    public static Optional<ResourceDef> toResourceDef(Class<?> clazz) {
        return toResourceDefRef(clazz).flatMap(resourceDefRef ->
                toResourceDescription(clazz).map(resourceDescription ->
                        new ResourceDef(resourceDefRef, resourceDescription, toPropertyDefs(clazz), toAssociationDefs(clazz))
                )
        );
    }

    /**
     * Extract resource definition reference out of a class annotated with @ResourceDefinition.
     *
     * @param clazz Class.
     * @return Optional resource definition reference.
     */
    public static Optional<ResourceDefRef> toResourceDefRef(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ResourceDefinition.class)) {
            LOGGER.warn(
                    "Cannot produce a resource definition reference from class {} " +
                            "because it is not annotated with @ResourceDefinition",
                    clazz.getCanonicalName()
            );
            return Optional.empty();
        }

        ResourceDefinition resourceDefAnnotation = clazz.getAnnotation(ResourceDefinition.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Obtained provider='{}', group='{}', name='{}' from class {}",
                    resourceDefAnnotation.provider(),
                    resourceDefAnnotation.group(),
                    resourceDefAnnotation.name(),
                    clazz.getCanonicalName()
            );
        }

        return Optional.of(new ResourceDefRef(
                resourceDefAnnotation.provider(),
                resourceDefAnnotation.group(),
                resourceDefAnnotation.name()
        ));
    }

    /**
     * Extract resource description out of a class annotated with @ResourceDefinition.
     *
     * @param clazz Class.
     * @return Optional resource description.
     */
    public static Optional<String> toResourceDescription(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ResourceDefinition.class)) {
            LOGGER.warn(
                    "Cannot produce resource description from class {} " +
                            "because it is not annotated with @ResourceDefinition",
                    clazz.getCanonicalName()
            );
            return Optional.empty();
        }

        ResourceDefinition resourceDefAnnotation = clazz.getAnnotation(ResourceDefinition.class);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Obtained description='{}' from class {}",
                    resourceDefAnnotation.description(),
                    clazz.getCanonicalName()
            );
        }

        return Optional.of(resourceDefAnnotation.description());
    }

    /**
     * Extract all property definitions out of fields annotated with @PropertyDefinition.
     *
     * @param clazz Class.
     * @return List of property definitions.
     */
    public static List<PropertyDef> toPropertyDefs(Class<?> clazz) {
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

        PropertyType propertyType = guessPropertyType(field);
        Boolean multiValued = guessMultiValued(field);

        switch (propertyType) {
            case KEY_VALUE:
            case NUMBER:
            case STRING:
            case BOOLEAN:
            case DATE:
                return Optional.of(
                        new PropertyDef(
                                propertyDefAnnotation.name(),
                                propertyDefAnnotation.description(),
                                propertyType,
                                multiValued,
                                propertyDefAnnotation.exampleValues()
                        )
                );
            case NESTED:
            default:
                Class<?> type = multiValued ?
                        (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0] :
                        field.getType();

                // get property definitions
                List<PropertyDef> propertyDefs = Stream.of(type.getDeclaredFields())
                        .map(subField -> toPropertyDef(type, subField))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                List<AssociationDef> associationDefs = Stream.of(type.getDeclaredFields())
                        .map(subField -> toAssociationDef(type, subField))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());

                if (propertyDefs.size() > 0 || associationDefs.size() > 0) {
                    return Optional.of(
                            new PropertyDef(
                                    propertyDefAnnotation.name(),
                                    propertyDefAnnotation.description(),
                                    PropertyType.NESTED,
                                    multiValued,
                                    propertyDefAnnotation.exampleValues(),
                                    propertyDefs,
                                    associationDefs
                            )
                    );
                }
        }

        Class<?> realClazz = getContainedClass(field);
        LOGGER.warn("Type {} of property '{}' in class {} is not supported",
                realClazz.getCanonicalName(),
                field.getName(),
                resourceClass.getCanonicalName()
        );

        return Optional.empty();
    }

    /**
     * Extract all association definitions out of fields annotated with @AssociationDefinition.
     *
     * @param clazz Class.
     * @return List of association definitions.
     */
    public static List<AssociationDef> toAssociationDefs(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .map(field -> toAssociationDef(clazz, field))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private static Optional<AssociationDef> toAssociationDef(Class<?> resourceClass, Field field) {
        if (!field.isAnnotationPresent(AssociationDefinition.class)) {
            return Optional.empty();
        }

        AssociationDefinition associationDefAnnotation = field.getAnnotation(AssociationDefinition.class);

        Boolean multiValued = guessMultiValued(field);
        Class<?> realClazz = getContainedClass(field);

        if (!realClazz.isAssignableFrom(String.class)) {
            LOGGER.warn("Type {} of association '{}' in class {} is not supported",
                    realClazz.getCanonicalName(),
                    field.getName(),
                    resourceClass.getCanonicalName()
            );

            return Optional.empty();
        }

        return toResourceDefRef(associationDefAnnotation.targetClass())
                .map(resourceDefRef -> new AssociationDef(
                        associationDefAnnotation.name(),
                        associationDefAnnotation.description(),
                        resourceDefRef,
                        multiValued
                ));
    }

    /**
     * Guess whether a field of a class is multivalued.
     *
     * @param field Field.
     * @return True if field is multi-valued. False otherwise.
     */
    public static Boolean guessMultiValued(Field field) {
        return field.getType().isAssignableFrom(List.class);
    }

    /**
     * Guess the property type of field of a class.
     *
     * @param field Field.
     * @return Property type.
     */
    public static PropertyType guessPropertyType(Field field) {
        Class<?> clazz = field.getType();

        if (field.getType().isAssignableFrom(List.class)) {
            clazz = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        }

        if (Number.class.isAssignableFrom(clazz)) {
            return PropertyType.NUMBER;
        } else if (clazz.isAssignableFrom(String.class)) {
            return PropertyType.STRING;
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return PropertyType.BOOLEAN;
        } else if (clazz.isAssignableFrom(Date.class) || clazz.isAssignableFrom(Instant.class)) {
            return PropertyType.DATE;
        } else if (clazz.isAssignableFrom(KeyValue.class)) {
            return PropertyType.KEY_VALUE;
        }

        // otherwise it must be nested
        return PropertyType.NESTED;
    }

    public static Class<?> getContainedClass(Field field) {
        Class<?> clazz = field.getType();

        if (field.getType().isAssignableFrom(List.class)) {
            clazz = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        }

        return clazz;
    }
}
