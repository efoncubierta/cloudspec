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
import cloudspec.aws.ec2.nested.EC2RecurringCharge
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.ReservedInstances
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "reserved_instances",
        description = "Reserved Instances"
)
data class EC2ReservedInstances(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone in which the Reserved Instance can be used"
        )
        val availabilityZone: String?,

        @PropertyDefinition(
                name = "duration",
                description = "The duration of the Reserved Instance, in seconds"
        )
        val duration: Long?,

        @PropertyDefinition(
                name = "end",
                description = "The time when the Reserved Instance expires"
        )
        val end: Instant?,

        @PropertyDefinition(
                name = "fixed_price",
                description = "The purchase price of the Reserved Instance"
        )
        val fixedPrice: Float?,

        @PropertyDefinition(
                name = "instance_count",
                description = "The number of reservations purchased"
        )
        val instanceCount: Int?,

        @PropertyDefinition(
                name = "instance_type",
                description = "The instance type on which the Reserved Instance can be used"
        )
        val instanceType: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "reserved_instances_id",
                description = "The ID of the Reserved Instance"
        )
        val reservedInstancesId: String?,

        @PropertyDefinition(
                name = "start",
                description = "The date and time the Reserved Instance started"
        )
        val start: Instant?,

        @PropertyDefinition(
                name = "state",
                description = "The state of the Reserved Instance purchase",
                exampleValues = "payment-pending | active | payment-failed | retired | queued | queue-deleted"
        )
        val state: String?,

        @PropertyDefinition(
                name = "usage_price",
                description = "The usage price of the Reserved Instance, per hour"
        )
        val usagePrice: Float?,

        @PropertyDefinition(
                name = "currency_code",
                description = "The currency of the Reserved Instance",
                exampleValues = "USD"
        )
        val currencyCode: String?,

        @PropertyDefinition(
                name = "instance_tenancy",
                description = "The tenancy of the instance",
                exampleValues = "default | dedicated | host"
        )
        val instanceTenancy: String?,

        @PropertyDefinition(
                name = "offering_class",
                description = "The offering class of the Reserved Instance",
                exampleValues = "standard | convertible"
        )
        val offeringClass: String?,

        @PropertyDefinition(
                name = "offering_type",
                description = "The Reserved Instance offering type",
                exampleValues = "Heavy Utilization | Medium Utilization | Light Utilization | No Upfront | Partial Upfront | All Upfront"
        )
        val offeringType: String?,

        @PropertyDefinition(
                name = "recurring_charges",
                description = "The recurring charge tag assigned to the resource"
        )
        val recurringCharges: List<EC2RecurringCharge>?,

        @PropertyDefinition(
                name = "scope",
                description = "The scope of the Reserved Instance"
        )
        val scope: String?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the resource"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, reservedInstances: ReservedInstances): EC2ReservedInstances {
            return reservedInstances.toEC2ReservedInstances(region)
        }
    }
}
