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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.apache.tinkerpop.gremlin.process.traversal.Contains;
import org.apache.tinkerpop.gremlin.process.traversal.P;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Statement for property values.
 */
public class PropertyStatement implements Statement {
    protected final String propertyName;
    protected final P<?> predicate;

    /**
     * Constructor.
     *
     * @param propertyName Property name.
     * @param predicate    Predicate for the value.
     */
    public PropertyStatement(String propertyName, P<?> predicate) {
        this.propertyName = propertyName;
        this.predicate = predicate;
    }

    /**
     * Get the property name.
     *
     * @return Property name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Get the predicate for the value.
     *
     * @return Predicate.
     */
    public P<?> getPredicate() {
        return predicate;
    }

    @Override
    public String toCloudSpecSyntax(Integer spaces) {
        StrBuilder sb = new StrBuilder();

        sb.append(
                String.format("%s%s ", StringUtils.repeat(" ", spaces), propertyName)
        );

        sb.append(predicateToCloudSpecSyntax(predicate));
        sb.append(valueToCloudSpecSyntax(predicate.getOriginalValue()));

        return sb.toString();
    }

    protected String predicateToCloudSpecSyntax(P<?> predicate) {
        if (predicate.getBiPredicate().equals(Compare.eq)) {
            return "EQUAL TO ";
        } else if (predicate.getBiPredicate().equals(Compare.neq)) {
            return "NOT EQUAL TO ";
        } else if (predicate.getBiPredicate().equals(Contains.within)) {
            return "WITHIN ";
        } else if (predicate.getBiPredicate().equals(Contains.without)) {
            return "NOT WITHIN ";
        }

        return "UNKNOWN";
    }

    protected String valueToCloudSpecSyntax(Object value) {
        if (value instanceof List) {
            return String.format(
                    "[%s]",
                    ((List<?>) value).stream()
                            .map(this::valueToCloudSpecSyntax)
                            .collect(Collectors.joining(", "))
            );

        } else if (value instanceof String) {
            return String.format("\"%s\"", value);
        } else {
            return value.toString();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, predicate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyStatement that = (PropertyStatement) o;
        return propertyName.equals(that.propertyName) &&
                predicate.getBiPredicate().equals(that.predicate.getBiPredicate()) &&
                predicate.getOriginalValue().equals(that.predicate.getOriginalValue());
    }

    @Override
    public String toString() {
        return "PropertyStatement{" +
                "propertyName='" + propertyName + '\'' +
                ", predicate=" + predicate +
                '}';
    }
}
