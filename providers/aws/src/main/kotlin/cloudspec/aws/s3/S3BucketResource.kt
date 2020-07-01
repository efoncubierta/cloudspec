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

import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = S3Resource.GROUP_NAME,
        name = S3BucketResource.RESOURCE_NAME,
        description = S3BucketResource.RESOURCE_DESCRIPTION
)
data class S3BucketResource(
        @property:PropertyDefinition(
                name = "region",
                description = "AWS Region"
        )
        var region: String?,

        @property:PropertyDefinition(
                name = "bucket_name",
                description = "Bucket name"
        )
        var bucketName: String?
) : S3Resource() {
    companion object {
        const val RESOURCE_NAME = "bucket"
        const val RESOURCE_DESCRIPTION = "S3 Bucket"
    }
}
