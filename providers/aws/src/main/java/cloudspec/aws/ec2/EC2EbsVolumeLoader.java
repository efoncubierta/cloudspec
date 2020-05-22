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
import software.amazon.awssdk.services.ec2.model.DescribeVolumesResponse;
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2EbsVolumeLoader extends EC2ResourceLoader<EC2EbsVolumeResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2EbsVolumeLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2EbsVolumeResource> getById(String volumeId) {
        return getVolumes(Collections.singletonList(volumeId)).findFirst();
    }

    @Override
    public List<EC2EbsVolumeResource> getAll() {
        return getVolumes().collect(Collectors.toList());
    }

    private Stream<EC2EbsVolumeResource> getVolumes() {
        return getVolumes(Collections.emptyList());
    }

    private Stream<EC2EbsVolumeResource> getVolumes(List<String> volumeIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getAllInstancesInRegion(region, volumeIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }

    }

    private Stream<EC2EbsVolumeResource> getAllInstancesInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                                 List<String> volumeIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeVolumesResponse response = volumeIds != null && !volumeIds.isEmpty() ?
                    client.describeVolumes(builder -> builder.volumeIds(volumeIds.toArray(new String[0]))) :
                    client.describeVolumes();

            return response.volumes()
                    .stream()
                    .map(volume -> toResource(region.regionName(), volume));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private EC2EbsVolumeResource toResource(String regionName, Volume volume) {
        EC2EbsVolumeResource resource = new EC2EbsVolumeResource();

        // volume properties
        resource.id = volume.volumeId();
        resource.region = regionName;
        resource.type = volume.volumeTypeAsString();
        resource.availabilityZone = volume.availabilityZone();
        resource.size = volume.size();
        resource.iops = volume.iops();
        resource.encrypted = volume.encrypted();
        resource.multiAttachEnabled = volume.multiAttachEnabled();
        resource.state = volume.stateAsString();
        resource.tags = toTags(volume.tags());

        return resource;
    }
}
