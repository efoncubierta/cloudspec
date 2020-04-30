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
        private final Boolean success;
        private final Optional<String> reason;
        private final Optional<Throwable> throwable;

        public RuleResult(String ruleName, Boolean success) {
            this(ruleName, success, null, null);
        }

        public RuleResult(String ruleName, Boolean success, String reason, Throwable throwable) {
            this.ruleName = ruleName;
            this.success = success;
            this.reason = reason != null ? Optional.of(reason) : Optional.empty();
            this.throwable = throwable != null ? Optional.of(throwable) : Optional.empty();
        }

        public RuleResult(String ruleName, Boolean success, String reason) {
            this(ruleName, success, reason, null);
        }

        public String getRuleName() {
            return ruleName;
        }

        public Boolean isError() {
            return !isSuccess();
        }

        public Boolean isSuccess() {
            return success;
        }

        public Optional<String> getReason() {
            return reason;
        }

        public Optional<Throwable> getThrowable() {
            return throwable;
        }
    }
}
