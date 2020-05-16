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

import cloudspec.aws.IAWSClientsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SQSQueueLoader implements SQSResourceLoader<SQSQueueResource> {
    private final IAWSClientsProvider clientsProvider;

    public SQSQueueLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<SQSQueueResource> getById(String id) {
        return getQueues(Collections.singletonList(id)).findFirst();
    }

    @Override
    public List<SQSQueueResource> getAll() {
        return getQueues(Collections.emptyList()).collect(Collectors.toList());
    }

    private Stream<SQSQueueResource> getQueues(List<String> queueArns) {
        SqsClient sqsClient = clientsProvider.getSqsClient();

        try {
            Stream<String> queueUrlsStream;
            if (queueArns != null && queueArns.size() > 0) {
                queueUrlsStream = queueArns.stream()
                        .map(queueArn -> queueArn.substring(queueArn.lastIndexOf(":")))
                        .map(queueName -> sqsClient.getQueueUrl(builder -> builder.queueName(queueName)))
                        .map(GetQueueUrlResponse::queueUrl);
            } else {
                queueUrlsStream = sqsClient.listQueues()
                        .queueUrls()
                        .stream();
            }

            return queueUrlsStream
                    .map(queueUrl -> sqsClient.getQueueAttributes(builder -> builder.queueUrl(queueUrl)).attributes())
                    .map(this::toResource);
        } finally {
            IoUtils.closeQuietly(sqsClient, null);
        }

    }

    private SQSQueueResource toResource(Map<QueueAttributeName, String> queueAttributes) {
        SQSQueueResource resource = new SQSQueueResource();
        resource.queueArn = queueAttributes.get(QueueAttributeName.QUEUE_ARN);
        resource.visibilityTimeout = Integer.valueOf(queueAttributes.get(QueueAttributeName.VISIBILITY_TIMEOUT));
        resource.delaySeconds = Integer.valueOf(queueAttributes.get(QueueAttributeName.DELAY_SECONDS));
        resource.messageRetentionPeriod = Integer.valueOf(queueAttributes.get(QueueAttributeName.MESSAGE_RETENTION_PERIOD));
        return resource;
    }
}
