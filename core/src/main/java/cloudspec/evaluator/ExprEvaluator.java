package cloudspec.evaluator;

public interface ExprEvaluator<T> {
    Boolean eval(T attributeValue);
}
