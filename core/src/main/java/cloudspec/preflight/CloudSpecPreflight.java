package cloudspec.preflight;

import cloudspec.core.Provider;
import cloudspec.core.ProvidersRegistry;
import cloudspec.core.ResourceDef;
import cloudspec.core.spec.*;

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
