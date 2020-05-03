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

import cloudspec.annotation.ResourceDefinition;

import java.util.List;

/**
 * Define a CloudSpec's resource.
 * <p>
 * A resource is anything that can be evaluated. It can be an EC2 instance, an S3 bucket, an entire service, etc.
 * Resources have members, that can be either properties or functions.
 * <p>
 * Resources are provided by the providers.
 */
public abstract class BaseResource implements Resource {
    private final ResourceFqn resourceFqn;

    protected BaseResource() {
        // check the class is annotated
        if (!this.getClass().isAnnotationPresent(ResourceDefinition.class)) {
            throw new RuntimeException(
                    String.format("Resource class %s is not annotated with @ResourceDefinition", this.getClass().getCanonicalName())
            );
        }

        // get resource definition
        ResourceDefinition resourceDefAnnotation = this.getClass().getAnnotation(ResourceDefinition.class);
        this.resourceFqn = new ResourceFqn(
                resourceDefAnnotation.provider(),
                resourceDefAnnotation.group(),
                resourceDefAnnotation.name()
        );
    }

    @Override
    public ResourceFqn getResourceFqn() {
        return resourceFqn;
    }

    @Override
    public List<Property> getProperties() {
        return ResourceReflectionUtil.resourceToProperties(this);
    }

    @Override
    public List<Function> getFunctions() {
        return ResourceReflectionUtil.toFunctions(this);
    }
}
