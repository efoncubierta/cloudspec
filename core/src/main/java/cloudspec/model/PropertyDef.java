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
 * Define a property definition.
 * <p>
 * Properties must be defined so resources integrity can be validated.
 */
public class PropertyDef extends BaseMemberDef {
    private final PropertyType propertyType;
    private final Boolean isArray;

    /**
     * Constructor.
     *
     * @param name         Property name.
     * @param description  Property description.
     * @param propertyType Property type.
     * @param isArray      Flag the property as multi-valued.
     */
    public PropertyDef(String name, String description, PropertyType propertyType, Boolean isArray) {
        super(name, description);
        this.propertyType = propertyType;
        this.isArray = isArray;
    }

    /**
     * Get the property type.
     *
     * @return Property type.
     */
    public PropertyType getPropertyType() {
        return propertyType;
    }

    /**
     * Check if the property is multi-valued.
     *
     * @return True if the property is multi-valued. False otherwise.
     */
    public Boolean isArray() {
        return isArray;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PropertyDef)) {
            return false;
        }

        return getName().equals(((PropertyDef) obj).getName()) &&
                getDescription().equals(((PropertyDef) obj).getDescription()) &&
                getPropertyType().equals(((PropertyDef) obj).getPropertyType()) &&
                isArray().equals(((PropertyDef) obj).isArray());
    }

    @Override
    public String toString() {
        return "PropertyDef{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", propertyType=" + propertyType +
                ", isArray=" + isArray +
                '}';
    }
}
