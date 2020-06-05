/*-
 * #%L
 * CloudSpec AWS Provider
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
package datagen

import com.github.javafaker.Faker
import java.time.Instant
import java.util.concurrent.TimeUnit

abstract class BaseGenerator {
    protected val faker = Faker()

    protected fun <T> listGenerator(size: Int? = null, f: () -> T): List<T> {
        return (0..(size ?: (1..10).random())).map { f() }
    }

    protected fun <T> valueFromArray(array: Array<T>): T {
        return array[(array.indices).random()]
    }

    protected fun <T> valueFromList(l: List<T>): T {
        return l[(l.indices).random()]
    }

    protected fun pastInstant(): Instant {
        return faker.date().past(100, TimeUnit.DAYS).toInstant()
    }

    protected fun futureInstant(): Instant {
        return faker.date().future(100, TimeUnit.DAYS).toInstant()
    }
}
