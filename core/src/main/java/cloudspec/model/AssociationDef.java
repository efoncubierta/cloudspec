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
 * Define an association definition.
 * <p>
 * Associations must be defined so resources integrity can be validated.
 */
public class AssociationDef extends BaseMemberDef {
    private final ResourceDefRef resourceDefRef;
    private final Boolean many;

    /**
     * Constructor.
     *
     * @param name           Association name.
     * @param description    Association description.
     * @param resourceDefRef Resource definition reference of the target resource.
     * @param many           Indicates whether the association is to many resources of the same kind
     */
    public AssociationDef(String name, String description, ResourceDefRef resourceDefRef, Boolean many) {
        super(name, description);
        this.resourceDefRef = resourceDefRef;
        this.many = many;
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
     * Check whether the association is to many resources of the same kind.
     *
     * @return True if association is to many.
     */
    public Boolean isMany() {
        return many;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof AssociationDef)) {
            return false;
        }

        var associationDef = (AssociationDef) obj;
        return getName().equals(associationDef.getName()) &&
                getDescription().equals(associationDef.getDescription()) &&
                getResourceDefRef().equals(associationDef.getResourceDefRef()) &&
                isMany().equals(associationDef.many);
    }

    @Override
    public String toString() {
        return "AssociationDef{" +
                "name=" + getName() +
                ", description=" + getDescription() +
                ", resourceDefRef=" + resourceDefRef +
                ", many=" + many +
                '}';
    }
}
