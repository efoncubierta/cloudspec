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
import cloudspec.lang.*
import cloudspec.model.Resource
import cloudspec.model.ResourceDefRef
import cloudspec.model.ResourceDefRef.Companion.fromString
import cloudspec.model.ResourceRef
import cloudspec.model.Resources
import cloudspec.store.ResourceStore
import org.slf4j.LoggerFactory

class ResourceLoader(private val providersRegistry: ProvidersRegistry,
                     private val resourceStore: ResourceStore) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val loadedResourceDefs = mutableSetOf<ResourceDefRef>()

    fun load(spec: CloudSpec) {
        logger.info("Loading resources required to run this test")
        spec.groups
                .flatMap { (_, rules) -> rules }
                .forEach { loadFromRule(it) }
    }

    private fun loadFromRule(ruleExpr: RuleExpr) {
        when (val resourceDefRefOpt = fromString(ruleExpr.resourceDefRef)) {
            is Some -> {
                // Load all resource definitions to the plan
                getAllResources(resourceDefRefOpt.t)
                        .forEach { resource ->
                            // load dependent resources from each statement
                            ruleExpr.withExpr
                                    .statements
                                    .forEach {
                                        loadFromStatement(resource, it, emptyList())
                                    }

                            // load dependent resources from each statement
                            ruleExpr.assertExpr
                                    .statements
                                    .forEach {
                                        loadFromStatement(resource, it, emptyList())
                                    }
                        }
            }
            else -> logger.error("Malformed resource definition '${ruleExpr.resourceDefRef}'. Ignoring it.")
        }
    }

    private fun loadFromStatement(resource: Resource, statement: Statement, path: List<String>) {
        when (statement) {
            is NestedStatement -> {
                statement.statements.forEach {
                    loadFromStatement(resource, it, path.plus(statement.propertyName))
                }
            }
            is AssociationStatement -> {
                loadFromAssociationStatement(resource, statement, path)
            }
            else -> {
            }
        }
    }

    private fun loadFromAssociationStatement(resource: Resource, statement: AssociationStatement, path: List<String>) {
        val associationPath = path.plus(statement.associationName)

        logger.debug("Loading resources for association '${statement.associationName}' " +
                             "in resource '${resource.ref}'")

        when (val associationOpt = resource.getAssociationByPath(associationPath)) {
            is Some -> {
                // load associated resource
                val association = associationOpt.t
                when (val associatedResourceOpt = getResource(association.resourceRef)) {
                    is Some ->
                        // load resources from each sub statement
                        statement.statements.forEach {
                            loadFromStatement(associatedResourceOpt.t, it, emptyList())
                        }
                    else ->
                        logger.error("Associated resource '${statement.associationName}' " +
                                             "in resource '${resource.ref}' " +
                                             "does not exist. Ignoring it.")

                }
            }
            else ->
                logger.error("Association '${statement.associationName}' does not exist " +
                                     "in resource '${resource.ref}'." +
                                     "Ignoring it.")
        }
    }

    private fun getAllResources(resourceDefRef: ResourceDefRef): Resources {
        return if (!loadedResourceDefs.contains(resourceDefRef)) {
            logger.debug("Loading all resources of type '{}'", resourceDefRef)

            loadedResourceDefs.add(resourceDefRef)

            providersRegistry.getProvider(resourceDefRef.providerName)
                    .map { provider ->
                        provider.resourcesByRef(resourceDefRef)
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

    private fun getResource(ref: ResourceRef): Option<Resource> {
        // TODO load incomplete resource from store
//        Optional<Resource> resourceOpt = resourceStore.getResource(resourceDefRef, resourceId);
//        if (!resourceOpt.isPresent()) {
        logger.debug("Loading resource of type '${ref}'")

        return providersRegistry.getProvider(ref.defRef)
                .flatMap { provider ->
                    provider.resource(ref)
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
