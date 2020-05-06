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
package cloudspec.aws.s3;

import cloudspec.aws.IAWSClientsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.GetBucketLocationRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.util.List;
import java.util.stream.Collectors;

public class S3BucketLoader implements S3ResourceLoader<S3BucketResource> {
    private final IAWSClientsProvider clientsProvider;

    public S3BucketLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    public List<S3BucketResource> load() {
        S3Client s3Client = clientsProvider.getS3Client();

        try {
            ListBucketsResponse response = s3Client.listBuckets();
            return response.buckets()
                    .stream()
                    .map(bucket -> toResource(s3Client, bucket))
                    .collect(Collectors.toList());
        } finally {
            IoUtils.closeQuietly(s3Client, null);
        }
    }

    private S3BucketResource toResource(S3Client s3Client, Bucket bucket) {
        return new S3BucketResource("",
                getRegion(s3Client, bucket.name()),
                bucket.name()
        );
    }

    private String getRegion(S3Client s3Client, String bucketName) {
        return s3Client.getBucketLocation(
                GetBucketLocationRequest
                        .builder()
                        .bucket(bucketName)
                        .build()
        ).locationConstraint().toString();
    }
}
