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
import cloudspec.aws.ec2.nested.EC2RecurringCharge
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "reserved_instances",
        description = "Reserved Instances"
)
data class EC2ReservedInstances(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone in which the Reserved Instance can be used"
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = "duration",
                description = "The duration of the Reserved Instance, in seconds"
        )
        val duration: Long?,

        @property:PropertyDefinition(
                name = "end",
                description = "The time when the Reserved Instance expires"
        )
        val end: Instant?,

        @property:PropertyDefinition(
                name = "fixed_price",
                description = "The purchase price of the Reserved Instance"
        )
        val fixedPrice: Float?,

        @property:PropertyDefinition(
                name = "instance_count",
                description = "The number of reservations purchased"
        )
        val instanceCount: Int?,

        @property:PropertyDefinition(
                name = "instance_type",
                description = "The instance type on which the Reserved Instance can be used"
        )
        val instanceType: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "reserved_instances_id",
                description = "The ID of the Reserved Instance"
        )
        val reservedInstancesId: String?,

        @property:PropertyDefinition(
                name = "start",
                description = "The date and time the Reserved Instance started"
        )
        val start: Instant?,

        @property:PropertyDefinition(
                name = "state",
                description = "The state of the Reserved Instance purchase",
                exampleValues = "payment-pending | active | payment-failed | retired | queued | queue-deleted"
        )
        val state: String?,

        @property:PropertyDefinition(
                name = "usage_price",
                description = "The usage price of the Reserved Instance, per hour"
        )
        val usagePrice: Float?,

        @property:PropertyDefinition(
                name = "currency_code",
                description = "The currency of the Reserved Instance",
                exampleValues = "USD"
        )
        val currencyCode: String?,

        @property:PropertyDefinition(
                name = "instance_tenancy",
                description = "The tenancy of the instance",
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @property:PropertyDefinition(
                name = "offering_class",
                description = "The offering class of the Reserved Instance",
                exampleValues = "standard | convertible"
        )
        val offeringClass: String?,

        @property:PropertyDefinition(
                name = "offering_type",
                description = "The Reserved Instance offering type",
                exampleValues = "Heavy Utilization | Medium Utilization | Light Utilization | No Upfront | Partial Upfront | All Upfront"
        )
        val offeringType: String?,

        @property:PropertyDefinition(
                name = "recurring_charges",
                description = "The recurring charge tag assigned to the resource"
        )
        val recurringCharges: List<EC2RecurringCharge>?,

        @property:PropertyDefinition(
                name = "scope",
                description = "The scope of the Reserved Instance"
        )
        val scope: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the resource"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
