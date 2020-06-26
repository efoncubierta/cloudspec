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
package cloudspec.util

import cloudspec.model.*
import com.github.javafaker.Faker
import java.time.Instant
import java.util.concurrent.TimeUnit

object ModelGenerator {
    private val faker = Faker()

    fun randomPropertyType(excludeNested: Boolean = false): PropertyType {
        return listOf(
                PropertyType.NUMBER,
                PropertyType.STRING,
                PropertyType.BOOLEAN,
                PropertyType.DATE,
                PropertyType.KEY_VALUE,
                PropertyType.NESTED
        )[faker.random().nextInt(0, if (excludeNested) 4 else 5)]
    }

    fun randomPropertyDefs(n: Int, excludeNested: Boolean = false): Set<PropertyDef> {
        return (0..n).map { randomPropertyDef(excludeNested) }.toSet()
    }

    fun randomPropertyDef(excludeNested: Boolean = false): PropertyDef {
        return when (val propertyType = randomPropertyType(excludeNested)) {
            PropertyType.NESTED -> PropertyDef(
                    randomName(),
                    randomDescription(),
                    PropertyType.NESTED,
                    faker.random().nextBoolean(),
                    faker.lorem().word(),
                    randomPropertyDefs(3, true),
                    randomAssociationDefs(3)
            )
            PropertyType.KEY_VALUE -> PropertyDef(
                    randomName(),
                    randomDescription(),
                    propertyType,
                    faker.random().nextBoolean(),
                    faker.lorem().word()
            )
            else -> PropertyDef(
                    randomName(),
                    randomDescription(),
                    propertyType,
                    faker.random().nextBoolean(),
                    faker.lorem().word()
            )
        }
    }

    fun randomResourceDef(resourceDefRef: ResourceDefRef = randomResourceDefRef()): ResourceDef {
        return ResourceDef(
                resourceDefRef,
                randomDescription(),
                randomPropertyDefs(faker.random().nextInt(5, 10)),
                randomAssociationDefs(faker.random().nextInt(2, 5))
        )
    }

    fun randomPropertyValues(n: Int): List<Any> {
        return (0..n).map { randomPropertyValue() }
    }

    fun randomPropertyValues(n: Int, propertyDef: PropertyDef): List<Any> {
        return (0..n).map { randomPropertyValue(propertyDef) }
    }

    fun randomPropertyValue(propertyDef: PropertyDef = randomPropertyDef()): Any {
        return when (propertyDef.propertyType) {
            PropertyType.NESTED -> NestedPropertyValue(
                    propertyDef.properties
                        .map { obj: PropertyDef -> randomProperty(obj) }
                        .toSet(),
                    emptySet()
            )
            PropertyType.KEY_VALUE -> KeyValue(faker.lorem().word(), faker.lorem().word())
            PropertyType.NUMBER -> if (faker.random().nextBoolean()) faker.random().nextInt(Int.MAX_VALUE) else faker.random().nextDouble()
            PropertyType.BOOLEAN -> faker.random().nextBoolean()
            PropertyType.DATE -> faker.date().past(1000, TimeUnit.DAYS).toInstant()
            PropertyType.STRING -> faker.lorem().sentence()
            else -> faker.lorem().sentence()
        }
    }

    fun randomProperties(n: Int): Properties {
        return randomProperties(randomPropertyDefs(n))
    }

    fun randomProperties(propertyDefs: Set<PropertyDef>): Properties {
        return propertyDefs.map { obj -> randomProperty(obj) }.toSet()
    }

    fun randomProperty(propertyDef: PropertyDef = randomPropertyDef()): Property<*> {
        return when (propertyDef.propertyType) {
            PropertyType.NUMBER -> NumberProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as Number)
            )
            PropertyType.BOOLEAN -> BooleanProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as Boolean)
            )
            PropertyType.DATE -> InstantProperty(
                    propertyDef.name,
                    randomPropertyValue(propertyDef) as Instant
            )
            PropertyType.KEY_VALUE -> KeyValueProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as KeyValue)
            )
            PropertyType.NESTED -> NestedProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as NestedPropertyValue)
            )
            PropertyType.STRING -> StringProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as String)
            )
            else -> StringProperty(
                    propertyDef.name,
                    (randomPropertyValue(propertyDef) as String)
            )
        }
    }

    fun randomAssociations(n: Int): Associations {
        return randomAssociations(randomAssociationDefs(n))
    }

    fun randomAssociations(associationDefs: Set<AssociationDef>): Associations {
        return associationDefs.map { obj -> randomAssociation(obj) }.toSet()
    }

    fun randomAssociationDefs(n: Int): Set<AssociationDef> {
        return (0..n).map { randomAssociationDef() }.toSet()
    }

    fun randomAssociation(associationDef: AssociationDef = randomAssociationDef()): Association {
        return Association(
                associationDef.name,
                randomResourceRef(associationDef.defRef)
        )
    }

    fun randomAssociationDef(): AssociationDef {
        return AssociationDef(
                randomName(),
                randomDescription(),
                randomResourceDefRef(),
                faker.random().nextBoolean()
        )
    }

    fun randomResourceId(): String {
        return faker.idNumber().valid()
    }

    fun randomName(): String {
        return faker.lorem().characters(5, 10).toLowerCase()
    }

    fun randomDescription(): String {
        return faker.lorem().sentence()
    }

    fun randomResourceDefRef(): ResourceDefRef {
        return ResourceDefRef(
                randomName(),
                randomName(),
                randomName()
        )
    }

    fun randomResourceRef(defRef: ResourceDefRef? = null): ResourceRef {
        return ResourceRef(defRef ?: randomResourceDefRef(), randomResourceId())
    }

    fun randomResource(resourceDef: ResourceDef = randomResourceDef()): Resource {
        return Resource(
                randomResourceRef(resourceDef.ref),
                randomProperties(resourceDef.properties),
                randomAssociations(resourceDef.associations)
        )
    }

    fun randomConfigRef(): ConfigRef {
        return ConfigRef(
                randomName(),
                randomName()
        )
    }

    fun randomConfigValueType(): SetValueType {
        return SetValueType.values()[faker.random().nextInt(0, SetValueType.values().size - 1)]
    }

    fun randomConfigDef(): ConfigDef {
        return ConfigDef(randomConfigRef(),
                         randomDescription(),
                         randomConfigValueType(),
                         faker.random().nextBoolean())
    }

    fun randomConfigDefs(n: Int): Set<ConfigDef> {
        return (0..n).map { randomConfigDef() }.toSet()
    }

    fun randomConfigValue(configDef: ConfigDef = randomConfigDef()): SetValue<*> {
        return when (configDef.type) {
            SetValueType.NUMBER -> NumberSetValue(
                    configDef.ref,
                    if (faker.random().nextBoolean()) faker.random().nextInt(Int.MAX_VALUE) else faker.random().nextDouble()
            )
            SetValueType.BOOLEAN -> BooleanSetValue(
                    configDef.ref,
                    faker.random().nextBoolean()
            )
            SetValueType.STRING -> StringSetValue(
                    configDef.ref,
                    faker.lorem().sentence()
            )
        }
    }

    fun randomConfigValues(n: Int): SetValues {
        return (0..n).map { randomConfigValue(randomConfigDef()) }
    }
}
