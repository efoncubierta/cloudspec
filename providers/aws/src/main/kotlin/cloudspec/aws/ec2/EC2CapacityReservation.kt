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
package cloudspec.aws.ec2

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.CapacityReservation
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "capacity_reservation",
        description = "Capacity Reservation"
)
data class EC2CapacityReservation(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "capacity_reservation_id",
                description = "The ID of the Capacity Reservation"
        )
        val capacityReservationId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the DHCP options set"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "capacity_reservation_arn",
                description = "The Amazon Resource Name (ARN) of the Capacity Reservation"
        )
        val capacityReservationArn: String,
        @PropertyDefinition(
                name = "instance_type",
                description = "The type of instance for which the Capacity Reservation reserves capacity"
        )
        val instanceType: String,
        @PropertyDefinition(
                name = "instance_platform",
                description = "The type of operating system for which the Capacity Reservation reserves capacity"
        )
        val instancePlatform: String?,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone in which the capacity is reserved"
        )
        val availabilityZone: String,
        @PropertyDefinition(
                name = "tenancy",
                description = "Indicates the tenancy of the Capacity Reservation",
                exampleValues = "default | dedicated"
        )
        val tenancy: String,
        @PropertyDefinition(
                name = "total_instance_count",
                description = "The total number of instances for which the Capacity Reservation reserves capacity"
        )
        val totalInstanceCount: Int?,

        @PropertyDefinition(
                name = "available_instance_count",
                description = "The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation"
        )
        val availableInstanceCount: Int,
        @PropertyDefinition(
                name = "ebs_optimized",
                description = "Indicates whether the Capacity Reservation supports EBS-optimized instances"
        )
        val ebsOptimized: Boolean,
        @PropertyDefinition(
                name = "ephemeral_storage",
                description = "Indicates whether the Capacity Reservation supports instances with temporary, block-level storage."
        )
        val ephemeralStorage: Boolean?,

        @PropertyDefinition(
                name = "state",
                description = "The current state of the Capacity Reservation",
                exampleValues = "active | expired | cancelled | pending | failed"
        )
        val state: String,
        @PropertyDefinition(
                name = "end_date",
                description = "The date and time at which the Capacity Reservation expires"
        )
        val endDate: Instant,
        @PropertyDefinition(
                name = "end_date_type",
                description = "Indicates the way in which the Capacity Reservation ends",
                exampleValues = "unlimited | limited"
        )
        val endDateType: String,
        @PropertyDefinition(
                name = "instance_match_criteria",
                description = "Indicates the type of instance launches that the Capacity Reservation accepts",
                exampleValues = "open | targeted"
        )
        val instanceMatchCriteria: String?,

        @PropertyDefinition(
                name = "create_date",
                description = "The date and time at which the Capacity Reservation was created"
        )
        val createDate: Instant,
        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the Capacity Reservation"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, capacityReservation: CapacityReservation): EC2CapacityReservation {
            return capacityReservation.toEC2CapacityReservation(region)
        }
    }
}
