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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2Resource;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.CapacityReservation;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2CapacityReservationResource.RESOURCE_NAME,
        description = "Capacity Reservation"
)
public class EC2CapacityReservationResource extends EC2Resource {
    public static final String RESOURCE_NAME = "capacity_reservation";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "capacity_reservation_id",
            description = "The ID of the Capacity Reservation"
    )
    private final String capacityReservationId;

    @PropertyDefinition(
            name = "capacity_reservation_arn",
            description = "The Amazon Resource Name (ARN) of the Capacity Reservation"
    )
    private final String capacityReservationArn;

    @PropertyDefinition(
            name = "instance_type",
            description = "The type of instance for which the Capacity Reservation reserves capacity"
    )
    private final String instanceType;

    @PropertyDefinition(
            name = "instance_platform",
            description = "The type of operating system for which the Capacity Reservation reserves capacity"
    )
    private final String instancePlatform;

    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone in which the capacity is reserved"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "tenancy",
            description = "Indicates the tenancy of the Capacity Reservation",
            exampleValues = "default | dedicated"
    )
    private final String tenancy;

    @PropertyDefinition(
            name = "total_instance_count",
            description = "The total number of instances for which the Capacity Reservation reserves capacity"
    )
    private final Integer totalInstanceCount;

    @PropertyDefinition(
            name = "available_instance_count",
            description = "The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation"
    )
    private final Integer availableInstanceCount;

    @PropertyDefinition(
            name = "ebs_optimized",
            description = "Indicates whether the Capacity Reservation supports EBS-optimized instances"
    )
    private final Boolean ebsOptimized;

    @PropertyDefinition(
            name = "ephemeral_storage",
            description = "Indicates whether the Capacity Reservation supports instances with temporary, block-level storage."
    )
    private final Boolean ephemeralStorage;

    @PropertyDefinition(
            name = "state",
            description = "The current state of the Capacity Reservation",
            exampleValues = "active | expired | cancelled | pending | failed"
    )
    private final String state;

    @PropertyDefinition(
            name = "end_date",
            description = "The date and time at which the Capacity Reservation expires"
    )
    private final Date endDate;

    @PropertyDefinition(
            name = "end_date_type",
            description = "Indicates the way in which the Capacity Reservation ends",
            exampleValues = "unlimited | limited"
    )
    private final String endDateType;

    @PropertyDefinition(
            name = "instance_match_criteria",
            description = "Indicates the type of instance launches that the Capacity Reservation accepts",
            exampleValues = "open | targeted"
    )
    private final String instanceMatchCriteria;

    @PropertyDefinition(
            name = "create_date",
            description = "The date and time at which the Capacity Reservation was created"
    )
    private final Date createDate;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the Capacity Reservation"
    )
    private final List<KeyValue> tags;

    public EC2CapacityReservationResource(String ownerId, String region, String capacityReservationId,
                                          String capacityReservationArn, String instanceType, String instancePlatform,
                                          String availabilityZone, String tenancy, Integer totalInstanceCount,
                                          Integer availableInstanceCount, Boolean ebsOptimized, Boolean ephemeralStorage,
                                          String state, Date endDate, String endDateType, String instanceMatchCriteria,
                                          Date createDate, List<KeyValue> tags) {
        super(ownerId, region);
        this.capacityReservationId = capacityReservationId;
        this.capacityReservationArn = capacityReservationArn;
        this.instanceType = instanceType;
        this.instancePlatform = instancePlatform;
        this.availabilityZone = availabilityZone;
        this.tenancy = tenancy;
        this.totalInstanceCount = totalInstanceCount;
        this.availableInstanceCount = availableInstanceCount;
        this.ebsOptimized = ebsOptimized;
        this.ephemeralStorage = ephemeralStorage;
        this.state = state;
        this.endDate = endDate;
        this.endDateType = endDateType;
        this.instanceMatchCriteria = instanceMatchCriteria;
        this.createDate = createDate;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2CapacityReservationResource that = (EC2CapacityReservationResource) o;
        return Objects.equals(capacityReservationId, that.capacityReservationId) &&
                Objects.equals(capacityReservationArn, that.capacityReservationArn) &&
                Objects.equals(instanceType, that.instanceType) &&
                Objects.equals(instancePlatform, that.instancePlatform) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(tenancy, that.tenancy) &&
                Objects.equals(totalInstanceCount, that.totalInstanceCount) &&
                Objects.equals(availableInstanceCount, that.availableInstanceCount) &&
                Objects.equals(ebsOptimized, that.ebsOptimized) &&
                Objects.equals(ephemeralStorage, that.ephemeralStorage) &&
                Objects.equals(state, that.state) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(endDateType, that.endDateType) &&
                Objects.equals(instanceMatchCriteria, that.instanceMatchCriteria) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacityReservationId, capacityReservationArn, instanceType, instancePlatform,
                availabilityZone, tenancy, totalInstanceCount, availableInstanceCount, ebsOptimized, ephemeralStorage,
                state, endDate, endDateType, instanceMatchCriteria, createDate, tags);
    }

    public static EC2CapacityReservationResource fromSdk(String regionName, CapacityReservation capacityReservation) {
        if (Objects.isNull(capacityReservation)) {
            return null;
        }

        return new EC2CapacityReservationResource(
                capacityReservation.ownerId(),
                regionName,
                capacityReservation.capacityReservationId(),
                capacityReservation.capacityReservationArn(),
                capacityReservation.instanceType(),
                capacityReservation.instancePlatformAsString(),
                capacityReservation.availabilityZone(),
                capacityReservation.tenancyAsString(),
                capacityReservation.totalInstanceCount(),
                capacityReservation.availableInstanceCount(),
                capacityReservation.ebsOptimized(),
                capacityReservation.ephemeralStorage(),
                capacityReservation.stateAsString(),
                dateFromSdk(capacityReservation.endDate()),
                capacityReservation.endDateTypeAsString(),
                capacityReservation.instanceMatchCriteriaAsString(),
                dateFromSdk(capacityReservation.createDate()),
                tagsFromSdk(capacityReservation.tags())
        );
    }
}
