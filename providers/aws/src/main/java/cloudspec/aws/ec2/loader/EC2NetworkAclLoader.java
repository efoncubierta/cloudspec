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
import cloudspec.aws.ec2.resource.EC2NetworkAclResource;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeNetworkAclsResponse;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2NetworkAclLoader extends EC2ResourceLoader<EC2NetworkAclResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2NetworkAclLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2NetworkAclResource> getById(String networkAclId) {
        return getNetworkAcls(Collections.singletonList(networkAclId)).findFirst();
    }

    @Override
    public List<EC2NetworkAclResource> getAll() {
        return getNetworkAcls().collect(Collectors.toList());
    }

    private Stream<EC2NetworkAclResource> getNetworkAcls() {
        return getNetworkAcls(Collections.emptyList());
    }

    private Stream<EC2NetworkAclResource> getNetworkAcls(List<String> networkAclIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getNetworkAclsInRegion(region, networkAclIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }
    }

    private Stream<EC2NetworkAclResource> getNetworkAclsInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                                 List<String> networkAclIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeNetworkAclsResponse response = networkAclIds != null && !networkAclIds.isEmpty() ?
                    client.describeNetworkAcls(builder -> builder.networkAclIds(networkAclIds.toArray(new String[0]))) :
                    client.describeNetworkAcls();

            return response.networkAcls()
                    .stream()
                    .map(networkAcl -> EC2NetworkAclResource.fromSdk(region.regionName(), networkAcl));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }
}
