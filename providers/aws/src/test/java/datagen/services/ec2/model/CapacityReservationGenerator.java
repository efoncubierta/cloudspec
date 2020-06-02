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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import datagen.CommonGenerator;
import software.amazon.awssdk.arns.Arn;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.UUID;

public class CapacityReservationGenerator extends BaseGenerator {

    public static String capacityReservationId() {
        return UUID.randomUUID().toString();
    }

    public static Arn capacityReservationArn() {
        return Arn.builder()
                  .service("ec2")
                  .region(CommonGenerator.region().id())
                  .accountId(CommonGenerator.accountId())
                  .partition("capacity-reservation")
                  .resource(capacityReservationId())
                  .build();
    }

    public static List<CapacityReservation> capacityReservations() {
        return capacityReservations(faker.random().nextInt(1, 10));
    }

    public static List<CapacityReservation> capacityReservations(Integer n) {
        return listGenerator(n, CapacityReservationGenerator::capacityReservation);
    }

    public static CapacityReservation capacityReservation() {
        return CapacityReservation.builder()
                                  .capacityReservationId(capacityReservationId())
                                  .ownerId(CommonGenerator.accountId())
                                  .capacityReservationArn(capacityReservationArn().toString())
                                  .availabilityZoneId(CommonGenerator.availabilityZone())
                                  .instanceType(InstanceGenerator.instanceType().toString())
                                  .instancePlatform(
                                          fromArray(CapacityReservationInstancePlatform.values())
                                  )
                                  .availabilityZone(CommonGenerator.availabilityZone())
                                  .tenancy(
                                          fromArray(CapacityReservationTenancy.values())
                                  )
                                  .totalInstanceCount(faker.random().nextInt(1, 10))
                                  .availableInstanceCount(faker.random().nextInt(1, 10))
                                  .ebsOptimized(faker.random().nextBoolean())
                                  .ephemeralStorage(faker.random().nextBoolean())
                                  .state(
                                          fromArray(CapacityReservationState.values())
                                  )
                                  .endDate(futureDate().toInstant())
                                  .endDateType(
                                          fromArray(EndDateType.values())
                                  )
                                  .instanceMatchCriteria(
                                          fromArray(InstanceMatchCriteria.values())
                                  )
                                  .createDate(pastDate().toInstant())
                                  .tags(TagGenerator.tags())
                                  .build();
    }
}
