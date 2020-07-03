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
import software.amazon.awssdk.services.dynamodb.model.KeyType

data class DDBKeySchemaElement(
        @property:PropertyDefinition(
                name = PROP_ATTRIBUTE_NAME,
                description = PROP_ATTRIBUTE_NAME_D
        )
        val attributeName: String?,

        @property:PropertyDefinition(
                name = PROP_KEY_TYPE,
                description = PROP_KEY_TYPE_D
        )
        val keyType: KeyType?
) {
    companion object {
        const val PROP_ATTRIBUTE_NAME = "attribute_name"
        const val PROP_ATTRIBUTE_NAME_D = "The name of a key attribute"
        const val PROP_KEY_TYPE = "key_type"
        const val PROP_KEY_TYPE_D = "The role that this key attribute will assume"
    }
}
