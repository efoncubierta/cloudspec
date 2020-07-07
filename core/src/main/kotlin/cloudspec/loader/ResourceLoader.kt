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
package cloudspec.loader

import arrow.core.Either
import arrow.core.Some
import arrow.core.extensions.option.monad.flatten
import arrow.core.extensions.sequence
import arrow.core.getOrElse
import arrow.core.none
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.applicative.applicative
import arrow.fx.extensions.io.concurrent.parSequence
import cloudspec.ProvidersRegistry
import cloudspec.lang.AssociationStatement
import cloudspec.lang.NestedStatement
import cloudspec.lang.Statement
import cloudspec.model.*
import cloudspec.store.ResourceStore
import org.slf4j.LoggerFactory

class ResourceLoader(private val providersRegistry: ProvidersRegistry,
                     private val resourceStore: ResourceStore) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val loadedResources = mutableMapOf<ResourceRef, IO<ResourceO>>()
    private val loadedResourceDefs = mutableMapOf<ResourceDefRef, IO<Resources>>()

    fun load(module: Module) {
        logger.info("Loading resources required to run this test")

        loadFromModule(module).parSequence().unsafeRunSync()
    }

    private fun loadFromModules(modules: List<Module>): List<IO<*>> {
        return modules.flatMap { loadFromModule(it) }
    }

    private fun loadFromModule(module: Module): List<IO<*>> {
        return loadFromModules(module.modules).plus(loadFromRules(module.rules))
    }

    private fun loadFromRules(rules: List<Rule>): List<IO<*>> {
        return rules.map { loadFromRule(it) }
    }

    private fun loadFromRule(rule: Rule): IO<*> {
        return IO.fx {
            // Load all resource definitions
            val (resources) = getAllResources(rule.configs, rule.defRef)
            val (statementsIO) = resources.flatMap { resource ->
                // load dependent resources from each (filter and validation) statement
                rule.filters
                    .plus(rule.validations)
                    .map { loadFromStatement(rule.configs, resource, it, emptyList()) }
            }
            val (x) = statementsIO.parSequence()
            x
        }
    }

    private fun loadFromStatement(sets: SetValues, resource: Resource, statement: Statement, path: List<String>): List<IO<*>> {
        return when (statement) {
            is NestedStatement -> {
                statement.statements.flatMap {
                    loadFromStatement(sets, resource, it, path.plus(statement.propertyName))
                }
            }
            is AssociationStatement -> {
                listOf(loadFromAssociationStatement(sets, resource, statement, path))
            }
            else -> emptyList()
        }
    }

    private fun loadFromAssociationStatement(sets: SetValues,
                                             resource: Resource,
                                             statement: AssociationStatement,
                                             path: List<String>): IO<*> {
        logger.debug("Loading resources for association '${statement.associationName}' " +
                             "in resource '${resource.ref}'")

        val associationPath = path.plus(statement.associationName)

        return IO.fx {
            when (val associationOpt = resource.getAssociationByPath(associationPath)) {
                is Some -> {
                    // load associated resource
                    val association = associationOpt.t

                    val (associatedResourceOpt) = getResource(sets, association.resourceRef)
                    val (x) = when (associatedResourceOpt) {
                        is Some -> {
                            // load resources from each sub statement
                            statement.statements.flatMap {
                                loadFromStatement(sets, associatedResourceOpt.t, it, emptyList())
                            }
                        }
                        else -> {
                            logger.error("Associated resource '${association.resourceRef}' " +
                                                 "from resource '${resource.ref}' (name = '${statement.associationName}') " +
                                                 "does not exist. Ignoring it.")
                            emptyList()
                        }
                    }.parSequence()
                    x
                }
                else -> {
                    logger.error("Association '${statement.associationName}' does not exist " +
                                         "in resource '${resource.ref}'." +
                                         "Ignoring it.")
                }
            }
        }
    }

    @Synchronized
    private fun getAllResources(sets: SetValues, resourceDefRef: ResourceDefRef): IO<Resources> {
        if (!loadedResourceDefs.containsKey(resourceDefRef)) {
            logger.debug("Loading all resources of type '{}'", resourceDefRef)

            loadedResourceDefs[resourceDefRef] = IO.fx {
                val (resources) = providersRegistry.getProvider(resourceDefRef.providerName)
                    .map { it.resourcesByDef(sets, resourceDefRef) }
                    .sequence(IO.applicative())
                    .map { it.getOrElse { emptyList() } }

                resources.onEach { (ref, properties, associations) ->
                    resourceStore.saveResource(ref, properties, associations)
                }
            }.attempt().map {
                when (it) {
                    is Either.Left -> {
                        logger.error(it.a.message)
                        emptyList()
                    }
                    is Either.Right -> it.b
                }
            }
        }

        return loadedResourceDefs[resourceDefRef]!!
    }

    @Synchronized
    private fun getResource(sets: SetValues, ref: ResourceRef): IO<ResourceO> {
        if (!loadedResources.containsKey(ref)) {
            logger.debug("Loading resource of type '${ref}'")

            loadedResources[ref] = IO.fx {
                val (resource) = providersRegistry.getProvider(ref.defRef)
                    .map { provider ->
                        provider.resource(sets, ref)
                    }
                    .sequence(IO.applicative())
                    .map { it.flatten() }
                resource.also {
                    if (it is Some) {
                        resourceStore.saveResource(ref, it.t.properties, it.t.associations)
                    }
                }
            }.attempt().map {
                when (it) {
                    is Either.Left -> {
                        logger.error(it.a.message)
                        none()
                    }
                    is Either.Right -> it.b
                }
            }
        }

        return loadedResources[ref]!!
    }
}