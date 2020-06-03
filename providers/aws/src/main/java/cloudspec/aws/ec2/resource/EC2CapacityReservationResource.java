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
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.CapacityReservation;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @IdDefinition
    @PropertyDefinition(
            name = "capacity_reservation_id",
            description = "The ID of the Capacity Reservation"
    )
    private final String capacityReservationId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the DHCP options set"
    )
    private final String ownerId;

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
    private final Instant endDate;

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
    private final Instant createDate;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the Capacity Reservation"
    )
    private final List<KeyValue> tags;

    public EC2CapacityReservationResource(String region, String capacityReservationId, String ownerId,
                                          String capacityReservationArn, String instanceType, String instancePlatform,
                                          String availabilityZone, String tenancy, Integer totalInstanceCount,
                                          Integer availableInstanceCount, Boolean ebsOptimized, Boolean ephemeralStorage,
                                          String state, Instant endDate, String endDateType, String instanceMatchCriteria,
                                          Instant createDate, List<KeyValue> tags) {
        this.region = region;
        this.capacityReservationId = capacityReservationId;
        this.ownerId = ownerId;
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
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof CapacityReservation))) {
            return false;
        }

        if (o instanceof CapacityReservation) {
            return sdkEquals((CapacityReservation) o);
        }

        EC2CapacityReservationResource that = (EC2CapacityReservationResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(capacityReservationId, that.capacityReservationId) &&
                Objects.equals(ownerId, that.ownerId) &&
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

    private boolean sdkEquals(CapacityReservation that) {
        return Objects.equals(capacityReservationId, that.capacityReservationId()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(capacityReservationArn, that.capacityReservationArn()) &&
                Objects.equals(instanceType, that.instanceType()) &&
                Objects.equals(instancePlatform, that.instancePlatformAsString()) &&
                Objects.equals(availabilityZone, that.availabilityZone()) &&
                Objects.equals(tenancy, that.tenancyAsString()) &&
                Objects.equals(totalInstanceCount, that.totalInstanceCount()) &&
                Objects.equals(availableInstanceCount, that.availableInstanceCount()) &&
                Objects.equals(ebsOptimized, that.ebsOptimized()) &&
                Objects.equals(ephemeralStorage, that.ephemeralStorage()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(endDate, that.endDate()) &&
                Objects.equals(endDateType, that.endDateTypeAsString()) &&
                Objects.equals(instanceMatchCriteria, that.instanceMatchCriteriaAsString()) &&
                Objects.equals(createDate, that.createDate()) &&
                Objects.equals(tags, tagsFromSdk(that.tags()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, capacityReservationId, ownerId, capacityReservationArn, instanceType,
                instancePlatform, availabilityZone, tenancy, totalInstanceCount, availableInstanceCount,
                ebsOptimized, ephemeralStorage, state, endDate, endDateType, instanceMatchCriteria, createDate, tags);
    }

    public static EC2CapacityReservationResource fromSdk(String regionName, CapacityReservation capacityReservation) {
        return Optional.ofNullable(capacityReservation)
                       .map(v -> new EC2CapacityReservationResource(
                               regionName,
                               v.capacityReservationId(),
                               v.ownerId(),
                               v.capacityReservationArn(),
                               v.instanceType(),
                               v.instancePlatformAsString(),
                               v.availabilityZone(),
                               v.tenancyAsString(),
                               v.totalInstanceCount(),
                               v.availableInstanceCount(),
                               v.ebsOptimized(),
                               v.ephemeralStorage(),
                               v.stateAsString(),
                               v.endDate(),
                               v.endDateTypeAsString(),
                               v.instanceMatchCriteriaAsString(),
                               v.createDate(),
                               tagsFromSdk(v.tags())
                       ))
                       .orElse(null);
    }
}
