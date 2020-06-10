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
import arrow.core.extensions.fx
import arrow.core.none
import arrow.core.toOption
import arrow.syntax.collections.flatten
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
     * @return Optional resource definition.
     */
    fun toResourceDef(kclass: KClass<*>): Option<ResourceDef> {
        return if (isValid(kclass)) {
            Option.fx {
                val (ref) = toResourceDefRef(kclass)
                val (description) = toResourceDescription(kclass)

                ResourceDef(ref,
                            description,
                            toPropertyDefs(kclass),
                            toAssociationDefs(kclass))
            }
        } else {
            return none()
        }
    }

    /**
     * Extract resource definition reference out of a class annotated with @ResourceDefinition.
     *
     * @param kclass Class.
     * @return Optional resource definition reference.
     */
    fun toResourceDefRef(kclass: KClass<*>): Option<ResourceDefRef> {
        return kclass.findAnnotation<ResourceDefinition>().toOption().map { annotation ->
            if (logger.isDebugEnabled) {
                logger.debug("Obtained " +
                                     "provider='${annotation.provider}', " +
                                     "group='${annotation.group}', " +
                                     "name='${annotation.name}' " +
                                     "from class ${kclass.qualifiedName}")
            }

            ResourceDefRef(annotation.provider, annotation.group, annotation.name)
        }
    }

    /**
     * Extract resource description out of a class annotated with @ResourceDefinition.
     *
     * @param kclass Class.
     * @return Optional resource description.
     */
    fun toResourceDescription(kclass: KClass<*>): Option<String> {
        return kclass.findAnnotation<ResourceDefinition>().toOption().map { annotation ->
            if (logger.isDebugEnabled) {
                logger.debug("Obtained description='${annotation.description}' " +
                                     "from class ${kclass.qualifiedName}")
            }

            annotation.description
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
                .map { toPropertyDef(it) }
                .flatten()
                .toSet()
    }

    /**
     * Extract property definition out of a class' property annotated with @PropertyDefinition.
     *
     * @param kprop Property.
     * @return Optional property definition.
     */
    private fun toPropertyDef(kprop: KProperty<*>): Option<PropertyDef> {
        return Option.fx {
            val (annotation) = kprop.findAnnotation<PropertyDefinition>().toOption()
            val (propertyType) = guessPropertyType(kprop)

            when (propertyType) {
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
                .map { toAssociationDef(kclass, it) }
                .flatten()
                .toSet()
    }

    /**
     * Extract association definition out of a property annotated with @AssociationDefinition.
     *
     * @param kclass Class.
     * @param kprop Property.
     * @return Optional association definition.
     */
    private fun toAssociationDef(kclass: KClass<*>, kprop: KProperty<*>): Option<AssociationDef> {
        val realType = guessContainedType(kprop.returnType)

        // only strings are supported
        if (!isNullableString(realType)) {
            logger.warn("Type $realType of association '${kprop.name}' " +
                                "in class ${kclass.qualifiedName} is not supported")
            return none<AssociationDef>()
        }

        return Option.fx {
            val (annotation) = kprop.findAnnotation<AssociationDefinition>().toOption()
            val (ref) = toResourceDefRef(annotation.targetClass)
            AssociationDef(annotation.name,
                           annotation.description,
                           ref,
                           isMultiValued(kprop))
        }
    }

    /**
     * Guess the property type of a class property.
     *
     * @param kprop Class property.
     * @return Optional property type.
     */
    fun guessPropertyType(kprop: KProperty<*>): Option<PropertyType> {
        return guessPropertyType(kprop.returnType)
    }

    /**
     * Guess the property type of a type.
     *
     * @param ktype Type.
     * @return Optional property type.
     */
    fun guessPropertyType(ktype: KType): Option<PropertyType> {
        return when {
            isMultiValued(ktype) -> guessPropertyType(guessContainedType(ktype))
            isNullableNumber(ktype) -> PropertyType.NUMBER.toOption()
            isNullableString(ktype) -> PropertyType.STRING.toOption()
            isNullableBoolean(ktype) -> PropertyType.BOOLEAN.toOption()
            isNullableDate(ktype) -> PropertyType.DATE.toOption()
            isNullableKeyValue(ktype) -> PropertyType.KEY_VALUE.toOption()
            isNullableNestedProperty(ktype) -> PropertyType.NESTED.toOption()
            else -> null.toOption()
        }
    }

    /**
     * Check whether the class is valid.
     */
    fun isValid(kclass: KClass<*>): Boolean {
        return hasResourceAnnotation(kclass) && hasIdAnnotation(kclass)
    }

    /**
     * Check whether the class has the @ResourceDefinition annotation.
     */
    fun hasResourceAnnotation(kclass: KClass<*>): Boolean {
        return kclass.findAnnotation<ResourceDefinition>() != null
    }

    /**
     * Check whether the class has only one @IdDefinition
     */
    fun hasIdAnnotation(kclass: KClass<*>): Boolean {
        val kprops = kclass.memberProperties
                .filter {
                    it.findAnnotation<IdDefinition>() != null
                }
                .filter {
                    if (it.returnType.isMarkedNullable) {
                        logger.error("Id '${it.name}' property of class '${kclass.qualifiedName}' cannot be nullable.")
                        return@filter false
                    }
                    return@filter true
                }

        return when {
            kprops.isEmpty() ->
                false.also {
                    logger.error("Class '${kclass.qualifiedName}' does not have any ID property")
                }
            kprops.size > 1 ->
                false.also {
                    logger.error("Class '${kclass.qualifiedName}' cannot have more than one ID property")
                }
            else -> true
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
