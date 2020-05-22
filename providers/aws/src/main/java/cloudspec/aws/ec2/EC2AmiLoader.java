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
import software.amazon.awssdk.services.ec2.model.DescribeImagesResponse;
import software.amazon.awssdk.services.ec2.model.Image;
import software.amazon.awssdk.utils.IoUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2AmiLoader extends EC2ResourceLoader<EC2AmiResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2AmiLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2AmiResource> getById(String imageId) {
        return getImages(Collections.singletonList(imageId)).findFirst();
    }

    @Override
    public List<EC2AmiResource> getAll() {
        return getImages().collect(Collectors.toList());
    }

    private Stream<EC2AmiResource> getImages() {
        return getImages(Collections.emptyList());
    }

    private Stream<EC2AmiResource> getImages(List<String> imageIds) {
        Ec2Client ec2Client = clientsProvider.getEc2Client();

        try {
            return ec2Client.describeRegions()
                    .regions()
                    .stream()
                    .flatMap(region -> getAllImagesInRegion(region, imageIds));
        } finally {
            IoUtils.closeQuietly(ec2Client, null);
        }

    }

    private Stream<EC2AmiResource> getAllImagesInRegion(software.amazon.awssdk.services.ec2.model.Region region,
                                                        List<String> imageIds) {
        Ec2Client client = clientsProvider.getEc2ClientForRegion(region.regionName());

        try {
            DescribeImagesResponse response = imageIds != null && !imageIds.isEmpty() ?
                    client.describeImages(builder -> builder.imageIds(imageIds.toArray(new String[0]))) :
                    client.describeImages();

            return response.images()
                    .stream()
                    .map(image -> toResource(region.regionName(), image));
        } finally {
            IoUtils.closeQuietly(client, null);
        }
    }

    private EC2AmiResource toResource(String regionName, Image image) {
        EC2AmiResource resource = new EC2AmiResource();

        // properties
        resource.id = image.imageId();
        resource.region = regionName;
        resource.type = image.imageTypeAsString();
        resource.name = image.name();
        resource.location = image.imageLocation();
        resource.architecture = image.architectureAsString();
        resource.kernelId = image.kernelId();
        resource.platform = image.platformAsString();
        resource.enaSupport = image.enaSupport();
        resource.state = image.stateAsString();
        resource.tags = toTags(image.tags());

        // product codes
        resource.productCodes = image.productCodes()
                .stream()
                .map(productCode -> {
                    EC2AmiResource.ProductCode pc = new EC2AmiResource.ProductCode();
                    pc.id = productCode.productCodeId();
                    pc.type = productCode.productCodeTypeAsString();
                    return pc;
                })
                .collect(Collectors.toList());

        // devices
        resource.rootDevice.name = image.rootDeviceName();
        resource.rootDevice.type = image.rootDeviceTypeAsString();
        resource.devices = image.blockDeviceMappings()
                .stream()
                .map(blockDeviceMapping -> {
                    EC2AmiResource.Device device = new EC2AmiResource.Device();
                    device.name = blockDeviceMapping.deviceName();
                    device.virtualName = blockDeviceMapping.virtualName();
                    device.deleteOnTermination = blockDeviceMapping.ebs().deleteOnTermination();
                    device.volumeType = blockDeviceMapping.ebs().volumeTypeAsString();
                    device.volumeSize = blockDeviceMapping.ebs().volumeSize();
                    device.encrypted = blockDeviceMapping.ebs().encrypted();
                    return device;
                })
                .collect(Collectors.toList());
        return resource;
    }
}
