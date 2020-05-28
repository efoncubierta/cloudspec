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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.ec2.resource.nested.*;
import cloudspec.aws.iam.IAMInstanceProfileResource;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2InstanceResource.RESOURCE_NAME,
        description = "EC2 Instance"
)
public class EC2InstanceResource extends EC2Resource {
    public static final String RESOURCE_NAME = "instance";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @AssociationDefinition(
            name = "image",
            description = "The AMI used to launch the instance",
            targetClass = EC2AmiResource.class
    )
    private final String imageId;

    @IdDefinition
    @PropertyDefinition(
            name = "instance_id",
            description = "The ID of the instance"
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "instance_type",
            description = "The instance type"
    )
    private final String instanceType;

    @PropertyDefinition(
            name = "kernel_id",
            description = "The kernel associated with this instance, if applicable"
    )
    private final String kernelId;

    @PropertyDefinition(
            name = "key_name",
            description = "The name of the key pair, if this instance was launched with an associated key pair"
    )
    private final String keyName;

    @PropertyDefinition(
            name = "launch_time",
            description = "The time the instance was launched"
    )
    private final Date launchTime;

    @PropertyDefinition(
            name = "monitoring",
            description = "The monitoring for the instance"
    )
    private final EC2Monitoring monitoring;

    @PropertyDefinition(
            name = "placement",
            description = "The location where the instance launched, if applicable"
    )
    private final EC2Placement placement;

    @PropertyDefinition(
            name = "platform",
            description = "The value is `Windows` for Windows instances; otherwise blank"
    )
    private final String platform;

    @PropertyDefinition(
            name = "private_dns_name",
            description = "(IPv4 only) The private DNS hostname name assigned to the instance"
    )
    private final String privateDnsName;

    @PropertyDefinition(
            name = "private_ip_address",
            description = "The private IPv4 address assigned to the instance"
    )
    private final String privateIpAddress;

    @PropertyDefinition(
            name = "product_codes",
            description = "The product codes attached to this instance, if applicable"
    )
    private final List<EC2ProductCode> productCodes;

    @PropertyDefinition(
            name = "public_dns_name",
            description = "(IPv4 only) The public DNS name assigned to the instance"
    )
    private final String publicDnsName;

    @PropertyDefinition(
            name = "public_ip_address",
            description = "The public IPv4 address assigned to the instance, if applicable"
    )
    private final String publicIpAddress;

//    @AssociationDefinition(
//            name = "ram_disk",
//            description = "The RAM disk associated with this instance, if applicable",
//    )
//    private final String ramdiskId;

    @PropertyDefinition(
            name = "state",
            description = "The current state of the instance"
    )
    private final String state;


    @AssociationDefinition(
            name = "subnet",
            description = "[EC2-VPC] The subnet in which the instance is running",
            targetClass = EC2SubnetResource.class
    )
    private final String subnetId;

    @AssociationDefinition(
            name = "vpc",
            description = "[EC2-VPC] The VPC in which the instance is running",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "architecture",
            description = "The architecture of the image"
    )
    private final String architecture;

    @PropertyDefinition(
            name = "block_device_mappings",
            description = "Any block device mapping entries for the instance"
    )
    private final List<EC2InstanceBlockDeviceMapping> blockDeviceMappings;

    @PropertyDefinition(
            name = "ebs_optimized",
            description = "Indicates whether the instance is optimized for Amazon EBS I/O"
    )
    private final Boolean ebsOptimized;

    @PropertyDefinition(
            name = "ena_support",
            description = "Specifies whether enhanced networking with ENA is enabled"
    )
    private final Boolean enaSupport;

    @PropertyDefinition(
            name = "hypervisor",
            description = "The hypervisor type of the instance",
            exampleValues = "ovm | xen"
    )
    private final String hypervisor;

    @AssociationDefinition(
            name = "iam_instance_profile",
            description = "The IAM instance profile associated with the instance, if applicable",
            targetClass = IAMInstanceProfileResource.class
    )
    private final String iamInstanceProfileId;

    @PropertyDefinition(
            name = "instance_lifecycle",
            description = "Indicates whether this is a Spot Instance or a Scheduled Instance",
            exampleValues = "spot | scheduled"
    )
    private final String instanceLifecycle;

    @AssociationDefinition(
            name = "elastic_gpus",
            description = "The Elastic GPU associated with the instance",
            targetClass = EC2ElasticGpuResource.class
    )
    private final List<String> elasticGpuIds;

    @PropertyDefinition(
            name = "elastic_inference_accelerators",
            description = "The elastic inference accelerator associated with the instance"
    )
    private final List<String> elasticInferenceAcceleratorArns;

    @PropertyDefinition(
            name = "network_interfaces",
            description = "[EC2-VPC] The network interfaces for the instance"
    )
    private final List<EC2InstanceNetworkInterface> networkInterfaces;

    @PropertyDefinition(
            name = "outpost_arn",
            description = "The Amazon Resource Name (ARN) of the Outpost"
    )
    private final String outpostArn;

    @PropertyDefinition(
            name = "root_device_name",
            description = "The device name of the root device volume"
    )
    private final String rootDeviceName;

    @PropertyDefinition(
            name = "root_device_type",
            description = "The root device type used by the AMI"
    )
    private final String rootDeviceType;

    @AssociationDefinition(
            name = "security_groups",
            description = "The security groups for the instance",
            targetClass = EC2SecurityGroupResource.class
    )
    private final List<String> securityGroupIds;

    @PropertyDefinition(
            name = "source_dest_check",
            description = "Specifies whether to enable an instance launched in a VPC to perform NAT"
    )
    private final Boolean sourceDestCheck;

    @PropertyDefinition(
            name = "sriov_net_support",
            description = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
    )
    private final String sriovNetSupport;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the instance"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "virtualization_type",
            description = "The virtualization type of the instance",
            exampleValues = "hvm | paravirtual"
    )
    private final String virtualizationType;

    @PropertyDefinition(
            name = "cpu_options",
            description = "The CPU options for the instance"
    )
    private final EC2CpuOptions cpuOptions;

    @AssociationDefinition(
            name = "capacity_reservation",
            description = "The Capacity Reservation",
            targetClass = EC2CapacityReservationResource.class
    )
    private final String capacityReservationId;

    @PropertyDefinition(
            name = "hibernation_configured",
            description = "Indicates whether the instance is enabled for hibernation"
    )
    private final Boolean hibernationConfigured;

    @PropertyDefinition(
            name = "licenses",
            description = "The license configurations"
    )
    private final List<String> licenseConfigurationArns;

    public EC2InstanceResource(String ownerId, String region, String imageId, String instanceId, String instanceType,
                               String kernelId, String keyName, Date launchTime, EC2Monitoring monitoring,
                               EC2Placement placement, String platform, String privateDnsName, String privateIpAddress,
                               List<EC2ProductCode> productCodes, String publicDnsName, String publicIpAddress,
                               String state, String subnetId, String vpcId, String architecture,
                               List<EC2InstanceBlockDeviceMapping> blockDeviceMappings, Boolean ebsOptimized,
                               Boolean enaSupport, String hypervisor, String iamInstanceProfileId,
                               String instanceLifecycle, List<String> elasticGpuIds, List<String> elasticInferenceAcceleratorArns,
                               List<EC2InstanceNetworkInterface> networkInterfaces, String outpostArn, String rootDeviceName,
                               String rootDeviceType, List<String> securityGroupIds, Boolean sourceDestCheck, String sriovNetSupport,
                               List<KeyValue> tags, String virtualizationType, EC2CpuOptions cpuOptions, String capacityReservationId,
                               Boolean hibernationConfigured, List<String> licenseConfigurationArns) {
        super(ownerId, region);
        this.imageId = imageId;
        this.instanceId = instanceId;
        this.instanceType = instanceType;
        this.kernelId = kernelId;
        this.keyName = keyName;
        this.launchTime = launchTime;
        this.monitoring = monitoring;
        this.placement = placement;
        this.platform = platform;
        this.privateDnsName = privateDnsName;
        this.privateIpAddress = privateIpAddress;
        this.productCodes = productCodes;
        this.publicDnsName = publicDnsName;
        this.publicIpAddress = publicIpAddress;
        this.state = state;
        this.subnetId = subnetId;
        this.vpcId = vpcId;
        this.architecture = architecture;
        this.blockDeviceMappings = blockDeviceMappings;
        this.ebsOptimized = ebsOptimized;
        this.enaSupport = enaSupport;
        this.hypervisor = hypervisor;
        this.iamInstanceProfileId = iamInstanceProfileId;
        this.instanceLifecycle = instanceLifecycle;
        this.elasticGpuIds = elasticGpuIds;
        this.elasticInferenceAcceleratorArns = elasticInferenceAcceleratorArns;
        this.networkInterfaces = networkInterfaces;
        this.outpostArn = outpostArn;
        this.rootDeviceName = rootDeviceName;
        this.rootDeviceType = rootDeviceType;
        this.securityGroupIds = securityGroupIds;
        this.sourceDestCheck = sourceDestCheck;
        this.sriovNetSupport = sriovNetSupport;
        this.tags = tags;
        this.virtualizationType = virtualizationType;
        this.cpuOptions = cpuOptions;
        this.capacityReservationId = capacityReservationId;
        this.hibernationConfigured = hibernationConfigured;
        this.licenseConfigurationArns = licenseConfigurationArns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2InstanceResource that = (EC2InstanceResource) o;
        return Objects.equals(imageId, that.imageId) &&
                Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(instanceType, that.instanceType) &&
                Objects.equals(kernelId, that.kernelId) &&
                Objects.equals(keyName, that.keyName) &&
                Objects.equals(launchTime, that.launchTime) &&
                Objects.equals(monitoring, that.monitoring) &&
                Objects.equals(placement, that.placement) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(privateDnsName, that.privateDnsName) &&
                Objects.equals(privateIpAddress, that.privateIpAddress) &&
                Objects.equals(productCodes, that.productCodes) &&
                Objects.equals(publicDnsName, that.publicDnsName) &&
                Objects.equals(publicIpAddress, that.publicIpAddress) &&
                Objects.equals(state, that.state) &&
                Objects.equals(subnetId, that.subnetId) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(architecture, that.architecture) &&
                Objects.equals(blockDeviceMappings, that.blockDeviceMappings) &&
                Objects.equals(ebsOptimized, that.ebsOptimized) &&
                Objects.equals(enaSupport, that.enaSupport) &&
                Objects.equals(hypervisor, that.hypervisor) &&
                Objects.equals(iamInstanceProfileId, that.iamInstanceProfileId) &&
                Objects.equals(instanceLifecycle, that.instanceLifecycle) &&
                Objects.equals(elasticGpuIds, that.elasticGpuIds) &&
                Objects.equals(elasticInferenceAcceleratorArns, that.elasticInferenceAcceleratorArns) &&
                Objects.equals(networkInterfaces, that.networkInterfaces) &&
                Objects.equals(outpostArn, that.outpostArn) &&
                Objects.equals(rootDeviceName, that.rootDeviceName) &&
                Objects.equals(rootDeviceType, that.rootDeviceType) &&
                Objects.equals(securityGroupIds, that.securityGroupIds) &&
                Objects.equals(sourceDestCheck, that.sourceDestCheck) &&
                Objects.equals(sriovNetSupport, that.sriovNetSupport) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(virtualizationType, that.virtualizationType) &&
                Objects.equals(cpuOptions, that.cpuOptions) &&
                Objects.equals(capacityReservationId, that.capacityReservationId) &&
                Objects.equals(hibernationConfigured, that.hibernationConfigured) &&
                Objects.equals(licenseConfigurationArns, that.licenseConfigurationArns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId, instanceId, instanceType, kernelId, keyName, launchTime, monitoring,
                placement, platform, privateDnsName, privateIpAddress, productCodes, publicDnsName,
                publicIpAddress, state, subnetId, vpcId, architecture, blockDeviceMappings, ebsOptimized,
                enaSupport, hypervisor, iamInstanceProfileId, instanceLifecycle, elasticGpuIds,
                elasticInferenceAcceleratorArns, networkInterfaces, outpostArn, rootDeviceName, rootDeviceType,
                securityGroupIds, sourceDestCheck, sriovNetSupport, tags, virtualizationType, cpuOptions,
                capacityReservationId, hibernationConfigured, licenseConfigurationArns);
    }

    public static EC2InstanceResource fromSdk(String regionName, Instance instance) {
        if (Objects.isNull(instance)) {
            return null;
        }

        return new EC2InstanceResource(
                "",
                regionName,
                instance.imageId(),
                instance.instanceId(),
                instance.instanceTypeAsString(),
                instance.kernelId(),
                instance.keyName(),
                dateFromSdk(instance.launchTime()),
                EC2Monitoring.fromSdk(instance.monitoring()),
                EC2Placement.fromSdk(instance.placement()),
                instance.platformAsString(),
                instance.privateDnsName(),
                instance.privateIpAddress(),
                EC2ProductCode.fromSdk(instance.productCodes()),
                instance.publicDnsName(),
                instance.publicIpAddress(),
                instance.state().nameAsString(),
                instance.subnetId(),
                instance.vpcId(),
                instance.architectureAsString(),
                EC2InstanceBlockDeviceMapping.fromSdk(instance.blockDeviceMappings()),
                instance.ebsOptimized(),
                instance.enaSupport(),
                instance.hypervisorAsString(),
                iamInstanceProfileIdFromSdk(instance.iamInstanceProfile()),
                instance.instanceLifecycleAsString(),
                elasticGpuIdsFromSdk(instance.elasticGpuAssociations()),
                elasticInferenceAcceleratorArnsFromSdk(instance.elasticInferenceAcceleratorAssociations()),
                EC2InstanceNetworkInterface.fromSdk(instance.networkInterfaces()),
                instance.outpostArn(),
                instance.rootDeviceName(),
                instance.rootDeviceTypeAsString(),
                EC2InstanceNetworkInterface.securityGroupIdsFromSdk(instance.securityGroups()),
                instance.sourceDestCheck(),
                instance.sriovNetSupport(),
                tagsFromSdk(instance.tags()),
                instance.virtualizationTypeAsString(),
                EC2CpuOptions.fromSdk(instance.cpuOptions()),
                instance.capacityReservationId(),
                hibernationConfiguredFromSdk(instance.hibernationOptions()),
                licenseArnsFromSdk(instance.licenses())
        );
    }

    public static String iamInstanceProfileIdFromSdk(IamInstanceProfile iamInstanceProfile) {
        if (Objects.isNull(iamInstanceProfile)) {
            return null;
        }

        return iamInstanceProfile.id();
    }

    public static List<String> elasticGpuIdsFromSdk(List<ElasticGpuAssociation> elasticGpuAssociations) {
        if (Objects.isNull(elasticGpuAssociations)) {
            return Collections.emptyList();
        }

        return elasticGpuAssociations
                .stream()
                .map(ElasticGpuAssociation::elasticGpuId)
                .collect(Collectors.toList());
    }

    public static List<String> elasticInferenceAcceleratorArnsFromSdk(List<ElasticInferenceAcceleratorAssociation> elasticInferenceAcceleratorAssociations) {
        if (Objects.isNull(elasticInferenceAcceleratorAssociations)) {
            return Collections.emptyList();
        }

        return elasticInferenceAcceleratorAssociations
                .stream()
                .map(ElasticInferenceAcceleratorAssociation::elasticInferenceAcceleratorArn)
                .collect(Collectors.toList());
    }

    public static Boolean hibernationConfiguredFromSdk(HibernationOptions hibernationOptions) {
        if (Objects.isNull(hibernationOptions)) {
            return null;
        }

        return hibernationOptions.configured();
    }

    public static List<String> licenseArnsFromSdk(List<LicenseConfiguration> licenseConfigurations) {
        if (Objects.isNull(licenseConfigurations)) {
            return Collections.emptyList();
        }

        return licenseConfigurations
                .stream()
                .map(LicenseConfiguration::licenseConfigurationArn)
                .collect(Collectors.toList());
    }
}
