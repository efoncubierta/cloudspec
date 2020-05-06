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
 * Resources have members, that can be either properties or functions.
 * <p>
 * Resources are provided by the providers.
 */
public class Resource {
    private final ResourceFqn resourceFqn;
    private final String resourceId;
    private final List<Property> properties;
    private final List<Function> functions;

    public Resource(ResourceFqn resourceFqn, String resourceId, List<Property> properties, List<Function> functions) {
        this.resourceFqn = resourceFqn;
        this.resourceId = resourceId;
        this.properties = properties;
        this.functions = functions;
    }

    /**
     * Get resource's fully-qualified name.
     *
     * @return Resource fully-qualified name.
     */
    public ResourceFqn getFqn() {
        return resourceFqn;
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
     * Get a resource's function.
     *
     * @param functionName Function's name.
     * @return Optional function.
     */
    public Optional<Function> getFunction(String functionName) {
        return getFunctions().stream().filter(function -> function.getName().equals(functionName)).findFirst();
    }

    /**
     * Get all resource's functions.
     *
     * @return List of functions.
     */
    public List<Function> getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceFqn=" + resourceFqn +
                ", resourceId='" + resourceId + '\'' +
                ", properties=" + properties +
                ", functions=" + functions +
                '}';
    }
}
