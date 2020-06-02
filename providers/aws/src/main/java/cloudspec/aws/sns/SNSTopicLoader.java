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
package cloudspec.aws.sns;

import cloudspec.aws.IAWSClientsProvider;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.Topic;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SNSTopicLoader implements SNSResourceLoader<SNSTopicResource> {
    private final IAWSClientsProvider clientsProvider;

    public SNSTopicLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<SNSTopicResource> getById(String id) {
        return getTopics(Collections.singletonList(id)).findFirst();
    }

    @Override
    public List<SNSTopicResource> getAll() {
        return getTopics(Collections.emptyList()).collect(Collectors.toList());
    }

    private Stream<SNSTopicResource> getTopics(List<String> topicArns) {
        SnsClient snsClient = clientsProvider.getSnsClient();

        try {
            Stream<String> topicArnsStream;
            if (topicArns != null && topicArns.size() > 0) {
                topicArnsStream = topicArns.stream();
            }
            else {
                topicArnsStream = snsClient.listTopics()
                                           .topics()
                                           .stream()
                                           .map(Topic::topicArn);
            }

            return topicArnsStream
                    .map(topicArn ->
                            this.toResource(
                                    topicArn,
                                    snsClient.getTopicAttributes(builder -> builder.topicArn(topicArn)).attributes()
                            )
                    );
        } finally {
            IoUtils.closeQuietly(snsClient, null);
        }

    }

    private SNSTopicResource toResource(String topicArn, Map<String, String> queueAttributes) {
        SNSTopicResource resource = new SNSTopicResource();
        resource.topicArn = topicArn;
        resource.enabled = Boolean.valueOf(queueAttributes.get("enable"));
        return resource;
    }
}
