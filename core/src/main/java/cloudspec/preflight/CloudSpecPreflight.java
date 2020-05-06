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
import cloudspec.model.ResourceDef;
import cloudspec.model.ResourceDefRef;
import cloudspec.store.ResourceDefStore;

import java.util.List;
import java.util.Optional;

public class CloudSpecPreflight {
    private final ResourceDefStore resourceDefStore;

    public CloudSpecPreflight(ResourceDefStore resourceDefStore) {
        this.resourceDefStore = resourceDefStore;
    }

    public void preflight(CloudSpec spec) {
        System.out.println("CloudSpec Preflight");

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

        // preflight withs and asserts
        preflightWiths(resourceDefOpt.get(), rule.getWiths());
        preflightAsserts(resourceDefOpt.get(), rule.getAsserts());
    }

    private void preflightWiths(ResourceDef resourceDef, List<WithExpr> withExprs) {
        withExprs.forEach(withExpr -> preflightProperty(resourceDef, withExpr.getPropertyName()));
    }

    private void preflightAsserts(ResourceDef resourceDef, List<AssertExpr> assertExprs) {
        assertExprs.forEach(assertExpr -> preflightProperty(resourceDef, assertExpr.getPropertyName()));
    }

    private void preflightProperty(ResourceDef resourceDef, String propertyName) {
        if (!resourceDef.getProperty(propertyName).isPresent()) {
            throw new CloudSpecPreflightException(String.format("Resource type '%s' does not define property '%s'.", resourceDef.getRef(), propertyName));
        }
    }
}
