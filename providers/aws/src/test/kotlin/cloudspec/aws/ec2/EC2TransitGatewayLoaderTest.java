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

import datagen.services.ec2.model.TransitGatewayGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeTransitGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DescribeTransitGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.TransitGateway;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2TransitGatewayLoaderTest extends EC2LoaderTest {
    private final EC2TransitGatewayLoader loader = new EC2TransitGatewayLoader(getClientsProvider());

    @Test
    public void shouldLoadTransitGateways() {
        var transitGateways = TransitGatewayGenerator.transitGateways();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeTransitGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeTransitGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeTransitGatewaysResponse.builder()
                                                  .transitGateways(transitGateways.toArray(new TransitGateway[0]))
                                                  .build();
        }).when(getEc2Client()).describeTransitGateways(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(transitGateways.size(), resources.size());
    }

    @Test
    public void shouldNotLoadTransitGatewayByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeTransitGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeTransitGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeTransitGatewaysResponse.builder()
                                                  .build();
        }).when(getEc2Client()).describeTransitGateways(any(Consumer.class));

        var resource = loader.byId(TransitGatewayGenerator.transitGatewayId());

        assertNotNull(resource);
    }

    @Test
    public void shouldLoadTransitGatewayById() {
        var transitGateways = TransitGatewayGenerator.transitGateways(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeTransitGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeTransitGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeTransitGatewaysResponse.builder()
                                                  .transitGateways(transitGateways.toArray(new TransitGateway[0]))
                                                  .build();
        }).when(getEc2Client()).describeTransitGateways(any(Consumer.class));

        var transitGateway = transitGateways.get(0);
        var resource = loader.byId(transitGateway.transitGatewayId());
        assertNotNull(resource);
    }
}
