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
