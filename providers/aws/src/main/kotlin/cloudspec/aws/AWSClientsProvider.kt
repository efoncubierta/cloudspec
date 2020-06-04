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
