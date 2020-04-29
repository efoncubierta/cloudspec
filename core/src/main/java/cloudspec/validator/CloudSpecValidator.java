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
package cloudspec.validator;

import cloudspec.ProvidersRegistry;
import cloudspec.lang.*;
import cloudspec.model.Property;
import cloudspec.model.Provider;
import cloudspec.model.Resource;
import cloudspec.model.ResourceDef;

import java.util.List;
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

    private CloudSpecValidatorResult.RuleResult validateRule(RuleExpr rule) {
        try {
            String providerName = rule.getResourceTypeFqn().split(":")[0];

            // lookup provider
            Optional<Provider> providerOpt = providersRegistry.getProvider(providerName);
            if (!providerOpt.isPresent()) {
                return new CloudSpecValidatorResult.RuleResult(
                        rule.getName(),
                        Boolean.FALSE,
                        String.format("Provider '%s' not found.", providerName)
                );
            }

            // lookup resource definition
            Optional<ResourceDef> resourceDefOpt = providerOpt.get().getResourceDef(rule.getResourceTypeFqn());
            if (!resourceDefOpt.isPresent()) {
                return new CloudSpecValidatorResult.RuleResult(
                        rule.getName(),
                        Boolean.FALSE,
                        String.format("Rule validator for resource of type %s not found.", rule.getResourceTypeFqn())
                );
            }

            List<ValidateExprResult> errors = resourceDefOpt.get()
                    .getResourceLoader()
                    .load()
                    .stream()
                    .filter(resource -> validateWithExprs(resource, rule.getWiths()))
                    .flatMap(resource -> validateAssertExprs(resource, rule.getAsserts()).stream())
                    .filter(result -> !result.success)
                    .collect(Collectors.toList());

            if (errors.size() > 0) {
                return new CloudSpecValidatorResult.RuleResult(rule.getName(), Boolean.FALSE, errors.get(0).message);
            }

            return new CloudSpecValidatorResult.RuleResult(rule.getName(), Boolean.TRUE);
        } catch (RuntimeException exception) {
            throw exception;
            //return new CloudSpecValidatorResult.RuleResult(rule.getName(), Boolean.FALSE, exception.getMessage(), exception);
        }
    }

    private Boolean validateWithExprs(Resource resource, List<WithExpr> withExpr) {
        return withExpr.stream().allMatch(with -> validateWithExpr(resource, with));
    }

    private Boolean validateWithExpr(Resource resource, WithExpr withExpr) {
        Optional<Property> propertyOpt = resource.getProperty(withExpr.getPropertyName());
        return propertyOpt.isPresent() ? withExpr.getEvaluator().eval(propertyOpt.get().getValue()) : Boolean.FALSE;
    }

    private List<ValidateExprResult> validateAssertExprs(Resource resource, List<AssertExpr> assertExprs) {
        return assertExprs
                .stream()
                .map(assertExpr -> validateAssertExpr(resource, assertExpr))
                .collect(Collectors.toList());
    }

    private ValidateExprResult validateAssertExpr(Resource resource, AssertExpr assertExpr) {
        Optional<Property> propertyOpt = resource.getProperty(assertExpr.getPropertyName());

        if (!propertyOpt.isPresent()) {
            return new ValidateExprResult(
                    Boolean.FALSE,
                    String.format("Property %s not found on resource of type %s", assertExpr.getPropertyName(), resource.getResourceTypeFqn())
            );
        }

        if (!assertExpr.getEvaluator().eval(propertyOpt.get().getValue())) {
            return new ValidateExprResult(
                    Boolean.FALSE,
                    String.format("Assert expression evaluated false", assertExpr.getPropertyName(), resource.getResourceTypeFqn())
            );
        }

        return new ValidateExprResult(Boolean.TRUE);
    }

    private class ValidateExprResult {
        private final Boolean success;
        private final String message;

        public ValidateExprResult(Boolean success) {
            this(success, "");
        }

        public ValidateExprResult(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
