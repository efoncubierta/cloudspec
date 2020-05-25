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

import org.apache.commons.lang.StringUtils;
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
