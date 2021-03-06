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
package cloudspec.aws.s3

import arrow.core.Option
import arrow.core.firstOrNone
import arrow.fx.IO
import cloudspec.aws.IAWSClientsProvider
import cloudspec.model.SetValues
import software.amazon.awssdk.services.s3.S3Client

class S3BucketLoader(private val clientsProvider: IAWSClientsProvider) : S3ResourceLoader<S3BucketResource> {
    override fun byId(sets: SetValues, id: String): IO<Option<S3BucketResource>> {
        return IO { getBuckets(listOf(id)).firstOrNone() }
    }

    override fun all(sets: SetValues): IO<List<S3BucketResource>> = IO { getBuckets(emptyList()) }

    fun getBuckets(bucketNames: List<String>): List<S3BucketResource> {
        clientsProvider.s3Client.use { client ->
            val buckets = if (bucketNames.isNullOrEmpty()) {
                bucketNames
            } else {
                client.listBuckets()
                    .buckets()
                    .map { it.name() }
            }

            return buckets.map {
                toResource(client, it)
            }
        }
    }

    private fun toResource(s3Client: S3Client, bucketName: String): S3BucketResource {

        val region = s3Client.getBucketLocation { it.bucket(bucketName) }
            .locationConstraint()
            .toString()

        return S3BucketResource(bucketName, region)
    }

}
