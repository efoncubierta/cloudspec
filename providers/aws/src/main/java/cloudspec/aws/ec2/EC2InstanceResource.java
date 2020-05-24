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

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;

import java.util.ArrayList;
import java.util.List;

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

    @IdDefinition
    @PropertyDefinition(
            name = "id",
            description = "The ID of the instance.",
            exampleValues = "i-00370a471026eb7f8"
    )
    public String id;

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1, eu-west-1"
    )
    public String region;

    @PropertyDefinition(
            name = "type",
            description = "The instance type",
            exampleValues = "t2.nano, m5.xlarge"
    )
    public String type;

    @PropertyDefinition(
            name = "key_name",
            description = "The name of the key pair, if this instance was launched with an associated key pair"
    )
    public String keyName;

    @PropertyDefinition(
            name = "architecture",
            description = "The architecture of the image",
            exampleValues = "i386, x86_64, arm64"
    )
    public String architecture;

    @PropertyDefinition(
            name = "kernel_id",
            description = "The kernel associated with this instance, if applicable"
    )
    public String kernelId;

    @PropertyDefinition(
            name = "ebs_optimized",
            description = "Indicates whether the instance is optimized for Amazon EBS I/O"
    )
    public Boolean ebsOptimized;

    @PropertyDefinition(
            name = "private_ip",
            description = "The private IPv4 address assigned to the instance",
            exampleValues = "172.31.40.126"
    )
    public String privateIp;

    @PropertyDefinition(
            name = "public_ip",
            description = "The public IPv4 address assigned to the instance",
            exampleValues = "34.241.109.11"
    )
    public String publicIp;

    @PropertyDefinition(
            name = "private_dns",
            description = "The private DNS hostname name assigned to the instance",
            exampleValues = "ip-172-31-40-126.eu-west-1.compute.internal"
    )
    public String privateDns;

    @PropertyDefinition(
            name = "public_dns",
            description = "The public DNS hostname name assigned to the instance",
            exampleValues = "ec2-34-241-109-11.eu-west-1.compute.amazonaws.com"
    )
    public String publicDns;

    @PropertyDefinition(
            name = "root_device",
            description = "The root device attached to the instance"
    )
    public RootDevice rootDevice = new RootDevice();

    @PropertyDefinition(
            name = "devices",
            description = "The devices attached to the instance"
    )
    public List<Device> devices = new ArrayList<>();

    @PropertyDefinition(
            name = "network_interfaces",
            description = "The network interfaces attached to the instance"
    )
    public List<NetworkInterface> networkInterfaces = new ArrayList<>();

    @PropertyDefinition(
            name = "hibernation_configured",
            description = "Indicates whether the instance is enabled for hibernation"
    )
    public Boolean hibernationConfigured;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the instance",
            exampleValues = "tags[\"my_key\"] = \"my_value\""
    )
    public List<KeyValue> tags;

    @AssociationDefinition(
            name = "ami",
            description = "The AMI used to launch the instance",
            targetClass = EC2AmiResource.class
    )
    public String imageId;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC in which the instance is running",
            targetClass = EC2VpcResource.class
    )
    public String vpcId;

    @AssociationDefinition(
            name = "subnet",
            description = "The subnet in which the instance is running",
            targetClass = EC2SubnetResource.class
    )
    public String subnetId;

    //    @AssociationDefinition(
//            name = "iam_instance_profile",
//            description = "IAM Instance Profile",
//            targetClass = IAMInstanceProfileResource.class
//    )
    public String iamInstanceProfileId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2InstanceResource that = (EC2InstanceResource) o;
        return region.equals(that.region) &&
                id.equals(that.id) &&
                type.equals(that.type) &&
                keyName.equals(that.keyName) &&
                architecture.equals(that.architecture) &&
                kernelId.equals(that.kernelId) &&
                ebsOptimized.equals(that.ebsOptimized) &&
                privateIp.equals(that.privateIp) &&
                publicIp.equals(that.publicIp) &&
                privateDns.equals(that.privateDns) &&
                publicDns.equals(that.publicDns) &&
                rootDevice.equals(that.rootDevice) &&
                devices.equals(that.devices) &&
                networkInterfaces.equals(that.networkInterfaces) &&
                hibernationConfigured.equals(that.hibernationConfigured) &&
                tags.equals(that.tags) &&
                imageId.equals(that.imageId) &&
                vpcId.equals(that.vpcId) &&
                subnetId.equals(that.subnetId) &&
                iamInstanceProfileId.equals(that.iamInstanceProfileId);
    }

    @Override
    public String toString() {
        return "EC2InstanceResource{" +
                "region='" + region + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", keyName='" + keyName + '\'' +
                ", architecture='" + architecture + '\'' +
                ", kernelId='" + kernelId + '\'' +
                ", ebsOptimized=" + ebsOptimized +
                ", privateIp='" + privateIp + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", privateDns='" + privateDns + '\'' +
                ", publicDns='" + publicDns + '\'' +
                ", rootDevice=" + rootDevice +
                ", devices=" + devices +
                ", networkInterfaces=" + networkInterfaces +
                ", hibernationConfigured=" + hibernationConfigured +
                ", tags=" + tags +
                ", imageId='" + imageId + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", subnetId='" + subnetId + '\'' +
                ", iamInstanceProfileId='" + iamInstanceProfileId + '\'' +
                '}';
    }

    public static class RootDevice {
        @PropertyDefinition(
                name = "name",
                description = "The device name of the root device volume"
        )
        public String name;

        @PropertyDefinition(
                name = "type",
                description = "The root device type used by the AMI"
        )
        public String type;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RootDevice that = (RootDevice) o;
            return name.equals(that.name) &&
                    type.equals(that.type);
        }

        @Override
        public String toString() {
            return "RootDevice{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    public static class Device {
        @PropertyDefinition(
                name = "name",
                description = "Device name"
        )
        public String name;

        @PropertyDefinition(
                name = "delete_on_termination",
                description = "Delete on termination"
        )
        public Boolean deleteOnTermination;

        @AssociationDefinition(
                name = "volume",
                description = "EBS Volume",
                targetClass = EC2EbsVolumeResource.class
        )
        public String volumeId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Device device = (Device) o;
            return name.equals(device.name) &&
                    deleteOnTermination.equals(device.deleteOnTermination) &&
                    volumeId.equals(device.volumeId);
        }

        @Override
        public String toString() {
            return "Device{" +
                    "name='" + name + '\'' +
                    ", deleteOnTermination=" + deleteOnTermination +
                    ", volumeId='" + volumeId + '\'' +
                    '}';
        }
    }

    public static class NetworkInterface {
        @PropertyDefinition(
                name = "private_ip",
                description = "Private IP address"
        )
        public String privateIp;

        @PropertyDefinition(
                name = "private_dns",
                description = "Private DNS address"
        )
        public String privateDns;

        @AssociationDefinition(
                name = "network_interface",
                description = "Network Interface",
                targetClass = EC2NetworkInterfaceResource.class
        )
        public String networkInterfaceId;

        @AssociationDefinition(
                name = "vpc",
                description = "VPC",
                targetClass = EC2VpcResource.class
        )
        public String vpcId;

        @AssociationDefinition(
                name = "subnet",
                description = "Subnet",
                targetClass = EC2SubnetResource.class
        )
        public String subnetId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NetworkInterface that = (NetworkInterface) o;
            return privateIp.equals(that.privateIp) &&
                    privateDns.equals(that.privateDns) &&
                    networkInterfaceId.equals(that.networkInterfaceId) &&
                    vpcId.equals(that.vpcId) &&
                    subnetId.equals(that.subnetId);
        }

        @Override
        public String toString() {
            return "NetworkInterface{" +
                    "privateIp='" + privateIp + '\'' +
                    ", privateDns='" + privateDns + '\'' +
                    ", networkInterfaceId='" + networkInterfaceId + '\'' +
                    ", vpcId='" + vpcId + '\'' +
                    ", subnetId='" + subnetId + '\'' +
                    '}';
        }
    }
}
