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

import datagen.services.ec2.model.ImageGenerator.imageId
import datagen.services.ec2.model.ImageGenerator.images
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeImagesRequest
import software.amazon.awssdk.services.ec2.model.DescribeImagesResponse
import java.util.function.Consumer

class EC2ImageLoaderTest : EC2LoaderTest() {
    private val loader = EC2ImageLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val images = images()

        every {
            ec2Client.describeImages(any<Consumer<DescribeImagesRequest.Builder>>())
        } answers {
            val builder = DescribeImagesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeImagesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertTrue(request.filters().isEmpty())

            DescribeImagesResponse.builder()
                    .images(images)
                    .build()
        }

        val resources = loader.all
        Assert.assertNotNull(resources)
        Assert.assertEquals(images.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeImages(any<Consumer<DescribeImagesRequest.Builder>>())
        } answers {
            val builder = DescribeImagesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeImagesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeImagesResponse.builder()
                    .build()
        }

        val resource = loader.byId(imageId())
        Assert.assertNull(resource)
    }

    @Test
    fun `should load resource by id`() {
        val images = images()

        every {
            ec2Client.describeImages(any<Consumer<DescribeImagesRequest.Builder>>())
        } answers {
            val builder = DescribeImagesRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeImagesRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            Assert.assertFalse(request.filters().isEmpty())

            DescribeImagesResponse.builder()
                    .images(images)
                    .build()
        }

        val image = images[0]
        val resource = loader.byId(image.imageId())
        Assert.assertNotNull(resource)
    }
}
