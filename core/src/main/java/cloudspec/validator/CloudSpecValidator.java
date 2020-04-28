package cloudspec.validator;

import cloudspec.core.*;
import cloudspec.core.spec.*;
import cloudspec.preload.CloudSpecPreloaderException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CloudSpecValidator {
    private final ProvidersRegistry providersRegistry;

    public CloudSpecValidator(ProvidersRegistry providersRegistry) {
        this.providersRegistry = providersRegistry;
    }

    public CloudSpecValidatorResult validate(CloudSpec spec) {
        return new CloudSpecValidatorResult(spec.getName(), validateGroups(spec.getGroups()));
    }

    private List<CloudSpecValidatorResult.GroupResult> validateGroups(List<GroupExpr> groups) {
        return groups.stream().map(this::validateGroup).collect(Collectors.toList());
    }

    private CloudSpecValidatorResult.GroupResult validateGroup(GroupExpr group) {
        return new CloudSpecValidatorResult.GroupResult(group.getName(), validateRules(group.getRules()));
    }

    private List<CloudSpecValidatorResult.RuleResult> validateRules(List<RuleExpr> rules) {
        return rules.stream().map(this::validateRule).collect(Collectors.toList());
    }

    public CloudSpecValidatorResult.RuleResult validateRule(RuleExpr rule) {
        try {
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

            Boolean result = resourceDefOpt.get()
                    .getResourceLoader()
                    .load()
                    .stream()
                    .anyMatch(resource -> validateResource(resource, rule.getWiths(), rule.getAsserts()));

            return new CloudSpecValidatorResult.RuleResult(rule.getTitle(), result);
        } catch (RuntimeException exception) {
            return new CloudSpecValidatorResult.RuleResult(rule.getTitle(), Boolean.FALSE, exception.getMessage(), exception);
        }
    }

    private Boolean validateResource(Resource resource, List<WithExpr> withExpr, List<AssertExpr> assertExpr) {
        return validateWiths(resource, withExpr) && validateAsserts(resource, assertExpr);
    }

    private Boolean validateWiths(Resource resource, List<WithExpr> withExpr) {
        return withExpr.stream().allMatch(with -> validateWith(resource, with));
    }

    private Boolean validateWith(Resource resource, WithExpr withExpr) {
        Optional<ResourceAttribute> attributeOpt = resource.getAttribute(withExpr.getAttribute());
        return attributeOpt.isPresent() ? withExpr.getEvaluator().eval(attributeOpt.get().getValue()) : Boolean.FALSE;
    }

    private Boolean validateAsserts(Resource resource, List<AssertExpr> assertExprs) {
        return assertExprs.stream().allMatch(assertExpr -> validateAssert(resource, assertExpr));
    }

    private Boolean validateAssert(Resource resource, AssertExpr assertExpr) {
        Optional<ResourceAttribute> attributeOpt = resource.getAttribute(assertExpr.getAttribute());
        return attributeOpt.isPresent() ? assertExpr.getEvaluator().eval(attributeOpt.get().getValue()) : Boolean.FALSE;
    }
}
