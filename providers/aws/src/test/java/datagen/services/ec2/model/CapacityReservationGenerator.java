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
import software.amazon.awssdk.services.ec2.model.CapacityReservation;

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
                  .build();
    }

    public static CapacityReservation capacityReservation() {
        // this.region = region;
        //        this.capacityReservationId = capacityReservationId;
        //        this.ownerId = ownerId;
        //        this.capacityReservationArn = capacityReservationArn;
        //        this.instanceType = instanceType;
        //        this.instancePlatform = instancePlatform;
        //        this.availabilityZone = availabilityZone;
        //        this.tenancy = tenancy;
        //        this.totalInstanceCount = totalInstanceCount;
        //        this.availableInstanceCount = availableInstanceCount;
        //        this.ebsOptimized = ebsOptimized;
        //        this.ephemeralStorage = ephemeralStorage;
        //        this.state = state;
        //        this.endDate = endDate;
        //        this.endDateType = endDateType;
        //        this.instanceMatchCriteria = instanceMatchCriteria;
        //        this.createDate = createDate;
        //        this.tags = tags;
        return CapacityReservation.builder()
                                  .capacityReservationId(capacityReservationId())
                                  .ownerId(CommonGenerator.accountId())
                                  .capacityReservationArn(capacityReservationArn().toString())
                                  .build();
    }
}
