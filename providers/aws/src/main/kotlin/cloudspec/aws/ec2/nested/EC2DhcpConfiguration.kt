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

data class EC2DhcpConfiguration(
        @property:PropertyDefinition(
                name = PROP_KEY,
                description = PROP_KEY_D
        )
        val key: String?,

        @property:PropertyDefinition(
                name = PROP_VALUES,
                description = PROP_VALUES_D
        )
        val values: List<String>?
) {
    companion object {
        const val PROP_KEY = "key"
        const val PROP_KEY_D = "The name of a DHCP option"
        const val PROP_VALUES = "values"
        const val PROP_VALUES_D = "One or more values for the DHCP option"
    }
}
