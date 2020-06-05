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

import datagen.services.ec2.model.ReservedInstancesGenerator.reservedInstancesId
import datagen.services.ec2.model.ReservedInstancesGenerator.reservedInstancesList
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeReservedInstancesRequest
import software.amazon.awssdk.services.ec2.model.DescribeReservedInstancesResponse
import java.util.function.Consumer

class EC2ReservedInstancesLoaderTest : EC2LoaderTest() {
    private val loader = EC2ReservedInstancesLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val reservedInstancesList = reservedInstancesList()

        every {
            ec2Client.describeReservedInstances(any<Consumer<DescribeReservedInstancesRequest.Builder>>())
        } answers {
            val builder = DescribeReservedInstancesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeReservedInstancesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.filters().isEmpty())

            DescribeReservedInstancesResponse.builder()
                    .reservedInstances(reservedInstancesList)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(reservedInstancesList.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeReservedInstances(any<Consumer<DescribeReservedInstancesRequest.Builder>>())
        } answers {
            val builder = DescribeReservedInstancesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeReservedInstancesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeReservedInstancesResponse.builder()
                    .build()
        }

        val resource = loader.byId(reservedInstancesId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val reservedInstancesList = reservedInstancesList()

        every {
            ec2Client.describeReservedInstances(any<Consumer<DescribeReservedInstancesRequest.Builder>>())
        } answers {
            val builder = DescribeReservedInstancesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeReservedInstancesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeReservedInstancesResponse.builder()
                    .reservedInstances(reservedInstancesList)
                    .build()
        }

        val reservedInstances = reservedInstancesList[0]
        val resource = loader.byId(reservedInstances.reservedInstancesId())
        Assert.assertNotNull(resource)
    }
}
