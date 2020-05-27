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
package cloudspec.lang.predicate;

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.util.AndP;

import java.util.Arrays;
import java.util.Date;
import java.util.function.BiPredicate;

public class DateP extends P<Date> {
    public DateP(final BiPredicate<Date, Date> biPredicate, final Date value) {
        super(biPredicate, value);
    }

    public boolean equals(final Object other) {
        return other instanceof DateP && super.equals(other);
    }

    public String toString() {
        return null == this.originalValue ?
                this.biPredicate.toString() :
                this.biPredicate.toString() + "(" + this.originalValue + ")";
    }

    public DateP negate() {
        return new DateP(this.biPredicate.negate(), this.originalValue);
    }

    public DateP clone() {
        return (DateP) super.clone();
    }

    public static DateP before(final Date value) {
        return new DateP(DateCompare.before, value);
    }

    public static DateP notBefore(final Date value) {
        return new DateP(DateCompare.notBefore, value);
    }

    public static DateP after(final Date value) {
        return new DateP(DateCompare.after, value);
    }

    public static DateP notAfter(final Date value) {
        return new DateP(DateCompare.notAfter, value);
    }

    public static P<Date> between(final Date from, final Date to) {
        return new AndP<>(Arrays.asList(
                new DateP(DateCompare.notBefore, from), new DateP(DateCompare.before, to)
        ));
    }
}
