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

import arrow.core.Option
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.syntax.collections.flatten
import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.regions.Region

class DDBBackupLoader(clientsProvider: IAWSClientsProvider) :
        DDBResourceLoader<DDBBackup>(clientsProvider) {

    override fun resourceByArn(arn: Arn): IO<Option<DDBBackup>> {
        val region = Region.of(arn.region().get())
        return IO.fx {
            val (res) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_DescribeBackup.html
                client.describeBackup { builder -> builder.backupArn(arn.toString()) }
            }
            Option.fromNullable(res.backupDescription()?.toDDBBackup(region))
        }
    }

    override fun resourcesByRegion(region: Region): IO<List<DDBBackup>> {
        return IO.fx {
            val (tableNames) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_ListBackups.html
                client.listBackups().backupSummaries()
            }

            val (tables) = tableNames.map { resourceByArn(Arn.fromString(it.backupArn())) }.parSequence()
            tables.flatten()
        }
    }
}
