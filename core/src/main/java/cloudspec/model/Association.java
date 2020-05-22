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
 * Define a resource association.
 * <p>
 * Resources can be associated with other resources. For example, an EC2 instance is associated
 * to a subnet, or an EBS volume. CloudSpec supports validation of resources by its associations.
 */
public class Association extends BaseMember {
    private final ResourceDefRef resourceDefRef;
    private final String resourceId;

    /**
     * Constructor.
     *
     * @param name Association name.
     */
    public Association(String name, ResourceDefRef resourceDefRef, String resourceId) {
        super(name);
        this.resourceDefRef = resourceDefRef;
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Association)) {
            return false;
        }

        Association association = (Association) obj;

        return getName().equals(association.getName()) &&
                getResourceDefRef().equals(association.getResourceDefRef()) &&
                getResourceId().equals(association.getResourceId());
    }

    /**
     * Get the resource definition reference of the target resource.
     *
     * @return Resource definition reference.
     */
    public ResourceDefRef getResourceDefRef() {
        return resourceDefRef;
    }

    /**
     * Get the resource id of the target resource.
     *
     * @return Resource id.
     */
    public String getResourceId() {
        return resourceId;
    }

    @Override
    public String toString() {
        return "Association{" +
                "name=" + getName() +
                ", resourceDefRef=" + resourceDefRef +
                ", resourceId=" + resourceId +
                '}';
    }
}
