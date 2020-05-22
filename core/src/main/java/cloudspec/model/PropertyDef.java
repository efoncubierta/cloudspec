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
 * Define a property definition.
 * <p>
 * Properties must be defined so resources integrity can be validated.
 */
public class PropertyDef extends BaseMemberDef implements MemberDefsContainer {
    private final PropertyType propertyType;
    private final Boolean multiValued;
    private final List<PropertyDef> properties;
    private final List<AssociationDef> associations;

    /**
     * Constructor.
     *
     * @param name         Property name.
     * @param description  Property description.
     * @param propertyType Property type.
     * @param multiValued  Flag the property as multi-valued.
     */
    public PropertyDef(String name, String description, PropertyType propertyType, Boolean multiValued) {
        this(name, description, propertyType, multiValued, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Constructor.
     *
     * @param name         Property name.
     * @param description  Property description.
     * @param propertyType Property type.
     * @param multiValued  Flag the property as multi-valued.
     * @param properties   List of sub properties (when type is nested).
     * @param associations List of sub associations (when type is nested).
     */
    public PropertyDef(String name, String description, PropertyType propertyType, Boolean multiValued,
                       List<PropertyDef> properties, List<AssociationDef> associations) {
        super(name, description);
        this.propertyType = propertyType;
        this.multiValued = multiValued;
        this.properties = properties;
        this.associations = associations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PropertyDef)) {
            return false;
        }

        PropertyDef propertyDef = (PropertyDef) obj;

        return getName().equals(propertyDef.getName()) &&
                getDescription().equals(propertyDef.getDescription()) &&
                getPropertyType().equals(propertyDef.getPropertyType()) &&
                isMultiValued().equals(propertyDef.isMultiValued()) &&
                getProperties().size() == propertyDef.getProperties().size() &&
                getProperties().containsAll(propertyDef.getProperties()) &&
                getAssociations().size() == propertyDef.getAssociations().size() &&
                getAssociations().containsAll(propertyDef.getAssociations());
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
    public Boolean isMultiValued() {
        return multiValued;
    }

    @Override
    public List<PropertyDef> getProperties() {
        return properties;
    }

    @Override
    public List<AssociationDef> getAssociations() {
        return associations;
    }

    @Override
    public String toString() {
        return "PropertyDef{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", propertyType=" + propertyType +
                ", multiValued=" + multiValued +
                ", properties=" + properties +
                ", associations=" + associations +
                '}';
    }
}
