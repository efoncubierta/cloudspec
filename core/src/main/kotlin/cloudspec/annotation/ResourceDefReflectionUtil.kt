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

import cloudspec.model.*
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.full.*

/**
 * Utility to extract resource definition information out of annotated classes.
 */
object ResourceDefReflectionUtil {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Extract resource definition out of a class annotated with @ResourceDefinition.
     *
     * @param kclass Class.
     * @return Resource definition or null.
     */
    fun toResourceDef(kclass: KClass<*>): ResourceDef? {
        return toResourceDefRef(kclass)?.let { ref ->
            toResourceDescription(kclass)?.let { description ->
                ResourceDef(ref,
                            description,
                            toPropertyDefs(kclass),
                            toAssociationDefs(kclass))
            }
        }
    }

    /**
     * Extract resource definition reference out of a class annotated with @ResourceDefinition.
     *
     * @param kclass Class.
     * @return Resource definition reference or null.
     */
    fun toResourceDefRef(kclass: KClass<*>): ResourceDefRef? {
        return kclass.findAnnotation<ResourceDefinition>()?.let {
            if (logger.isDebugEnabled) {
                logger.debug("Obtained " +
                                     "provider='${it.provider}', " +
                                     "group='${it.group}', " +
                                     "name='${it.name}' " +
                                     "from class ${kclass.qualifiedName}")
            }

            return ResourceDefRef(it.provider,
                                  it.group,
                                  it.name)
        }
    }

    /**
     * Extract resource description out of a class annotated with @ResourceDefinition.
     *
     * @param kclass Class.
     * @return Resource description or null
     */
    fun toResourceDescription(kclass: KClass<*>): String? {
        return kclass.findAnnotation<ResourceDefinition>()?.let {
            if (logger.isDebugEnabled) {
                logger.debug("Obtained description='${it.description}' " +
                                     "from class ${kclass.qualifiedName}")
            }

            return it.description
        }
    }

    /**
     * Extract all property definitions out of a class' properties annotated with @PropertyDefinition.
     *
     * @param kclass Class.
     * @return List of property definitions.
     */
    fun toPropertyDefs(kclass: KClass<*>): Set<PropertyDef> {
        return kclass.memberProperties
                .mapNotNull { toPropertyDef(kclass, it) }
                .toSet()
    }

    /**
     * Extract property definition out of a class' property annotated with @PropertyDefinition.
     *
     * @param kclass Class.
     * @param kprop Property.
     * @return Property definition or null.
     */
    private fun toPropertyDef(kclass: KClass<*>, kprop: KProperty<*>): PropertyDef? {
        return kprop.findAnnotation<PropertyDefinition>()?.let { annotation ->
            when (val propertyType = guessPropertyType(kprop)) {
                PropertyType.KEY_VALUE,
                PropertyType.NUMBER,
                PropertyType.STRING,
                PropertyType.BOOLEAN,
                PropertyType.DATE ->
                    PropertyDef(annotation.name,
                                annotation.description,
                                propertyType,
                                isMultiValued(kprop),
                                annotation.exampleValues)
                PropertyType.NESTED -> {
                    val nestedClass = toClass(kprop.returnType)
                    PropertyDef(annotation.name,
                                annotation.description,
                                PropertyType.NESTED,
                                isMultiValued(kprop),
                                annotation.exampleValues,
                                toPropertyDefs(nestedClass),
                                toAssociationDefs(nestedClass))
                }
                else -> {
                    val realType = guessContainedType(kprop.returnType)
                    logger.warn("Type $realType of property '${kprop.name}' " +
                                        "in class ${kclass.qualifiedName} is not supported")
                    null
                }
            }
        }
    }

    /**
     * Extract all association definitions out of properties annotated with @AssociationDefinition.
     *
     * @param kclass Class.
     * @return List of association definitions.
     */
    fun toAssociationDefs(kclass: KClass<*>): Set<AssociationDef> {
        return kclass.memberProperties
                .mapNotNull { toAssociationDef(kclass, it) }
                .toSet()
    }

    /**
     * Extract association definition out of a property annotated with @AssociationDefinition.
     *
     * @param kclass Class.
     * @param kprop Property.
     * @return Association definition or null.
     */
    private fun toAssociationDef(kclass: KClass<*>, kprop: KProperty<*>): AssociationDef? {
        return kprop.findAnnotation<AssociationDefinition>()?.let { annotation ->
            val realType = guessContainedType(kprop.returnType)

            // only strings are supported
            if (!isNullableString(realType)) {
                logger.warn("Type $realType of association '${kprop.name}' " +
                                    "in class ${kclass.qualifiedName} is not supported"
                )
                return@let null
            }

            return@let toResourceDefRef(annotation.targetClass)?.let { ref ->
                AssociationDef(annotation.name,
                               annotation.description,
                               ref,
                               isMultiValued(kprop))
            }
        }
    }

    /**
     * Guess the property type of a class property.
     *
     * @param kprop Class property.
     * @return Property type or null.
     */
    fun guessPropertyType(kprop: KProperty<*>): PropertyType? {
        return guessPropertyType(kprop.returnType)
    }

    /**
     * Guess the property type of a type.
     *
     * @param ktype Type.
     * @return Property type or null
     */
    fun guessPropertyType(ktype: KType): PropertyType? {
        return when {
            isMultiValued(ktype) -> guessPropertyType(guessContainedType(ktype))
            isNullableNumber(ktype) -> PropertyType.NUMBER
            isNullableString(ktype) -> PropertyType.STRING
            isNullableBoolean(ktype) -> PropertyType.BOOLEAN
            isNullableDate(ktype) -> PropertyType.DATE
            isNullableKeyValue(ktype) -> PropertyType.KEY_VALUE
            isNullableNestedProperty(ktype) -> PropertyType.NESTED
            else -> null
        }
    }

    /**
     * Check whether a class property is multivalued.
     *
     * @param kprop Class property.
     * @return True if type is multi-valued. False otherwise.
     */
    fun isMultiValued(kprop: KProperty<*>): Boolean {
        return isMultiValued(kprop.returnType)
    }

    /**
     * Check whether a type is multivalued.
     *
     * @param ktype Type.
     * @return True if type is multi-valued. False otherwise.
     */
    fun isMultiValued(ktype: KType): Boolean {
        return isNullableList(ktype) || isNullableSet(ktype)
    }

    /**
     * Check whether a type is a list.
     *
     * @param ktype Type.
     * @return True if type is a list. False otherwise.
     */
    fun isNullableList(ktype: KType): Boolean {
        return ktype.isSubtypeOf(List::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a type is a set.
     *
     * @param ktype Type.
     * @return True if type is a set. False otherwise.
     */
    fun isNullableSet(ktype: KType): Boolean {
        return ktype.isSubtypeOf(Set::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a type is a number.
     *
     * @param ktype Type.
     * @return True if type is a number. False otherwise.
     */
    fun isNullableNumber(ktype: KType): Boolean {
        return ktype.isSubtypeOf(Number::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a type is a string.
     *
     * @param ktype Type.
     * @return True if type is a string. False otherwise.
     */
    fun isString(ktype: KType): Boolean {
        return ktype.isSubtypeOf(String::class.starProjectedType)
    }

    /**
     * Check whether a type is a string.
     *
     * @param ktype Type.
     * @return True if type is a string. False otherwise.
     */
    fun isNullableString(ktype: KType): Boolean {
        return ktype.isSubtypeOf(String::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a type is a boolean.
     *
     * @param ktype Type.
     * @return True if type is a boolean. False otherwise.
     */
    fun isNullableBoolean(ktype: KType): Boolean {
        return ktype.isSubtypeOf(Boolean::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a type is a date.
     *
     * @param ktype Type.
     * @return True if type is a date. False otherwise.
     */
    fun isNullableDate(ktype: KType): Boolean {
        return ktype.isSubtypeOf(Date::class.starProjectedType.withNullability(true)) ||
                ktype.isSubtypeOf(Instant::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a property of a class is a key-value.
     *
     * @param ktype Type.
     * @return True if type is a key-value. False otherwise.
     */
    fun isNullableKeyValue(ktype: KType): Boolean {
        return ktype.isSubtypeOf(KeyValue::class.starProjectedType.withNullability(true))
    }

    /**
     * Check whether a property of a class is a nested property.
     *
     * @param ktype Type.
     * @return True if type is a nested property. False otherwise.
     */
    fun isNullableNestedProperty(ktype: KType): Boolean {
        return (ktype.classifier as KClass<*>).memberProperties.any { it.findAnnotation<PropertyDefinition>() != null }
    }

    /**
     * If a type has generics, return the first type parameter. Return the same type otherwise.
     */
    fun guessContainedType(ktype: KType): KType {
        if (ktype.arguments.isNotEmpty()) {
            return ktype.arguments[0].type ?: ktype
        }
        return ktype
    }

    /**
     * Convert a type to a class.
     *
     * @param ktype Type.
     * @return Class.
     */
    fun toClass(ktype: KType): KClass<*> {
        return guessContainedType(ktype).classifier as KClass<*>
    }
}
