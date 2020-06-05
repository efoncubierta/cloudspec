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

import datagen.services.ec2.model.LocalGatewayGenerator.localGatewayId
import datagen.services.ec2.model.LocalGatewayGenerator.localGateways
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeLocalGatewaysRequest
import software.amazon.awssdk.services.ec2.model.DescribeLocalGatewaysResponse
import java.util.function.Consumer

class EC2LocalGatewayLoaderTest : EC2LoaderTest() {
    private val loader = EC2LocalGatewayLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val localGateways = localGateways()

        every {
            ec2Client.describeLocalGateways(any<Consumer<DescribeLocalGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeLocalGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeLocalGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.filters().isEmpty())

            DescribeLocalGatewaysResponse.builder()
                    .localGateways(localGateways)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(localGateways.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeLocalGateways(any<Consumer<DescribeLocalGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeLocalGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeLocalGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeLocalGatewaysResponse.builder()
                    .build()
        }

        val resource = loader.byId(localGatewayId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val localGateways = localGateways()

        every {
            ec2Client.describeLocalGateways(any<Consumer<DescribeLocalGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeLocalGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeLocalGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeLocalGatewaysResponse.builder()
                    .localGateways(localGateways)
                    .build()
        }

        val localGateway = localGateways[0]
        val resource = loader.byId(localGateway.localGatewayId())
        Assert.assertNotNull(resource)
    }
}
