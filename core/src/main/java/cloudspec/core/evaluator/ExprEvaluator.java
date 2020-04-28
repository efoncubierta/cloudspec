package cloudspec.core.evaluator;

public interface ExprEvaluator<T> {
    Boolean eval(T attributeValue);
}
