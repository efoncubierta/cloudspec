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

import cloudspec.aws.ec2.loader.EC2InstanceLoader;
import datagen.services.ec2.model.InstanceGenerator;
import datagen.services.ec2.model.ReservationGenerator;
import org.junit.Test;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Reservation;

import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

public class EC2InstanceLoaderTest extends EC2LoaderTest {
    private final EC2InstanceLoader loader = new EC2InstanceLoader(clientsProvider);

    @Test
    public void shouldLoadInstances() {
        var reservation = ReservationGenerator.reservation();
        doAnswer(answer -> {
            var builder = (Consumer<DescribeInstancesRequest.Builder>) answer.getArguments()[0];
            builder.accept(DescribeInstancesRequest.builder());
            return DescribeInstancesResponse
                    .builder()
                    .reservations(reservation)
                    .build();
        }).when(ec2Client).describeInstances(any(Consumer.class));

        var resources = loader.getAll();

        assertNotNull(resources);
        assertEquals(reservation.instances().size(), resources.size());
        assertTrue(reservation.instances().containsAll(resources));
    }

    @Test
    public void shouldNotLoadInstanceByRandomId() {
        doAnswer(answer -> {
            var builder = (Consumer<DescribeInstancesRequest.Builder>) answer.getArguments()[0];
            builder.accept(DescribeInstancesRequest.builder());
            return DescribeInstancesResponse
                    .builder()
                    .reservations(Reservation.builder().build())
                    .build();
        }).when(ec2Client).describeInstances(any(Consumer.class));

        var resourceOpt = loader.getById(InstanceGenerator.instanceId());

        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isEmpty());
    }

    @Test
    public void shouldLoadInstanceById() {
        var reservation = ReservationGenerator.reservation(1);

        doAnswer(answer -> {
            var builder = (Consumer<DescribeInstancesRequest.Builder>) answer.getArguments()[0];
            builder.accept(DescribeInstancesRequest.builder());
            return DescribeInstancesResponse
                    .builder()
                    .reservations(reservation)
                    .build();
        }).when(ec2Client).describeInstances(any(Consumer.class));

        var instance = reservation.instances().get(0);
        var resourceOpt = loader.getById(instance.instanceId());
        assertNotNull(resourceOpt);
        assertTrue(resourceOpt.isPresent());
        assertEquals(resourceOpt.get(), instance);
    }
}
