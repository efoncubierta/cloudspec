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
import software.amazon.awssdk.services.ec2.model.DescribeVpcAttributeResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2VpcLoader extends EC2ResourceLoader<EC2VpcResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2VpcLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2VpcResource> getById(String vpcId) {
        return getVpcs(Collections.singletonList(vpcId)).findFirst();
    }

    @Override
    public List<EC2VpcResource> getAll() {
        return getVpcs().collect(Collectors.toList());
    }

    private Stream<EC2VpcResource> getVpcs() {
        return getVpcs(Collections.emptyList());
    }

    private Stream<EC2VpcResource> getVpcs(List<String> vpcIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getVpcsInRegion(region, vpcIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }
    }

    private Stream<EC2VpcResource> getVpcsInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                   List<String> vpcIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeVpcsResponse response = vpcIds != null && !vpcIds.isEmpty() ?
                    client.describeVpcs(builder -> builder.vpcIds(vpcIds.toArray(new String[0]))) :
                    client.describeVpcs();

            return response.vpcs()
                    .stream()
                    .map(vpc -> toVpc(client, region.regionName(), vpc));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private EC2VpcResource toVpc(Ec2Client client, String regionName, Vpc vpc) {
        EC2VpcResource resource = new EC2VpcResource();

        DescribeVpcAttributeResponse describeVpcAttributeResponse =
                client.describeVpcAttribute(builder -> builder.vpcId(vpc.vpcId()));

        // vpc properties
        resource.region = regionName;
        resource.vpcId = vpc.vpcId();
        resource.cidrBlock = vpc.cidrBlock();
        resource.state = vpc.stateAsString();
        resource.isDefault = vpc.isDefault();
        resource.dnsHostnamesEnabled = describeVpcAttributeResponse.enableDnsHostnames().value();
        resource.dnsSupportEnabled = describeVpcAttributeResponse.enableDnsSupport().value();
        resource.tags = toTags(vpc.tags());

        return resource;
    }
}
