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
 * Class that manage associations based on {@link ArrayList}.
 */
public class Associations extends ArrayList<Association> {
    /**
     * Constructor.
     *
     * @param associations Associations array
     */
    public Associations(Association... associations) {
        super(Arrays.asList(associations));
    }

    /**
     * Constructor.
     *
     * @param associations Associations stream
     */
    public Associations(Stream<Association> associations) {
        super(associations.collect(Collectors.toList()));
    }

    /**
     * Constructor.
     *
     * @param associations Associations list
     */
    public Associations(List<Association> associations) {
        super(associations);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Associations)) {
            return false;
        }

        Associations associations = (Associations) obj;

        return size() == associations.size() && containsAll(associations);
    }
}
