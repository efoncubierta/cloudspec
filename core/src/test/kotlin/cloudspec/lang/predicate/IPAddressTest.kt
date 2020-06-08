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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IPAddressTest {
    private val faker = Faker()

    @Test
    fun shouldNotValidateRandomStrings() {
        val baseIp = faker.lorem().word()
        val targetIp = faker.lorem().word()
        assertFalse(IPAddress.eq.test(baseIp, targetIp))
        assertFalse(IPAddress.neq.test(baseIp, targetIp))
        assertFalse(IPAddress.lt.test(baseIp, targetIp))
        assertFalse(IPAddress.lte.test(baseIp, targetIp))
        assertFalse(IPAddress.gt.test(baseIp, targetIp))
        assertFalse(IPAddress.gte.test(baseIp, targetIp))
        assertFalse(IPAddress.withinNetwork.test(baseIp, targetIp))
        assertFalse(IPAddress.withoutNetwork.test(baseIp, targetIp))
    }

    @Test
    fun shouldValidateEqualIpAddresses() {
        val baseIp = "10.0.0.1"
        val targetIp = "10.0.0.1"
        assertTrue(IPAddress.eq.test(baseIp, targetIp))
        assertFalse(IPAddress.neq.test(baseIp, targetIp))
    }

    @Test
    fun shouldNotValidateEqualIpAddresses() {
        val baseIp = "10.0.0.1"
        val targetIp = "10.0.0.2"
        assertFalse(IPAddress.eq.test(baseIp, targetIp))
        assertTrue(IPAddress.neq.test(baseIp, targetIp))
    }

    @Test
    fun shouldValidateLessThanIpAddresses() {
        val baseIp = "10.0.0.1"
        val targetIp1 = "10.0.0.1"
        val targetIp2 = "10.0.0.2"
        assertTrue(IPAddress.lte.test(baseIp, targetIp1))
        assertTrue(IPAddress.lt.test(baseIp, targetIp2))
    }

    @Test
    fun shouldValidateGreaterThanIpAddresses() {
        val baseIp = "10.0.0.2"
        val targetIp1 = "10.0.0.2"
        val targetIp2 = "10.0.0.1"
        assertTrue(IPAddress.gte.test(baseIp, targetIp1))
        assertTrue(IPAddress.gt.test(baseIp, targetIp2))
    }

    @Test
    fun shouldValidateWithinNetwork() {
        val network = "10.0.0.0/24"
        val targetIp1 = "10.0.0.1"
        val targetIp2 = "10.0.1.1"
        assertTrue(IPAddress.withinNetwork.test(targetIp1, network))
        assertFalse(IPAddress.withinNetwork.test(targetIp2, network))
        assertTrue(IPAddress.withoutNetwork.test(targetIp2, network))
        assertFalse(IPAddress.withoutNetwork.test(targetIp1, network))
    }
}
