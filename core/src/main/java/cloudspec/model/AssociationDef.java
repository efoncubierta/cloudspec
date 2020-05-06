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
 * Define an association definition.
 * <p>
 * Associations must be defined so resources integrity can be validated.
 */
public class AssociationDef extends BaseMemberDef {
    private final ResourceDefRef resourceDefRef;

    /**
     * Constructor.
     *
     * @param name           Association name.
     * @param description    Association description.
     * @param resourceDefRef Resource definition reference of the target resource.
     */
    public AssociationDef(String name, String description, ResourceDefRef resourceDefRef) {
        super(name, description);
        this.resourceDefRef = resourceDefRef;
    }

    /**
     * Get the resource definition reference of the target resource.
     *
     * @return Resource definition reference.
     */
    public ResourceDefRef getResourceDefRef() {
        return resourceDefRef;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AssociationDef)) {
            return false;
        }

        return getName().equals(((AssociationDef) obj).getName()) &&
                getDescription().equals(((AssociationDef) obj).getDescription()) &&
                getResourceDefRef().equals(((AssociationDef) obj).getResourceDefRef());
    }

    @Override
    public String toString() {
        return "AssociationDef{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", resourceDefRef=" + resourceDefRef +
                '}';
    }
}
