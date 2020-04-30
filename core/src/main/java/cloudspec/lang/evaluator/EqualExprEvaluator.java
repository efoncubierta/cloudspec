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

/**
 * Evaluator for 'equal' expressions.
 * <p>
 * Evaluate if a property's value is equal to a predefined one.
 * <p>
 * - property == 'some_value'
 * - property != 'some_value'
 * - property EQUAL TO 'some_value'
 * - property NOT EQUAL TO 'some_value'
 *
 * @param <T> Type of the property.
 */
public class EqualExprEvaluator<T> implements ExprEvaluator<T> {
    private final T value;
    private final Boolean not;

    /**
     * Constructor.
     *
     * @param value Value to be compared with the property's value.
     * @param not   Flag to negate the expression. If true, it'll evaluate 'not equal to'.
     */
    public EqualExprEvaluator(T value, Boolean not) {
        this.value = value;
        this.not = not;
    }

    @Override
    public Boolean eval(T propertyValue) {
        return not ^ propertyValue.equals(value);
    }
}
