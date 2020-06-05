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
package cloudspec.model;

public class NestedPropertyValue implements MembersContainer {
    private final Properties properties;
    private final Associations associations;

    public NestedPropertyValue(Properties properties, Associations associations) {
        this.properties = properties;
        this.associations = associations;
    }

    @Override
    public Associations getAssociations() {
        return associations;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NestedPropertyValue that = (NestedPropertyValue) o;
        return properties.equals(that.properties) &&
                associations.equals(that.associations);
    }

    @Override
    public String toString() {
        return "NestedPropertyValue{" +
                "properties=" + properties +
                ", associations=" + associations +
                '}';
    }
}
