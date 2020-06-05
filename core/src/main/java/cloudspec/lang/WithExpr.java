/*-
 * #%L
 * CloudSpec Core Library
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.lang;

import org.apache.commons.lang.text.StrBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Define a 'with' expression.
 * <p>
 * With expressions ares used to filter resources.
 */
public class WithExpr implements CloudSpecSyntaxProducer {
    private final List<Statement> statements;

    /**
     * Constructor.
     *
     * @param statements List of statements.
     */
    public WithExpr(List<Statement> statements) {
        this.statements = statements;
    }

    public static WithExprBuilder builder() {
        return new WithExprBuilder();
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
                                        "%sWith", " ".repeat(spaces)
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
        WithExpr withExpr = (WithExpr) o;
        return statements.size() == withExpr.statements.size() &&
                statements.containsAll(withExpr.statements);
    }

    @Override
    public String toString() {
        return "WithExpr{" +
                "statements=" + statements +
                '}';
    }

    public static class WithExprBuilder {
        private List<Statement> statements;

        public WithExprBuilder setStatements(List<Statement> statements) {
            this.statements = statements;
            return this;
        }

        public WithExpr build() {
            return new WithExpr(statements);
        }
    }
}
