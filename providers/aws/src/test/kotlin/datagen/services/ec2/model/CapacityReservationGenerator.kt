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
package datagen.services.ec2.model

import datagen.BaseGenerator
import datagen.CommonGenerator
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.ec2.model.*
import java.util.*

object CapacityReservationGenerator : BaseGenerator() {
    fun capacityReservationId(): String {
        return UUID.randomUUID().toString()
    }

    fun capacityReservationArn(): Arn {
        return Arn.builder()
                .service("ec2")
                .region(CommonGenerator.region().id())
                .accountId(CommonGenerator.accountId())
                .partition("capacity-reservation")
                .resource(capacityReservationId())
                .build()
    }

    fun capacityReservations(size: Int? = null): List<CapacityReservation> {
        return listGenerator(size) { capacityReservation() }
    }

    fun capacityReservation(): CapacityReservation {
        return CapacityReservation.builder()
                .capacityReservationId(capacityReservationId())
                .ownerId(CommonGenerator.accountId())
                .capacityReservationArn(capacityReservationArn().toString())
                .availabilityZoneId(CommonGenerator.availabilityZone())
                .instanceType(InstanceGenerator.instanceType().toString())
                .instancePlatform(
                        valueFromArray(CapacityReservationInstancePlatform.values())
                )
                .availabilityZone(CommonGenerator.availabilityZone())
                .tenancy(
                        valueFromArray(CapacityReservationTenancy.values())
                )
                .totalInstanceCount(faker.random().nextInt(1, 10))
                .availableInstanceCount(faker.random().nextInt(1, 10))
                .ebsOptimized(faker.random().nextBoolean())
                .ephemeralStorage(faker.random().nextBoolean())
                .state(
                        valueFromArray(CapacityReservationState.values())
                )
                .endDate(futureInstant())
                .endDateType(
                        valueFromArray(EndDateType.values())
                )
                .instanceMatchCriteria(
                        valueFromArray(InstanceMatchCriteria.values())
                )
                .createDate(pastInstant())
                .tags(TagGenerator.tags())
                .build()
    }
}
