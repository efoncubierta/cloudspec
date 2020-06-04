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

import datagen.services.ec2.model.FlowLogGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeFlowLogsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeFlowLogsResponse;
import software.amazon.awssdk.services.ec2.model.FlowLog;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2FlowLogLoaderTest extends EC2LoaderTest {
    private final EC2FlowLogLoader loader = new EC2FlowLogLoader(getClientsProvider());

    @Test
    public void shouldLoadFlowLogs() {
        var flowlogs = FlowLogGenerator.flowLogs();
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeFlowLogsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeFlowLogsRequest.builder();
            builderLambda.accept(builder);
            assertTrue(builder.build().filter().isEmpty());
            return DescribeFlowLogsResponse.builder()
                                           .flowLogs(flowlogs.toArray(new FlowLog[0]))
                                           .build();
        }).when(getEc2Client()).describeFlowLogs(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(flowlogs.size(), resources.size());
    }

    @Test
    public void shouldNotLoadFlowLogByRandomId() {
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeFlowLogsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeFlowLogsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filter().isEmpty());
            return DescribeFlowLogsResponse.builder()
                                           .build();
        }).when(getEc2Client()).describeFlowLogs(any(Consumer.class));

        var resource = loader.byId(FlowLogGenerator.flowLogId());

        assertNotNull(resource);
    }

    @Test
    public void shouldLoadFlowLogById() {
        var flowlogs = FlowLogGenerator.flowLogs(1);
        doAnswer(answer -> {
            var builderLambda = (Consumer<DescribeFlowLogsRequest.Builder>) answer.getArguments()[0];
            var builder = DescribeFlowLogsRequest.builder();
            builderLambda.accept(builder);
            assertFalse(builder.build().filter().isEmpty());
            return DescribeFlowLogsResponse.builder()
                                           .flowLogs(flowlogs.toArray(new FlowLog[0]))
                                           .build();
        }).when(getEc2Client()).describeFlowLogs(any(Consumer.class));

        var flowlog = flowlogs.get(0);
        var resource = loader.byId(flowlog.flowLogId());
        assertNotNull(resource);
    }
}
