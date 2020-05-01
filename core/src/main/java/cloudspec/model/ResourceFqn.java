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

public class ResourceFqn {
    private final String providerName;
    private final String groupName;
    private final String resourceName;

    public ResourceFqn(String providerName, String groupName, String resourceName) {
        this.providerName = providerName;
        this.groupName = groupName;
        this.resourceName = resourceName;
    }

    public static ResourceFqn fromString(String resourceFqn) {
        // TODO manage null or malformed strings
        String[] parts = resourceFqn.split(":");
        return new ResourceFqn(parts[0], parts[1], parts[2]);
    }

    public String getProviderName() {
        return providerName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ResourceFqn)) {
            return false;
        }

        return toString().equals(obj.toString());
    }

    @Override
    public String toString() {
        return String.format("%s:%s:%s", providerName, groupName, resourceName);
    }
}
