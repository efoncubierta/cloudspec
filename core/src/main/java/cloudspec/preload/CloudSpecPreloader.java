package cloudspec.preload;

import cloudspec.core.Provider;
import cloudspec.core.ProvidersRegistry;
import cloudspec.core.ResourceDef;
import cloudspec.core.spec.CloudSpec;
import cloudspec.core.spec.GroupExpr;
import cloudspec.core.spec.RuleExpr;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CloudSpecPreloader {
    private final ProvidersRegistry providersRegistry;

    public CloudSpecPreloader(ProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    public void preload(CloudSpec spec) {
        System.out.println("CloudSpec Preloader");

        preloadGroups(spec.getGroups());
    }

    private void preloadGroups(List<GroupExpr> groups) {
        groups.stream().forEach(this::preloadGroup);
    }

    private void preloadGroup(GroupExpr group) {
        preloadRules(group.getRules());
    }

    private void preloadRules(List<RuleExpr> rules) {
        rules.stream().forEach(this::preloadRule);
    }

    private void preloadRule(RuleExpr rule) {
        String providerName = rule.getResourceTypeFQName().split(":")[0];

        // lookup provider
        Provider provider = providersRegistry.getProvider(providerName);
        if (Objects.isNull(provider)) {
            throw new CloudSpecPreloaderException(String.format("Provider '%s' not found.", providerName));
        }

        // lookup resource definition
        Optional<ResourceDef> resourceDefOpt = provider.getResourceDef(rule.getResourceTypeFQName());
        if (!resourceDefOpt.isPresent()) {
            throw new CloudSpecPreloaderException(String.format("Rule validator for resource of type %s not found.", rule.getResourceTypeFQName()));
        }

        // load resources
        resourceDefOpt.get()
                .getResourceLoader()
                .load();
    }
}
