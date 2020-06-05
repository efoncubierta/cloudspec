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
package cloudspec.aws

import software.amazon.awssdk.core.retry.RetryMode
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.iam.IamClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sqs.SqsClient

class AWSClientsProvider : IAWSClientsProvider {
    override val iamClient: IamClient
        get() = IamClient.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .build()

    override val ec2Client: Ec2Client
        get() = Ec2Client.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .build()

    override fun ec2ClientForRegion(region: String): Ec2Client {
        return Ec2Client.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .region(Region.of(region))
                .build()
    }

    override val s3Client: S3Client
        get() = S3Client.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .build()

    override val sqsClient: SqsClient
        get() = SqsClient.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .build()

    override val snsClient: SnsClient
        get() = SnsClient.builder()
                .overrideConfiguration { builder -> builder.retryPolicy(RetryMode.STANDARD) }
                .build()
}
