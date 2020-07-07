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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.kms.KMSKey
import software.amazon.awssdk.services.dynamodb.model.SSEStatus
import software.amazon.awssdk.services.dynamodb.model.SSEType
import java.time.Instant

data class DDBSSEDescription(
        @property:PropertyDefinition(
                name = PROP_STATUS,
                description = PROP_STATUS_D
        )
        val status: SSEStatus?,

        @property:PropertyDefinition(
                name = PROP_SSE_TYPE,
                description = PROP_SSE_TYPE_D
        )
        val sseType: SSEType?,

        @property:AssociationDefinition(
                name = PROP_KMS_KEY,
                description = PROP_KMS_KEY_D,
                targetClass = KMSKey::class
        )
        val kmsMasterKeyArn: String?,

        @property:PropertyDefinition(
                name = PROP_INACCESSIBLE_ENCRYPTION_DATE,
                description = PROP_INACCESSIBLE_ENCRYPTION_DATE_D
        )
        val inaccessibleEncryptionDateTime: Instant?
) {
    companion object {
        const val PROP_STATUS = "status"
        const val PROP_STATUS_D = "Represents the current state of server-side encryption"
        const val PROP_SSE_TYPE = "sse_type"
        const val PROP_SSE_TYPE_D = "Server-side encryption type"
        const val PROP_KMS_KEY = "kms_key"
        const val PROP_KMS_KEY_D = "The AWS KMS customer master key (CMK) used for the AWS KMS encryption"
        const val PROP_INACCESSIBLE_ENCRYPTION_DATE = "inaccessible_encryption_date"
        const val PROP_INACCESSIBLE_ENCRYPTION_DATE_D = "Indicates the time, in UNIX epoch date format, when DynamoDB detected that the table's AWS KMS key was inaccessible"
    }
}
