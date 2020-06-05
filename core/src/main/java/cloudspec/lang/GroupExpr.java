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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Define a group expression.
 * <p>
 * Rules in a spec are grouped in groups.
 */
public class GroupExpr implements CloudSpecSyntaxProducer {
    private final String name;
    private final List<RuleExpr> rules;

    /**
     * Constructor.
     *
     * @param name  Group's name.
     * @param rules List of rules.
     */
    public GroupExpr(String name, List<RuleExpr> rules) {
        this.name = name;
        this.rules = rules;
    }

    public static GroupExprBuilder builder() {
        return new GroupExprBuilder();
    }

    /**
     * Get group's name.
     *
     * @return Group name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get group's list of rules.
     *
     * @return List of rules.
     */
    public List<RuleExpr> getRules() {
        return rules;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();
        sb.appendln(
                String.format("%sGroup \"%s\"", " ".repeat(spaces), name)
        );

        // add rules
        rules.stream()
                .map(rule -> rule.toCloudSpecSyntax(4))
                .forEach(sb::append);

        sb.appendNewLine();

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rules);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupExpr groupExpr = (GroupExpr) o;
        return name.equals(groupExpr.name) &&
                rules.size() == groupExpr.rules.size() &&
                rules.containsAll(groupExpr.rules);
    }

    @Override
    public String toString() {
        return "SpecGroup{" +
                "name='" + name + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static class GroupExprBuilder {
        private String name;
        private List<RuleExpr> rules = new ArrayList<>();

        public GroupExprBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public GroupExprBuilder addRules(RuleExpr... rules) {
            this.rules.addAll(Arrays.asList(rules));
            return this;
        }

        public GroupExpr build() {
            return new GroupExpr(name, rules);
        }
    }
}
