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
package cloudspec.aws.ec2

import arrow.core.Option
import arrow.core.firstOrNone
import cloudspec.aws.AWSConfig
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import cloudspec.model.SetValues
import cloudspec.model.getStrings
import kotlinx.coroutines.*
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.ec2.model.Filter

abstract class EC2ResourceLoader<T : EC2Resource>(protected val clientsProvider: IAWSClientsProvider) : AWSResourceLoader<T> {
    override fun byId(sets: SetValues, id: String): Option<T> = runBlocking {
        resources(sets, listOf(id)).firstOrNone()
    }

    override fun all(sets: SetValues): List<T> = runBlocking { resources(sets) }

    private suspend fun resources(sets: SetValues,
                                  ids: List<String> = emptyList()): List<T> = coroutineScope {
        sets.getStrings(AWSConfig.REGIONS_REF)
            .let { if (it.isEmpty()) availableRegions(sets) else it }
            .map { async { resourcesInRegion(it, ids) } }
            .awaitAll()
            .flatten()
    }

    abstract suspend fun resourcesInRegion(region: String, ids: List<String>): List<T>

    protected suspend fun requestInRegion(region: String,
                                          handler: (client: Ec2Client) -> List<T>): List<T> = coroutineScope {
        clientsProvider.ec2ClientForRegion(region).use { client ->
            withContext(Dispatchers.Default) {
                handler(client)
            }
        }
    }

    protected suspend fun availableRegions(sets: SetValues): List<String> = coroutineScope {
        clientsProvider.ec2Client.use { ec2Client ->
            withContext(Dispatchers.Default) {
                ec2Client.describeRegions()
                    .regions()
                    .map { it.regionName() }
            }
        }
    }

    protected fun buildFilters(filterValues: Map<String, List<String>>): List<Filter> {
        return filterValues.keys
            .filter {
                !filterValues[it].isNullOrEmpty()
            }.map {
                Filter.builder()
                    .name(it)
                    .values(filterValues[it])
                    .build()
            }
    }
}
