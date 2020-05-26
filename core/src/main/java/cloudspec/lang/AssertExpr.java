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

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Define an 'assert' expression.
 * <p>
 * Assert expressions ares used to validate resources.
 */
public class AssertExpr implements CloudSpecSyntaxProducer {
    private final List<Statement> statements;

    /**
     * Constructor.
     *
     * @param statements List of statements.
     */
    public AssertExpr(List<Statement> statements) {
        this.statements = statements;
    }

    public static AssertExprBuilder builder() {
        return new AssertExprBuilder();
    }

    /**
     * Get list of statements.
     *
     * @return List of statements.
     */
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();

        IntStream.range(0, statements.size())
                .forEach(i -> {
                    if (i == 0) {
                        sb.appendln(
                                String.format(
                                        "%sAssert", " ".repeat(spaces)
                                )
                        );
                    } else {
                        sb.appendln(
                                String.format(
                                        "%sAnd", " ".repeat(spaces)
                                )
                        );
                    }
                    sb.appendln(statements.get(i).toCloudSpecSyntax(spaces + 4));
                });

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(statements);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssertExpr assertExpr = (AssertExpr) o;
        return statements.size() == assertExpr.statements.size() &&
                statements.containsAll(assertExpr.statements);
    }

    @Override
    public String toString() {
        return "AssertExpr{" +
                "statements=" + statements +
                '}';
    }

    public static class AssertExprBuilder {
        private List<Statement> statements;

        public AssertExprBuilder setStatement(List<Statement> statements) {
            this.statements = statements;
            return this;
        }

        public AssertExpr build() {
            return new AssertExpr(statements);
        }
    }
}
