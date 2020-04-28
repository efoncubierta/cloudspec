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
import cloudspec.model.Provider;
import cloudspec.ProvidersRegistry;
import cloudspec.model.ResourceDef;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CloudSpecPreflight {
    private final ProvidersRegistry providersRegistry;

    public CloudSpecPreflight(ProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    public void preflight(CloudSpec spec) {
        System.out.println("CloudSpec Preflight");

        preflightGroups(spec.getGroups());
    }

    private void preflightGroups(List<GroupExpr> groups) {
        groups.stream().forEach(this::preflightGroup);
    }

    private void preflightGroup(GroupExpr group) {
        preflightRules(group.getRules());
    }

    private void preflightRules(List<RuleExpr> rules) {
        rules.stream().forEach(this::preflightRule);
    }

    private void preflightRule(RuleExpr rule) {
        String providerName = rule.getResourceTypeFQName().split(":")[0];

        // lookup provider
        Provider provider = providersRegistry.getProvider(providerName);
        if (Objects.isNull(provider)) {
            throw new CloudSpecPreflightException(String.format("Provider '%s' not found.", providerName));
        }

        // lookup resource definition
        Optional<ResourceDef> resourceDefOpt = provider.getResourceDef(rule.getResourceTypeFQName());
        if (!resourceDefOpt.isPresent()) {
            throw new CloudSpecPreflightException(String.format("Rule validator for resource of type %s not found.", rule.getResourceTypeFQName()));
        }

        // preflight withs and asserts
        preflightWiths(resourceDefOpt.get(), rule.getWiths());
        preflightAsserts(resourceDefOpt.get(), rule.getAsserts());
    }

    private void preflightWiths(ResourceDef resourceDef, List<WithExpr> withExprs) {
        withExprs.forEach(withExpr -> preflightAttribute(resourceDef, withExpr.getAttribute()));
    }

    private void preflightAsserts(ResourceDef resourceDef, List<AssertExpr> assertExprs) {
        assertExprs.forEach(assertExpr -> preflightAttribute(resourceDef, assertExpr.getAttribute()));
    }

    private void preflightAttribute(ResourceDef resourceDef, String attributeFQName) {
        if (!resourceDef.getAttributeDefinition(attributeFQName).isPresent()) {
            throw new CloudSpecPreflightException(String.format("Resource type '%s' does not define attribute '%s'.", resourceDef.getFQName(), attributeFQName));
        }
    }
}
