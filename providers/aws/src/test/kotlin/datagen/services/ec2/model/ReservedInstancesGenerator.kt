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
