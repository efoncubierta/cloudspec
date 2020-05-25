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
import cloudspec.model.AssociationDef;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CloudSpecPreflight {
    private final Logger LOGGER = LoggerFactory.getLogger(CloudSpecPreflight.class);

    private final ResourceDefStore resourceDefStore;

    public CloudSpecPreflight(ResourceDefStore resourceDefStore) {
        this.resourceDefStore = resourceDefStore;
    }

    public void preflight(CloudSpec spec) {
        LOGGER.info("Validating cloud specification");

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
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefRef.fromString(rule.getResourceDefRef());
        if (!resourceDefRefOpt.isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format("Malformed resource definition reference '%s'", rule.getResourceDefRef())
            );
        }

        // lookup resource definition
        Optional<ResourceDef> resourceDefOpt = resourceDefStore.getResourceDef(resourceDefRefOpt.get());
        if (!resourceDefOpt.isPresent()) {
            throw new CloudSpecPreflightException(String.format("Resource of type '%s' is not supported.", rule.getResourceDefRef()));
        }

        ResourceDef resourceDef = resourceDefOpt.get();

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
        List<String> nestedPath = new ArrayList<>(path);
        nestedPath.add(statement.getPropertyName());

        if (!resourceDef.getPropertyByPath(nestedPath).isPresent()) {
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
        List<String> associationPath = new ArrayList<>(path);
        associationPath.add(statement.getAssociationName());

        Optional<AssociationDef> associationDefOpt = resourceDef.getAssociationByPath(associationPath);
        if (!associationDefOpt.isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define association '%s'.",
                            resourceDef.getRef(), statement.getAssociationName()
                    )
            );
        }

        AssociationDef associationDef = associationDefOpt.get();

        Optional<ResourceDef> associatedResourceDefOpt = resourceDefStore.getResourceDef(associationDef.getResourceDefRef());
        if (!associatedResourceDefOpt.isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Associated resource of type '%s' is not supported.",
                            associationDef.getResourceDefRef()
                    )
            );
        }

        ResourceDef associatedResourceDef = associatedResourceDefOpt.get();
        statement.getStatements().forEach(stmt -> preflightStatement(associatedResourceDef, stmt, new ArrayList<>()));
    }

    private void preflightPropertyStatement(ResourceDef resourceDef, PropertyStatement statement, List<String> path) {
        List<String> propertyPath = new ArrayList<>(path);
        propertyPath.add(statement.getPropertyName());

        if (!resourceDef.getPropertyByPath(propertyPath).isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            String.join(".", propertyPath)
                    )
            );
        }
    }

    private void preflightKeyValueStatement(ResourceDef resourceDef, KeyValueStatement statement, List<String> path) {
        List<String> propertyPath = new ArrayList<>(path);
        propertyPath.add(statement.getPropertyName());

        if (!resourceDef.getPropertyByPath(propertyPath).isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            statement.getPropertyName()
                    )
            );
        }
    }
}
