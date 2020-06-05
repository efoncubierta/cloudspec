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
