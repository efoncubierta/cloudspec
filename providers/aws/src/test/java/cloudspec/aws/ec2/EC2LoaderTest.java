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
package cloudspec.aws.ec2;

import cloudspec.aws.IAWSClientsProvider;
import datagen.services.ec2.model.RegionGenerator;
import org.junit.Before;
import org.mockito.Mockito;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class EC2LoaderTest {
    protected final IAWSClientsProvider clientsProvider = mock(IAWSClientsProvider.class);
    protected final Ec2Client ec2Client = mock(Ec2Client.class);

    @Before
    public void doBefore() {
        // restore clients provider
        Mockito.reset(clientsProvider);
        when(clientsProvider.getEc2Client()).thenReturn(ec2Client);
        when(clientsProvider.getEc2ClientForRegion(anyString())).thenReturn(ec2Client);

        // restore ec2 client
        Mockito.reset(ec2Client);
        when(ec2Client.describeRegions()).thenReturn(DescribeRegionsResponse
                .builder()
                .regions(RegionGenerator.region())
                .build()
        );
    }
}
