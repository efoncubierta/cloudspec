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
import java.time.Instant

data class DDBRestoreSummary(
        @property:PropertyDefinition(
                name = PROP_SOURCE_BACKUP_ARN,
                description = PROP_SOURCE_BACKUP_ARN_D
        )
        val sourceBackupArn: String?,

        @property:PropertyDefinition(
                name = PROP_SOURCE_TABLE_ARN,
                description = PROP_SOURCE_TABLE_ARN_D
        )
        val sourceTableArn: String?,

        @property:PropertyDefinition(
                name = PROP_RESTORE_DATE,
                description = PROP_RESTORE_DATE_D
        )
        val restoreDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_RESTORE_IN_PROGRESS,
                description = PROP_RESTORE_IN_PROGRESS_D
        )
        val restoreInProgress: Boolean?
) {
    companion object {
        const val PROP_SOURCE_BACKUP_ARN = "source_backup_arn"
        const val PROP_SOURCE_BACKUP_ARN_D = "The Amazon Resource Name (ARN) of the backup from which the table was restored"
        const val PROP_SOURCE_TABLE_ARN = "source_table_arn"
        const val PROP_SOURCE_TABLE_ARN_D = "The ARN of the source table of the backup that is being restored"
        const val PROP_RESTORE_DATE = "restore_date"
        const val PROP_RESTORE_DATE_D = "Point in time or source backup time"
        const val PROP_RESTORE_IN_PROGRESS = "restore_in_progress"
        const val PROP_RESTORE_IN_PROGRESS_D = "Indicates if a restore is in progress or not"
    }
}
