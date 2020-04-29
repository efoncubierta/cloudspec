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
 * Define a group expression.
 * <p>
 * Rules in a spec are grouped in groups.
 */
public class GroupExpr {
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
    public String toString() {
        return "SpecGroup{" +
                "name='" + name + '\'' +
                ", rules=" + rules +
                '}';
    }
}
