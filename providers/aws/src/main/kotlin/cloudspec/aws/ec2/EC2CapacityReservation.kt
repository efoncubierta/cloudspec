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
import cloudspec.aws.AWSProvider
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2CapacityReservation.RESOURCE_NAME,
        description = EC2CapacityReservation.RESOURCE_DESCRIPTION
)
data class EC2CapacityReservation(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_CAPACITY_RESERVATION_ID,
                description = PROP_CAPACITY_RESERVATION_ID_D
        )
        val capacityReservationId: String,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_CAPACITY_RESERVATION_ARN,
                description = PROP_CAPACITY_RESERVATION_ARN_D
        )
        val capacityReservationArn: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_TYPE,
                description = PROP_INSTANCE_TYPE_D
        )
        val instanceType: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_PLATFORM,
                description = PROP_INSTANCE_PLATFORM_D
        )
        val instancePlatform: String?,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_TENANCY,
                description = PROP_TENANCY_D,
                exampleValues = "default | dedicated"
        )
        val tenancy: String?,

        @property:PropertyDefinition(
                name = PROP_TOTAL_INSTANCE_COUNT,
                description = PROP_TOTAL_INSTANCE_COUNT_D
        )
        val totalInstanceCount: Int?,

        @property:PropertyDefinition(
                name = PROP_AVAILABLE_INSTANCE_COUNT,
                description = PROP_AVAILABLE_INSTANCE_COUNT_D
        )
        val availableInstanceCount: Int?,

        @property:PropertyDefinition(
                name = PROP_EBS_OPTIMIZED,
                description = PROP_EBS_OPTIMIZED_D
        )
        val ebsOptimized: Boolean?,

        @property:PropertyDefinition(
                name = PROP_EPHEMERAL_STORAGE,
                description = PROP_EPHEMERAL_STORAGE_D
        )
        val ephemeralStorage: Boolean?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D,
                exampleValues = "active | expired | cancelled | pending | failed"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = PROP_END_DATE,
                description = PROP_END_DATE_D
        )
        val endDate: Instant?,

        @property:PropertyDefinition(
                name = PROP_END_DATE_TYPE,
                description = PROP_END_DATE_TYPE_D,
                exampleValues = "unlimited | limited"
        )
        val endDateType: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_MATCH_CRITERIA,
                description = PROP_INSTANCE_MATCH_CRITERIA_D,
                exampleValues = "open | targeted"
        )
        val instanceMatchCriteria: String?,

        @property:PropertyDefinition(
                name = PROP_CREATE_DATE,
                description = PROP_CREATE_DATE_D
        )
        val createDate: Instant?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "capacity_reservation"
        const val RESOURCE_DESCRIPTION = "Capacity Reservation"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_CAPACITY_RESERVATION_ID = "capacity_reservation_id"
        const val PROP_CAPACITY_RESERVATION_ID_D = "The ID of the Capacity Reservation"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the DHCP options set"
        const val PROP_CAPACITY_RESERVATION_ARN = "capacity_reservation_arn"
        const val PROP_CAPACITY_RESERVATION_ARN_D = "The Amazon Resource Name (ARN) of the Capacity Reservation"
        const val PROP_INSTANCE_TYPE = "instance_type"
        const val PROP_INSTANCE_TYPE_D = "The type of instance for which the Capacity Reservation reserves capacity"
        const val PROP_INSTANCE_PLATFORM = "instance_platform"
        const val PROP_INSTANCE_PLATFORM_D = "The type of operating system for which the Capacity Reservation reserves capacity"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone in which the capacity is reserved"
        const val PROP_TENANCY = "tenancy"
        const val PROP_TENANCY_D = "Indicates the tenancy of the Capacity Reservation"
        const val PROP_TOTAL_INSTANCE_COUNT = "total_instance_count"
        const val PROP_TOTAL_INSTANCE_COUNT_D = "The total number of instances for which the Capacity Reservation reserves capacity"
        const val PROP_AVAILABLE_INSTANCE_COUNT = "available_instance_count"
        const val PROP_AVAILABLE_INSTANCE_COUNT_D = "The remaining capacity. Indicates the number of instances that can be launched in the Capacity Reservation"
        const val PROP_EBS_OPTIMIZED = "ebs_optimized"
        const val PROP_EBS_OPTIMIZED_D = "Indicates whether the Capacity Reservation supports EBS-optimized instances"
        const val PROP_EPHEMERAL_STORAGE = "ephemeral_storage"
        const val PROP_EPHEMERAL_STORAGE_D = "Indicates whether the Capacity Reservation supports instances with temporary"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The current state of the Capacity Reservation"
        const val PROP_END_DATE = "end_date"
        const val PROP_END_DATE_D = "The date and time at which the Capacity Reservation expires"
        const val PROP_END_DATE_TYPE = "end_date_type"
        const val PROP_END_DATE_TYPE_D = "Indicates the way in which the Capacity Reservation ends"
        const val PROP_INSTANCE_MATCH_CRITERIA = "instance_match_criteria"
        const val PROP_INSTANCE_MATCH_CRITERIA_D = "Indicates the type of instance launches that the Capacity Reservation accepts"
        const val PROP_CREATE_DATE = "create_date"
        const val PROP_CREATE_DATE_D = "The date and time at which the Capacity Reservation was created"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the Capacity Reservation"
    }
}
