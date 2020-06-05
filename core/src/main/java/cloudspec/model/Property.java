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

/**
 * Define a resource property.
 */
public abstract class Property<T> extends BaseMember implements MembersContainer {
    private final T value;

    /**
     * Constructor.
     *
     * @param name  Property name.
     * @param value Property value.
     */
    public Property(String name, T value) {
        super(name);

        this.value = value;
    }

    /**
     * Get property value.
     *
     * @return Property value.
     */
    public T getValue() {
        return value;
    }

    @Override
    public Properties getProperties() {
        if (value instanceof NestedPropertyValue) {
            return ((NestedPropertyValue) value).getProperties();
        }
        return new Properties();
    }

    @Override
    public Associations getAssociations() {
        if (value instanceof NestedPropertyValue) {
            return ((NestedPropertyValue) value).getAssociations();
        }
        return new Associations();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Property)) {
            return false;
        }

        Property<?> property = (Property<?>) obj;

        return getName().equals(property.getName()) &&
                getValue().equals(property.getValue());
    }

    @Override
    public String toString() {
        return "Property{" +
                "name=" + getName() +
                ", value=" + value +
                '}';
    }
}
