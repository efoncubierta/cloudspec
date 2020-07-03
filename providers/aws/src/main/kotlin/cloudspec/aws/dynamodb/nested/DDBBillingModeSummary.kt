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
import software.amazon.awssdk.services.dynamodb.model.BillingMode
import java.time.Instant

data class DDBBillingModeSummary(
        @property:PropertyDefinition(
                name = PROP_BILLING_MODE,
                description = PROP_BILLING_MODE_D
        )
        val billingMode: BillingMode?,

        @property:PropertyDefinition(
                name = PROP_LAST_UPDATE_TO_PAY_PER_REQUEST,
                description = PROP_LAST_UPDATE_TO_PAY_PER_REQUEST_D
        )
        val lastUpdateToPayPerRequestDateTime: Instant?
) {
    companion object {
        const val PROP_BILLING_MODE = "billing_mode"
        const val PROP_BILLING_MODE_D = "Controls how you are charged for read and write throughput and how you manage capacity"
        const val PROP_LAST_UPDATE_TO_PAY_PER_REQUEST = "last_update_to_pay_per_request"
        const val PROP_LAST_UPDATE_TO_PAY_PER_REQUEST_D = "Represents the time when `PAY_PER_REQUEST` was last set as the read/write capacity mode"
    }
}
