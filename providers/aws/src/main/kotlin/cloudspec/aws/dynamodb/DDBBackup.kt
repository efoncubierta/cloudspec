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
package cloudspec.aws.dynamodb

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import software.amazon.awssdk.services.dynamodb.model.BackupStatus
import software.amazon.awssdk.services.dynamodb.model.BackupType
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = DDBResource.GROUP_NAME,
        name = DDBBackup.RESOURCE_NAME,
        description = DDBBackup.RESOURCE_DESCRIPTION
)
data class DDBBackup(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        val region: String?,

        @IdDefinition
        @property:PropertyDefinition(
                name = PROP_BACKUP_ARN,
                description = PROP_BACKUP_ARN_D
        )
        val backupArn: String,

        @property:PropertyDefinition(
                name = PROP_BACKUP_NAME,
                description = PROP_BACKUP_NAME_D
        )
        val backupName: String?,

        @property:PropertyDefinition(
                name = PROP_BACKUP_SIZE,
                description = PROP_BACKUP_SIZE_D
        )
        val backupSizeBytes: Long?,

        @property:PropertyDefinition(
                name = PROP_BACKUP_STATUS,
                description = PROP_BACKUP_STATUS_D
        )
        val backupStatus: BackupStatus?,

        @property:PropertyDefinition(
                name = PROP_BACKUP_TYPE,
                description = PROP_BACKUP_TYPE_D
        )
        val backupType: BackupType?,

        @property:PropertyDefinition(
                name = PROP_BACKUP_CREATION_DATE,
                description = PROP_BACKUP_CREATION_DATE_D
        )
        val backupCreationDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_BACKUP_EXPIRY_DATE,
                description = PROP_BACKUP_EXPIRY_DATE_D
        )
        val backupExpiryDateTime: Instant?,

        @property:AssociationDefinition(
                name = ASSOC_TABLE,
                description = ASSOC_TABLE_NAME_D,
                targetClass = DDBTable::class
        )
        val tableArn: String?
) : DDBResource() {
    companion object {
        const val RESOURCE_NAME = "backup"
        const val RESOURCE_DESCRIPTION = "Backup"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_BACKUP_ARN = "backup_arn"
        const val PROP_BACKUP_ARN_D = "ARN associated with the backup"
        const val PROP_BACKUP_NAME = "backup_name"
        const val PROP_BACKUP_NAME_D = "Name of the specified backup"
        const val PROP_BACKUP_SIZE = "backup_size"
        const val PROP_BACKUP_SIZE_D = "Size of the backup in bytes"
        const val PROP_BACKUP_STATUS = "backup_status"
        const val PROP_BACKUP_STATUS_D = "Backup state"
        const val PROP_BACKUP_TYPE = "backup_type"
        const val PROP_BACKUP_TYPE_D = "Backup type"
        const val PROP_BACKUP_CREATION_DATE = "backup_creation_date"
        const val PROP_BACKUP_CREATION_DATE_D = "Time at which the backup was created"
        const val PROP_BACKUP_EXPIRY_DATE = "backup_expiry_date"
        const val PROP_BACKUP_EXPIRY_DATE_D = "Time at which the automatic on-demand backup created by DynamoDB will expire"
        const val ASSOC_TABLE = "table"
        const val ASSOC_TABLE_NAME_D = "The source table"
    }
}
