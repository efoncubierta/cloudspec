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
 * Define a CloudSpec resource.
 * <p>
 * A resource is anything that can be evaluated. It can be an EC2 instance, an S3 bucket, an entire service, etc.
 * Resources have members, that can be either properties or associations.
 * <p>
 * Resources are provided by the providers.
 */
public class Resource implements MembersContainer {
    private final ResourceDefRef resourceDefRef;
    private final String resourceId;
    private final Properties properties;
    private final Associations associations;

    /**
     * Constructor.
     *
     * @param resourceDefRef Resource definition reference.
     * @param resourceId     Resource id.
     * @param properties     List of properties.
     * @param associations   List of associations.
     */
    public Resource(ResourceDefRef resourceDefRef, String resourceId, Properties properties, Associations associations) {
        this.resourceDefRef = resourceDefRef;
        this.resourceId = resourceId;
        this.properties = properties;
        this.associations = associations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Resource)) {
            return false;
        }

        Resource resource = (Resource) obj;

        return getResourceDefRef().equals(resource.getResourceDefRef()) &&
                getResourceId().equals(resource.getResourceId()) &&
                getProperties().equals(resource.getProperties()) &&
                getAssociations().equals(resource.getAssociations());
    }

    /**
     * Get resource definition reference.
     *
     * @return Resource definition reference.
     */
    public ResourceDefRef getResourceDefRef() {
        return resourceDefRef;
    }

    /**
     * Get resource id.
     *
     * @return Resource id.
     */
    public String getResourceId() {
        return resourceId;
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public Associations getAssociations() {
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
