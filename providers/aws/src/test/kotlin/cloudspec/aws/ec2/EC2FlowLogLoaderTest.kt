/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.aws.ec2

import arrow.core.None
import arrow.core.Some
import datagen.services.ec2.model.FlowLogGenerator.flowLogId
import datagen.services.ec2.model.FlowLogGenerator.flowLogs
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeFlowLogsRequest
import software.amazon.awssdk.services.ec2.model.DescribeFlowLogsResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2FlowLogLoaderTest : EC2LoaderTest() {
    private val loader = EC2FlowLogLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val flowLogs = flowLogs()

        every {
            ec2Client.describeFlowLogs(any<Consumer<DescribeFlowLogsRequest.Builder>>())
        } answers {
            val builder = DescribeFlowLogsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeFlowLogsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filter().isEmpty())

            DescribeFlowLogsResponse.builder()
                    .flowLogs(flowLogs)
                    .build()
        }

        val resources = loader.all(emptyList())
        assertNotNull(resources)
        assertEquals(flowLogs.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeFlowLogs(any<Consumer<DescribeFlowLogsRequest.Builder>>())
        } answers {
            val builder = DescribeFlowLogsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeFlowLogsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filter().isEmpty())

            DescribeFlowLogsResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(emptyList(), flowLogId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val flowLogs = flowLogs()

        every {
            ec2Client.describeFlowLogs(any<Consumer<DescribeFlowLogsRequest.Builder>>())
        } answers {
            val builder = DescribeFlowLogsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeFlowLogsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filter().isEmpty())

            DescribeFlowLogsResponse.builder()
                    .flowLogs(flowLogs)
                    .build()
        }

        val flowLog = flowLogs[0]
        val resourceOpt = loader.byId(emptyList(), flowLog.flowLogId())
        assertTrue(resourceOpt is Some<*>)
    }
}
