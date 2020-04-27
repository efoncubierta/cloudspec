package cloudspec.model;

import cloudspec.spec.AssertExpr;
import cloudspec.spec.WithExpr;

import java.util.List;
import java.util.stream.Collectors;

public class ResourceValidator {
    private final ResourceDef definition;

    public ResourceValidator(ResourceDef definition) {
        this.definition = definition;
    }

    public static ResourceValidator forDef(ResourceDef definition) {
        return new ResourceValidator(definition);
    }

    public Boolean validate(List<WithExpr> withExprs, List<AssertExpr> assertExpr) {
        return !definition
                .getResourceLoader()
                .load()
                .stream()
                .filter(resource -> validateResource(resource, withExprs, assertExpr))
                .collect(Collectors.toList()).isEmpty();
    }

    private Boolean validateResource(Resource resource, List<WithExpr> withExpr, List<AssertExpr> assertExpr) {
        return validateWiths(resource, withExpr) && validateAsserts(resource, assertExpr);
    }

    private Boolean validateWiths(Resource resource, List<WithExpr> withExpr) {
        return withExpr.stream().allMatch(with -> validateWith(resource, with));
    }

    private Boolean validateWith(Resource resource, WithExpr withExpr) {
        ResourceAttribute attribute = resource.getAttribute(withExpr.getAttribute());
        return attribute != null ? withExpr.getEvaluator().eval(attribute.getValue()) : Boolean.FALSE;
    }

    private Boolean validateAsserts(Resource resource, List<AssertExpr> assertExprs) {
        return assertExprs.stream().allMatch(assertExpr -> validateAssert(resource, assertExpr));
    }

    private Boolean validateAssert(Resource resource, AssertExpr assertExpr) {
        ResourceAttribute attribute = resource.getAttribute(assertExpr.getAttribute());
        return attribute != null ? assertExpr.getEvaluator().eval(attribute.getValue()) : Boolean.FALSE;
    }
}
