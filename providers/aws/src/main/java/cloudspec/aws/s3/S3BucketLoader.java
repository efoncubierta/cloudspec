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
import software.amazon.awssdk.services.s3.model.LoggingEnabled;
import software.amazon.awssdk.services.s3.model.ServerSideEncryptionConfiguration;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class S3BucketLoader implements S3ResourceLoader<S3BucketResource> {
    private final IAWSClientsProvider clientsProvider;

    public S3BucketLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<S3BucketResource> getById(String id) {
        return getBuckets(Collections.singletonList(id)).findFirst();
    }

    @Override
    public List<S3BucketResource> getAll() {
        return getBuckets(Collections.emptyList()).collect(Collectors.toList());
    }

    public Stream<S3BucketResource> getBuckets(List<String> bucketNames) {
        S3Client s3Client = clientsProvider.getS3Client();

        try {
            Stream<String> bucketNamesStream;
            if (bucketNames != null && bucketNames.size() > 0) {
                bucketNamesStream = bucketNames.stream();
            } else {
                bucketNamesStream = s3Client.listBuckets()
                        .buckets()
                        .stream()
                        .map(Bucket::name);
            }

            return bucketNamesStream
                    .map(bucketName -> toResource(s3Client, bucketName));
        } finally {
            IoUtils.closeQuietly(s3Client, null);
        }
    }

    private S3BucketResource toResource(S3Client s3Client, String bucketName) {
        S3BucketResource.Builder resourceBuilder = S3BucketResource.builder();
        resourceBuilder.setBucketName(bucketName);

        // load region
        resourceBuilder.setRegion(
                s3Client.getBucketLocation(
                        builder -> builder.bucket(bucketName)
                )
                        .locationConstraint()
                        .toString()
        );

        // load encryption
        ServerSideEncryptionConfiguration encryptionConfiguration =
                s3Client
                        .getBucketEncryption(builder ->
                                builder.bucket(bucketName)
                        )
                        .serverSideEncryptionConfiguration();
        resourceBuilder.setEncryption(
                new S3BucketResource.S3BucketEncryption(
                        encryptionConfiguration.hasRules(),
                        encryptionConfiguration
                                .rules()
                                .get(0)
                                .applyServerSideEncryptionByDefault()
                                .kmsMasterKeyID() != null ?
                                "KMS" : "SSE"
                )

        );

        // load logging
        LoggingEnabled loggingEnabled = s3Client
                .getBucketLogging(builder ->
                        builder.bucket(bucketName)
                )
                .loggingEnabled();
        resourceBuilder.setLogging(
                new S3BucketResource.S3BucketLogging(
                        loggingEnabled != null
                )

        );

        return resourceBuilder.build();
    }
}
