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
