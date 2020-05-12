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
package cloudspec.aws.sqs;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.ResourceDefRef;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = SQSResource.GROUP_NAME,
        name = SQSQueueResource.RESOURCE_NAME,
        description = "SQS Queue"
)
public class SQSQueueResource extends SQSResource {
    public static final String RESOURCE_NAME = "queue";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @IdDefinition
    @PropertyDefinition(
            name = "queue_arn",
            description = "Queue Arn"
    )
    private String queueArn;

    @PropertyDefinition(
            name = "visibility_timeout",
            description = "Visibility timeout"
    )
    private Integer visibilityTimeout;

    @PropertyDefinition(
            name = "delay_seconds",
            description = "Delay in seconds"
    )
    private Integer delaySeconds;

    @PropertyDefinition(
            name = "message_retention_period",
            description = "Message retention period"
    )
    private Integer messageRetentionPeriod;

    public String getQueueArn() {
        return queueArn;
    }

    public Integer getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public Integer getDelaySeconds() {
        return delaySeconds;
    }

    public Integer getMessageRetentionPeriod() {
        return messageRetentionPeriod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String queueArn;
        private Integer visibilityTimeout;
        private Integer delaySeconds;
        private Integer messageRetentionPeriod;

        public Builder setQueueArn(String queueArn) {
            this.queueArn = queueArn;
            return this;
        }

        public Builder setVisibilityTimeout(Integer visibilityTimeout) {
            this.visibilityTimeout = visibilityTimeout;
            return this;
        }

        public Builder setDelaySeconds(Integer delaySeconds) {
            this.delaySeconds = delaySeconds;
            return this;
        }

        public Builder setMessageRetentionPeriod(Integer messageRetentionPeriod) {
            this.messageRetentionPeriod = messageRetentionPeriod;
            return this;
        }

        public SQSQueueResource build() {
            SQSQueueResource resource = new SQSQueueResource();
            resource.queueArn = queueArn;
            resource.visibilityTimeout = visibilityTimeout;
            resource.delaySeconds = delaySeconds;
            resource.messageRetentionPeriod = messageRetentionPeriod;
            return resource;
        }
    }
}
