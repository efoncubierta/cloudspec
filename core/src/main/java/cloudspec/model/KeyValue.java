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
 * Class for key-value properties.
 */
public class KeyValue {
    private final String key;
    private final Object value;

    /**
     * Constructor.
     *
     * @param key   Key
     * @param value Value
     */
    public KeyValue(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the value.
     *
     * @return Value.
     */
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof KeyValue)) {
            return false;
        }

        KeyValue keyValue = (KeyValue) obj;

        return getKey().equals(keyValue.getKey()) &&
                getValue().equals(keyValue.getValue());
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
