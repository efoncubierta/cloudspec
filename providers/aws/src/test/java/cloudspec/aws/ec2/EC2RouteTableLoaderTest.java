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

import cloudspec.aws.ec2.loader.EC2RouteTableLoader;
import datagen.services.ec2.model.RouteTableGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeRouteTablesResponse;
import software.amazon.awssdk.services.ec2.model.RouteTable;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2RouteTableLoaderTest extends EC2LoaderTest {
    private final EC2RouteTableLoader loader = new EC2RouteTableLoader(clientsProvider);

    @Test
    public void shouldLoadRouteTables() {
        var routeTables = RouteTableGenerator.routeTables();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeRouteTablesRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeRouteTablesRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filters().isEmpty());
            return DescribeRouteTablesResponse.builder()
                                              .routeTables(routeTables.toArray(new RouteTable[0]))
                                              .build();
        }).when(ec2Client).describeRouteTables(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(routeTables.size(), resources.size());
        assertTrue(routeTables.containsAll(resources));
    }

    @Test
    public void shouldNotLoadRouteTableByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeRouteTablesRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeRouteTablesRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeRouteTablesResponse.builder()
                                              .build();
        }).when(ec2Client).describeRouteTables(any(Consumer.class));

        var resourceOpt = loader.getById(RouteTableGenerator.routeTableId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadRouteTableById() {
        var routeTables = RouteTableGenerator.routeTables(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeRouteTablesRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeRouteTablesRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filters().isEmpty());
            return DescribeRouteTablesResponse.builder()
                                              .routeTables(routeTables.toArray(new RouteTable[0]))
                                              .build();
        }).when(ec2Client).describeRouteTables(any(Consumer.class));

        var routeTable = routeTables.get(0);
        var resourceOpt = loader.getById(routeTable.routeTableId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), routeTable);
    }
}
