/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.*
import cloudspec.aws.iam.IAMInstanceProfileResource
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "instance",
        description = "EC2 Instance"
)
data class EC2Instance constructor(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:AssociationDefinition(
                name = "image",
                description = "The AMI used to launch the instance",
                targetClass = EC2Image::class
        )
        val imageId: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "instance_id",
                description = "The ID of the instance"
        )
        val instanceId: String,

        @property:PropertyDefinition(
                name = "instance_type",
                description = "The instance type"
        )
        val instanceType: String?,

        @property:PropertyDefinition(
                name = "kernel_id",
                description = "The kernel associated with this instance, if applicable"
        )
        val kernelId: String?,

        @property:PropertyDefinition(
                name = "key_name",
                description = "The name of the key pair, if this instance was launched with an associated key pair"
        )
        val keyName: String?,

        @property:PropertyDefinition(
                name = "launch_time",
                description = "The time the instance was launched"
        )
        val launchTime: Instant?,

        @property:PropertyDefinition(
                name = "monitoring",
                description = "The monitoring for the instance"
        )
        val monitoring: EC2Monitoring?,

        @property:PropertyDefinition(
                name = "placement",
                description = "The location where the instance launched, if applicable"
        )
        val placement: EC2Placement?,

        @property:PropertyDefinition(
                name = "platform",
                description = "The value is `Windows` for Windows instances; otherwise blank"
        )
        val platform: String?,

        @property:PropertyDefinition(
                name = "private_dns_name",
                description = "(IPv4 only) The private DNS hostname name assigned to the instance"
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = "private_ip_address",
                description = "The private IPv4 address assigned to the instance"
        )
        val privateIpAddress: String?,

        @property:PropertyDefinition(
                name = "product_codes",
                description = "The product codes attached to this instance, if applicable"
        )
        val productCodes: List<EC2ProductCode>?,

        @property:PropertyDefinition(
                name = "public_dns_name",
                description = "(IPv4 only) The public DNS name assigned to the instance"
        )
        val publicDnsName: String?,

        @property:PropertyDefinition(
                name = "public_ip_address",
                description = "The public IPv4 address assigned to the instance, if applicable"
        )
        val publicIpAddress: String?,

        @property:PropertyDefinition(
                name = "state",
                description = "The current state of the instance"
        )
        val state: String?,

        @property:AssociationDefinition(
                name = "subnet",
                description = "[EC2-VPC] The subnet in which the instance is running",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "[EC2-VPC] The VPC in which the instance is running",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "architecture",
                description = "The architecture of the image"
        )
        val architecture: String?,

        @property:PropertyDefinition(
                name = "block_device_mappings",
                description = "Any block device mapping entries for the instance"
        )
        val blockDeviceMappings: List<EC2InstanceBlockDeviceMapping>?,

        @property:PropertyDefinition(
                name = "ebs_optimized",
                description = "Indicates whether the instance is optimized for Amazon EBS I/O"
        )
        val ebsOptimized: Boolean?,

        @property:PropertyDefinition(
                name = "ena_support",
                description = "Specifies whether enhanced networking with ENA is enabled"
        )
        val enaSupport: Boolean?,

        @property:PropertyDefinition(
                name = "hypervisor",
                description = "The hypervisor type of the instance",
                exampleValues = "ovm | xen"
        )
        val hypervisor: String?,

        @property:AssociationDefinition(
                name = "iam_instance_profile",
                description = "The IAM instance profile associated with the instance, if applicable",
                targetClass = IAMInstanceProfileResource::class
        )
        val iamInstanceProfileId: String?,

        @property:PropertyDefinition(
                name = "instance_lifecycle",
                description = "Indicates whether this is a Spot Instance or a Scheduled Instance",
                exampleValues = "spot | scheduled"
        )
        val instanceLifecycle: String?,

        @property:AssociationDefinition(
                name = "elastic_gpus",
                description = "The Elastic GPU associated with the instance",
                targetClass = EC2ElasticGpu::class
        )
        val elasticGpuIds: List<String>?,

        @property:PropertyDefinition(
                name = "elastic_inference_accelerators",
                description = "The elastic inference accelerator associated with the instance"
        )
        val elasticInferenceAcceleratorArns: List<String>?,

        @property:PropertyDefinition(
                name = "network_interfaces",
                description = "[EC2-VPC] The network interfaces for the instance"
        )
        val networkInterfaces: List<EC2InstanceNetworkInterface>?,

        @property:PropertyDefinition(
                name = "outpost_arn",
                description = "The Amazon Resource Name (ARN) of the Outpost"
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = "root_device_name",
                description = "The device name of the root device volume"
        )
        val rootDeviceName: String?,

        @property:PropertyDefinition(
                name = "root_device_type",
                description = "The root device type used by the AMI"
        )
        val rootDeviceType: String?,

        @property:AssociationDefinition(
                name = "security_groups",
                description = "The security groups for the instance",
                targetClass = EC2SecurityGroup::class
        )
        val securityGroupIds: List<String>?,

        @property:PropertyDefinition(
                name = "source_dest_check",
                description = "Specifies whether to enable an instance launched in a VPC to perform NAT"
        )
        val sourceDestCheck: Boolean?,

        @property:PropertyDefinition(
                name = "sriov_net_support",
                description = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
        )
        val sriovNetSupport: String?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the instance"
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = "virtualization_type",
                description = "The virtualization type of the instance",
                exampleValues = "hvm | paravirtual"
        )
        val virtualizationType: String?,

        @property:PropertyDefinition(
                name = "cpu_options",
                description = "The CPU options for the instance"
        )
        val cpuOptions: EC2CpuOptions?,

        @property:AssociationDefinition(
                name = "capacity_reservation",
                description = "The Capacity Reservation",
                targetClass = EC2CapacityReservation::class
        )
        val capacityReservationId: String?,

        @property:PropertyDefinition(
                name = "hibernation_configured",
                description = "Indicates whether the instance is enabled for hibernation"
        )
        val hibernationConfigured: Boolean?,

        @property:PropertyDefinition(
                name = "licenses",
                description = "The license configurations"
        )
        val licenseConfigurationArns: List<String>?
) : EC2Resource(region)
