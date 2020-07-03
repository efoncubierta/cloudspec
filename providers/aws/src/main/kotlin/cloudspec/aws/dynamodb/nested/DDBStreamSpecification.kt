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
import software.amazon.awssdk.services.dynamodb.model.StreamViewType

data class DDBStreamSpecification(
        @property:PropertyDefinition(
                name = PROP_STREAM_ENABLED,
                description = PROP_STREAM_ENABLED_D
        )
        val streamEnabled: Boolean?,

        @property:PropertyDefinition(
                name = PROP_STREAM_VIEW_TYPE,
                description = PROP_STREAM_VIEW_TYPE_D
        )
        val streamViewType: StreamViewType?
) {
    companion object {
        const val PROP_STREAM_ENABLED = "stream_enabled"
        const val PROP_STREAM_ENABLED_D = "Indicates whether DynamoDB Streams is enabled (true) or disabled (false) on the table"
        const val PROP_STREAM_VIEW_TYPE = "stream_view_type"
        const val PROP_STREAM_VIEW_TYPE_D = "When an item in the table is modified, `StreamViewType` determines what information is written to the stream for this table"
    }
}
