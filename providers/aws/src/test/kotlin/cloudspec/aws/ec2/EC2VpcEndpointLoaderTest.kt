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

import datagen.services.ec2.model.VpcEndpointGenerator.vpcEndpointId
import datagen.services.ec2.model.VpcEndpointGenerator.vpcEndpoints
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsRequest
import software.amazon.awssdk.services.ec2.model.DescribeVpcEndpointsResponse
import java.util.function.Consumer

class EC2VpcEndpointLoaderTest : EC2LoaderTest() {
    private val loader = EC2VpcEndpointLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val vpcEndpoints = vpcEndpoints()

        every {
            ec2Client.describeVpcEndpoints(any<Consumer<DescribeVpcEndpointsRequest.Builder>>())
        } answers {
            val builder = DescribeVpcEndpointsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeVpcEndpointsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.filters().isEmpty())

            DescribeVpcEndpointsResponse.builder()
                    .vpcEndpoints(vpcEndpoints)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(vpcEndpoints.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeVpcEndpoints(any<Consumer<DescribeVpcEndpointsRequest.Builder>>())
        } answers {
            val builder = DescribeVpcEndpointsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeVpcEndpointsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeVpcEndpointsResponse.builder()
                    .build()
        }

        val resource = loader.byId(vpcEndpointId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val vpcEndpoints = vpcEndpoints()

        every {
            ec2Client.describeVpcEndpoints(any<Consumer<DescribeVpcEndpointsRequest.Builder>>())
        } answers {
            val builder = DescribeVpcEndpointsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeVpcEndpointsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeVpcEndpointsResponse.builder()
                    .vpcEndpoints(vpcEndpoints)
                    .build()
        }

        val vpcEndpoint = vpcEndpoints[0]
        val resource = loader.byId(vpcEndpoint.vpcEndpointId())
        Assert.assertNotNull(resource)
    }
}
