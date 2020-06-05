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

    @Override
    public String toString() {
        return "Association{" +
                "name=" + getName() +
                ", resourceDefRef=" + resourceDefRef +
                ", resourceId=" + resourceId +
                '}';
    }
}
