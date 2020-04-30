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

import java.util.List;

/**
 * Define a rule expression.
 * <p>
 * A rule filters and validates resources.
 */
public class RuleExpr {
    private final String name;
    private final String resourceFqn;
    private final List<WithExpr> withs;
    private final List<AssertExpr> asserts;

    /**
     * Constructor.
     *
     * @param name        Rules' name.
     * @param resourceFqn Fully-qualified name of the resource the rule applies to.
     * @param withs       List of 'with' expressions to filter the resources.
     * @param asserts     List of 'assert' expressions to validate the resources.
     */
    public RuleExpr(String name, String resourceFqn, List<WithExpr> withs, List<AssertExpr> asserts) {
        this.name = name;
        this.resourceFqn = resourceFqn;
        this.withs = withs;
        this.asserts = asserts;
    }

    /**
     * Get the rule's name.
     *
     * @return Rule's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the fully-qualified name of the resource the rule applies to.
     *
     * @return Resource's FQ name.
     */
    public String getResourceFqn() {
        return resourceFqn;
    }

    /**
     * Get the rule's 'with' expressions.
     *
     * @return List of with expressions.
     */
    public List<WithExpr> getWiths() {
        return withs;
    }

    /**
     * Get the rule's 'assert' expressions.
     *
     * @return List of assert expressions.
     */
    public List<AssertExpr> getAsserts() {
        return asserts;
    }

    @Override
    public String toString() {
        return "SpecRule{" +
                "name='" + name + '\'' +
                ", resourceFqn='" + resourceFqn + '\'' +
                ", withs=" + withs +
                ", asserts=" + asserts +
                '}';
    }
}
