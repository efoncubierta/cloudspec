package cloudspec.spec;

import java.util.List;

public class GroupExpr {
    private final String name;
    private final List<RuleExpr> rules;

    public GroupExpr(String name, List<RuleExpr> rules) {
        this.name = name;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public List<RuleExpr> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return "SpecGroup{" +
                "name='" + name + '\'' +
                ", rules=" + rules +
                '}';
    }
}
