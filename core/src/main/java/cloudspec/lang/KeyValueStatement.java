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
        sb.append(valueToCloudSpecSyntax(predicate.getOriginalValue()));

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
