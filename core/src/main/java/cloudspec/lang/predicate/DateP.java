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
