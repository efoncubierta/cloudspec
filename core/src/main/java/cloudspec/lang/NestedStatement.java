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
 * Statement for nested properties.
 */
public class NestedStatement implements Statement {
    private final String propertyName;
    private final List<Statement> statements;

    /**
     * Constructor.
     *
     * @param propertyName Property name.
     * @param statements   List of statements within the property.
     */
    public NestedStatement(String propertyName, List<Statement> statements) {
        this.propertyName = propertyName;
        this.statements = statements;
    }

    /**
     * Get the property name.
     *
     * @return Property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Get list of statements within the property.
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
                String.format("%s%s ( ", " ".repeat(spaces), propertyName)
        );

        sb.appendln(
                statements.stream()
                        .map(statement -> statement.toCloudSpecSyntax(spaces + 4))
                        .collect(Collectors.joining(" and \n"))
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
        NestedStatement that = (NestedStatement) o;
        return propertyName.equals(that.propertyName) &&
                statements.size() == that.statements.size() &&
                statements.containsAll(that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, statements);
    }

    @Override
    public String toString() {
        return "NestedStatement{" +
                "propertyName='" + propertyName + '\'' +
                ", statements=" + statements +
                '}';
    }
}
