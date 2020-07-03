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

data class DDBArchivalSummary(
        @property:PropertyDefinition(
                name = PROP_ARCHIVAL_DATE,
                description = PROP_ARCHIVAL_DATE_D
        )
        val archivalDateTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_ARCHIVAL_REASON,
                description = PROP_ARCHIVAL_REASON_D
        )
        val archivalReason: String?,

        @property:PropertyDefinition(
                name = PROP_ARCHIVAL_BACKUP_ARN,
                description = PROP_ARCHIVAL_BACKUP_ARN_D
        )
        val archivalBackupArn: String?
) {
    companion object {
        const val PROP_ARCHIVAL_DATE = "archival_date"
        const val PROP_ARCHIVAL_DATE_D = "The date and time when table archival was initiated by DynamoDB, in UNIX epoch time format"
        const val PROP_ARCHIVAL_REASON = "archival_reason"
        const val PROP_ARCHIVAL_REASON_D = "The reason DynamoDB archived the table"
        const val PROP_ARCHIVAL_BACKUP_ARN = "archival_backup_arn"
        const val PROP_ARCHIVAL_BACKUP_ARN_D = "The Amazon Resource Name (ARN) of the backup the table was archived to, when applicable in the archival reason"
    }
}
