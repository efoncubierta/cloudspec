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
