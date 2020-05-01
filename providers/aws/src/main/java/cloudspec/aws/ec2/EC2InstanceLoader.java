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

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeRegionsResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.utils.IoUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EC2InstanceLoader implements EC2ResourceLoader<EC2InstanceResource> {
    private List<EC2InstanceResource> instances;

    public List<EC2InstanceResource> load() {
        if (instances != null) {
            return instances;
        }

        Ec2Client ec2Client = getEc2Client(Optional.empty());

        try {
            instances = getAllInstances(ec2Client);
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }

        return instances;
    }

    private Ec2Client getEc2Client(Optional<String> regionOpt) {
        return regionOpt.isPresent() ? Ec2Client.builder().region(Region.of(regionOpt.get())).build() : Ec2Client.create();
    }

    private List<EC2InstanceResource> getAllInstances(Ec2Client ec2Client) {
        DescribeRegionsResponse response = ec2Client.describeRegions();
        return response
                .regions()
                .stream()
                .flatMap(region -> getAllInstancesInRegion(region).stream())
                .collect(Collectors.toList());
    }


    private List<EC2InstanceResource> getAllInstancesInRegion(software.amazon.awssdk.services.ec2.model.Region region) {
        Ec2Client client = getEc2Client(Optional.of(region.regionName()));

        try {
            DescribeInstancesResponse response = client.describeInstances();
            return response.reservations()
                    .stream()
                    .flatMap(reservation -> reservation.instances().stream())
                    .map(instance -> toResource(region.regionName(), instance))
                    .collect(Collectors.toList());
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private EC2InstanceResource toResource(String regionName, Instance instance) {
        return new EC2InstanceResource("", regionName, "", instance.instanceId(), instance.instanceType().toString(), instance.vpcId());
    }
}
