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
package cloudspec.store;

import cloudspec.model.*;

import java.util.List;
import java.util.Optional;

public interface ResourceStore {
    void createResource(ResourceDefRef resourceDefRef, String resourceId);

    void createResource(ResourceDefRef resourceDefRef, String resourceId,
                        Properties properties, Associations associations);

    Boolean exists(ResourceDefRef resourceDefRef, String resourceId);

    Optional<Resource> getResource(ResourceDefRef resourceDefRef, String resourceId);

    List<Resource> getResourcesByDefinition(ResourceDefRef resourceDefRef);

    Properties getProperties(ResourceDefRef resourceDefRef, String resourceId);

    void setProperty(ResourceDefRef resourceDefRef, String resourceId, Property property);

    void setProperties(ResourceDefRef resourceDefRef, String resourceId, Properties properties);

    Associations getAssociations(ResourceDefRef resourceDefRef, String resourceId);

    void setAssociation(ResourceDefRef resourceDefRef, String resourceId, Association association);

    void setAssociations(ResourceDefRef resourceDefRef, String resourceId, Associations associations);
}
