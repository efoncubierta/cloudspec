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
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkInterfacesResponse;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2NetworkInterfaceLoader extends EC2ResourceLoader<EC2NetworkInterfaceResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2NetworkInterfaceLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2NetworkInterfaceResource> getById(String interfaceId) {
        return getInterfaces(Collections.singletonList(interfaceId)).findFirst();
    }

    @Override
    public List<EC2NetworkInterfaceResource> getAll() {
        return getInterfaces().collect(Collectors.toList());
    }

    private Stream<EC2NetworkInterfaceResource> getInterfaces() {
        return getInterfaces(Collections.emptyList());
    }

    private Stream<EC2NetworkInterfaceResource> getInterfaces(List<String> interfaceIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getAllInterfacesInRegion(region, interfaceIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }

    }

    private Stream<EC2NetworkInterfaceResource> getAllInterfacesInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                                         List<String> interfaceIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeNetworkInterfacesResponse response = interfaceIds != null && !interfaceIds.isEmpty() ?
                    client.describeNetworkInterfaces(builder -> builder.networkInterfaceIds(interfaceIds.toArray(new String[0]))) :
                    client.describeNetworkInterfaces();

            return response.networkInterfaces()
                    .stream()
                    .map(networkInterface -> toResource(region.regionName(), networkInterface));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private EC2NetworkInterfaceResource toResource(String regionName, NetworkInterface networkInterface) {
        EC2NetworkInterfaceResource resource = new EC2NetworkInterfaceResource();

        // interface properties
        resource.region = regionName;
        resource.id = networkInterface.networkInterfaceId();
        resource.availabilityZone = networkInterface.availabilityZone();
        resource.type = networkInterface.interfaceTypeAsString();
        resource.macAddress = networkInterface.macAddress();
        resource.privateDns = networkInterface.privateDnsName();
        resource.privateIp = networkInterface.privateIpAddress();
        resource.status = networkInterface.statusAsString();
        resource.tags = toTags(networkInterface.tagSet());

        // interface associations
        resource.vpcId = networkInterface.vpcId();
        resource.subnetId = networkInterface.subnetId();

        return resource;
    }
}
