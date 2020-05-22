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
import java.util.Stack;

/**
 * Interface for classes that manage properties and associations definitions.
 */
public interface MemberDefsContainer extends PropertyDefsContainer, AssociationDefsContainer {
    /**
     * Get a property by its path.
     *
     * @param path Property path
     * @return Optional property.
     */
    default Optional<PropertyDef> getPropertyByPath(Stack<String> path) {
        if (path.size() > 0) {
            Optional<PropertyDef> initialPropertyDefOpt = getProperty(path.get(0));
            return path.stream().skip(1).reduce(
                    initialPropertyDefOpt,
                    (propertyDefOpt, s) -> propertyDefOpt.isPresent() ? getProperty(s) : propertyDefOpt,
                    (propertyDefOpt, propertyDef2Opt) -> propertyDefOpt.flatMap(propertyDef -> propertyDef2Opt)
            );
        }
        return Optional.empty();
    }

    /**
     * Get an association by its path.
     *
     * @param path Association path
     * @return Optional association.
     */
    default Optional<AssociationDef> getAssociationByPath(Stack<String> path) {
        String associationName = path.pop();

        if (path.size() > 0) {
            Optional<PropertyDef> initialPropertyDefOpt = getProperty(path.get(0));
            return path.stream()
                    .skip(1)
                    .reduce(
                            initialPropertyDefOpt,
                            (propertyDefOpt, s) -> propertyDefOpt.isPresent() ? getProperty(s) : propertyDefOpt,
                            (propertyDefOpt, propertyDef2Opt) -> propertyDefOpt.flatMap(propertyDef -> propertyDef2Opt)
                    )
                    .flatMap(propertyDef -> propertyDef.getAssociation(associationName));
        }

        return getAssociation(associationName);
    }
}
