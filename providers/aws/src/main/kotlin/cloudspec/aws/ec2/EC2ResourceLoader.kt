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
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.services.ec2.model.Filter

abstract class EC2ResourceLoader<T : EC2Resource>(protected val clientsProvider: IAWSClientsProvider) : AWSResourceLoader<T> {
    override fun byId(id: String): Option<T> {
        return getResources(listOf(id)).firstOrNone()
    }

    override val all: List<T>
        get() {
            return getResources()
        }

    private fun getResources(ids: List<String> = emptyList()): List<T> {
        return availableRegions.flatMap {
            getResourcesInRegion(it, ids)
        }
    }

    abstract fun getResourcesInRegion(region: String, ids: List<String>): List<T>

    protected val availableRegions: List<String>
        get() {
            clientsProvider.ec2Client.use { ec2Client ->
                return ec2Client.describeRegions()
                        .regions()
                        .map { it.regionName() }
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
