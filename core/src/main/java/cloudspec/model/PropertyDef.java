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
    private final String exampleValues;

    /**
     * Constructor.
     *
     * @param name          Property name.
     * @param description   Property description.
     * @param propertyType  Property type.
     * @param multiValued   Flag the property as multi-valued.
     * @param exampleValues Example values
     */
    public PropertyDef(String name, String description, PropertyType propertyType,
                       Boolean multiValued, String exampleValues) {
        this(name, description, propertyType, multiValued, exampleValues, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Constructor.
     *
     * @param name          Property name.
     * @param description   Property description.
     * @param propertyType  Property type.
     * @param multiValued   Flag the property as multi-valued.
     * @param exampleValues Example values
     * @param properties    List of sub properties (when type is nested).
     * @param associations  List of sub associations (when type is nested).
     */
    public PropertyDef(String name, String description, PropertyType propertyType, Boolean multiValued,
                       String exampleValues, List<PropertyDef> properties, List<AssociationDef> associations) {
        super(name, description);
        this.propertyType = propertyType;
        this.multiValued = multiValued;
        this.exampleValues = exampleValues;
        this.properties = properties;
        this.associations = associations;
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

    /**
     * Get example values for this property.
     *
     * @return Example values.
     */
    public String getExampleValues() {
        return exampleValues;
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
                getExampleValues().equals(propertyDef.getExampleValues()) &&
                getProperties().size() == propertyDef.getProperties().size() &&
                getProperties().containsAll(propertyDef.getProperties()) &&
                getAssociations().size() == propertyDef.getAssociations().size() &&
                getAssociations().containsAll(propertyDef.getAssociations());
    }

    @Override
    public String toString() {
        return "PropertyDef{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", propertyType=" + propertyType +
                ", multiValued=" + multiValued +
                ", exampleValues=" + exampleValues +
                ", properties=" + properties +
                ", associations=" + associations +
                '}';
    }
}
