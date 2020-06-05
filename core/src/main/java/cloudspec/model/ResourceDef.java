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

import java.util.List;

/**
 * Define a resource definition.
 * <p>
 * All resources to be validated must be defined beforehand.
 */
public class ResourceDef implements MemberDefsContainer {
    private final ResourceDefRef ref;
    private final String description;
    private final List<PropertyDef> properties;
    private final List<AssociationDef> associations;

    /**
     * Constructor.
     *
     * @param ref          Resource definition reference.
     * @param description  Resource description.
     * @param properties   List of property definitions.
     * @param associations List of association definitions.
     */
    public ResourceDef(ResourceDefRef ref, String description, List<PropertyDef> properties, List<AssociationDef> associations) {
        this.ref = ref;
        this.description = description;
        this.properties = properties;
        this.associations = associations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ResourceDef)) {
            return false;
        }

        ResourceDef resourceDef = (ResourceDef) obj;

        return getRef().equals(resourceDef.getRef()) &&
                getDescription().equals(resourceDef.getDescription()) &&
                getProperties().size() == resourceDef.getProperties().size() &&
                getProperties().containsAll(resourceDef.getProperties()) &&
                getAssociations().size() == resourceDef.getAssociations().size() &&
                getAssociations().containsAll(resourceDef.getAssociations());
    }

    /**
     * Get resource definition reference.
     *
     * @return Resource definition reference.
     */
    public ResourceDefRef getRef() {
        return ref;
    }

    /**
     * Get resource description.
     *
     * @return Resource description.
     */
    public String getDescription() {
        return description;
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
        return "ResourceDef{" +
                "ref=" + ref +
                ", description='" + description + '\'' +
                ", properties=" + properties +
                ", associations=" + associations +
                '}';
    }
}
