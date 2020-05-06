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
import java.util.Optional;

/**
 * Define a CloudSpec's resource.
 * <p>
 * A resource is anything that can be evaluated. It can be an EC2 instance, an S3 bucket, an entire service, etc.
 * Resources have members, that can be either properties or associations.
 * <p>
 * Resources are provided by the providers.
 */
public class Resource {
    private final ResourceDefRef resourceDefRef;
    private final String resourceId;
    private final List<Property> properties;
    private final List<Association> associations;

    public Resource(ResourceDefRef resourceDefRef, String resourceId, List<Property> properties, List<Association> associations) {
        this.resourceDefRef = resourceDefRef;
        this.resourceId = resourceId;
        this.properties = properties;
        this.associations = associations;
    }

    /**
     * Get resource's fully-qualified name.
     *
     * @return Resource fully-qualified name.
     */
    public ResourceDefRef getResourceDefRef() {
        return resourceDefRef;
    }

    /**
     * Get resource ID.
     *
     * @return Resource ID.
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * Get a resource's property.
     *
     * @param propertyName Property's name.
     * @return Optional property.
     */
    public Optional<Property> getProperty(String propertyName) {
        return getProperties()
                .stream()
                .filter(p -> p.getName().equals(propertyName))
                .findFirst();
    }

    /**
     * Get all resource's properties.
     *
     * @return List of properties.
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Get a resource's association.
     *
     * @param associationName Association's name.
     * @return Optional association.
     */
    public Optional<Association> getAssociation(String associationName) {
        return getAssociations().stream().filter(association -> association.getName().equals(associationName)).findFirst();
    }

    /**
     * Get all resource's associations.
     *
     * @return List of associations.
     */
    public List<Association> getAssociations() {
        return associations;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceDefRef=" + resourceDefRef +
                ", resourceId='" + resourceId + '\'' +
                ", properties=" + properties +
                ", associations=" + associations +
                '}';
    }
}
