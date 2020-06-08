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

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "capacity_reservation",
        description = "Capacity Reservation"
)
data class EC2CapacityReservation(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "capacity_reservation_id",
                description = "The ID of the Capacity Reservation"
        )
        val capacityReservationId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the DHCP options set"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "capacity_reservation_arn",
                description = "The Amazon Resource Name (ARN) of the Capacity Reservation"
        )
        val capacityReservationArn: String?,

        @property:PropertyDefinition(
                name = "instance_type",
                description = "The type of instance for which the Capacity Reservation reserves capacity"
        )
        val instanceType: String?,

        @property:PropertyDefinition(
                name = "instance_platform",
                description = "The type of operating system for which the Capacity Reservation reserves capacity"
        )
        val instancePlatform: String?,

        @property:PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone in which the capacity is reserved"
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = "tenancy",
                description = "Indicates the tenancy of the Capacity Reservation",
                exampleValues = "default | dedicated"
        )
        val tenancy: String?,

        @property:PropertyDefinition(
                name = "total_instance_count",
                description = "The total number of instances for which the Capacity Reservation reserves capacity"
        )
        val totalInstanceCount: Int?,

        @property:PropertyDefinition(
                name = "available_instance_count",
                description = "The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation"
        )
        val availableInstanceCount: Int?,

        @property:PropertyDefinition(
                name = "ebs_optimized",
                description = "Indicates whether the Capacity Reservation supports EBS-optimized instances"
        )
        val ebsOptimized: Boolean?,

        @property:PropertyDefinition(
                name = "ephemeral_storage",
                description = "Indicates whether the Capacity Reservation supports instances with temporary, block-level storage."
        )
        val ephemeralStorage: Boolean?,

        @property:PropertyDefinition(
                name = "state",
                description = "The current state of the Capacity Reservation",
                exampleValues = "active | expired | cancelled | pending | failed"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = "end_date",
                description = "The date and time at which the Capacity Reservation expires"
        )
        val endDate: Instant?,

        @property:PropertyDefinition(
                name = "end_date_type",
                description = "Indicates the way in which the Capacity Reservation ends",
                exampleValues = "unlimited | limited"
        )
        val endDateType: String?,

        @property:PropertyDefinition(
                name = "instance_match_criteria",
                description = "Indicates the type of instance launches that the Capacity Reservation accepts",
                exampleValues = "open | targeted"
        )
        val instanceMatchCriteria: String?,

        @property:PropertyDefinition(
                name = "create_date",
                description = "The date and time at which the Capacity Reservation was created"
        )
        val createDate: Instant?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the Capacity Reservation"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
