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
import datagen.services.ec2.model.SubnetGenerator.subnetId
import datagen.services.ec2.model.SubnetGenerator.subnets
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsRequest
import software.amazon.awssdk.services.ec2.model.DescribeSubnetsResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2SubnetLoaderTest : EC2LoaderTest() {
    private val loader = EC2SubnetLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val subnets = subnets()

        every {
            ec2Client.describeSubnets(any<Consumer<DescribeSubnetsRequest.Builder>>())
        } answers {
            val builder = DescribeSubnetsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSubnetsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filters().isEmpty())

            DescribeSubnetsResponse.builder()
                    .subnets(subnets)
                    .build()
        }

        val resources = loader.all(emptyList())
        assertNotNull(resources)
        assertEquals(subnets.size, resources.unsafeRunSync().size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeSubnets(any<Consumer<DescribeSubnetsRequest.Builder>>())
        } answers {
            val builder = DescribeSubnetsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSubnetsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeSubnetsResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(emptyList(), subnetId())
        assertTrue(resourceOpt.unsafeRunSync() is None)
    }

    @Test
    fun `should load resource by id`() {
        val subnets = subnets()

        every {
            ec2Client.describeSubnets(any<Consumer<DescribeSubnetsRequest.Builder>>())
        } answers {
            val builder = DescribeSubnetsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSubnetsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeSubnetsResponse.builder()
                    .subnets(subnets)
                    .build()
        }

        val subnet = subnets[0]
        val resourceOpt = loader.byId(emptyList(), subnet.subnetId())
        assertTrue(resourceOpt.unsafeRunSync() is Some<*>)
    }
}
