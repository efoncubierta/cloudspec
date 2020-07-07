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
package cloudspec.aws.kms

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.kms.model.*
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = KMSGroup.GROUP_NAME,
        name = KMSKey.RESOURCE_NAME,
        description = KMSKey.RESOURCE_DESCRIPTION
)
data class KMSKey(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val region: String?,

        @property:PropertyDefinition(
                name = PROP_ACCOUNT_ID,
                description = PROP_ACCOUNT_ID_D
        )
        val awsAccountId: String?,

        @IdDefinition
        @property:PropertyDefinition(
                name = PROP_KEY_ID,
                description = PROP_KEY_ID_D
        )
        val keyId: String,

        @property:PropertyDefinition(
                name = PROP_ARN,
                description = PROP_ARN_D
        )
        val arn: String?,

        @property:PropertyDefinition(
                name = PROP_CREATION_DATE,
                description = PROP_CREATION_DATE_D
        )
        val creationDate: Instant?,

        @property:PropertyDefinition(
                name = PROP_ENABLED,
                description = PROP_ENABLED_D
        )
        val enabled: Boolean?,

        @property:PropertyDefinition(
                name = PROP_DESCRIPTION,
                description = PROP_DESCRIPTION_D
        )
        val description: String?,

        @property:PropertyDefinition(
                name = PROP_KEY_USAGE,
                description = PROP_KEY_USAGE_D
        )
        val keyUsage: KeyUsageType?,

        @property:PropertyDefinition(
                name = PROP_KEY_STATE,
                description = PROP_KEY_STATE_D
        )
        val keyState: KeyState?,

        @property:PropertyDefinition(
                name = PROP_DELETION_DATE,
                description = PROP_DELETION_DATE_D
        )
        val deletionDate: Instant?,

        @property:PropertyDefinition(
                name = PROP_VALID_TO,
                description = PROP_VALID_TO_D
        )
        val validTo: Instant?,

        @property:PropertyDefinition(
                name = PROP_ORIGIN,
                description = PROP_ORIGIN_D
        )
        val origin: OriginType?,

        // TODO associate with custom key store
        val customKeyStoreId: String?,

        // TODO associate with cloud HSM cluster
        val cloudHsmClusterId: String?,

        @property:PropertyDefinition(
                name = PROP_EXPIRATIONAL_MODE,
                description = PROP_EXPIRATIONAL_MODE_D
        )
        val expirationModel: ExpirationModelType?,

        @property:PropertyDefinition(
                name = PROP_KEY_MANAGER,
                description = PROP_KEY_MANAGER_D
        )
        val keyManager: KeyManagerType?,

        @property:PropertyDefinition(
                name = PROP_CUSTOMER_MASTER_KEY_SPEC,
                description = PROP_CUSTOMER_MASTER_KEY_SPEC_D
        )
        val customerMasterKeySpec: CustomerMasterKeySpec?,

        @property:PropertyDefinition(
                name = PROP_ENCRYPTION_ALGORITHMS,
                description = PROP_ENCRYPTION_ALGORITHMS_D
        )
        val encryptionAlgorithms: List<EncryptionAlgorithmSpec>?,

        @property:PropertyDefinition(
                name = PROP_SIGNING_ALGORITHMS,
                description = PROP_SIGNING_ALGORITHMS_D
        )
        val signingAlgorithms: List<SigningAlgorithmSpec>?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : KMSResource() {
    companion object {
        const val RESOURCE_NAME = "key"
        const val RESOURCE_DESCRIPTION = "Key"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          KMSGroup.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ACCOUNT_ID = "account_id"
        const val PROP_ACCOUNT_ID_D = "The twelve-digit account ID of the AWS account that owns the CMK"
        const val PROP_KEY_ID = "key_id"
        const val PROP_KEY_ID_D = "The globally unique identifier for the CMK"
        const val PROP_ARN = "arn"
        const val PROP_ARN_D = "The Amazon Resource Name (ARN) of the CMK"
        const val PROP_CREATION_DATE = "creation_date"
        const val PROP_CREATION_DATE_D = "The date and time when the CMK was created"
        const val PROP_ENABLED = "enabled"
        const val PROP_ENABLED_D = "Specifies whether the CMK is enabled"
        const val PROP_DESCRIPTION = "description"
        const val PROP_DESCRIPTION_D = "The description of the CMK"
        const val PROP_KEY_USAGE = "key_usage"
        const val PROP_KEY_USAGE_D = "The cryptographic operations for which you can use the CMK"
        const val PROP_KEY_STATE = "key_state"
        const val PROP_KEY_STATE_D = "The state of the CMK"
        const val PROP_DELETION_DATE = "deletion_date"
        const val PROP_DELETION_DATE_D = "The date and time after which AWS KMS deletes the CMK"
        const val PROP_VALID_TO = "valid_to"
        const val PROP_VALID_TO_D = "The time at which the imported key material expires"
        const val PROP_ORIGIN = "origin"
        const val PROP_ORIGIN_D = "The source of the CMK's key material"
        const val PROP_EXPIRATIONAL_MODE = "expirational_mode"
        const val PROP_EXPIRATIONAL_MODE_D = "Specifies whether the CMK's key material expires"
        const val PROP_KEY_MANAGER = "key_manager"
        const val PROP_KEY_MANAGER_D = "The manager of the CMK"
        const val PROP_CUSTOMER_MASTER_KEY_SPEC = "customer_master_key_spec"
        const val PROP_CUSTOMER_MASTER_KEY_SPEC_D = "Describes the type of key material in the CMK"
        const val PROP_ENCRYPTION_ALGORITHMS = "encryption_algorithms"
        const val PROP_ENCRYPTION_ALGORITHMS_D = "A list of encryption algorithms that the CMK supports"
        const val PROP_SIGNING_ALGORITHMS = "signing_algorithms"
        const val PROP_SIGNING_ALGORITHMS_D = "A list of signing algorithms that the CMK supports"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags attached to the table"
    }
}
