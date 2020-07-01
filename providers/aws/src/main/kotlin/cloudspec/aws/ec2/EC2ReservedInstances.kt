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
import cloudspec.aws.ec2.nested.EC2RecurringCharge
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2ReservedInstances.RESOURCE_NAME,
        description = EC2ReservedInstances.RESOURCE_DESCRIPTION
)
data class EC2ReservedInstances(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D,
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_DURATION,
                description = PROP_DURATION_D
        )
        val duration: Long?,

        @property:PropertyDefinition(
                name = PROP_END,
                description = PROP_END_D
        )
        val end: Instant?,

        @property:PropertyDefinition(
                name = PROP_FIXED_PRICE,
                description = PROP_FIXED_PRICE_D
        )
        val fixedPrice: Float?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_COUNT,
                description = PROP_INSTANCE_COUNT_D
        )
        val instanceCount: Int?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_TYPE,
                description = PROP_INSTANCE_TYPE_D
        )
        val instanceType: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_RESERVED_INSTANCES_ID,
                description = PROP_RESERVED_INSTANCES_ID_D
        )
        val reservedInstancesId: String,

        @property:PropertyDefinition(
                name = PROP_START,
                description = PROP_START_D
        )
        val start: Instant?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D,
                exampleValues = "payment-pending | active | payment-failed | retired | queued | queue-deleted"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = PROP_USAGE_PRICE,
                description = PROP_USAGE_PRICE_D
        )
        val usagePrice: Float?,

        @property:PropertyDefinition(
                name = PROP_CURRENCY_CODE,
                description = PROP_CURRENCY_CODE_D,
                exampleValues = "USD"
        )
        val currencyCode: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_TENANCY,
                description = PROP_INSTANCE_TENANCY_D,
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @property:PropertyDefinition(
                name = PROP_OFFERING_CLASS,
                description = PROP_OFFERING_CLASS_D,
                exampleValues = "standard | convertible"
        )
        val offeringClass: String?,

        @property:PropertyDefinition(
                name = PROP_OFFERING_TYPE,
                description = PROP_OFFERING_TYPE_D,
                exampleValues = "Heavy Utilization | Medium Utilization | Light Utilization | No Upfront | Partial Upfront | All Upfront"
        )
        val offeringType: String?,

        @property:PropertyDefinition(
                name = PROP_RECURRING_CHARGES,
                description = PROP_RECURRING_CHARGES_D
        )
        val recurringCharges: List<EC2RecurringCharge>?,

        @property:PropertyDefinition(
                name = PROP_SCOPE,
                description = PROP_SCOPE_D
        )
        val scope: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "reserved_instances"
        const val RESOURCE_DESCRIPTION = "Reserved Instances"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone in which the Reserved Instance can be used"
        const val PROP_DURATION = "duration"
        const val PROP_DURATION_D = "The duration of the Reserved Instance, in seconds"
        const val PROP_END = "end"
        const val PROP_END_D = "The time when the Reserved Instance expires"
        const val PROP_FIXED_PRICE = "fixed_price"
        const val PROP_FIXED_PRICE_D = "The purchase price of the Reserved Instance"
        const val PROP_INSTANCE_COUNT = "instance_count"
        const val PROP_INSTANCE_COUNT_D = "The number of reservations purchased"
        const val PROP_INSTANCE_TYPE = "instance_type"
        const val PROP_INSTANCE_TYPE_D = "The instance type on which the Reserved Instance can be used"
        const val PROP_RESERVED_INSTANCES_ID = "reserved_instances_id"
        const val PROP_RESERVED_INSTANCES_ID_D = "The ID of the Reserved Instance"
        const val PROP_START = "start"
        const val PROP_START_D = "The date and time the Reserved Instance started"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the Reserved Instance purchase"
        const val PROP_USAGE_PRICE = "usage_price"
        const val PROP_USAGE_PRICE_D = "The usage price of the Reserved Instance, per hour"
        const val PROP_CURRENCY_CODE = "currency_code"
        const val PROP_CURRENCY_CODE_D = "The currency of the Reserved Instance"
        const val PROP_INSTANCE_TENANCY = "instance_tenancy"
        const val PROP_INSTANCE_TENANCY_D = "The tenancy of the instance"
        const val PROP_OFFERING_CLASS = "offering_class"
        const val PROP_OFFERING_CLASS_D = "The offering class of the Reserved Instance"
        const val PROP_OFFERING_TYPE = "offering_type"
        const val PROP_OFFERING_TYPE_D = "The Reserved Instance offering type"
        const val PROP_RECURRING_CHARGES = "recurring_charges"
        const val PROP_RECURRING_CHARGES_D = "The recurring charge tag assigned to the resource"
        const val PROP_SCOPE = "scope"
        const val PROP_SCOPE_D = "The scope of the Reserved Instance"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the resource"
    }
}
