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
import datagen.services.ec2.model.NetworkInterfaceGenerator.networkInterfaceId
import datagen.services.ec2.model.NetworkInterfaceGenerator.networkInterfaces
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesRequest
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2NetworkInterfaceLoaderTest : EC2LoaderTest() {
    private val loader = EC2NetworkInterfaceLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val networkInterfaces = networkInterfaces()

        every {
            ec2Client.describeNetworkInterfaces(any<Consumer<DescribeNetworkInterfacesRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkInterfacesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkInterfacesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filters().isEmpty())

            DescribeNetworkInterfacesResponse.builder()
                    .networkInterfaces(networkInterfaces)
                    .build()
        }

        val resources = loader.all
        assertNotNull(resources)
        assertEquals(networkInterfaces.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeNetworkInterfaces(any<Consumer<DescribeNetworkInterfacesRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkInterfacesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkInterfacesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeNetworkInterfacesResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(networkInterfaceId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val networkInterfaces = networkInterfaces()

        every {
            ec2Client.describeNetworkInterfaces(any<Consumer<DescribeNetworkInterfacesRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkInterfacesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkInterfacesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeNetworkInterfacesResponse.builder()
                    .networkInterfaces(networkInterfaces)
                    .build()
        }

        val networkInterface = networkInterfaces[0]
        val resourceOpt = loader.byId(networkInterface.networkInterfaceId())
        assertTrue(resourceOpt is Some<*>)
    }
}
