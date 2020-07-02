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
package cloudspec.aws.ec2

import cloudspec.aws.ec2.nested.toKeyValues
import datagen.CommonGenerator
import datagen.services.ec2.model.CapacityReservationGenerator
import org.junit.Test
import kotlin.test.assertEquals

class EC2CapacityReservationTest {
    @Test
    fun `should convert SDK model to CloudSpec model`() {
        val source = CapacityReservationGenerator.capacityReservation()
        val target = source.toEC2CapacityReservation(CommonGenerator.region().id())

        assertEquals(source.capacityReservationId(), target.capacityReservationId)
        assertEquals(source.ownerId(), target.ownerId)
        assertEquals(source.capacityReservationArn(), target.capacityReservationArn)
        assertEquals(source.instanceType(), target.instanceType)
        assertEquals(source.instancePlatform(), target.instancePlatform)
        assertEquals(source.availabilityZone(), target.availabilityZone)
        assertEquals(source.tenancy(), target.tenancy)
        assertEquals(source.totalInstanceCount(), target.totalInstanceCount)
        assertEquals(source.availableInstanceCount(), target.availableInstanceCount)
        assertEquals(source.ebsOptimized(), target.ebsOptimized)
        assertEquals(source.ephemeralStorage(), target.ephemeralStorage)
        assertEquals(source.state(), target.state)
        assertEquals(source.endDate(), target.endDate)
        assertEquals(source.endDateType(), target.endDateType)
        assertEquals(source.instanceMatchCriteria(), target.instanceMatchCriteria)
        assertEquals(source.createDate(), target.createDate)
        assertEquals(source.tags()?.toKeyValues(), target.tags)
    }
}
