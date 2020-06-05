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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CloudSpecValidatorResult {
    private final String specName;
    private final List<GroupResult> groupResults;

    public CloudSpecValidatorResult(String specName, List<GroupResult> groupResults) {
        this.specName = specName;
        this.groupResults = groupResults;
    }

    public String getSpecName() {
        return specName;
    }

    public List<GroupResult> getGroupResults() {
        return groupResults;
    }

    public static class GroupResult {
        private final String groupName;
        private final List<RuleResult> ruleResults;

        public GroupResult(String groupName, List<RuleResult> ruleResults) {
            this.groupName = groupName;
            this.ruleResults = ruleResults;
        }

        public String getGroupName() {
            return groupName;
        }

        public List<RuleResult> getRuleResults() {
            return ruleResults;
        }
    }

    public static class RuleResult {
        private final String ruleName;
        private final List<ResourceValidationResult> resourceValidationResults;
        private final Optional<Throwable> throwableOpt;

        public RuleResult(String ruleName, List<ResourceValidationResult> resourceValidationResults) {
            this.ruleName = ruleName;
            this.resourceValidationResults = resourceValidationResults;
            this.throwableOpt = Optional.empty();
        }

        public RuleResult(String ruleName, Throwable throwableOpt) {
            this.ruleName = ruleName;
            this.resourceValidationResults = Collections.emptyList();
            this.throwableOpt = Optional.of(throwableOpt);
        }

        public String getRuleName() {
            return ruleName;
        }

        public List<ResourceValidationResult> getResourceValidationResults() {
            return resourceValidationResults;
        }

        public Optional<Throwable> getThrowable() {
            return throwableOpt;
        }

        public Boolean isSuccess() {
            return throwableOpt.isEmpty() &&
                    resourceValidationResults
                            .stream()
                            .allMatch(ResourceValidationResult::isSuccess);
        }
    }
}
