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
package cloudspec.aws.kms

import arrow.core.Option
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.syntax.collections.flatten
import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.regions.Region

class KMSKeyLoader(clientsProvider: IAWSClientsProvider) :
        KMSResourceLoader<KMSKey>(clientsProvider) {
    override fun resourceByArn(arn: Arn): IO<Option<KMSKey>> {
        val region = Region.of(arn.region().get())
        return resourceById(region, arn.resource().resource())
    }

    private fun resourceById(region: Region, id: String): IO<Option<KMSKey>> {
        return IO.fx {
            val (keyMetadata) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/kms/latest/APIReference/API_DescribeKey.html
                client.describeKey { builder -> builder.keyId(id) }.keyMetadata()
            }
            val (tags) = listTags(region, id)
            Option.fromNullable(keyMetadata.toKMSKey(region, tags))
        }
    }

    override fun resourcesByRegion(region: Region): IO<List<KMSKey>> {
        return IO.fx {
            val (keyEntries) = requestInRegion(region) { client ->
                // https://docs.aws.amazon.com/kms/latest/APIReference/API_ListKeys.html
                client.listKeys().keys()
            }

            val (keys) = keyEntries.map { resourceByArn(Arn.fromString(it.keyArn())) }.parSequence()
            keys.flatten()
        }
    }
}
