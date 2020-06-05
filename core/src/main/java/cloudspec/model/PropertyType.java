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
 * Property type.
 */
public enum PropertyType {
    STRING("string"),
    BOOLEAN("boolean"),
    NUMBER("number"),
    DATE("date"),
    KEY_VALUE("key_value"),
    NESTED("nested");

    private final String text;

    PropertyType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
