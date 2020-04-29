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
