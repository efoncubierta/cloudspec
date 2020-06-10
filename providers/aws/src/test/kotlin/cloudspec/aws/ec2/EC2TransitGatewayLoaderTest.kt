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
import datagen.services.ec2.model.TransitGatewayGenerator.transitGatewayId
import datagen.services.ec2.model.TransitGatewayGenerator.transitGateways
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeTransitGatewaysRequest
import software.amazon.awssdk.services.ec2.model.DescribeTransitGatewaysResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2TransitGatewayLoaderTest : EC2LoaderTest() {
    private val loader = EC2TransitGatewayLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val transitGateways = transitGateways()

        every {
            ec2Client.describeTransitGateways(any<Consumer<DescribeTransitGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeTransitGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeTransitGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filters().isEmpty())

            DescribeTransitGatewaysResponse.builder()
                    .transitGateways(transitGateways)
                    .build()
        }

        val resources = loader.all
        assertNotNull(resources)
        assertEquals(transitGateways.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeTransitGateways(any<Consumer<DescribeTransitGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeTransitGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeTransitGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeTransitGatewaysResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(transitGatewayId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val transitGateways = transitGateways()

        every {
            ec2Client.describeTransitGateways(any<Consumer<DescribeTransitGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeTransitGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeTransitGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeTransitGatewaysResponse.builder()
                    .transitGateways(transitGateways)
                    .build()
        }

        val transitGateway = transitGateways[0]
        val resourceOpt = loader.byId(transitGateway.transitGatewayId())
        assertTrue(resourceOpt is Some<*>)
    }
}
