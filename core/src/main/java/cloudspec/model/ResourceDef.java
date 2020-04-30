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

public class ResourceDef {
    private final ResourceFqn resourceFqn;
    private final List<PropertyDef> properties;
    private final List<FunctionDef> functions;

    public ResourceDef(ResourceFqn resourceFqn, List<PropertyDef> properties, List<FunctionDef> functions) {
        this.resourceFqn = resourceFqn;
        this.properties = properties;
        this.functions = functions;
    }

    public ResourceFqn getResourceFqn() {
        return resourceFqn;
    }

    public Optional<PropertyDef> getProperty(String propertyName) {
        return getProperties().stream().filter(def -> def.getName().equals(propertyName)).findFirst();
    }

    public List<PropertyDef> getProperties() {
        return properties;
    }

    public Optional<FunctionDef> getFunction(String functionName) {
        return getFunctions().stream().filter(def -> def.getName().equals(functionName)).findFirst();
    }

    public List<FunctionDef> getFunctions() {
        return functions;
    }
}
