package cloudspec.lang.evaluator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualExprEvaluatorTest {
    @Test
    public void shouldEvalAnIntegerValue() {
        EqualExprEvaluator<Integer> evaluator = new EqualExprEvaluator<>(1, Boolean.FALSE);

        assertTrue(evaluator.eval(1));
        assertFalse(evaluator.eval(2));

        // test negative
        evaluator = new EqualExprEvaluator<>(1, Boolean.TRUE);
        assertTrue(evaluator.eval(2));
        assertFalse(evaluator.eval(1));
    }

    @Test
    public void shouldEvalAStringValue() {
        EqualExprEvaluator<String> evaluator = new EqualExprEvaluator<>("success", Boolean.FALSE);

        assertTrue(evaluator.eval("success"));
        assertFalse(evaluator.eval("fail"));

        // test negative
        evaluator = new EqualExprEvaluator<>("success", Boolean.TRUE);
        assertTrue(evaluator.eval("fail"));
        assertFalse(evaluator.eval("success"));
    }

    @Test
    public void shouldEvalABooleanValue() {
        EqualExprEvaluator<Boolean> evaluator = new EqualExprEvaluator<>(Boolean.TRUE, Boolean.FALSE);

        assertTrue(evaluator.eval(Boolean.TRUE));
        assertFalse(evaluator.eval(Boolean.FALSE));

        // test negative
        evaluator = new EqualExprEvaluator<>(Boolean.TRUE, Boolean.TRUE);
        assertTrue(evaluator.eval(Boolean.FALSE));
        assertFalse(evaluator.eval(Boolean.TRUE));
    }
}
