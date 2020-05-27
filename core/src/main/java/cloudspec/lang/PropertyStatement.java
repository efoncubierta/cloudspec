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

import cloudspec.lang.predicate.DateCompare;
import cloudspec.lang.predicate.IPAddress;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.tinkerpop.gremlin.process.traversal.Compare;
import org.apache.tinkerpop.gremlin.process.traversal.Contains;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Text;
import org.apache.tinkerpop.gremlin.process.traversal.util.AndP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                String.format("%s%s ", " ".repeat(spaces), propertyName)
        );

        sb.append(predicateToCloudSpecSyntax(predicate));

        return sb.toString();
    }

    protected String predicateToCloudSpecSyntax(P<?> predicate) {
        var biPredicate = predicate.getBiPredicate();
        var originalValue = predicate.getOriginalValue();

        if (biPredicate.equals(Compare.eq)) {
            if (originalValue == "") {
                return "is empty";
            } else if (originalValue == null) {
                return "is null";
            } else {
                return "is equal to " + valueToCloudSpecSyntax(originalValue);
            }
        } else if (biPredicate.equals(Compare.neq)) {
            if (originalValue == "") {
                return "is not empty";
            } else if (originalValue == null) {
                return "is not null";
            } else {
                return "is not equal to " + valueToCloudSpecSyntax(originalValue);
            }
        } else if (biPredicate.equals(Compare.lt)) {
            return "is less than " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Compare.lte)) {
            return "is less than or equal to " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Compare.gt)) {
            return "is greater than " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Compare.gte)) {
            return "is greater than or equal to " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Contains.within)) {
            return "is within " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Contains.without)) {
            return "is not within " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.startingWith)) {
            return "is starting with " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.notStartingWith)) {
            return "is not starting with " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.endingWith)) {
            return "is ending with " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.notEndingWith)) {
            return "is not ending with " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.containing)) {
            return "is containing " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(Text.notContaining)) {
            return "is not containing " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.eq)) {
            return "is equal to ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.neq)) {
            return "is not equal to ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.lt)) {
            return "is less than ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.lte)) {
            return "is less than or equal to ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.gt)) {
            return "is greater than ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.gte)) {
            return "is greater than or equal to ip address " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.withinNetwork)) {
            return "is within network cidr " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.withoutNetwork)) {
            return "is not within network cidr " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(IPAddress.isIpv4)) {
            return "is ipv4";
        } else if (biPredicate.equals(IPAddress.isIpv6)) {
            return "is ipv6";
        } else if (biPredicate.equals(DateCompare.before)) {
            return "is before " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(DateCompare.notBefore)) {
            return "is not before " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(DateCompare.after)) {
            return "is after " + valueToCloudSpecSyntax(originalValue);
        } else if (biPredicate.equals(DateCompare.notAfter)) {
            return "is not after " + valueToCloudSpecSyntax(originalValue);
        } else if (predicate instanceof AndP<?>) {
            var andP = (AndP<?>) predicate;
            var leftPredicate = andP.getPredicates().get(0);
            var rightPredicate = andP.getPredicates().get(1);
            if ((leftPredicate.getBiPredicate().equals(Compare.gte) &&
                    rightPredicate.getBiPredicate().equals(Compare.lt)) ||
                    (leftPredicate.getBiPredicate().equals(DateCompare.notBefore) &&
                            rightPredicate.getBiPredicate().equals(DateCompare.before))
            ) {
                return "is between " + valueToCloudSpecSyntax(leftPredicate.getOriginalValue()) +
                        " and " + valueToCloudSpecSyntax(rightPredicate.getOriginalValue());
            }
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
        } else if (value instanceof Date) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return String.format("\"%s\"", df.format(value));
        } else {
            return value.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyStatement that = (PropertyStatement) o;

        return propertyName.equals(that.propertyName) &&
                predicate.equals(that.predicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, predicate);
    }

    @Override
    public String toString() {
        return "PropertyStatement{" +
                "propertyName='" + propertyName + '\'' +
                ", predicate=" + predicate +
                '}';
    }
}
