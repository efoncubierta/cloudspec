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
import arrow.core.extensions.list.traverse.sequence
import arrow.core.extensions.listk.functorFilter.filter
import arrow.core.firstOrNone
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.applicative.applicative
import cloudspec.aws.AWSConfig
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import cloudspec.model.SetValues
import cloudspec.model.getStrings
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.ec2.model.Filter

abstract class EC2ResourceLoader<T : EC2Resource>(protected val clientsProvider: IAWSClientsProvider) : AWSResourceLoader<T> {
    override fun byId(sets: SetValues, id: String): IO<Option<T>> {
        return IO.fx {
            val (resources) = resources(sets, listOf(id))
            resources.firstOrNone()
        }
    }

    override fun all(sets: SetValues): IO<List<T>> = resources(sets)

    private fun resources(sets: SetValues,
                          ids: List<String> = emptyList()): IO<List<T>> {
        return IO.fx {
            val (regions) = sets.getStrings(AWSConfig.REGIONS_REF)
                .let { if (it.isEmpty()) availableRegions(sets) else IO.just(it) }

            val (resources) = regions.map { resourcesInRegion(it, ids) }.parSequence()
            resources.flatten()
        }
    }

    abstract fun resourcesInRegion(region: String, ids: List<String>): IO<List<T>>

    protected fun <V> requestGlobal(handler: (client: Ec2Client) -> V): IO<V> {
        return IO.effect {
            clientsProvider.ec2Client.use { client ->
                handler(client)
            }
        }
    }

    protected fun <V> requestInRegion(region: String,
                                      handler: (client: Ec2Client) -> V): IO<V> {
        return IO.effect {
            clientsProvider.ec2ClientForRegion(region).use { client ->
                handler(client)
            }
        }
    }

    protected fun availableRegions(sets: SetValues): IO<List<String>> {
        return requestGlobal { client ->
            client.describeRegions()
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
