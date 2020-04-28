package cloudspec.core.evaluator;

public class EqualExprEvaluator<T> implements ExprEvaluator<T> {
    private final T value;
    private final Boolean not;

    public EqualExprEvaluator(T value, Boolean not) {
        this.value = value;
        this.not = not;
    }

    public Boolean eval(T attributeValue) {
        return not ^ attributeValue.equals(value);
    }
}
