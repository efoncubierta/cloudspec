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
