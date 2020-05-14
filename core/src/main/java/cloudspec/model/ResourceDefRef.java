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

    /**
     * Build a resource definition reference object from string.
     *
     * @param refString Resource definition reference as string.
     * @return Optional resource definition reference.
     */
    public static Optional<ResourceDefRef> fromString(String refString) {
        // TODO manage null or malformed strings
        String[] parts = refString.split(":");
        if(parts.length != 3) {
            return Optional.empty();
        }

        return Optional.of(new ResourceDefRef(parts[0], parts[1], parts[2]));
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
