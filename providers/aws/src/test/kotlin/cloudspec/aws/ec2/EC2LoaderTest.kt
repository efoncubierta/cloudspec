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

import cloudspec.aws.IAWSClientsProvider
import datagen.services.ec2.model.RegionGenerator
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import software.amazon.awssdk.services.ec2.Ec2Client
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse

abstract class EC2LoaderTest {
    protected val clientsProvider = mockk<IAWSClientsProvider>()
    protected val ec2Client = mockk<Ec2Client>()

    @Before
    fun doBefore() {
        // restore clients provider
        clearMocks(clientsProvider)
        every { clientsProvider.ec2Client } returns ec2Client
        every { clientsProvider.ec2ClientForRegion(any()) } returns ec2Client

        // restore ec2 client
        clearMocks(ec2Client)
        every { ec2Client.describeRegions() } returns DescribeRegionsResponse
                .builder()
                .regions(RegionGenerator.region())
                .build()
        every { ec2Client.close() } returns Unit
    }
}
