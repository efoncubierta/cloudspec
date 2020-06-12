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
package cloudspec.lang.predicate

import com.github.javafaker.Faker
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateCompareTest {
    private val faker = Faker()

    @Test
    fun shouldValidateBeforeDate() {
        val baseDate = faker.date().past(1000, TimeUnit.DAYS)
        val targetDate = Date()
        assertTrue(DateCompare.before.test(baseDate.toInstant(), targetDate.toInstant()))
        assertFalse(DateCompare.notBefore.test(baseDate.toInstant(), targetDate.toInstant()))
    }

    @Test
    fun shouldValidateAfterDate() {
        val baseDate = Date()
        val targetDate = faker.date().past(1000, TimeUnit.DAYS)
        assertTrue(DateCompare.after.test(baseDate.toInstant(), targetDate.toInstant()))
        assertFalse(DateCompare.notAfter.test(baseDate.toInstant(), targetDate.toInstant()))
    }
}
