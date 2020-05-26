/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.preflight;

import cloudspec.lang.*;
import cloudspec.model.PropertyDef;
import cloudspec.model.PropertyType;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;
import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CloudSpecPreflight {
    private final Logger LOGGER = LoggerFactory.getLogger(CloudSpecPreflight.class);

    private final ResourceDefStore resourceDefStore;

    public CloudSpecPreflight(ResourceDefStore resourceDefStore) {
        this.resourceDefStore = resourceDefStore;
    }

    public void preflight(CloudSpec spec) {
        LOGGER.info("Validating CloudSpec input");

        preflightGroups(spec.getGroups());
    }

    private void preflightGroups(List<GroupExpr> groups) {
        groups.forEach(this::preflightGroup);
    }

    private void preflightGroup(GroupExpr group) {
        LOGGER.debug("Preflight of group {}", group.getName());
        preflightRules(group.getRules());
    }

    private void preflightRules(List<RuleExpr> rules) {
        rules.forEach(this::preflightRule);
    }

    private void preflightRule(RuleExpr rule) {
        LOGGER.debug("Preflight of rule {}", rule.getName());

        var resourceDefRefOpt = ResourceDefRef.fromString(rule.getResourceDefRef());
        if (resourceDefRefOpt.isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format("Malformed resource definition reference '%s'", rule.getResourceDefRef())
            );
        }

        // lookup resource definition
        var resourceDefOpt = resourceDefStore.getResourceDef(resourceDefRefOpt.get());
        if (resourceDefOpt.isEmpty()) {
            throw new CloudSpecPreflightException(String.format("Resource of type '%s' is not supported.", rule.getResourceDefRef()));
        }

        var resourceDef = resourceDefOpt.get();

        // preflight withs and asserts
        rule.getWithExpr()
                .getStatements()
                .forEach(statement ->
                        preflightStatement(resourceDef, statement, new ArrayList<>())
                );
        rule.getAssertExpr()
                .getStatements()
                .forEach(statement ->
                        preflightStatement(resourceDef, statement, new ArrayList<>())
                );
    }

    private void preflightStatement(ResourceDef resourceDef, Statement statement, List<String> path) {
        if (statement instanceof NestedStatement) {
            preflightNestedStatement(resourceDef, ((NestedStatement) statement), path);
        } else if (statement instanceof KeyValueStatement) {
            preflightKeyValueStatement(resourceDef, (KeyValueStatement) statement, path);
        } else if (statement instanceof PropertyStatement) {
            preflightPropertyStatement(resourceDef, (PropertyStatement) statement, path);
        } else if (statement instanceof AssociationStatement) {
            preflightAssociationStatement(resourceDef, (AssociationStatement) statement, path);
        }
    }

    private void preflightNestedStatement(ResourceDef resourceDef, NestedStatement statement, List<String> path) {
        var nestedPath = new ArrayList<>(path);
        nestedPath.add(statement.getPropertyName());

        if (resourceDef.getPropertyByPath(nestedPath).isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(), statement.getPropertyName()
                    )
            );
        }

        statement.getStatements()
                .forEach(stmt -> preflightStatement(resourceDef, stmt, nestedPath));
    }

    private void preflightAssociationStatement(ResourceDef resourceDef, AssociationStatement statement, List<String> path) {
        var associationPath = new ArrayList<>(path);
        associationPath.add(statement.getAssociationName());

        var associationDefOpt = resourceDef.getAssociationByPath(associationPath);
        if (associationDefOpt.isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define association '%s'.",
                            resourceDef.getRef(), statement.getAssociationName()
                    )
            );
        }

        var associationDef = associationDefOpt.get();

        var associatedResourceDefOpt = resourceDefStore.getResourceDef(associationDef.getResourceDefRef());
        if (associatedResourceDefOpt.isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Associated resource of type '%s' is not supported.",
                            associationDef.getResourceDefRef()
                    )
            );
        }

        var associatedResourceDef = associatedResourceDefOpt.get();
        statement.getStatements()
                .forEach(stmt ->
                        preflightStatement(associatedResourceDef, stmt, new ArrayList<>())
                );
    }

    private void preflightPropertyStatement(ResourceDef resourceDef, PropertyStatement statement, List<String> path) {
        var propertyPath = new ArrayList<>(path);
        propertyPath.add(statement.getPropertyName());

        var propertyDefOpt = resourceDef.getPropertyByPath(propertyPath);
        if (propertyDefOpt.isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            String.join(".", propertyPath)
                    )
            );
        }

        var propertyDef = propertyDefOpt.get();

        if (isNumberOnlyPredicate(statement.getPredicate()) && !isNumberProperty(propertyDef)) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Predicate %s is only supported on number properties (i.e. integer and double), " +
                                    "and property '%s' is a '%s'",
                            statement.getPredicate(),
                            String.join(".", propertyPath),
                            propertyDef.getPropertyType()
                    )
            );
        }

        if (isStringOnlyPredicate(statement.getPredicate()) && !isStringProperty(propertyDef)) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Predicate %s is only supported on string properties, " +
                                    "and property '%s' is a '%s'",
                            statement.getPredicate(),
                            String.join(".", propertyPath),
                            propertyDef.getPropertyType()
                    )
            );
        }
    }

    private void preflightKeyValueStatement(ResourceDef resourceDef, KeyValueStatement statement, List<String> path) {
        var propertyPath = new ArrayList<>(path);
        propertyPath.add(statement.getPropertyName());

        if (resourceDef.getPropertyByPath(propertyPath).isEmpty()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            statement.getPropertyName()
                    )
            );
        }
    }

    private Boolean isNumberOnlyPredicate(P<?> predicate) {
        var biPredicate = predicate.getBiPredicate();
        return (biPredicate.equals(Compare.lt) ||
                biPredicate.equals(Compare.lte) ||
                biPredicate.equals(Compare.gt) ||
                biPredicate.equals(Compare.gte));
    }

    private Boolean isStringOnlyPredicate(P<?> predicate) {
        var biPredicate = predicate.getBiPredicate();
        return (biPredicate.equals(Text.startingWith) ||
                biPredicate.equals(Text.notStartingWith) ||
                biPredicate.equals(Text.endingWith) ||
                biPredicate.equals(Text.notEndingWith) ||
                biPredicate.equals(Text.containing) ||
                biPredicate.equals(Text.notContaining));
    }

    private Boolean isNumberProperty(PropertyDef propertyDef) {
        return (propertyDef.getPropertyType().equals(PropertyType.INTEGER) &&
                propertyDef.getPropertyType().equals(PropertyType.DOUBLE));
    }

    private Boolean isStringProperty(PropertyDef propertyDef) {
        return (propertyDef.getPropertyType().equals(PropertyType.STRING));
    }
}
