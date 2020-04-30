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
package cloudspec.lang;

import cloudspec.lang.evaluator.ExprEvaluator;

/**
 * Define a 'with' expression.
 * <p>
 * With expressions ares used to filter resources.
 */
public class WithExpr {
    private final String propertyName;
    private final ExprEvaluator<Object> evaluator;

    /**
     * Constructor.
     *
     * @param propertyName Resource's property that will be evaluated.
     * @param evaluator    Expression evaluator.
     */
    public WithExpr(String propertyName, ExprEvaluator<Object> evaluator) {
        this.propertyName = propertyName;
        this.evaluator = evaluator;
    }

    /**
     * Get the resource's property name that will be evaluated.
     *
     * @return Property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Get the expression evaluator.
     *
     * @return Expression evaluator.
     */
    public ExprEvaluator<Object> getEvaluator() {
        return evaluator;
    }

    @Override
    public String toString() {
        return "WithExpr{" +
                "propertyName='" + propertyName + '\'' +
                ", evaluator=" + evaluator +
                '}';
    }
}
