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
import datagen.services.ec2.model.NetworkAclGenerator.networkAclId
import datagen.services.ec2.model.NetworkAclGenerator.networkAcls
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsRequest
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2NetworkAclLoaderTest : EC2LoaderTest() {
    private val loader = EC2NetworkAclLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val networkAcls = networkAcls()

        every {
            ec2Client.describeNetworkAcls(any<Consumer<DescribeNetworkAclsRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkAclsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkAclsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.filters().isEmpty())

            DescribeNetworkAclsResponse.builder()
                    .networkAcls(networkAcls)
                    .build()
        }

        val resources = loader.all(emptySet())
        assertNotNull(resources)
        assertEquals(networkAcls.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeNetworkAcls(any<Consumer<DescribeNetworkAclsRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkAclsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkAclsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeNetworkAclsResponse.builder()
                    .build()
        }

        val resourceOpt = loader.byId(emptySet(), networkAclId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val networkAcls = networkAcls()

        every {
            ec2Client.describeNetworkAcls(any<Consumer<DescribeNetworkAclsRequest.Builder>>())
        } answers {
            val builder = DescribeNetworkAclsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeNetworkAclsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.filters().isEmpty())

            DescribeNetworkAclsResponse.builder()
                    .networkAcls(networkAcls)
                    .build()
        }

        val networkAcl = networkAcls[0]
        val resourceOpt = loader.byId(emptySet(), networkAcl.networkAclId())
        assertTrue(resourceOpt is Some<*>)
    }
}
