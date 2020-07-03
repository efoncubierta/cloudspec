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
import arrow.core.extensions.list.traverse.sequence
import arrow.core.extensions.listk.functorFilter.filter
import arrow.core.firstOrNone
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.io.applicative.applicative
import arrow.syntax.collections.flatten
import cloudspec.aws.AWSConfig
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import cloudspec.aws.dynamodb.nested.toKeyValues
import cloudspec.model.KeyValue
import cloudspec.model.SetValues
import cloudspec.model.getStrings
import kotlinx.coroutines.runBlocking
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

abstract class DDBResourceLoader<T : DDBResource>(protected val clientsProvider: IAWSClientsProvider) : AWSResourceLoader<T> {
    override fun byId(sets: SetValues, id: String): Option<T> = runBlocking {
        resources(sets, listOf(id)).unsafeRunSync().firstOrNone()
    }

    override fun all(sets: SetValues): List<T> = resources(sets, emptyList()).unsafeRunSync()

    protected fun resources(sets: SetValues,
                            ids: List<String>): IO<List<T>> {
        return if (ids.isNotEmpty()) {
            // TODO filter out ARNs not in AWSConfig.REGIONS_REF
            resourcesByArns(ids.map { Arn.fromString(it) })
        } else {
            allResources(sets)
        }
    }

    private fun resourcesByArns(arns: List<Arn>): IO<List<T>> {
        return IO.fx {
            val (tables) = arns.map { resourceByArn(it) }.sequence(IO.applicative())
            tables.filter { it.isDefined() }.flatten()
        }
    }

    abstract fun resourceByArn(arn: Arn): IO<Option<T>>

    private fun allResources(sets: SetValues): IO<List<T>> {
        return IO.fx {
            val (tables) = sets.getStrings(AWSConfig.REGIONS_REF)
                .let { regions -> if (regions.isEmpty()) Region.regions() else regions.map { Region.of(it) } }
                .map { resourcesByRegion(it) }
                .sequence(IO.applicative())

            tables.filter { it.isNotEmpty() }.flatten()
        }
    }

    abstract fun resourcesByRegion(region: Region): IO<List<T>>

    protected fun <V> requestGlobal(handler: (client: DynamoDbClient) -> V): IO<V> {
        return IO.effect {
            clientsProvider.dynamoDbClient.use { client ->
                handler(client)
            }
        }
    }

    protected fun <V> requestInRegion(region: Region,
                                      handler: (client: DynamoDbClient) -> V): IO<V> {
        return IO.effect {
            clientsProvider.dynamoDbClientForRegion(region).use { client ->
                handler(client)
            }
        }
    }

    protected fun listTags(region: Region, arn: String): IO<List<KeyValue>> {
        return requestInRegion(region) { client ->
            // https://docs.aws.amazon.com/amazondynamodb/latest/APIReference/API_ListTagsOfResource.html
            client.listTagsOfResource { builder -> builder.resourceArn(arn) }
                .tags()
                .toKeyValues()
        }
    }
}
