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
package datagen.services.ec2.model

import datagen.BaseGenerator
import datagen.CommonGenerator
import software.amazon.awssdk.services.ec2.model.*
import java.util.*

object ReservedInstancesGenerator : BaseGenerator() {
    fun reservedInstancesId(): String {
        return UUID.randomUUID().toString()
    }

    fun reservedInstanceState(): ReservedInstanceState {
        return valueFromArray(ReservedInstanceState.values())
    }

    fun currencyCode(): CurrencyCodeValues {
        return valueFromArray(CurrencyCodeValues.values())
    }

    fun tenancy(): Tenancy {
        return valueFromArray(Tenancy.values())
    }

    fun offeringClassType(): OfferingClassType {
        return valueFromArray(OfferingClassType.values())
    }

    fun offeringTypeValues(): OfferingTypeValues {
        return valueFromArray(OfferingTypeValues.values())
    }

    fun scope(): Scope {
        return valueFromArray(Scope.values())
    }

    fun reservedInstancesList(n: Int? = null): List<ReservedInstances> {
        return listGenerator(n) { reservedInstances() }
    }

    fun reservedInstances(): ReservedInstances {
        return ReservedInstances.builder()
                .availabilityZone(CommonGenerator.availabilityZone())
                .duration(faker.random().nextLong())
                .end(futureInstant())
                .fixedPrice(faker.random().nextDouble().toFloat())
                .instanceCount(faker.random().nextInt(1, 5))
                .instanceType(InstanceGenerator.instanceType())
                .productDescription(faker.lorem().sentence())
                .reservedInstancesId(reservedInstancesId())
                .start(pastInstant())
                .state(reservedInstanceState())
                .usagePrice(faker.random().nextDouble().toFloat())
                .currencyCode(currencyCode())
                .instanceTenancy(tenancy())
                .offeringClass(offeringClassType())
                .offeringType(offeringTypeValues())
                .recurringCharges(RecurringChargeGenerator.recurringCharges())
                .scope(scope())
                .tags(TagGenerator.tags())
                .build()
    }
}
