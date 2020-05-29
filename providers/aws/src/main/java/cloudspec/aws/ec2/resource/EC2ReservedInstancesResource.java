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
import cloudspec.aws.ec2.resource.nested.EC2RecurringCharge;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.ReservedInstances;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2ReservedInstancesResource.RESOURCE_NAME,
        description = "Reserved Instances"
)
public class EC2ReservedInstancesResource extends EC2Resource {
    public static final String RESOURCE_NAME = "reserved_instances";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone in which the Reserved Instance can be used"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "duration",
            description = "The duration of the Reserved Instance, in seconds"
    )
    private final Long duration;

    @PropertyDefinition(
            name = "end",
            description = "The time when the Reserved Instance expires"
    )
    private final Date end;

    @PropertyDefinition(
            name = "fixed_price",
            description = "The purchase price of the Reserved Instance"
    )
    private final Double fixedPrice;

    @PropertyDefinition(
            name = "instance_count",
            description = "The number of reservations purchased"
    )
    private final Integer instanceCount;

    @PropertyDefinition(
            name = "instance_type",
            description = "The instance type on which the Reserved Instance can be used"
    )
    private final String instanceType;

    @IdDefinition
    @PropertyDefinition(
            name = "reserved_instances_id",
            description = "The ID of the Reserved Instance"
    )
    private final String reservedInstancesId;

    @PropertyDefinition(
            name = "start",
            description = "The date and time the Reserved Instance started"
    )
    private final Date start;

    @PropertyDefinition(
            name = "state",
            description = "The state of the Reserved Instance purchase",
            exampleValues = "payment-pending | active | payment-failed | retired | queued | queue-deleted"
    )
    private final String state;

    @PropertyDefinition(
            name = "usage_price",
            description = "The usage price of the Reserved Instance, per hour"
    )
    private final Double usagePrice;

    @PropertyDefinition(
            name = "currency_code",
            description = "The currency of the Reserved Instance",
            exampleValues = "USD"
    )
    private final String currencyCode;

    @PropertyDefinition(
            name = "instance_tenancy",
            description = "The tenancy of the instance",
            exampleValues = "default | dedicated | host"
    )
    private final String instanceTenancy;

    @PropertyDefinition(
            name = "offering_class",
            description = "The offering class of the Reserved Instance",
            exampleValues = "standard | convertible"
    )
    private final String offeringClass;

    @PropertyDefinition(
            name = "offering_type",
            description = "The Reserved Instance offering type",
            exampleValues = "Heavy Utilization | Medium Utilization | Light Utilization | No Upfront | Partial Upfront | All Upfront"
    )
    private final String offeringType;

    @PropertyDefinition(
            name = "recurring_charges",
            description = "The recurring charge tag assigned to the resource"
    )
    private final List<EC2RecurringCharge> recurringCharges;

    @PropertyDefinition(
            name = "scope",
            description = "The scope of the Reserved Instance"
    )
    private final String scope;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the resource"
    )
    private final List<KeyValue> tags;

    public EC2ReservedInstancesResource(String region, String availabilityZone, Long duration, Date end,
                                        Double fixedPrice, Integer instanceCount, String instanceType,
                                        String reservedInstancesId, Date start, String state, Double usagePrice,
                                        String currencyCode, String instanceTenancy, String offeringClass,
                                        String offeringType, List<EC2RecurringCharge> recurringCharges, String scope,
                                        List<KeyValue> tags) {
        this.region = region;
        this.availabilityZone = availabilityZone;
        this.duration = duration;
        this.end = end;
        this.fixedPrice = fixedPrice;
        this.instanceCount = instanceCount;
        this.instanceType = instanceType;
        this.reservedInstancesId = reservedInstancesId;
        this.start = start;
        this.state = state;
        this.usagePrice = usagePrice;
        this.currencyCode = currencyCode;
        this.instanceTenancy = instanceTenancy;
        this.offeringClass = offeringClass;
        this.offeringType = offeringType;
        this.recurringCharges = recurringCharges;
        this.scope = scope;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2ReservedInstancesResource that = (EC2ReservedInstancesResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(end, that.end) &&
                Objects.equals(fixedPrice, that.fixedPrice) &&
                Objects.equals(instanceCount, that.instanceCount) &&
                Objects.equals(instanceType, that.instanceType) &&
                Objects.equals(reservedInstancesId, that.reservedInstancesId) &&
                Objects.equals(start, that.start) &&
                Objects.equals(state, that.state) &&
                Objects.equals(usagePrice, that.usagePrice) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(instanceTenancy, that.instanceTenancy) &&
                Objects.equals(offeringClass, that.offeringClass) &&
                Objects.equals(offeringType, that.offeringType) &&
                Objects.equals(recurringCharges, that.recurringCharges) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, availabilityZone, duration, end, fixedPrice, instanceCount, instanceType,
                reservedInstancesId, start, state, usagePrice, currencyCode, instanceTenancy, offeringClass,
                offeringType, recurringCharges, scope, tags);
    }

    public static EC2ReservedInstancesResource fromSdk(String regionName, ReservedInstances reservedInstances) {
        return Optional.ofNullable(reservedInstances)
                .map(v -> new EC2ReservedInstancesResource(
                                regionName,
                                v.availabilityZone(),
                                v.duration(),
                                dateFromSdk(v.end()),
                                Double.valueOf(v.fixedPrice()),
                                v.instanceCount(),
                                v.instanceTypeAsString(),
                                v.reservedInstancesId(),
                                dateFromSdk(v.start()),
                                v.stateAsString(),
                                Double.valueOf(v.usagePrice()),
                                v.currencyCodeAsString(),
                                v.instanceTenancyAsString(),
                                v.offeringClassAsString(),
                                v.offeringTypeAsString(),
                                EC2RecurringCharge.fromSdk(v.recurringCharges()),
                                v.scopeAsString(),
                                tagsFromSdk(v.tags())
                        )
                )
                .orElse(null);
    }
}
