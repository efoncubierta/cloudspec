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

import org.apache.commons.lang.text.StrBuilder;

import java.util.Collections;
import java.util.Objects;

/**
 * Define a rule expression.
 * <p>
 * A rule filters and validates resources.
 */
public class RuleExpr implements CloudSpecSyntaxProducer {
    private final String name;
    private final String resourceDefRef;
    private final WithExpr withExpr;
    private final AssertExpr assertExpr;

    /**
     * Constructor.
     *
     * @param name           Rules' name.
     * @param resourceDefRef Fully-qualified name of the resource type the rule applies to.
     * @param withExpr       'With' expression to filter the resources.
     * @param assertExpr     'Assert' expression to validate the resources.
     */
    public RuleExpr(String name, String resourceDefRef, WithExpr withExpr, AssertExpr assertExpr) {
        this.name = name;
        this.resourceDefRef = resourceDefRef;
        this.withExpr = withExpr;
        this.assertExpr = assertExpr;
    }

    public static RuleExprBuilder builder() {
        return new RuleExprBuilder();
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
    public String getResourceDefRef() {
        return resourceDefRef;
    }

    /**
     * Get the rule 'With' expression.
     *
     * @return 'With' expression.
     */
    public WithExpr getWithExpr() {
        return withExpr;
    }

    /**
     * Get the rule 'Assert' expression.
     *
     * @return 'Assert' expression.
     */
    public AssertExpr getAssertExpr() {
        return assertExpr;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();
        sb.appendln(
                String.format("%sRule \"%s\"", " ".repeat(spaces), name)
        );
        sb.appendln(
                String.format("%sOn %s", " ".repeat(spaces), resourceDefRef)
        );

        // add with statements
        sb.append(withExpr.toCloudSpecSyntax(spaces));

        // add assert statements
        sb.append(assertExpr.toCloudSpecSyntax(spaces));

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resourceDefRef, withExpr, assertExpr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleExpr ruleExpr = (RuleExpr) o;
        return name.equals(ruleExpr.name) &&
                resourceDefRef.equals(ruleExpr.resourceDefRef) &&
                withExpr.equals(ruleExpr.withExpr) &&
                assertExpr.equals(ruleExpr.assertExpr);
    }

    @Override
    public String toString() {
        return "SpecRule{" +
                "name='" + name + '\'' +
                ", resourceDefRef='" + resourceDefRef + '\'' +
                ", with=" + withExpr +
                ", assert=" + assertExpr +
                '}';
    }

    public static class RuleExprBuilder {
        private String name;
        private String resourceDefRef;
        private WithExpr withExpr = new WithExpr(Collections.emptyList());
        private AssertExpr assertExpr = new AssertExpr(Collections.emptyList());

        public RuleExprBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RuleExprBuilder setResourceDefRef(String resourceDefRef) {
            this.resourceDefRef = resourceDefRef;
            return this;
        }

        public RuleExprBuilder setWithExpr(WithExpr withExpr) {
            this.withExpr = withExpr;
            return this;
        }

        public RuleExprBuilder setAssertExp(AssertExpr assertExpr) {
            this.assertExpr = assertExpr;
            return this;
        }

        public RuleExpr build() {
            return new RuleExpr(name, resourceDefRef, withExpr, assertExpr);
        }
    }
}
