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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2RecurringCharge(
        @property:PropertyDefinition(
                name = PROP_AMOUNT,
                description = PROP_AMOUNT_D
        )
        val amount: Double?,

        @property:PropertyDefinition(
                name = PROP_FREQUENCY,
                description = PROP_FREQUENCY_D,
                exampleValues = "Hourly"
        )
        val frequency: String?
) {
    companion object {
        const val PROP_AMOUNT = "amount"
        const val PROP_AMOUNT_D = "The amount of the recurring charge"
        const val PROP_FREQUENCY = "frequency"
        const val PROP_FREQUENCY_D = "The frequency of the recurring charge"
    }
}
