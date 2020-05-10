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

import java.lang.reflect.Field;
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
        } else {
            List<PropertyDef> propertyDefs = Stream.of(type.getDeclaredFields())
                    .map(subField -> toPropertyDef(type, subField))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            if (propertyDefs.size() > 0) {
                return Optional.of(
                        new PropertyDef(
                                propertyDefAnnotation.name(),
                                propertyDefAnnotation.description(),
                                PropertyType.MAP,
                                Boolean.FALSE,
                                propertyDefs
                        )
                );
            }
        }

        LOGGER.warn("Type {} of property '{}' in class {} is not supported",
                field.getDeclaringClass().getCanonicalName(),
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

        if (!field.getType().isAssignableFrom(String.class)) {
            LOGGER.warn("Type {} of association '{}' in class {} is not supported",
                    field.getDeclaringClass().getCanonicalName(),
                    field.getName(),
                    resourceClass.getCanonicalName()
            );

            return Optional.empty();
        }


        return toResourceDefRef(associationDefAnnotation.targetClass())
                .map(resourceDefRef -> new AssociationDef(
                        associationDefAnnotation.name(),
                        associationDefAnnotation.description(),
                        resourceDefRef
                ));
    }
}
