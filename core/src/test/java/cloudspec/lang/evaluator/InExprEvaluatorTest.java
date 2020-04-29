package cloudspec.lang.evaluator;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InExprEvaluatorTest {
    @Test
    public void shouldEvalAListOfIntegerValues() {
        InExprEvaluator<Integer> evaluator = new InExprEvaluator<>(Arrays.asList(1, 2, 3), Boolean.FALSE);

        assertTrue(evaluator.eval(1));
        assertFalse(evaluator.eval(4));

        // test negative
        evaluator = new InExprEvaluator<>(Arrays.asList(1, 2, 3), Boolean.TRUE);
        assertTrue(evaluator.eval(4));
        assertFalse(evaluator.eval(1));
    }

    @Test
    public void shouldEvalAListOfStringValues() {
        InExprEvaluator<String> evaluator = new InExprEvaluator<>(Arrays.asList("foo", "bar"), Boolean.FALSE);

        assertTrue(evaluator.eval("foo"));
        assertFalse(evaluator.eval("baz"));

        // test negative
        evaluator = new InExprEvaluator<>(Arrays.asList("foo", "bar"), Boolean.TRUE);
        assertTrue(evaluator.eval("baz"));
        assertFalse(evaluator.eval("foo"));
    }

    @Test
    public void shouldEvalAListOfBooleanValues() {
        InExprEvaluator<Boolean> evaluator = new InExprEvaluator<>(Arrays.asList(Boolean.TRUE, Boolean.TRUE), Boolean.FALSE);

        assertTrue(evaluator.eval(Boolean.TRUE));
        assertFalse(evaluator.eval(Boolean.FALSE));

        // test negative
        evaluator = new InExprEvaluator<>(Arrays.asList(Boolean.TRUE, Boolean.TRUE), Boolean.TRUE);
        assertTrue(evaluator.eval(Boolean.FALSE));
        assertFalse(evaluator.eval(Boolean.TRUE));
    }
}
