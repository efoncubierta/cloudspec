/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.s3

import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Bucket

class S3BucketLoader(private val clientsProvider: IAWSClientsProvider) : S3ResourceLoader<S3BucketResource> {
    override fun byId(id: String): S3BucketResource? {
        return getBuckets(listOf(id)).firstOrNull()
    }

    override val all: List<S3BucketResource>
        get() = getBuckets(emptyList<String>())

    fun getBuckets(bucketNames: List<String>): List<S3BucketResource> {
        clientsProvider.s3Client.use { client ->
            val buckets = if (bucketNames.isNullOrEmpty()) {
                bucketNames
            } else {
                client.listBuckets()
                        .buckets()
                        .map { obj: Bucket -> obj.name() }
            }

            return buckets.map { bucketName ->
                toResource(
                        client,
                        bucketName
                )
            }
        }
    }

    private fun toResource(s3Client: S3Client, bucketName: String?): S3BucketResource {

        val region = s3Client.getBucketLocation { builder -> builder.bucket(bucketName) }
                .locationConstraint()
                .toString()

        return S3BucketResource(
                bucketName,
                region
        )
    }

}
