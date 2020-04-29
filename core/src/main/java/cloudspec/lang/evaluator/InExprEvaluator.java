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

import java.util.List;

/**
 * Evaluator for 'in' expressions.
 * <p>
 * Evaluate if an property's value is in a list of predefined values.
 * <p>
 * - property IN [1, 2, 3]
 * - property NOT IN [1, 2, 3]
 *
 * @param <T> Type of the property.
 */
public class InExprEvaluator<T> implements ExprEvaluator<T> {
    private final List<T> values;
    private final Boolean not;

    /**
     * Constructor.
     *
     * @param values List of values to be compared with the property's value.
     * @param not    Flag to negate the expression. If true, it'll evaluate 'not equal to'.
     */
    public InExprEvaluator(List<T> values, Boolean not) {
        this.values = values;
        this.not = not;
    }

    @Override
    public Boolean eval(T propertyValue) {
        return not ^ values.contains(propertyValue);
    }
}
