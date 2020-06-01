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

import cloudspec.aws.ec2.loader.EC2InternetGatewayLoader;
import datagen.services.ec2.model.InternetGatewayGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeInternetGatewaysResponse;
import software.amazon.awssdk.services.ec2.model.InternetGateway;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class EC2InternetGatewayLoaderTest extends EC2LoaderTest {
    private final EC2InternetGatewayLoader loader = new EC2InternetGatewayLoader(clientsProvider);

    @Test
    public void shouldLoadInternetGateways() {
        var internetGateways = InternetGatewayGenerator.internetGateways();
        when(ec2Client.describeInternetGateways(any(Consumer.class))).thenReturn(
                DescribeInternetGatewaysResponse.builder()
                                                .internetGateways(internetGateways.toArray(new InternetGateway[0]))
                                                .build()
        );

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(internetGateways.size(), resources.size());
        assertTrue(internetGateways.containsAll(resources));
    }

    @Test
    public void shouldNotLoadInternetGatewayByRandomId() {
        when(ec2Client.describeInternetGateways(any(Consumer.class))).thenReturn(
                DescribeInternetGatewaysResponse.builder()
                                                .build()
        );

        var resourceOpt = loader.getById(InternetGatewayGenerator.internetGatewayId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadInternetGatewayById() {
        var internetGateways = InternetGatewayGenerator.internetGateways(1);
        when(ec2Client.describeInternetGateways(any(Consumer.class))).thenReturn(
                DescribeInternetGatewaysResponse.builder()
                                                .internetGateways(internetGateways.toArray(new InternetGateway[0]))
                                                .build()
        );

        var internetGateway = internetGateways.get(0);
        var resourceOpt = loader.getById(internetGateway.internetGatewayId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), internetGateway);
    }
}
