package cloudspec.validator;

import java.util.List;

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
        private final String reason;
        private final Throwable throwable;

        public RuleResult(String ruleName, Boolean success) {
            this(ruleName, success, "", null);
        }

        public RuleResult(String ruleName, Boolean success, String reason, Throwable throwable) {
            this.ruleName = ruleName;
            this.success = success;
            this.reason = reason;
            this.throwable = throwable;
        }

        public String getRuleName() {
            return ruleName;
        }

        public Boolean isSuccess() {
            return success;
        }

        public Boolean isError() {
            return !isSuccess();
        }

        public String getReason() {
            return reason;
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
