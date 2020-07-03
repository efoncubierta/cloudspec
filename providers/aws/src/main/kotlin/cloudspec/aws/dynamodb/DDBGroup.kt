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

import arrow.syntax.collections.flatten
import cloudspec.annotation.ResourceDefReflectionUtil
import cloudspec.aws.AWSGroup
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import cloudspec.model.GroupDef
import cloudspec.model.ResourceDefRef

class DDBGroup(clientsProvider: IAWSClientsProvider) : AWSGroup {
    override val loaders: Map<ResourceDefRef, AWSResourceLoader<*>> = mapOf(
            DDBBackup.RESOURCE_DEF to DDBBackupLoader(clientsProvider),
            DDBGlobalTable.RESOURCE_DEF to DDBGlobalTableLoader(clientsProvider),
            DDBTable.RESOURCE_DEF to DDBTableLoader(clientsProvider)
    )

    override val definition: GroupDef
        get() = GROUP_DEF


    companion object {
        const val GROUP_NAME = "dynamodb"
        const val GROUP_DESCRIPTION = "DynamoDB"

        private val GROUP_DEF = GroupDef(GROUP_NAME,
                                         GROUP_DESCRIPTION,
                                         listOf(DDBBackup::class,
                                                DDBGlobalTable::class,
                                                DDBTable::class)
                                             .map { ResourceDefReflectionUtil.toResourceDef(it) }
                                             .flatten())
    }
}
