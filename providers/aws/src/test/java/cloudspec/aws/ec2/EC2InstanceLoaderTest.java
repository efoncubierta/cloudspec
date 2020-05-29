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

public class EC2InstanceLoaderTest {
//    private static final IAWSClientsProvider TEST_CLIENTS_PROVIDER = mock(IAWSClientsProvider.class);
//    private static final Ec2Client TEST_EC2_CLIENT = mock(Ec2Client.class);
//
//    private static final EC2InstanceResource TEST_EC2_INSTANCE = EC2InstanceUtil.randomEc2InstanceResource();
//
//    static {
//        when(TEST_CLIENTS_PROVIDER.getEc2Client()).thenReturn(TEST_EC2_CLIENT);
//        when(TEST_CLIENTS_PROVIDER.getEc2ClientForRegion(anyString())).thenReturn(TEST_EC2_CLIENT);
//
//        when(TEST_EC2_CLIENT.describeRegions()).thenReturn(DescribeRegionsResponse
//                .builder()
//                .regions(
//                        software.amazon.awssdk.services.ec2.model.Region
//                                .builder()
//                                .regionName(TEST_EC2_INSTANCE.)
//                                .build()
//                )
//                .build()
//        );
//
//        when(TEST_EC2_CLIENT.describeInstances()).thenReturn(
//                DescribeInstancesResponse
//                        .builder()
//                        .reservations(
//                                Reservation
//                                        .builder()
//                                        .instances(
//                                                EC2InstanceUtil.asInstance(TEST_EC2_INSTANCE)
//                                        )
//                                        .build()
//                        )
//                        .build()
//        );
//    }
//
//    @Test
//    public void shouldLoadAllEc2Instances() {
//        EC2InstanceLoader loader = new EC2InstanceLoader(TEST_CLIENTS_PROVIDER);
//
//        List<EC2InstanceResource> resources = loader.getAll();
//
//        assertNotNull(resources);
//        assertEquals(1, resources.size());
//        assertEquals(TEST_EC2_INSTANCE, resources.get(0));
//    }
}
