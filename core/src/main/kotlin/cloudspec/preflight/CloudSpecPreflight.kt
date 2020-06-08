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

import cloudspec.lang.*
import cloudspec.model.PropertyDef
import cloudspec.model.PropertyType
import cloudspec.model.ResourceDef
import cloudspec.model.ResourceDefRef.Companion.fromString
import cloudspec.store.ResourceDefStore
import org.apache.tinkerpop.gremlin.process.traversal.Compare
import org.apache.tinkerpop.gremlin.process.traversal.P
import org.apache.tinkerpop.gremlin.process.traversal.Text
import org.slf4j.LoggerFactory
import java.util.*

class CloudSpecPreflight(private val resourceDefStore: ResourceDefStore) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun preflight(spec: CloudSpec) {
        logger.info("Validating CloudSpec input")
        preflightGroups(spec.groups)
    }

    private fun preflightGroups(groups: List<GroupExpr>) {
        groups.forEach { preflightGroup(it) }
    }

    private fun preflightGroup(group: GroupExpr) {
        logger.debug("Preflight of group {}", group.name)
        preflightRules(group.rules)
    }

    private fun preflightRules(rules: List<RuleExpr>) {
        rules.forEach { preflightRule(it) }
    }

    private fun preflightRule(rule: RuleExpr) {
        logger.debug("Preflight of rule {}", rule.name)

        val resourceDefRef = fromString(rule.resourceDefRef)
                ?: throw CloudSpecPreflightException("Malformed resource definition reference '${rule.resourceDefRef}'")

        // lookup resource definition
        val resourceDef = resourceDefStore.getResourceDef(resourceDefRef)
                ?: throw CloudSpecPreflightException("Resource of type '${rule.resourceDefRef}' is not supported.")

        // preflight withs and asserts
        rule.withExpr.statements
                .forEach { preflightStatement(resourceDef, it, emptyList()) }
        rule.assertExpr.statements
                .forEach { preflightStatement(resourceDef, it, emptyList()) }
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

        if (resourceDef.propertyByPath(nestedPath) == null) {
            throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' " +
                                                      "does not define property '${statement.propertyName}'.")
        }

        statement.statements
                .forEach { preflightStatement(resourceDef, it, nestedPath) }
    }

    private fun preflightAssociationStatement(resourceDef: ResourceDef, statement: AssociationStatement, path: List<String>) {
        val associationPath = path.plus(statement.associationName)

        val (_, _, resourceDefRef) = resourceDef.associationByPath(associationPath)
                ?: throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' does not define association " +
                                                             "'${statement.associationName}'.")

        val associatedResourceDef = resourceDefStore.getResourceDef(resourceDefRef)
                ?: throw CloudSpecPreflightException("Associated resource of type '${resourceDefRef}' " +
                                                             "is not supported.")

        statement.statements
                .forEach { preflightStatement(associatedResourceDef, it, ArrayList()) }
    }

    private fun preflightPropertyStatement(resourceDef: ResourceDef, statement: PropertyStatement, path: List<String>) {
        val propertyPath = path.plus(statement.propertyName)

        val propertyDef = resourceDef.propertyByPath(propertyPath)
                ?: throw CloudSpecPreflightException("Resource type '${resourceDef.ref}' does not define property " +
                                                             "'${propertyPath.joinToString(".")}'.")

        if (isNumberOnlyPredicate(statement.predicate) && !isNumberProperty(propertyDef)) {
            throw CloudSpecPreflightException("Predicate ${statement.predicate} is only supported " +
                                                      "on number properties (i.e. integer and double), " +
                                                      "and property '${propertyPath.joinToString(".")}' " +
                                                      "is a '${propertyDef.propertyType}'")
        }

        if (isStringOnlyPredicate(statement.predicate) && !isStringProperty(propertyDef)) {
            throw CloudSpecPreflightException("Predicate ${statement.predicate} is only supported " +
                                                      "on string properties, " +
                                                      "and property '${propertyPath.joinToString(".")}' " +
                                                      "is a '${propertyDef.propertyType}'"
            )
        }
    }

    private fun preflightKeyValueStatement(resourceDef: ResourceDef, statement: KeyValueStatement, path: List<String>) {
        val propertyPath = path.plus(statement.propertyName)

        if (resourceDef.propertyByPath(propertyPath) == null) {
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
}
