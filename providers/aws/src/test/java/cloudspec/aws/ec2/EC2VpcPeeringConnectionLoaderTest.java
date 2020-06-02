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

import cloudspec.aws.ec2.loader.EC2VpcPeeringConnectionLoader;
import datagen.services.ec2.model.VpcPeeringConnectionGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeVpcPeeringConnectionsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeVpcPeeringConnectionsResponse;
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2VpcPeeringConnectionLoaderTest extends EC2LoaderTest {
    private final EC2VpcPeeringConnectionLoader loader = new EC2VpcPeeringConnectionLoader(clientsProvider);

    @Test
    public void shouldLoadVpcPeeringConnections() {
        var vpcPeeringConnections = VpcPeeringConnectionGenerator.vpcPeeringConnections();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcPeeringConnectionsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcPeeringConnectionsRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeVpcPeeringConnectionsResponse.builder()
                                                        .vpcPeeringConnections(vpcPeeringConnections.toArray(new VpcPeeringConnection[0]))
                                                        .build();
        }).when(ec2Client).describeVpcPeeringConnections(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(vpcPeeringConnections.size(), resources.size());
        assertTrue(vpcPeeringConnections.containsAll(resources));
    }

    @Test
    public void shouldNotLoadVpcPeeringConnectionByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcPeeringConnectionsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcPeeringConnectionsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeVpcPeeringConnectionsResponse.builder()
                                                        .build();
        }).when(ec2Client).describeVpcPeeringConnections(any(Consumer.class));

        var resourceOpt = loader.getById(VpcPeeringConnectionGenerator.vpcPeeringConnectionId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadVpcPeeringConnectionById() {
        var vpcPeeringConnections = VpcPeeringConnectionGenerator.vpcPeeringConnections(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeVpcPeeringConnectionsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeVpcPeeringConnectionsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeVpcPeeringConnectionsResponse.builder()
                                                        .vpcPeeringConnections(vpcPeeringConnections.toArray(new VpcPeeringConnection[0]))
                                                        .build();
        }).when(ec2Client).describeVpcPeeringConnections(any(Consumer.class));

        var vpcPeeringConnection = vpcPeeringConnections.get(0);
        var resourceOpt = loader.getById(vpcPeeringConnection.vpcPeeringConnectionId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), vpcPeeringConnection);
    }
}
