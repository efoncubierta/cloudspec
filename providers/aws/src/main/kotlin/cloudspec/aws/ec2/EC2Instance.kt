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
import cloudspec.aws.AWSProvider
import cloudspec.aws.ec2.nested.*
import cloudspec.aws.iam.IAMInstanceProfileResource
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.*
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2Instance.RESOURCE_NAME,
        description = EC2Instance.RESOURCE_DESCRIPTION
)
data class EC2Instance constructor(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:AssociationDefinition(
                name = ASSOC_IMAGE,
                description = ASSOC_IMAGE_D,
                targetClass = EC2Image::class
        )
        val imageId: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_INSTANCE_ID,
                description = PROP_INSTANCE_ID_D
        )
        val instanceId: String,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_TYPE,
                description = PROP_INSTANCE_TYPE_D
        )
        val instanceType: InstanceType?,

        @property:PropertyDefinition(
                name = PROP_KERNEL_ID,
                description = PROP_KERNEL_ID_D
        )
        val kernelId: String?,

        @property:PropertyDefinition(
                name = PROP_KEY_NAME,
                description = PROP_KEY_NAME_D
        )
        val keyName: String?,

        @property:PropertyDefinition(
                name = PROP_LAUNCH_TIME,
                description = PROP_LAUNCH_TIME_D
        )
        val launchTime: Instant?,

        @property:PropertyDefinition(
                name = PROP_MONITORING,
                description = PROP_MONITORING_D
        )
        val monitoring: EC2Monitoring?,

        @property:PropertyDefinition(
                name = PROP_PLACEMENT,
                description = PROP_PLACEMENT_D
        )
        val placement: EC2Placement?,

        @property:PropertyDefinition(
                name = PROP_PLATFORM,
                description = PROP_PLATFORM_D
        )
        val platform: PlatformValues?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_DNS_NAME,
                description = PROP_PRIVATE_DNS_NAME_D
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESS,
                description = PROP_PRIVATE_IP_ADDRESS_D
        )
        val privateIpAddress: String?,

        @property:PropertyDefinition(
                name = PROP_PRODUCT_CODES,
                description = PROP_PRODUCT_CODES_D
        )
        val productCodes: List<EC2ProductCode>?,

        @property:PropertyDefinition(
                name = PROP_PUBLIC_DNS_NAME,
                description = PROP_PUBLIC_DNS_NAME_D
        )
        val publicDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PUBLIC_IP_ADDRESS,
                description = PROP_PUBLIC_IP_ADDRESS_D
        )
        val publicIpAddress: String?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: InstanceStateName?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNET,
                description = ASSOC_SUBNET_D,
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_ARCHITECTURE,
                description = PROP_ARCHITECTURE_D
        )
        val architecture: ArchitectureValues?,

        @property:PropertyDefinition(
                name = PROP_BLOCK_DEVICE_MAPPINGS,
                description = PROP_BLOCK_DEVICE_MAPPINGS_D
        )
        val blockDeviceMappings: List<EC2InstanceBlockDeviceMapping>?,

        @property:PropertyDefinition(
                name = PROP_EBS_OPTIMIZED,
                description = PROP_EBS_OPTIMIZED_D
        )
        val ebsOptimized: Boolean?,

        @property:PropertyDefinition(
                name = PROP_ENA_SUPPORT,
                description = PROP_ENA_SUPPORT_D
        )
        val enaSupport: Boolean?,

        @property:PropertyDefinition(
                name = PROP_HYPERVISOR,
                description = PROP_HYPERVISOR_D
        )
        val hypervisor: HypervisorType?,

        @property:AssociationDefinition(
                name = ASSOC_IAM_INSTANCE_PROFILE,
                description = ASSOC_IAM_INSTANCE_PROFILE_D,
                targetClass = IAMInstanceProfileResource::class
        )
        val iamInstanceProfileId: String?,

        @property:PropertyDefinition(
                name = PROP_INSTANCE_LIFECYCLE,
                description = PROP_INSTANCE_LIFECYCLE_D
        )
        val instanceLifecycle: InstanceLifecycleType?,

        @property:AssociationDefinition(
                name = ASSOC_ELASTIC_GPUS,
                description = ASSOC_ELASTIC_GPUS_D,
                targetClass = EC2ElasticGpu::class
        )
        val elasticGpuIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_ELASTIC_INFERENCE_ACCELERATORS,
                description = PROP_ELASTIC_INFERENCE_ACCELERATORS_D
        )
        val elasticInferenceAcceleratorArns: List<String>?,

        @property:PropertyDefinition(
                name = PROP_NETWORK_INTERFACES,
                description = PROP_NETWORK_INTERFACES_D
        )
        val networkInterfaces: List<EC2InstanceNetworkInterface>?,

        @property:PropertyDefinition(
                name = PROP_OUTPOST_ARN,
                description = PROP_OUTPOST_ARN_D
        )
        val outpostArn: String?,

        @property:PropertyDefinition(
                name = PROP_ROOT_DEVICE_NAME,
                description = PROP_ROOT_DEVICE_NAME_D
        )
        val rootDeviceName: String?,

        @property:PropertyDefinition(
                name = PROP_ROOT_DEVICE_TYPE,
                description = PROP_ROOT_DEVICE_TYPE_D
        )
        val rootDeviceType: DeviceType?,

        @property:AssociationDefinition(
                name = ASSOC_SECURITY_GROUPS,
                description = ASSOC_SECURITY_GROUPS_D,
                targetClass = EC2SecurityGroup::class
        )
        val securityGroupIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_SOURCE_DEST_CHECK,
                description = PROP_SOURCE_DEST_CHECK_D
        )
        val sourceDestCheck: Boolean?,

        @property:PropertyDefinition(
                name = PROP_SRIOV_NET_SUPPORT,
                description = PROP_SRIOV_NET_SUPPORT_D
        )
        val sriovNetSupport: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = PROP_VIRTUALIZATION_TYPE,
                description = PROP_VIRTUALIZATION_TYPE_D
        )
        val virtualizationType: VirtualizationType?,

        @property:PropertyDefinition(
                name = PROP_CPU_OPTIONS,
                description = PROP_CPU_OPTIONS_D
        )
        val cpuOptions: EC2CpuOptions?,

        @property:AssociationDefinition(
                name = ASSOC_CAPACITY_RESERVATION,
                description = ASSOC_CAPACITY_RESERVATION_D,
                targetClass = EC2CapacityReservation::class
        )
        val capacityReservationId: String?,

        @property:PropertyDefinition(
                name = PROP_HIBERNATION_CONFIGURED,
                description = PROP_HIBERNATION_CONFIGURED_D
        )
        val hibernationConfigured: Boolean?,

        @property:PropertyDefinition(
                name = PROP_LICENSES,
                description = PROP_LICENSES_D
        )
        val licenseConfigurationArns: List<String>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "instance"
        const val RESOURCE_DESCRIPTION = "EC2 Instance"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val ASSOC_IMAGE = "image"
        const val ASSOC_IMAGE_D = "The AMI used to launch the instance"
        const val PROP_INSTANCE_ID = "instance_id"
        const val PROP_INSTANCE_ID_D = "The ID of the instance"
        const val PROP_INSTANCE_TYPE = "instance_type"
        const val PROP_INSTANCE_TYPE_D = "The instance type"
        const val PROP_KERNEL_ID = "kernel_id"
        const val PROP_KERNEL_ID_D = "The kernel associated with this instance, if applicable"
        const val PROP_KEY_NAME = "key_name"
        const val PROP_KEY_NAME_D = "The name of the key pair, if this instance was launched with an associated key pair"
        const val PROP_LAUNCH_TIME = "launch_time"
        const val PROP_LAUNCH_TIME_D = "The time the instance was launched"
        const val PROP_MONITORING = "monitoring"
        const val PROP_MONITORING_D = "The monitoring for the instance"
        const val PROP_PLACEMENT = "placement"
        const val PROP_PLACEMENT_D = "The location where the instance launched, if applicable"
        const val PROP_PLATFORM = "platform"
        const val PROP_PLATFORM_D = "The value is `Windows` for Windows instances; otherwise blank"
        const val PROP_PRIVATE_DNS_NAME = "private_dns_name"
        const val PROP_PRIVATE_DNS_NAME_D = "(IPv4 only) The private DNS hostname name assigned to the instance"
        const val PROP_PRIVATE_IP_ADDRESS = "private_ip_address"
        const val PROP_PRIVATE_IP_ADDRESS_D = "The private IPv4 address assigned to the instance"
        const val PROP_PRODUCT_CODES = "product_codes"
        const val PROP_PRODUCT_CODES_D = "The product codes attached to this instance, if applicable"
        const val PROP_PUBLIC_DNS_NAME = "public_dns_name"
        const val PROP_PUBLIC_DNS_NAME_D = "(IPv4 only) The public DNS name assigned to the instance"
        const val PROP_PUBLIC_IP_ADDRESS = "public_ip_address"
        const val PROP_PUBLIC_IP_ADDRESS_D = "The public IPv4 address assigned to the instance, if applicable"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The current state of the instance"
        const val ASSOC_SUBNET = "subnet"
        const val ASSOC_SUBNET_D = "[EC2-VPC] The subnet in which the instance is running"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "[EC2-VPC] The VPC in which the instance is running"
        const val PROP_ARCHITECTURE = "architecture"
        const val PROP_ARCHITECTURE_D = "The architecture of the image"
        const val PROP_BLOCK_DEVICE_MAPPINGS = "block_device_mappings"
        const val PROP_BLOCK_DEVICE_MAPPINGS_D = "Any block device mapping entries for the instance"
        const val PROP_EBS_OPTIMIZED = "ebs_optimized"
        const val PROP_EBS_OPTIMIZED_D = "Indicates whether the instance is optimized for Amazon EBS I/O"
        const val PROP_ENA_SUPPORT = "ena_support"
        const val PROP_ENA_SUPPORT_D = "Specifies whether enhanced networking with ENA is enabled"
        const val PROP_HYPERVISOR = "hypervisor"
        const val PROP_HYPERVISOR_D = "The hypervisor type of the instance"
        const val ASSOC_IAM_INSTANCE_PROFILE = "iam_instance_profile"
        const val ASSOC_IAM_INSTANCE_PROFILE_D = "The IAM instance profile associated with the instance, if applicable"
        const val PROP_INSTANCE_LIFECYCLE = "instance_lifecycle"
        const val PROP_INSTANCE_LIFECYCLE_D = "Indicates whether this is a Spot Instance or a Scheduled Instance"
        const val ASSOC_ELASTIC_GPUS = "elastic_gpus"
        const val ASSOC_ELASTIC_GPUS_D = "The Elastic GPU associated with the instance"
        const val PROP_ELASTIC_INFERENCE_ACCELERATORS = "elastic_inference_accelerators"
        const val PROP_ELASTIC_INFERENCE_ACCELERATORS_D = "The elastic inference accelerator associated with the instance"
        const val PROP_NETWORK_INTERFACES = "network_interfaces"
        const val PROP_NETWORK_INTERFACES_D = "[EC2-VPC] The network interfaces for the instance"
        const val PROP_OUTPOST_ARN = "outpost_arn"
        const val PROP_OUTPOST_ARN_D = "The Amazon Resource Name (ARN) of the Outpost"
        const val PROP_ROOT_DEVICE_NAME = "root_device_name"
        const val PROP_ROOT_DEVICE_NAME_D = "The device name of the root device volume"
        const val PROP_ROOT_DEVICE_TYPE = "root_device_type"
        const val PROP_ROOT_DEVICE_TYPE_D = "The root device type used by the AMI"
        const val ASSOC_SECURITY_GROUPS = "security_groups"
        const val ASSOC_SECURITY_GROUPS_D = "The security groups for the instance"
        const val PROP_SOURCE_DEST_CHECK = "source_dest_check"
        const val PROP_SOURCE_DEST_CHECK_D = "Specifies whether to enable an instance launched in a VPC to perform NAT"
        const val PROP_SRIOV_NET_SUPPORT = "sriov_net_support"
        const val PROP_SRIOV_NET_SUPPORT_D = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the instance"
        const val PROP_VIRTUALIZATION_TYPE = "virtualization_type"
        const val PROP_VIRTUALIZATION_TYPE_D = "The virtualization type of the instance"
        const val PROP_CPU_OPTIONS = "cpu_options"
        const val PROP_CPU_OPTIONS_D = "The CPU options for the instance"
        const val ASSOC_CAPACITY_RESERVATION = "capacity_reservation"
        const val ASSOC_CAPACITY_RESERVATION_D = "The Capacity Reservation"
        const val PROP_HIBERNATION_CONFIGURED = "hibernation_configured"
        const val PROP_HIBERNATION_CONFIGURED_D = "Indicates whether the instance is enabled for hibernation"
        const val PROP_LICENSES = "licenses"
        const val PROP_LICENSES_D = "The license configurations"
    }
}

