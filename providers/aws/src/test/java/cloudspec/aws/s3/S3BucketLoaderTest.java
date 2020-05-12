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
import org.junit.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class S3BucketLoaderTest {

    private static final IAWSClientsProvider TEST_CLIENTS_PROVIDER = mock(IAWSClientsProvider.class);
    private static final S3Client TEST_S3_CLIENT = mock(S3Client.class);

    private static final Region TEST_REGION = Region.EU_WEST_1;
    private static final String TEST_BUCKET_NAME = "bucketName";

    public static final ListBucketsResponse TEST_LIST_BUCKETS_RESPONSE =
            ListBucketsResponse
                    .builder()
                    .buckets(
                            Bucket.builder()
                                    .name(TEST_BUCKET_NAME)
                                    .build()
                    )
                    .build();

    public static final GetBucketLocationResponse TEST_BUCKET_LOCATION_RESPONSE =
            GetBucketLocationResponse
                    .builder()
                    .locationConstraint(TEST_REGION.id())
                    .build();

    public static final GetBucketEncryptionResponse TEST_GET_BUCKET_ENCRYPTION_RESPONSE =
            GetBucketEncryptionResponse
                    .builder()
                    .serverSideEncryptionConfiguration(
                            ServerSideEncryptionConfiguration
                                    .builder()
                                    .rules(
                                            ServerSideEncryptionRule
                                                    .builder()
                                                    .applyServerSideEncryptionByDefault(
                                                            ServerSideEncryptionByDefault
                                                                    .builder()
                                                                    .kmsMasterKeyID(UUID.randomUUID().toString())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

    public static final GetBucketLoggingResponse TEST_GET_BUCKET_LOGGING_RESPONSE =
            GetBucketLoggingResponse
                    .builder()
                    .loggingEnabled(
                            LoggingEnabled
                                    .builder()
                                    .build()
                    )
                    .build();

    static {
        when(TEST_CLIENTS_PROVIDER.getS3Client()).thenReturn(TEST_S3_CLIENT);

        when(TEST_S3_CLIENT.listBuckets()).thenReturn(TEST_LIST_BUCKETS_RESPONSE);
        when(TEST_S3_CLIENT.getBucketLocation(any(Consumer.class)))
                .thenReturn(TEST_BUCKET_LOCATION_RESPONSE);
        when(TEST_S3_CLIENT.getBucketEncryption(any(Consumer.class)))
                .thenReturn(TEST_GET_BUCKET_ENCRYPTION_RESPONSE);
        when(TEST_S3_CLIENT.getBucketLogging(any(Consumer.class)))
                .thenReturn(TEST_GET_BUCKET_LOGGING_RESPONSE);
    }

    @Test
    public void shouldLoadResources() {
        S3BucketLoader loader = new S3BucketLoader(TEST_CLIENTS_PROVIDER);

        List<S3BucketResource> resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals(TEST_REGION.id(), resources.get(0).getRegion());
        assertEquals(TEST_BUCKET_NAME, resources.get(0).getBucketName());
    }
}
