package cloudspec.evaluator;

import java.util.List;

public class InExprEvaluator<T> implements ExprEvaluator<T> {
    private final List<T> values;
    private final Boolean not;

    public InExprEvaluator(List<T> values, Boolean not) {
        this.values = values;
        this.not = not;
    }

    public Boolean eval(T attributeValue) {
        return not ^ values.contains(attributeValue);
    }
}
