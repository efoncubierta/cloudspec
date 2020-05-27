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

import com.github.javafaker.Faker;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateCompareTest {
    private final Faker faker = new Faker();

    @Test
    public void shouldValidateBeforeDate() {
        Date baseDate = faker.date().past(1000, TimeUnit.DAYS);
        Date targetDate = new Date();

        assertTrue(DateCompare.before.test(baseDate, targetDate));
        assertFalse(DateCompare.notBefore.test(baseDate, targetDate));
    }

    @Test
    public void shouldValidateAfterDate() {
        Date baseDate = new Date();
        Date targetDate = faker.date().past(1000, TimeUnit.DAYS);

        assertTrue(DateCompare.after.test(baseDate, targetDate));
        assertFalse(DateCompare.notAfter.test(baseDate, targetDate));
    }
}
