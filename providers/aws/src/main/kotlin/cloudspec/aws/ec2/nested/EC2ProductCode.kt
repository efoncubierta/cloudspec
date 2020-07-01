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

data class EC2ProductCode(
        @property:PropertyDefinition(
                name = PROP_ID,
                description = PROP_ID_D
        )
        val productCodeId: String,

        @property:PropertyDefinition(
                name = PROP_TYPE,
                description = PROP_TYPE_D,
                exampleValues = "devpay | marketplace"
        )
        val productCodeType: String
) {
    companion object {
        const val PROP_ID = "id"
        const val PROP_ID_D = "The product code"
        const val PROP_TYPE = "type"
        const val PROP_TYPE_D = "The type of product code"
    }
}
