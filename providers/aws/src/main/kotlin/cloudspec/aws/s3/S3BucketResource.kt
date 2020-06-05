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

@ResourceDefinition(
        provider = "aws",
        group = "s3",
        name = "bucket",
        description = "S3 Bucket"
)
data class S3BucketResource(
        @PropertyDefinition(
                name = "region",
                description = "AWS Region"
        )
        var region: String?,

        @PropertyDefinition(
                name = "bucket_name",
                description = "Bucket name"
        )
        var bucketName: String?
) : S3Resource()
