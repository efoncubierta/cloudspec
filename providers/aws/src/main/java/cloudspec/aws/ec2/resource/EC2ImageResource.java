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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.EC2BlockDeviceMapping;
import cloudspec.aws.ec2.resource.nested.EC2ProductCode;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.Image;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2ImageResource.RESOURCE_NAME,
        description = "Amazon Machine Image"
)
public class EC2ImageResource extends EC2Resource {
    public static final String RESOURCE_NAME = "image";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "architecture",
            description = "The architecture of the image",
            exampleValues = "i386 | x86_64 | arm64"
    )
    private final String architecture;

    @PropertyDefinition(
            name = "creation_date",
            description = "The date and time the image was created"
    )
    private final Date creationDate;

    @IdDefinition
    @PropertyDefinition(
            name = "image_id",
            description = "The ID of the AMI"
    )
    private final String imageId;

    @PropertyDefinition(
            name = "image_location",
            description = "The location of the AMI"
    )
    private final String imageLocation;

    @PropertyDefinition(
            name = "type",
            description = "The type of image",
            exampleValues = "machine | kernel | ramdisk"
    )
    private final String imageType;

    @PropertyDefinition(
            name = "kernel_id",
            description = "The kernel associated with the image, if any. Only applicable for machine images"
    )
    private final String kernelId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The AWS account ID of the image owner"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "platform",
            description = "This value is set to 'windows' for Windows AMIs; otherwise, it is blank",
            exampleValues = "windows"
    )
    private final String platform;

    @PropertyDefinition(
            name = "product_codes",
            description = "Any product codes associated with the AMI"
    )
    private final List<EC2ProductCode> productCodes;

    @PropertyDefinition(
            name = "state",
            description = "State of the image"
    )
    private final String state;

    @PropertyDefinition(
            name = "block_device_mappings",
            description = "Any block device mapping entries"
    )
    private final List<EC2BlockDeviceMapping> blockDeviceMappings;

    @PropertyDefinition(
            name = "ena_support",
            description = "Flag indicating ENA support"
    )
    private final Boolean enaSupport;

    @PropertyDefinition(
            name = "hypervisor",
            description = "The hypervisor type of the image"
    )
    private final String hypervisor;

    @PropertyDefinition(
            name = "name",
            description = "The name of the AMI that was provided during image creation"
    )
    private final String name;

    @PropertyDefinition(
            name = "root_device_name",
            description = "The device name of the root device volume"
    )
    private final String rootDeviceName;

    @PropertyDefinition(
            name = "root_device_type",
            description = "The type of root device used by the AMI",
            exampleValues = "/dev/sda1"
    )
    private final String rootDeviceType;

    @PropertyDefinition(
            name = "sriov_net_support",
            description = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
    )
    private final String sriovNetSupport;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the image"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "virtualization_type",
            description = "The type of virtualization of the AMI"
    )
    private final String virtualizationType;

    @PropertyDefinition(
            name = "public_launch_permissions",
            description = "Indicates whether the image has public launch permissions"
    )
    private final Boolean publicLaunchPermissions;

    public EC2ImageResource(String region, String architecture, Date creationDate, String imageId,
                            String imageLocation, String imageType, String kernelId, String ownerId, String platform,
                            List<EC2ProductCode> productCodes, String state, List<EC2BlockDeviceMapping> blockDeviceMappings,
                            Boolean enaSupport, String hypervisor, String name, String rootDeviceName,
                            String rootDeviceType, String sriovNetSupport, List<KeyValue> tags,
                            String virtualizationType, Boolean publicLaunchPermissions) {
        this.region = region;
        this.architecture = architecture;
        this.creationDate = creationDate;
        this.imageId = imageId;
        this.imageLocation = imageLocation;
        this.imageType = imageType;
        this.kernelId = kernelId;
        this.ownerId = ownerId;
        this.platform = platform;
        this.productCodes = productCodes;
        this.state = state;
        this.blockDeviceMappings = blockDeviceMappings;
        this.enaSupport = enaSupport;
        this.hypervisor = hypervisor;
        this.name = name;
        this.rootDeviceName = rootDeviceName;
        this.rootDeviceType = rootDeviceType;
        this.sriovNetSupport = sriovNetSupport;
        this.tags = tags;
        this.virtualizationType = virtualizationType;
        this.publicLaunchPermissions = publicLaunchPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof Image))) {
            return false;
        }

        if (o instanceof Image) {
            return sdkEquals((Image) o);
        }

        EC2ImageResource that = (EC2ImageResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(architecture, that.architecture) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(imageId, that.imageId) &&
                Objects.equals(imageLocation, that.imageLocation) &&
                Objects.equals(imageType, that.imageType) &&
                Objects.equals(kernelId, that.kernelId) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(productCodes, that.productCodes) &&
                Objects.equals(state, that.state) &&
                Objects.equals(blockDeviceMappings, that.blockDeviceMappings) &&
                Objects.equals(enaSupport, that.enaSupport) &&
                Objects.equals(hypervisor, that.hypervisor) &&
                Objects.equals(name, that.name) &&
                Objects.equals(rootDeviceName, that.rootDeviceName) &&
                Objects.equals(rootDeviceType, that.rootDeviceType) &&
                Objects.equals(sriovNetSupport, that.sriovNetSupport) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(virtualizationType, that.virtualizationType) &&
                Objects.equals(publicLaunchPermissions, that.publicLaunchPermissions);
    }

    private boolean sdkEquals(Image that) {
        return Objects.equals(architecture, that.architectureAsString()) &&
                Objects.equals(creationDate, dateFromSdk(that.creationDate())) &&
                Objects.equals(imageId, that.imageId()) &&
                Objects.equals(imageLocation, that.imageLocation()) &&
                Objects.equals(imageType, that.imageTypeAsString()) &&
                Objects.equals(kernelId, that.kernelId()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(platform, that.platformAsString()) &&
                Objects.equals(productCodes, that.productCodes()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(blockDeviceMappings, that.blockDeviceMappings()) &&
                Objects.equals(enaSupport, that.enaSupport()) &&
                Objects.equals(hypervisor, that.hypervisorAsString()) &&
                Objects.equals(name, that.name()) &&
                Objects.equals(rootDeviceName, that.rootDeviceName()) &&
                Objects.equals(rootDeviceType, that.rootDeviceTypeAsString()) &&
                Objects.equals(sriovNetSupport, that.sriovNetSupport()) &&
                Objects.equals(tags, tagsFromSdk(that.tags())) &&
                Objects.equals(virtualizationType, that.virtualizationTypeAsString()) &&
                Objects.equals(publicLaunchPermissions, that.publicLaunchPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, architecture, creationDate, imageId, imageLocation, imageType, kernelId,
                ownerId, platform, productCodes, state, blockDeviceMappings, enaSupport, hypervisor, name,
                rootDeviceName, rootDeviceType, sriovNetSupport, tags, virtualizationType, publicLaunchPermissions);
    }

    public static EC2ImageResource fromSdk(String regionName, Image image) {
        return Optional.ofNullable(image)
                       .map(v -> new EC2ImageResource(
                               regionName,
                               v.architectureAsString(),
                               dateFromSdk(v.creationDate()),
                               v.imageId(),
                               v.imageLocation(),
                               v.imageTypeAsString(),
                               v.kernelId(),
                               v.ownerId(),
                               v.platformAsString(),
                               EC2ProductCode.fromSdk(v.productCodes()),
                               v.stateAsString(),
                               EC2BlockDeviceMapping.fromSdk(v.blockDeviceMappings()),
                               v.enaSupport(),
                               v.hypervisorAsString(),
                               v.name(),
                               v.rootDeviceName(),
                               v.rootDeviceTypeAsString(),
                               v.sriovNetSupport(),
                               tagsFromSdk(v.tags()),
                               v.virtualizationTypeAsString(),
                               v.publicLaunchPermissions()
                       ))
                       .orElse(null);
    }
}
