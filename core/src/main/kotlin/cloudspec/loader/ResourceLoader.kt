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

import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.syntax.collections.flatten
import cloudspec.ProvidersRegistry
import cloudspec.annotation.ResourceReflectionUtil
import cloudspec.lang.AssociationStatement
import cloudspec.lang.NestedStatement
import cloudspec.lang.Statement
import cloudspec.model.*
import cloudspec.store.ResourceStore
import org.slf4j.LoggerFactory

class ResourceLoader(private val providersRegistry: ProvidersRegistry,
                     private val resourceStore: ResourceStore) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val loadedResourceDefs = mutableSetOf<ResourceDefRef>()

    fun load(plan: Plan) {
        logger.info("Loading resources required to run this test")

        loadFromModules(plan.modules)
        loadFromGroups(plan.groups)
        loadFromRules(plan.rules)
    }

    private fun loadFromModules(modules: List<Module>) {
        modules.forEach { loadFromModule(it) }
    }

    private fun loadFromModule(module: Module) {
        loadFromModules(module.modules)
        loadFromGroups(module.groups)
        loadFromRules(module.rules)
    }

    private fun loadFromGroups(groups: List<Group>) {
        groups.forEach { loadFromGroup(it) }
    }

    private fun loadFromGroup(group: Group) {
        loadFromRules(group.rules)
    }

    private fun loadFromRules(rules: List<Rule>) {
        rules.forEach { loadFromRule(it) }
    }

    private fun loadFromRule(rule: Rule) {
        // Load all resource definitions to the plan
        getAllResources(rule.configs, rule.defRef)
            .forEach { resource ->
                // load dependent resources from each statement
                rule.filters
                    .forEach {
                        loadFromStatement(rule.configs, resource, it, emptyList())
                    }

                // load dependent resources from each statement
                rule.validations
                    .forEach {
                        loadFromStatement(rule.configs, resource, it, emptyList())
                    }
            }
    }

    private fun loadFromStatement(config: ConfigValues, resource: Resource, statement: Statement, path: List<String>) {
        when (statement) {
            is NestedStatement -> {
                statement.statements.forEach {
                    loadFromStatement(config, resource, it, path.plus(statement.propertyName))
                }
            }
            is AssociationStatement -> {
                loadFromAssociationStatement(config, resource, statement, path)
            }
            else -> {
            }
        }
    }

    private fun loadFromAssociationStatement(config: ConfigValues,
                                             resource: Resource,
                                             statement: AssociationStatement,
                                             path: List<String>) {
        val associationPath = path.plus(statement.associationName)

        logger.debug("Loading resources for association '${statement.associationName}' " +
                             "in resource '${resource.ref}'")

        when (val associationOpt = resource.getAssociationByPath(associationPath)) {
            is Some -> {
                // load associated resource
                val association = associationOpt.t
                when (val associatedResourceOpt = getResource(config, association.resourceRef)) {
                    is Some ->
                        // load resources from each sub statement
                        statement.statements.forEach {
                            loadFromStatement(config, associatedResourceOpt.t, it, emptyList())
                        }
                    else ->
                        logger.error("Associated resource '${association.resourceRef}' " +
                                             "from resource '${resource.ref}' (name = '${statement.associationName}') " +
                                             "does not exist. Ignoring it.")

                }
            }
            else ->
                logger.error("Association '${statement.associationName}' does not exist " +
                                     "in resource '${resource.ref}'." +
                                     "Ignoring it.")
        }
    }

    private fun getAllResources(config: ConfigValues, resourceDefRef: ResourceDefRef): Resources {
        return if (!loadedResourceDefs.contains(resourceDefRef)) {
            logger.debug("Loading all resources of type '{}'", resourceDefRef)

            loadedResourceDefs.add(resourceDefRef)

            providersRegistry.getProvider(resourceDefRef.providerName)
                .map { provider ->
                    provider.resourcesByRef(config, resourceDefRef)
                        .map { ResourceReflectionUtil.toResource(it) }
                        .flatten()
                        .onEach { (ref, properties, associations) ->
                            resourceStore.saveResource(ref,
                                                       properties,
                                                       associations)
                        }
                }.getOrElse {
                    emptyList()
                }
        } else {
            resourceStore.resourcesByDefinition(resourceDefRef)
        }
    }

    private fun getResource(config: ConfigValues, ref: ResourceRef): Option<Resource> {
        // TODO load incomplete resource from store
//        Optional<Resource> resourceOpt = resourceStore.getResource(resourceDefRef, resourceId);
//        if (!resourceOpt.isPresent()) {
        logger.debug("Loading resource of type '${ref}'")

        return providersRegistry.getProvider(ref.defRef)
            .flatMap { provider ->
                provider.resource(config, ref)
                    .flatMap { ResourceReflectionUtil.toResource(it) }
                    .also {
                        if (it is Some) {
                            resourceStore.saveResource(ref,
                                                       it.t.properties,
                                                       it.t.associations)
                        }
                    }
            }

        //        }
//        return resourceOpt;
    }

}
