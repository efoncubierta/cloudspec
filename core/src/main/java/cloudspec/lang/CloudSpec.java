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
 * Define the structure of a Cloud Spec.
 */
public class CloudSpec {
    private final String name;
    private final List<GroupExpr> groups;

    /**
     * Constructor.
     *
     * @param name   Spec's name.
     * @param groups List of groups of rules.
     */
    public CloudSpec(String name, List<GroupExpr> groups) {
        this.name = name;
        this.groups = groups;
    }

    /**
     * Get the spec's name.
     *
     * @return Spec's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the spec's list of groups of rules.
     *
     * @return List of groups.
     */
    public List<GroupExpr> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "CloudSpec{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                '}';
    }
}