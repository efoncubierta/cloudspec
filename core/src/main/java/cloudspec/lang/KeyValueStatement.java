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
package cloudspec.lang;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.Objects;

/**
 * Statement for key values.
 */
public class KeyValueStatement extends PropertyStatement {
    private final String key;

    /**
     * Constructor.
     *
     * @param propertyName Property name.
     * @param key          Key.
     * @param predicate    Predicate for the value.
     */
    public KeyValueStatement(String propertyName, String key, P<?> predicate) {
        super(propertyName, predicate);
        this.key = key;
    }

    /**
     * Get key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();

        sb.append(
                String.format("%s%s[\"%s\"] ", " ".repeat(spaces), propertyName, key)
        );

        sb.append(predicateToCloudSpecSyntax(predicate));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValueStatement that = (KeyValueStatement) o;
        return propertyName.equals(that.propertyName) &&
                key.equals(that.key) &&
                predicate.getBiPredicate().equals(that.predicate.getBiPredicate()) &&
                predicate.getOriginalValue().equals(that.predicate.getOriginalValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, key, predicate);
    }

    @Override
    public String toString() {
        return "PropertyStatement{" +
                "propertyName='" + propertyName + '\'' +
                ", key='" + key + '\'' +
                ", predicate=" + predicate +
                '}';
    }
}
