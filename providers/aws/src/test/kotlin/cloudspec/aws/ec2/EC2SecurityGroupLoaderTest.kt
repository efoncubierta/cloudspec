/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.ec2

import datagen.services.ec2.model.SecurityGroupGenerator.securityGroupId
import datagen.services.ec2.model.SecurityGroupGenerator.securityGroups
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsRequest
import software.amazon.awssdk.services.ec2.model.DescribeSecurityGroupsResponse
import java.util.function.Consumer

class EC2SecurityGroupLoaderTest : EC2LoaderTest() {
    private val loader = EC2SecurityGroupLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val securityGroups = securityGroups()

        every {
            ec2Client.describeSecurityGroups(any<Consumer<DescribeSecurityGroupsRequest.Builder>>())
        } answers {
            val builder = DescribeSecurityGroupsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSecurityGroupsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.filters().isEmpty())

            DescribeSecurityGroupsResponse.builder()
                    .securityGroups(securityGroups)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(securityGroups.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeSecurityGroups(any<Consumer<DescribeSecurityGroupsRequest.Builder>>())
        } answers {
            val builder = DescribeSecurityGroupsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSecurityGroupsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeSecurityGroupsResponse.builder()
                    .build()
        }

        val resource = loader.byId(securityGroupId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val securityGroups = securityGroups()

        every {
            ec2Client.describeSecurityGroups(any<Consumer<DescribeSecurityGroupsRequest.Builder>>())
        } answers {
            val builder = DescribeSecurityGroupsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeSecurityGroupsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeSecurityGroupsResponse.builder()
                    .securityGroups(securityGroups)
                    .build()
        }

        val securityGroup = securityGroups[0]
        val resource = loader.byId(securityGroup.groupId())
        Assert.assertNotNull(resource)
    }
}
