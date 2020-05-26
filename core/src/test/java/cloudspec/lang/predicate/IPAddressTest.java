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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IPAddressTest {
    private final Faker faker = new Faker();

    @Test
    public void shouldNotValidateRandomStrings() {
        String baseIp = faker.lorem().word();
        String targetIp = faker.lorem().word();

        assertFalse(IPAddress.eq.test(baseIp, targetIp));
        assertFalse(IPAddress.neq.test(baseIp, targetIp));
        assertFalse(IPAddress.lt.test(baseIp, targetIp));
        assertFalse(IPAddress.lte.test(baseIp, targetIp));
        assertFalse(IPAddress.gt.test(baseIp, targetIp));
        assertFalse(IPAddress.gte.test(baseIp, targetIp));
        assertFalse(IPAddress.withinNetwork.test(baseIp, targetIp));
        assertFalse(IPAddress.withoutNetwork.test(baseIp, targetIp));
    }

    @Test
    public void shouldValidateEqualIpAddresses() {
        String baseIp = "10.0.0.1";
        String targetIp = "10.0.0.1";

        assertTrue(IPAddress.eq.test(baseIp, targetIp));
        assertFalse(IPAddress.neq.test(baseIp, targetIp));
    }

    @Test
    public void shouldNotValidateEqualIpAddresses() {
        String baseIp = "10.0.0.1";
        String targetIp = "10.0.0.2";

        assertFalse(IPAddress.eq.test(baseIp, targetIp));
        assertTrue(IPAddress.neq.test(baseIp, targetIp));
    }

    @Test
    public void shouldValidateLessThanIpAddresses() {
        String baseIp = "10.0.0.1";
        String targetIp1 = "10.0.0.1";
        String targetIp2 = "10.0.0.2";

        assertTrue(IPAddress.lte.test(baseIp, targetIp1));
        assertTrue(IPAddress.lt.test(baseIp, targetIp2));
    }

    @Test
    public void shouldValidateGreaterThanIpAddresses() {
        String baseIp = "10.0.0.2";
        String targetIp1 = "10.0.0.2";
        String targetIp2 = "10.0.0.1";

        assertTrue(IPAddress.gte.test(baseIp, targetIp1));
        assertTrue(IPAddress.gt.test(baseIp, targetIp2));
    }

    @Test
    public void shouldValidateWithinNetwork() {
        String network = "10.0.0.0/24";
        String targetIp1 = "10.0.0.1";
        String targetIp2 = "10.0.1.1";

        assertTrue(IPAddress.withinNetwork.test(targetIp1, network));
        assertFalse(IPAddress.withinNetwork.test(targetIp2, network));
        assertTrue(IPAddress.withoutNetwork.test(targetIp2, network));
        assertFalse(IPAddress.withoutNetwork.test(targetIp1, network));
    }
}
