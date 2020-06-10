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
import datagen.services.ec2.model.NatGatewayGenerator.natGatewayId
import datagen.services.ec2.model.NatGatewayGenerator.natGateways
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysRequest
import software.amazon.awssdk.services.ec2.model.DescribeNatGatewaysResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2NatGatewayLoaderTest : EC2LoaderTest() {
    private val loader = EC2NatGatewayLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val natGateways = natGateways()

        every {
            ec2Client.describeNatGateways(any<Consumer<DescribeNatGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeNatGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNatGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filter().isEmpty())

            DescribeNatGatewaysResponse.builder()
                    .natGateways(natGateways)
                    .build()
        }

        val resources = loader.all
        assertNotNull(resources)
        assertEquals(natGateways.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeNatGateways(any<Consumer<DescribeNatGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeNatGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNatGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filter().isEmpty())

            DescribeNatGatewaysResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(natGatewayId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val natGateways = natGateways()

        every {
            ec2Client.describeNatGateways(any<Consumer<DescribeNatGatewaysRequest.Builder>>())
        } answers {
            val builder = DescribeNatGatewaysRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNatGatewaysRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filter().isEmpty())

            DescribeNatGatewaysResponse.builder()
                    .natGateways(natGateways)
                    .build()
        }

        val natGateway = natGateways[0]
        val resourceOpt = loader.byId(natGateway.natGatewayId())
        assertTrue(resourceOpt is Some<*>)
    }
}
