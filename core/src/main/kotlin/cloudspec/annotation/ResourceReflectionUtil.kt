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
package cloudspec.annotation

import arrow.core.Option
import arrow.core.Some
import arrow.core.firstOrNone
import cloudspec.annotation.ResourceDefReflectionUtil.guessContainedType
import cloudspec.annotation.ResourceDefReflectionUtil.guessPropertyType
import cloudspec.annotation.ResourceDefReflectionUtil.isNullableString
import cloudspec.annotation.ResourceDefReflectionUtil.isString
import cloudspec.annotation.ResourceDefReflectionUtil.toResourceDefRef
import cloudspec.model.*
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

/**
 * Util to extract resource information out of annotated classes.
 */
object ResourceReflectionUtil {
    private val logger = LoggerFactory.getLogger(ResourceReflectionUtil::class.java)

    /**
     * Extract resource out of an object of a class annotated with @ResourceDefinition.
     *
     * @param obj Object.
     * @return Optional resource.
     */
    fun toResource(obj: Any): Option<Resource> {
        return toResourceDefRef(obj::class).flatMap { ref ->
            toResourceId(obj).map { id ->
                Resource(ref, id, toProperties(obj), toAssociations(obj))
            }
        }
    }

    /**
     * Extract resource id out of an object with a class annotated with @ResourceDefinition.
     *
     * @param obj Object.
     * @return Resource id or null.
     */
    fun toResourceId(obj: Any): Option<String> {
        return obj::class.memberProperties
                .onEach {
                    if (it.findAnnotation<IdDefinition>() == null || !isString(it.returnType)) {
                        logger.debug("Cannot produce a resource id from property '${it.name}' " +
                                             "of object of class '${obj::class.qualifiedName}' " +
                                             "because it is not annotated with @IdDefinition " +
                                             "or is not a String")
                    }
                }
                .filter { it.findAnnotation<IdDefinition>() != null && isString(it.returnType) }
                .map {
                    if (logger.isDebugEnabled) {
                        logger.debug("Found @IdDefinition annotation in property '${it.name}' " +
                                             "of object of class '${obj::class.qualifiedName}'")
                    }

                    try {
                        return@map it.call(obj) as String
                    } catch (exception: IllegalAccessException) {
                        throw RuntimeException("Could not obtain ID from property '${it.name}' " +
                                                       "of object of class ${obj::class.qualifiedName}'")
                    }
                }
                .firstOrNone()
    }

    /**
     * Extract all properties out of object properties annotated with @PropertyDefinition.
     *
     * @param obj Object.
     * @return Set of properties.
     */
    fun toProperties(obj: Any): Set<Property<*>> {
        return obj::class.memberProperties
                .filter { it.findAnnotation<PropertyDefinition>() != null }
                .flatMap { kprop ->
                    val propertyDefAnnotation = kprop.findAnnotation<PropertyDefinition>()!!

                    if (logger.isDebugEnabled) {
                        logger.debug("Found @PropertyDefinition annotation in property '${kprop.name}' " +
                                             "of object of class '${kprop.returnType}'")
                    }

                    try {
                        // get property value in set form
                        val values = when (val value = kprop.call(obj)) {
                            is List<*> -> value.toSet()
                            is Set<*> -> value
                            else -> setOf(value)
                        }

                        values.mapNotNull { value ->
                            when (val propertyType = guessPropertyType(kprop)) {
                                is Some -> when (propertyType.t) {
                                    PropertyType.NUMBER ->
                                        NumberProperty(propertyDefAnnotation.name, (value as Number?))
                                    PropertyType.STRING ->
                                        StringProperty(propertyDefAnnotation.name, (value as String?))
                                    PropertyType.BOOLEAN ->
                                        BooleanProperty(propertyDefAnnotation.name, (value as Boolean?))
                                    PropertyType.DATE ->
                                        InstantProperty(
                                                propertyDefAnnotation.name,
                                                when (value) {
                                                    is Date -> value.toInstant()
                                                    is Instant -> value
                                                    else -> null
                                                }
                                        )
                                    PropertyType.KEY_VALUE ->
                                        KeyValueProperty(propertyDefAnnotation.name, (value as KeyValue?))
                                    PropertyType.NESTED ->
                                        NestedProperty(
                                                propertyDefAnnotation.name,
                                                value?.let {
                                                    NestedPropertyValue(toProperties(value), toAssociations(value))
                                                }
                                        )
                                }
                                else -> {
                                    val realType = guessContainedType(kprop.returnType)
                                    logger.warn("Type $realType of property '${kprop.name}' " +
                                                        "in class ${obj::class.qualifiedName} is not supported")
                                    null
                                }
                            }
                        }
                    } catch (exception: IllegalAccessException) {
                        throw RuntimeException("Could not obtain value from property ${kprop.name} " +
                                                       "in class ${obj::class.qualifiedName}")
                    }
                }
                .toSet()
    }

    /**
     * Extract all associations out of object properties annotated with @AssociationDefinition.
     *
     * @param obj Object.
     * @return List of associations.
     */
    fun toAssociations(obj: Any): Set<Association> {
        return obj::class.memberProperties
                .filter { it.findAnnotation<AssociationDefinition>() != null }
                .flatMap { kprop ->
                    val associationDefinitionAnnotation = kprop.findAnnotation<AssociationDefinition>()!!

                    // get type
                    val containedType = guessContainedType(kprop.returnType)

                    // only strings are supported
                    if (!isNullableString(containedType)) {
                        logger.warn("Type $containedType of association '${kprop.name}' " +
                                            "in class ${obj::class.qualifiedName} is not supported")
                        return@flatMap emptyList<Association>()
                    }

                    when (val resourceDefRefOpt= toResourceDefRef(associationDefinitionAnnotation.targetClass)) {
                        is Some -> {
                            // TODO add validation for association name and resourceDefRef
                            try {
                                kprop.call(obj)?.let { id ->
                                    when (id) {
                                        is List<*> -> id.toSet()
                                        is Set<*> -> id
                                        else -> setOf(id)
                                    }.map { resourceId ->
                                        Association(associationDefinitionAnnotation.name,
                                                    resourceDefRefOpt.t,
                                                    resourceId as String)
                                    }
                                } ?: emptyList()
                            } catch (exception: IllegalAccessException) {
                                throw RuntimeException("Could not obtain value from property ${kprop.name} " +
                                                               "in class ${obj::class.qualifiedName}")
                            }
                        }
                        else -> emptyList()
                    }
                }
                .toSet()
    }
}
