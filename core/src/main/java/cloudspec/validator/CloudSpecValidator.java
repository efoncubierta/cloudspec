/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.validator;

import cloudspec.lang.CloudSpec;
import cloudspec.lang.GroupExpr;
import cloudspec.lang.RuleExpr;
import cloudspec.model.ResourceDefRef;

import java.util.List;
import java.util.Optional;
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
        Optional<ResourceDefRef> resourceDefRefOpt = ResourceDefRef.fromString(rule.getResourceDefRef());
        if (resourceDefRefOpt.isEmpty()) {
            return new CloudSpecValidatorResult.RuleResult(
                    rule.getName(),
                    new RuntimeException(String.format("Malformed resource definition reference '%s'", rule.getResourceDefRef()))
            );
        }

        try {
            // validate all resources
            return new CloudSpecValidatorResult.RuleResult(rule.getName(), resourceValidator.validateAll(
                    resourceDefRefOpt.get(),
                    rule.getWithExpr().getStatements(),
                    rule.getAssertExpr().getStatements()
            ));
        } catch (RuntimeException e) {
            return new CloudSpecValidatorResult.RuleResult(rule.getName(), e);
        }
    }

}
