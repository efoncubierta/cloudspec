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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that manage properties based on {@link ArrayList}.
 */
public class Properties extends ArrayList<Property> implements MembersContainer {
    private final Associations associations;

    /**
     * Constructor.
     *
     * @param properties Properties array
     */
    public Properties(Property... properties) {
        super(Arrays.asList(properties));

        associations = new Associations();
    }

    /**
     * Constructor.
     *
     * @param propertiesStream Properties stream
     */
    public Properties(Stream<Property> propertiesStream) {
        super(propertiesStream.collect(Collectors.toList()));

        associations = new Associations();
    }

    /**
     * Constructor.
     *
     * @param propertiesStream   Properties stream
     * @param associationsStream Associations stream
     */
    public Properties(Stream<Property> propertiesStream, Stream<Association> associationsStream) {
        super(propertiesStream.collect(Collectors.toList()));

        associations = new Associations(associationsStream);
    }

    /**
     * Constructor.
     *
     * @param propertiesList Properties list
     */
    public Properties(List<Property> propertiesList) {
        super(propertiesList);

        associations = new Associations();
    }

    /**
     * Constructor.
     *
     * @param propertiesList   Properties list
     * @param associationsList Associations list
     */
    public Properties(List<Property> propertiesList, List<Association> associationsList) {
        super(propertiesList);

        associations = new Associations(associationsList);
    }

    @Override
    public Properties getProperties() {
        return this;
    }

    @Override
    public Associations getAssociations() {
        return associations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Properties)) {
            return false;
        }

        Properties properties = (Properties) obj;

        return size() == properties.size() &&
                containsAll(properties) &&
                associations.size() == properties.associations.size() &&
                associations.containsAll(properties.associations);
    }
}