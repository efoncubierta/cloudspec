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
package cloudspec.aws.dynamodb.nested

import cloudspec.annotation.PropertyDefinition
import java.time.Instant

data class DDBProvisionedThroughputDescription(
        @property:PropertyDefinition(
                name = PROP_LAST_INCREASE_DATE,
                description = PROP_LAST_INCREASE_DATE_D
        )
        val lastIncreaseDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_LAST_DECREASE_DATE,
                description = PROP_LAST_DECREASE_DATE_D
        )
        val lastDecreaseDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_NUMBER_OF_DECREASES_TODAY,
                description = PROP_NUMBER_OF_DECREASES_TODAY_D
        )
        val numberOfDecreasesToday: Long?,

        @property:PropertyDefinition(
                name = PROP_READ_CAPACITY_UNITS,
                description = PROP_READ_CAPACITY_UNITS_D
        )
        val readCapacityUnits: Long?,

        @property:PropertyDefinition(
                name = PROP_WRITE_CAPACITY_UNITS,
                description = PROP_WRITE_CAPACITY_UNITS_D
        )
        val writeCapacityUnits: Long?
) {
    companion object {
        const val PROP_LAST_INCREASE_DATE = "last_increase_date"
        const val PROP_LAST_INCREASE_DATE_D = "The date and time of the last provisioned throughput increase for this table"
        const val PROP_LAST_DECREASE_DATE = "last_decrease_date"
        const val PROP_LAST_DECREASE_DATE_D = "The date and time of the last provisioned throughput decrease for this table"
        const val PROP_NUMBER_OF_DECREASES_TODAY = "number_of_decreases_today"
        const val PROP_NUMBER_OF_DECREASES_TODAY_D = "The number of provisioned throughput decreases for this table during this UTC calendar day"
        const val PROP_READ_CAPACITY_UNITS = "read_capacity_units"
        const val PROP_READ_CAPACITY_UNITS_D = "The maximum number of strongly consistent reads consumed per second before DynamoDB returns a `ThrottlingException`"
        const val PROP_WRITE_CAPACITY_UNITS = "write_capacity_units"
        const val PROP_WRITE_CAPACITY_UNITS_D = "The maximum number of writes consumed per second before DynamoDB returns a `ThrottlingException`"
    }
}
