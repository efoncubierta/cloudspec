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
import java.util.stream.Collectors;

/**
 * Statement for association.
 */
public class AssociationStatement implements Statement {
    private final String associationName;
    private final List<Statement> statements;

    /**
     * Constructor.
     *
     * @param associationName Association name.
     * @param statements      List of statements within the association.
     */
    public AssociationStatement(String associationName, List<Statement> statements) {
        this.associationName = associationName;
        this.statements = statements;
    }

    /**
     * Get the association name.
     *
     * @return Association name.
     */
    public String getAssociationName() {
        return associationName;
    }

    /**
     * Get list of statements within the association.
     *
     * @return List of statements.
     */
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();

        sb.appendln(
                String.format("%s>%s ( ", " ".repeat(spaces), associationName)
        );

        sb.appendln(
                statements.stream()
                        .map(statement -> statement.toCloudSpecSyntax(spaces + 4))
                        .collect(Collectors.joining(" And \n"))
        );

        sb.appendln(
                String.format("%s)", " ".repeat(spaces))
        );

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssociationStatement statement = (AssociationStatement) o;
        return associationName.equals(statement.associationName) &&
                statements.size() == statement.statements.size() &&
                statements.containsAll(statement.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associationName, statements);
    }

    @Override
    public String toString() {
        return "AssociationStatement{" +
                "associationName='" + associationName + '\'' +
                ", statements=" + statements +
                '}';
    }
}
