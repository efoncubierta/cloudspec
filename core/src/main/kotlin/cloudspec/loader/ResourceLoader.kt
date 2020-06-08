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

import cloudspec.ProvidersRegistry
import cloudspec.annotation.ResourceReflectionUtil
import cloudspec.lang.*
import cloudspec.model.Resource
import cloudspec.model.ResourceDefRef
import cloudspec.model.ResourceDefRef.Companion.fromString
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
        val resourceDefRef = fromString(ruleExpr.resourceDefRef)

        if (resourceDefRef == null) {
            logger.error("Malformed resource definition '${ruleExpr.resourceDefRef}'. Ignoring it.")
            return
        }

        // Load all resource definitions to the plan
        getAllResources(resourceDefRef)
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
                             "in resource '${resource.resourceDefRef}' with id '${resource.resourceId}'")

        val association = resource.getAssociationByPath(associationPath)
        if (association == null) {
            logger.error("Association '${statement.associationName}' does not exist " +
                                 "in resource '${resource.resourceDefRef}' with id '${resource.resourceId}'. " +
                                 "Ignoring it.")
            return
        }

        // load associated resource
        val associatedResource = getResourceById(association.resourceDefRef, association.resourceId)
        if (associatedResource == null) {
            logger.error("Associated resource '${statement.associationName}' " +
                                 "in resource '${resource.resourceDefRef}' " +
                                 "with id '${resource.resourceId}' does not exist. Ignoring it.")
            return
        }

        // load resources from each sub statement
        statement.statements.forEach {
            loadFromStatement(associatedResource, it, emptyList())
        }
    }

    private fun getAllResources(resourceDefRef: ResourceDefRef): List<Resource> {
        return if (!loadedResourceDefs.contains(resourceDefRef)) {
            logger.debug("Loading all resources of type '{}'", resourceDefRef)

            loadedResourceDefs.add(resourceDefRef)

            providersRegistry.getProvider(resourceDefRef.providerName)
                    ?.resourcesByRef(resourceDefRef)
                    ?.mapNotNull { ResourceReflectionUtil.toResource(it) }
                    ?.onEach { (_, resourceId, properties, associations) ->
                        resourceStore.saveResource(resourceDefRef,
                                                   resourceId,
                                                   properties,
                                                   associations)
                    } ?: emptyList()
        } else {
            resourceStore.resourcesByDefinition(resourceDefRef)
        }
    }

    private fun getResourceById(resourceDefRef: ResourceDefRef, resourceId: String): Resource? {
        // TODO load incomplete resource from store
//        Optional<Resource> resourceOpt = resourceStore.getResource(resourceDefRef, resourceId);
//        if (!resourceOpt.isPresent()) {
        logger.debug("Loading resource of type '${resourceDefRef}' with id '${resourceId}'")

        return providersRegistry.getProvider(resourceDefRef.providerName)
                ?.resourceById(resourceDefRef, resourceId)
                ?.let { ResourceReflectionUtil.toResource(it) }
                ?.also { (_, _, properties, associations) ->
                    resourceStore.saveResource(resourceDefRef,
                                               resourceId,
                                               properties,
                                               associations)
                }
        //        }
//        return resourceOpt;
    }

}
