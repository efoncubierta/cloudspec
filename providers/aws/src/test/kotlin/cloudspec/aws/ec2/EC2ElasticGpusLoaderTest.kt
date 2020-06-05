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

import datagen.services.ec2.model.ElasticGpusGenerator.elasticGpuId
import datagen.services.ec2.model.ElasticGpusGenerator.elasticGpusList
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeElasticGpusRequest
import software.amazon.awssdk.services.ec2.model.DescribeElasticGpusResponse
import java.util.function.Consumer

class EC2ElasticGpusLoaderTest : EC2LoaderTest() {
    private val loader = EC2ElasticGpuLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val elasticGpusList = elasticGpusList()

        every {
            ec2Client.describeElasticGpus(any<Consumer<DescribeElasticGpusRequest.Builder>>())
        } answers {
            val builder = DescribeElasticGpusRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeElasticGpusRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.elasticGpuIds().isEmpty())

            DescribeElasticGpusResponse.builder()
                    .elasticGpuSet(elasticGpusList)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(elasticGpusList.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeElasticGpus(any<Consumer<DescribeElasticGpusRequest.Builder>>())
        } answers {
            val builder = DescribeElasticGpusRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeElasticGpusRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.elasticGpuIds().isEmpty())

            DescribeElasticGpusResponse.builder()
                    .build()
        }

        val resource = loader.byId(elasticGpuId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val elasticGpusList = elasticGpusList()

        every {
            ec2Client.describeElasticGpus(any<Consumer<DescribeElasticGpusRequest.Builder>>())
        } answers {
            val builder = DescribeElasticGpusRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeElasticGpusRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.elasticGpuIds().isEmpty())

            DescribeElasticGpusResponse.builder()
                    .elasticGpuSet(elasticGpusList)
                    .build()
        }

        val elasticGpus = elasticGpusList[0]
        val resource = loader.byId(elasticGpus.elasticGpuId())
        Assert.assertNotNull(resource)
    }
}
