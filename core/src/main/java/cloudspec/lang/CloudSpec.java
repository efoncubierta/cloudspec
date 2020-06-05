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

/**
 * Define the structure of a Cloud Spec.
 */
public class CloudSpec implements CloudSpecSyntaxProducer {
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

    public static CloudSpecBuilder builder() {
        return new CloudSpecBuilder();
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
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();
        sb.appendln(
                String.format("%sSpec \"%s\"", " ".repeat(spaces), name)
        );

        // add groups
        groups.stream()
                .map(group -> group.toCloudSpecSyntax(spaces))
                .forEach(sb::append);

        sb.appendNewLine();

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloudSpec cloudSpec = (CloudSpec) o;
        return name.equals(cloudSpec.name) &&
                groups.size() == cloudSpec.groups.size() &&
                groups.containsAll(cloudSpec.groups);
    }

    @Override
    public String toString() {
        return "CloudSpec{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                '}';
    }

    public static class CloudSpecBuilder {
        private String name;
        private List<GroupExpr> groups = new ArrayList<>();

        public CloudSpecBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public CloudSpecBuilder addGroups(GroupExpr... groups) {
            this.groups.addAll(Arrays.asList(groups));
            return this;
        }

        public CloudSpec build() {
            return new CloudSpec(name, groups);
        }
    }
}
