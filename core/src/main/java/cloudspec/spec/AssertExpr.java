package cloudspec.spec;

import cloudspec.evaluator.ExprEvaluator;

public class AssertExpr {
    private final String attribute;
    private final ExprEvaluator<Object> evaluator;

    public AssertExpr(String attribute, ExprEvaluator<Object> evaluator) {
        this.attribute = attribute;
        this.evaluator = evaluator;
    }

    public String getAttribute() {
        return attribute;
    }

    public ExprEvaluator<Object> getEvaluator() {
        return evaluator;
    }

    @Override
    public String toString() {
        return "AssertExpr{" +
                "attribute='" + attribute + '\'' +
                ", evaluator=" + evaluator +
                '}';
    }
}
