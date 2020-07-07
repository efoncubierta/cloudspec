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
package cloudspec.aws.sns

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.aws.kms.KMSKey
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = SNSGroup.GROUP_NAME,
        name = SNSTopic.RESOURCE_NAME,
        description = SNSTopic.RESOURCE_DESCRIPTION
)
data class SNSTopic(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val region: String?,

        // TODO unmarshall JSON and extract data
        val deliveryPolicy: String?,

        @property:PropertyDefinition(
                name = PROP_DISPLAY_NAME,
                description = PROP_DISPLAY_NAME_D
        )
        val displayName: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER,
                description = PROP_OWNER_D
        )
        val owner: String?,

        // TODO unmarshall JSON and extract data
        val policy: String?,

        @property:PropertyDefinition(
                name = PROP_SUBSCRIPTION_CONFIRMED,
                description = PROP_SUBSCRIPTION_CONFIRMED_D
        )
        val subscriptionConfirmed: Long?,

        @property:PropertyDefinition(
                name = PROP_SUBSCRIPTION_DELETED,
                description = PROP_SUBSCRIPTION_DELETED_D
        )
        val subscriptionDeleted: Long?,

        @property:PropertyDefinition(
                name = PROP_SUBSCRIPTION_PENDING,
                description = PROP_SUBSCRIPTION_PENDING_D
        )
        val subscriptionPending: Long?,

        @IdDefinition
        @property:PropertyDefinition(
                name = PROP_TOPIC_ARN,
                description = PROP_TOPIC_ARN_D
        )
        val topicArn: String,

        // TODO unmarshall JSON and extract data
        val effectiveDeliveryPolicy: String?,

        // TODO associate with KMS
        @property:AssociationDefinition(
                name = PROP_KMS_KEY,
                description = PROP_KMS_KEY_D,
                targetClass = KMSKey::class
        )
        val kmsMasterKeyId: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : SNSResource() {
    companion object {
        const val RESOURCE_NAME = "topic"
        const val RESOURCE_DESCRIPTION = "Topic"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          SNSGroup.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_DISPLAY_NAME = "display_name"
        const val PROP_DISPLAY_NAME_D = "The human-readable name used in the `From` field for notifications to email and `email-json` endpoints"
        const val PROP_OWNER = "owner"
        const val PROP_OWNER_D = "The AWS account ID of the topic's owner"
        const val PROP_SUBSCRIPTION_CONFIRMED = "subscription_confirmed"
        const val PROP_SUBSCRIPTION_CONFIRMED_D = "The number of confirmed subscriptions for the topic"
        const val PROP_SUBSCRIPTION_DELETED = "subscription_deleted"
        const val PROP_SUBSCRIPTION_DELETED_D = "The number of deleted subscriptions for the topic"
        const val PROP_SUBSCRIPTION_PENDING = "subscription_pending"
        const val PROP_SUBSCRIPTION_PENDING_D = "The number of subscriptions pending confirmation for the topic"
        const val PROP_TOPIC_ARN = "topic_arn"
        const val PROP_TOPIC_ARN_D = "The topic's ARN"
        const val PROP_KMS_KEY = "kms_key"
        const val PROP_KMS_KEY_D = "The AWS-managed customer master key (CMK) for Amazon SNS or a custom CMK"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags attached to the table"
    }
}
