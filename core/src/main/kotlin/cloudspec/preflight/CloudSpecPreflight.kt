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
package cloudspec.preflight

import arrow.core.None
import arrow.core.Some
import cloudspec.ProvidersRegistry
import cloudspec.lang.*
import cloudspec.model.*
import cloudspec.store.ResourceDefStore
import org.apache.tinkerpop.gremlin.process.traversal.Compare
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.Text
import org.slf4j.LoggerFactory

class CloudSpecPreflight(private val providersRegistry: ProvidersRegistry,
                         private val resourceDefStore: ResourceDefStore) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun preflight(plan: PlanDecl) {
        logger.info("Validating CloudSpec input")
        preflightConfig(plan.configs)
        preflightModules(plan.modules)
    }

    private fun preflightModules(modules: List<ModuleDecl>) {
        modules.forEach { preflightModule(it) }
    }

    private fun preflightModule(module: ModuleDecl) {
        logger.debug("Preflight of module {}", module.name)
        preflightConfig(module.configs)
        preflightGroups(module.groups)
    }

    private fun preflightGroups(groups: List<GroupDecl>) {
        groups.forEach { preflightGroup(it) }
    }

    private fun preflightGroup(group: GroupDecl) {
        logger.debug("Preflight of group {}", group.name)
        preflightConfig(group.configs)
        preflightRules(group.rules)
    }

    private fun preflightRules(rules: List<RuleDecl>) {
        rules.forEach { preflightRule(it) }
    }

    private fun preflightRule(rule: RuleDecl) {
        logger.debug("Preflight of rule {}", rule.name)

        preflightConfig(rule.configs)

        ResourceDefRef.fromString(rule.defRef)
            .flatMap {
                resourceDefStore.getResourceDef(it)
            }
            .also { resourceDefOpt ->
                when (resourceDefOpt) {
                    is Some -> {
                        // preflight withs and asserts
                        rule.withs.statements
                            .forEach { preflightStatement(resourceDefOpt.t, it, emptyList()) }
                        rule.asserts.statements
                            .forEach { preflightStatement(resourceDefOpt.t, it, emptyList()) }
                    }
                    else ->
                        throw CloudSpecPreflightException("Resource of type '${rule.defRef}' " +
                                                                  "is not supported.")
                }
            }
    }

    private fun preflightStatement(resourceDef: ResourceDef, statement: Statement, path: List<String>) {
        when (statement) {
            is NestedStatement -> preflightNestedStatement(resourceDef, statement, path)
            is KeyValueStatement -> preflightKeyValueStatement(resourceDef, statement, path)
            is PropertyStatement -> preflightPropertyStatement(resourceDef, statement, path)
            is AssociationStatement -> preflightAssociationStatement(resourceDef, statement, path)
        }
    }

    private fun preflightNestedStatement(resourceDef: ResourceDef, statement: NestedStatement, path: List<String>) {
        val nestedPath = path.plus(statement.propertyName)

        when (resourceDef.propertyByPath(nestedPath)) {
            is Some ->
                statement.statements.forEach {
                    preflightStatement(resourceDef, it, nestedPath)
                }
            else ->
                throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' " +
                                                          "does not define property '${statement.propertyName}'.")
        }
    }

    private fun preflightAssociationStatement(resourceDef: ResourceDef, statement: AssociationStatement, path: List<String>) {
        val associationPath = path.plus(statement.associationName)

        when (val associationOpt = resourceDef.associationByPath(associationPath)) {
            is Some -> {
                val (_, _, resourceDefRef) = associationOpt.t

                when (val associatedResourceDefOpt = resourceDefStore.getResourceDef(resourceDefRef)) {
                    is Some ->
                        statement.statements.forEach {
                            preflightStatement(associatedResourceDefOpt.t, it, emptyList())
                        }
                    else ->
                        throw CloudSpecPreflightException("Associated resource of type '${resourceDefRef}' " +
                                                                  "is not supported.")
                }
            }
            else ->
                throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' does not define association " +
                                                          "'${statement.associationName}'.")
        }
    }

    private fun preflightPropertyStatement(resourceDef: ResourceDef, statement: PropertyStatement, path: List<String>) {
        val propertyPath = path.plus(statement.propertyName)

        when (val propertyDefOpt = resourceDef.propertyByPath(propertyPath)) {
            is Some -> {
                if (isNumberOnlyPredicate(statement.predicate) && !isNumberProperty(propertyDefOpt.t)) {
                    throw CloudSpecPreflightException("Predicate ${statement.predicate} is only supported " +
                                                              "on number properties (i.e. integer and double), " +
                                                              "and property '${propertyPath.joinToString(".")}' " +
                                                              "is a '${propertyDefOpt.t.propertyType}'")
                }
            }
            else ->
                throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' does not define property " +
                                                          "'${propertyPath.joinToString(".")}'.")
        }
    }

    private fun preflightKeyValueStatement(resourceDef: ResourceDef, statement: KeyValueStatement, path: List<String>) {
        val propertyPath = path.plus(statement.propertyName)

        if (resourceDef.propertyByPath(propertyPath) is None) {
            throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' " +
                                                      "does not define property '${statement.propertyName}'.")
        }
    }

    private fun isNumberOnlyPredicate(predicate: P<*>): Boolean {
        val biPredicate = predicate.biPredicate
        return biPredicate == Compare.lt ||
                biPredicate == Compare.lte ||
                biPredicate == Compare.gt ||
                biPredicate == Compare.gte
    }

    private fun isStringOnlyPredicate(predicate: P<*>): Boolean {
        val biPredicate = predicate.biPredicate
        return biPredicate == Text.startingWith ||
                biPredicate == Text.notStartingWith ||
                biPredicate == Text.endingWith ||
                biPredicate == Text.notEndingWith ||
                biPredicate == Text.containing ||
                biPredicate == Text.notContaining
    }

    private fun isNumberProperty(propertyDef: PropertyDef): Boolean {
        return propertyDef.propertyType == PropertyType.NUMBER
    }

    private fun isStringProperty(propertyDef: PropertyDef): Boolean {
        return propertyDef.propertyType == PropertyType.STRING
    }

    private fun preflightConfig(config: List<ConfigDecl>) {
        config.forEach { c ->
            when (val configRefOpt = ConfigRef.fromString(c.configRef)) {
                is Some<ConfigRef> ->
                    if (!providersRegistry.getProvider(configRefOpt.t.provider)
                                .exists {
                                    it.configDefs.any { configDef -> configRefOpt.t == configDef.ref }
                                }
                    ) {
                        throw CloudSpecPreflightException("Config '${c.configRef}' is not supported.")
                    }
                else ->
                    throw CloudSpecPreflightException("Config ref '${c.configRef}' is malformed.")
            }

        }
    }
}
