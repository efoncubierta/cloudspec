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
package cloudspec.aws.ec2;

import datagen.services.ec2.model.ElasticGpusGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeElasticGpusRequest;
import software.amazon.awssdk.services.ec2.model.DescribeElasticGpusResponse;
import software.amazon.awssdk.services.ec2.model.ElasticGpus;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2ElasticGpusLoaderTest extends EC2LoaderTest {
    private final EC2ElasticGpuLoader loader = new EC2ElasticGpuLoader(getClientsProvider());

    @Test
    public void shouldLoadElasticGpus() {
        var elasticGpusList = ElasticGpusGenerator.elasticGpusList();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeElasticGpusRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeElasticGpusRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().elasticGpuIds().isEmpty());
            return DescribeElasticGpusResponse.builder()
                                              .elasticGpuSet(elasticGpusList.toArray(new ElasticGpus[0]))
                                              .build();
        }).when(getEc2Client()).describeElasticGpus(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(elasticGpusList.size(), resources.size());
    }

    @Test
    public void shouldNotLoadElasticGpusByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeElasticGpusRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeElasticGpusRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().elasticGpuIds().isEmpty());
            return DescribeElasticGpusResponse.builder()
                                              .build();
        }).when(getEc2Client()).describeElasticGpus(any(Consumer.class));

        var resource = loader.byId(ElasticGpusGenerator.elasticGpuId());

        assertNotNull(resource);
    }

    @Test
    public void shouldLoadElasticGpusById() {
        var elasticGpusList = ElasticGpusGenerator.elasticGpusList(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeElasticGpusRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeElasticGpusRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().elasticGpuIds().isEmpty());
            return DescribeElasticGpusResponse.builder()
                                              .elasticGpuSet(elasticGpusList.toArray(new ElasticGpus[0]))
                                              .build();
        }).when(getEc2Client()).describeElasticGpus(any(Consumer.class));

        var elasticGpus = elasticGpusList.get(0);
        var resource = loader.byId(elasticGpus.elasticGpuId());
        assertNotNull(resource);
    }
}
