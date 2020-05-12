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

import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.AWSProvider;
import cloudspec.model.ResourceDefRef;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = S3Resource.GROUP_NAME,
        name = S3BucketResource.RESOURCE_NAME,
        description = "S3 Bucket"
)
public class S3BucketResource extends S3Resource {
    public static final String RESOURCE_NAME = "bucket";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "AWS Region"
    )
    private String region;

    @PropertyDefinition(
            name = "bucket_name",
            description = "Bucket name"
    )
    private String bucketName;

    @PropertyDefinition(
            name = "encryption",
            description = "Encryption"
    )
    private S3BucketEncryption encryption;

    @PropertyDefinition(
            name = "logging",
            description = "Logging"
    )
    private S3BucketLogging logging;

    public String getRegion() {
        return region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public S3BucketEncryption getEncryption() {
        return encryption;
    }

    public S3BucketLogging getLogging() {
        return logging;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class S3BucketEncryption {
        @PropertyDefinition(
                name = "enabled",
                description = "Encryption is enabled"
        )
        private Boolean enabled;

        @PropertyDefinition(
                name = "type",
                description = "Encryption type"
        )
        private String type;

        public S3BucketEncryption(Boolean enabled, String type) {
            this.enabled = enabled;
            this.type = type;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public String getType() {
            return type;
        }
    }

    public static class S3BucketLogging {
        @PropertyDefinition(
                name = "enabled",
                description = "Logging is enabled"
        )
        private Boolean enabled;

        public S3BucketLogging(Boolean enabled) {
            this.enabled = enabled;
        }

        public Boolean getEnabled() {
            return enabled;
        }
    }

    public static class Builder {
        private String region;
        private String bucketName;
        private S3BucketEncryption encryption;
        private S3BucketLogging logging;

        public void setRegion(String region) {
            this.region = region;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public void setEncryption(S3BucketEncryption encryption) {
            this.encryption = encryption;
        }

        public void setLogging(S3BucketLogging logging) {
            this.logging = logging;
        }

        public S3BucketResource build() {
            S3BucketResource resource = new S3BucketResource();
            resource.region = region;
            resource.bucketName = bucketName;
            resource.encryption = encryption;
            resource.logging = logging;
            return resource;
        }
    }
}
