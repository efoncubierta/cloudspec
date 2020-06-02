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

import cloudspec.aws.ec2.loader.EC2NatGatewayLoader;
import datagen.services.ec2.model.NatGatewayGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysRequest;
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.NatGateway;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2NatGatewayLoaderTest extends EC2LoaderTest {
    private final EC2NatGatewayLoader loader = new EC2NatGatewayLoader(clientsProvider);

    @Test
    public void shouldLoadNatGateways() {
        var natGateways = NatGatewayGenerator.natGateways();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeNatGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeNatGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filter().isEmpty());
            return DescribeNatGatewaysResponse.builder()
                                              .natGateways(natGateways.toArray(new NatGateway[0]))
                                              .build();
        }).when(ec2Client).describeNatGateways(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(natGateways.size(), resources.size());
        assertTrue(natGateways.containsAll(resources));
    }

    @Test
    public void shouldNotLoadNatGatewayByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeNatGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeNatGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filter().isEmpty());
            return DescribeNatGatewaysResponse.builder()
                                              .build();
        }).when(ec2Client).describeNatGateways(any(Consumer.class));

        var resourceOpt = loader.getById(NatGatewayGenerator.natGatewayId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadNatGatewayById() {
        var natGateways = NatGatewayGenerator.natGateways(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeNatGatewaysRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeNatGatewaysRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filter().isEmpty());
            return DescribeNatGatewaysResponse.builder()
                                              .natGateways(natGateways.toArray(new NatGateway[0]))
                                              .build();
        }).when(ec2Client).describeNatGateways(any(Consumer.class));

        var natGateway = natGateways.get(0);
        var resourceOpt = loader.getById(natGateway.natGatewayId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), natGateway);
    }
}
