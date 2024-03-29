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
import software.amazon.awssdk.services.ec2.model.MonitoringState

data class EC2Monitoring(
        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: MonitoringState?
) {
    companion object {
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "Indicates whether detailed monitoring is enabled. Otherwise, basic monitoring is enabled"
    }
}
