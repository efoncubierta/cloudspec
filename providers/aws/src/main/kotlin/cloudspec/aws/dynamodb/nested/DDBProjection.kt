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
import software.amazon.awssdk.services.dynamodb.model.ProjectionType

data class DDBProjection(
        @property:PropertyDefinition(
                name = PROP_PROJECTION_TYPE,
                description = PROP_PROJECTION_TYPE_D
        )
        val projectionType: ProjectionType?,

        @property:PropertyDefinition(
                name = PROP_NON_KEY_ATTRIBUTES,
                description = PROP_NON_KEY_ATTRIBUTES_D
        )
        val nonKeyAttributes: List<String>?
) {
    companion object {
        const val PROP_PROJECTION_TYPE = "projection_type"
        const val PROP_PROJECTION_TYPE_D = "The set of attributes that are projected into the index"
        const val PROP_NON_KEY_ATTRIBUTES = "non_key_attributes"
        const val PROP_NON_KEY_ATTRIBUTES_D = "Represents the non-key attribute names which will be projected into the index"
    }
}
