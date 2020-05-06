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

import cloudspec.lang.CloudSpec;
import cloudspec.lang.GroupExpr;
import cloudspec.lang.RuleExpr;
import cloudspec.model.ResourceFqn;

import java.util.List;
import java.util.stream.Collectors;

public class CloudSpecValidator {
    private final ResourceValidator resourceValidator;

    public CloudSpecValidator(ResourceValidator resourceValidator) {
        this.resourceValidator = resourceValidator;
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
        // validate all resources
        List<ResourceValidatorResult> errors = resourceValidator.validate(
                ResourceFqn.fromString(rule.getResourceFqn()),
                rule.getWiths(),
                rule.getAsserts()
        );

        // check for errors
        if (errors.size() > 0) {
            return new CloudSpecValidatorResult.RuleResult(rule.getName(), Boolean.FALSE, errors.get(0).getMessage());
        }

        // return success
        return new CloudSpecValidatorResult.RuleResult(rule.getName(), Boolean.TRUE);
    }

}
