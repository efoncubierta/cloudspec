package cloudspec.core.spec;

import cloudspec.core.evaluator.ExprEvaluator;

public class WithExpr {
    private final String attribute;
    private final ExprEvaluator<Object> evaluator;

    public WithExpr(String attribute, ExprEvaluator<Object> evaluator) {
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
        return "WithExpr{" +
                "attribute='" + attribute + '\'' +
                ", evaluator=" + evaluator +
                '}';
    }
}
