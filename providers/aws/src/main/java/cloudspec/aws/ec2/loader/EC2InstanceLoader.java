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
package cloudspec.aws.ec2.loader;

import cloudspec.aws.IAWSClientsProvider;
import cloudspec.aws.ec2.resource.EC2InstanceResource;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2InstanceLoader extends EC2ResourceLoader<EC2InstanceResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2InstanceLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2InstanceResource> getById(String instanceId) {
        return getInstances(Collections.singletonList(instanceId)).findFirst();
    }

    @Override
    public List<EC2InstanceResource> getAll() {
        return getInstances().collect(Collectors.toList());
    }

    private Stream<EC2InstanceResource> getInstances() {
        return getInstances(Collections.emptyList());
    }

    private Stream<EC2InstanceResource> getInstances(List<String> instanceIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getInstancesInRegion(region, instanceIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }

    }

    private Stream<EC2InstanceResource> getInstancesInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                             List<String> instanceIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeInstancesResponse response = instanceIds != null && !instanceIds.isEmpty() ?
                    client.describeInstances(builder -> builder.instanceIds(instanceIds.toArray(new String[0]))) :
                    client.describeInstances();

            return response.reservations()
                    .stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .map(instance -> EC2InstanceResource.fromSdk(region.regionName(), instance));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }
}
