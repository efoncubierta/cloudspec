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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that manage properties based on {@link ArrayList}.
 */
public class Properties extends ArrayList<Property<?>> {
    /**
     * Constructor.
     *
     * @param properties Properties array
     */
    public Properties(Property<?>... properties) {
        super(Arrays.asList(properties));
    }

    /**
     * Constructor.
     *
     * @param propertiesStream Properties stream
     */
    public Properties(Stream<Property<?>> propertiesStream) {
        super(propertiesStream.collect(Collectors.toList()));
    }

    /**
     * Constructor.
     *
     * @param propertiesList Properties list
     */
    public Properties(List<Property<?>> propertiesList) {
        super(propertiesList);
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
                containsAll(properties);
    }
}
