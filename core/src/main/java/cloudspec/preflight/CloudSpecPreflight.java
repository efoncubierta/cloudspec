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
import cloudspec.loader.ResourceLoader;
import cloudspec.model.AssociationDef;
import cloudspec.model.PropertyDef;
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CloudSpecPreflight {
    private final Logger LOGGER = LoggerFactory.getLogger(ResourceLoader.class);

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
        preflightRules(group.getRules());
    }

    private void preflightRules(List<RuleExpr> rules) {
        rules.forEach(this::preflightRule);
    }

    private void preflightRule(RuleExpr rule) {
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
        rule.getWithExpr().getStatements().forEach(statement -> preflightStatement(resourceDef, statement));
        rule.getAssertExpr().getStatements().forEach(statement -> preflightStatement(resourceDef, statement));
    }

    private void preflightStatement(ResourceDef resourceDef, Statement statement) {
        if (statement instanceof NestedStatement) {
            preflightNestedStatement(resourceDef, ((NestedStatement) statement));
        } else if (statement instanceof PropertyStatement) {
            preflightPropertyStatement(resourceDef, (PropertyStatement) statement);
        } else if (statement instanceof KeyValueStatement) {
            preflightKeyValueStatement(resourceDef, (KeyValueStatement) statement);
        } else if (statement instanceof AssociationStatement) {
            preflightAssociationStatement(resourceDef, (AssociationStatement) statement);
        }
    }

    private void preflightNestedStatement(ResourceDef resourceDef, NestedStatement statement) {
        Optional<PropertyDef> propertyDefOpt = resourceDef.getPropertyByPath(toPath(statement));
        if (!propertyDefOpt.isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(), statement.getMemberName()
                    )
            );
        }
    }

    private void preflightAssociationStatement(ResourceDef resourceDef, AssociationStatement statement) {
        Optional<AssociationDef> associationDefOpt = resourceDef.getAssociation(statement.getAssociationName());
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
        statement.getStatements().forEach(stmt -> preflightStatement(associatedResourceDef, stmt));
    }

    private void preflightPropertyStatement(ResourceDef resourceDef, PropertyStatement statement) {
        if (!resourceDef.getProperty(statement.getPropertyName()).isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            statement.getPropertyName()
                    )
            );
        }
    }

    private void preflightKeyValueStatement(ResourceDef resourceDef, KeyValueStatement statement) {
        if (!resourceDef.getProperty(statement.getPropertyName()).isPresent()) {
            throw new CloudSpecPreflightException(
                    String.format(
                            "Resource type '%s' does not define property '%s'.",
                            resourceDef.getRef(),
                            statement.getPropertyName()
                    )
            );
        }
    }

    private List<String> toPath(Statement statement) {
        if (statement instanceof NestedStatement) {
            List<String> path = new ArrayList<>();
            path.add(((NestedStatement) statement).getMemberName());
            path.addAll(toPath(((NestedStatement) statement).getStatement()));
            return path;
        } else if (statement instanceof PropertyStatement) {
            return Collections.singletonList(((PropertyStatement) statement).getPropertyName());
        } else if (statement instanceof AssociationStatement) {
            return Collections.singletonList(((AssociationStatement) statement).getAssociationName());
        }
        return Collections.emptyList();
    }
}
