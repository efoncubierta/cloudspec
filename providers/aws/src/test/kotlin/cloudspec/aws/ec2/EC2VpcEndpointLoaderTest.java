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

import datagen.services.ec2.model.VpcEndpointGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsResponse;
import software.amazon.awssdk.services.ec2.model.VpcEndpoint;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2VpcEndpointLoaderTest extends EC2LoaderTest {
    private final EC2VpcEndpointLoader loader = new EC2VpcEndpointLoader(getClientsProvider());

    @Test
    public void shouldLoadVpcEndpoints() {
        var vpcEndpoints = VpcEndpointGenerator.vpcEndpoints();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcEndpointsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcEndpointsRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeVpcEndpointsResponse.builder()
                                               .vpcEndpoints(vpcEndpoints.toArray(new VpcEndpoint[0]))
                                               .build();
        }).when(getEc2Client()).describeVpcEndpoints(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(vpcEndpoints.size(), resources.size());
    }

    @Test
    public void shouldNotLoadVpcEndpointByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcEndpointsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcEndpointsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeVpcEndpointsResponse.builder()
                                               .build();
        }).when(getEc2Client()).describeVpcEndpoints(any(Consumer.class));

        var resource = loader.byId(VpcEndpointGenerator.vpcEndpointId());

        assertNotNull(resource);
    }

    @Test
    public void shouldLoadVpcEndpointById() {
        var vpcEndpoints = VpcEndpointGenerator.vpcEndpoints(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcEndpointsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcEndpointsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeVpcEndpointsResponse.builder()
                                               .vpcEndpoints(vpcEndpoints.toArray(new VpcEndpoint[0]))
                                               .build();
        }).when(getEc2Client()).describeVpcEndpoints(any(Consumer.class));

        var vpcEndpoint = vpcEndpoints.get(0);
        var resource = loader.byId(vpcEndpoint.vpcEndpointId());
        assertNotNull(resource);
    }
}
