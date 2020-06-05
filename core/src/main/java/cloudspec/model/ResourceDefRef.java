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

import java.util.Optional;

/**
 * Define the reference of a resource definition.
 */
public class ResourceDefRef {
    private final String providerName;
    private final String groupName;
    private final String resourceName;

    /**
     * Constructor.
     *
     * @param providerName Provider name.
     * @param groupName    Group name.
     * @param resourceName Resource name.
     */
    public ResourceDefRef(String providerName, String groupName, String resourceName) {
        this.providerName = providerName;
        this.groupName = groupName;
        this.resourceName = resourceName;
    }

    /**
     * Build a resource definition reference object from string.
     *
     * @param refString Resource definition reference as string.
     * @return Optional resource definition reference.
     */
    public static Optional<ResourceDefRef> fromString(String refString) {
        // TODO manage null or malformed strings
        String[] parts = refString.split(":");
        if (parts.length != 3) {
            return Optional.empty();
        }

        return Optional.of(new ResourceDefRef(parts[0], parts[1], parts[2]));
    }

    /**
     * Get provider name.
     *
     * @return Provider name.
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Get group name.
     *
     * @return Group name.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Get resource name.
     *
     * @return Resource name.
     */
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ResourceDefRef)) {
            return false;
        }

        return toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s", providerName, groupName, resourceName);
    }
}
