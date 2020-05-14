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

import java.util.Collections;
import java.util.List;

/**
 * Define a resource property.
 */
public class Property extends BaseMember implements PropertiesContainer {
    private final Object value;

    /**
     * Constructor.
     *
     * @param name  Property name.
     * @param value Property value.
     */
    public Property(String name, Object value) {
        super(name);

        this.value = value;
    }

    /**
     * Get property value.
     *
     * @return Property value.
     */
    public Object getValue() {
        return value;
    }

    @Override
    public List<Property> getProperties() {
        // TODO review this code
        if (value instanceof List) {
            List<?> values = ((List<?>) value);
            if (values.size() > 0 && values.get(0) instanceof Property) {
                return ((List<Property>) values);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Property)) {
            return false;
        }

        Property property = (Property) obj;

        return getName().equals(property.getName()) &&
                getValue() instanceof List ?
                ((List<?>) getValue()).containsAll((List<?>) property.getValue()) :
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
