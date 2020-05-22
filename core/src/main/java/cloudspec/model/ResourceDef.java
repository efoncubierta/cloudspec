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
