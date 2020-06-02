/*-
 * #%L
 * CloudSpec AWS Provider
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
package datagen;

import com.github.javafaker.Faker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseGenerator {
    protected static final Faker faker = new Faker();

    protected static <T> List<T> listGenerator(Supplier<T> f) {
        return listGenerator(faker.random().nextInt(1, 10), f);
    }

    protected static <T> List<T> listGenerator(Integer n, Supplier<T> f) {
        return IntStream.range(0, n)
                        .mapToObj(i -> f.get())
                        .collect(Collectors.toList());
    }

    protected static <T> T fromArray(T[] a) {
        return a[faker.random().nextInt(0, a.length - 1)];
    }

    protected static <T> T fromList(List<T> l) {
        return l.get(faker.random().nextInt(0, l.size() - 1));
    }

    protected static Date pastDate() {
        return faker.date().past(100, TimeUnit.DAYS);
    }

    protected static Date futureDate() {
        return faker.date().future(100, TimeUnit.DAYS);
    }
}
