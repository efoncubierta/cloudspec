/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
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
