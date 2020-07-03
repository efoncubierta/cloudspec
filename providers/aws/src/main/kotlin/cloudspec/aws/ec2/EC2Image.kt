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

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.aws.ec2.nested.EC2BlockDeviceMapping
import cloudspec.aws.ec2.nested.EC2ProductCode
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.ec2.model.*
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2Image.RESOURCE_NAME,
        description = EC2Image.RESOURCE_DESCRIPTION
)
data class EC2Image(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ARCHITECTURE,
                description = PROP_ARCHITECTURE_D
        )
        val architecture: ArchitectureValues?,

        @property:PropertyDefinition(
                name = PROP_CREATION_DATE,
                description = PROP_CREATION_DATE_D
        )
        val creationDate: Instant?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_IMAGE_ID,
                description = PROP_IMAGE_ID_D
        )
        val imageId: String,

        @property:PropertyDefinition(
                name = PROP_IMAGE_LOCATION,
                description = PROP_IMAGE_LOCATION_D
        )
        val imageLocation: String?,

        @property:PropertyDefinition(
                name = PROP_TYPE,
                description = PROP_TYPE_D
        )
        val imageType: ImageTypeValues?,

        @property:PropertyDefinition(
                name = PROP_KERNEL_ID,
                description = PROP_KERNEL_ID_D
        )
        val kernelId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_PLATFORM,
                description = PROP_PLATFORM_D
        )
        val platform: PlatformValues?,

        @property:PropertyDefinition(
                name = PROP_PRODUCT_CODES,
                description = PROP_PRODUCT_CODES_D
        )
        val productCodes: List<EC2ProductCode>?,

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: ImageState?,

        @property:PropertyDefinition(
                name = PROP_BLOCK_DEVICE_MAPPINGS,
                description = PROP_BLOCK_DEVICE_MAPPINGS_D
        )
        val blockDeviceMappings: List<EC2BlockDeviceMapping>?,

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

        @property:PropertyDefinition(
                name = PROP_NAME,
                description = PROP_NAME_D
        )
        val name: String?,

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
                name = PROP_PUBLIC_LAUNCH_PERMISSIONS,
                description = PROP_PUBLIC_LAUNCH_PERMISSIONS_D
        )
        val publicLaunchPermissions: Boolean?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "image"
        const val RESOURCE_DESCRIPTION = "Amazon Machine Image"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ARCHITECTURE = "architecture"
        const val PROP_ARCHITECTURE_D = "The architecture of the image"
        const val PROP_CREATION_DATE = "creation_date"
        const val PROP_CREATION_DATE_D = "The date and time the image was created"
        const val PROP_IMAGE_ID = "image_id"
        const val PROP_IMAGE_ID_D = "The ID of the AMI"
        const val PROP_IMAGE_LOCATION = "image_location"
        const val PROP_IMAGE_LOCATION_D = "The location of the AMI"
        const val PROP_TYPE = "type"
        const val PROP_TYPE_D = "The type of image"
        const val PROP_KERNEL_ID = "kernel_id"
        const val PROP_KERNEL_ID_D = "The kernel associated with the image, if any. Only applicable for machine images"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The AWS account ID of the image owner"
        const val PROP_PLATFORM = "platform"
        const val PROP_PLATFORM_D = "This value is set to 'windows' for Windows AMIs; otherwise, it is blank"
        const val PROP_PRODUCT_CODES = "product_codes"
        const val PROP_PRODUCT_CODES_D = "Any product codes associated with the AMI"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "State of the image"
        const val PROP_BLOCK_DEVICE_MAPPINGS = "block_device_mappings"
        const val PROP_BLOCK_DEVICE_MAPPINGS_D = "Any block device mapping entries"
        const val PROP_ENA_SUPPORT = "ena_support"
        const val PROP_ENA_SUPPORT_D = "Flag indicating ENA support"
        const val PROP_HYPERVISOR = "hypervisor"
        const val PROP_HYPERVISOR_D = "The hypervisor type of the image"
        const val PROP_NAME = "name"
        const val PROP_NAME_D = "The name of the AMI that was provided during image creation"
        const val PROP_ROOT_DEVICE_NAME = "root_device_name"
        const val PROP_ROOT_DEVICE_NAME_D = "The device name of the root device volume"
        const val PROP_ROOT_DEVICE_TYPE = "root_device_type"
        const val PROP_ROOT_DEVICE_TYPE_D = "The type of root device used by the AMI"
        const val PROP_SRIOV_NET_SUPPORT = "sriov_net_support"
        const val PROP_SRIOV_NET_SUPPORT_D = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the image"
        const val PROP_VIRTUALIZATION_TYPE = "virtualization_type"
        const val PROP_VIRTUALIZATION_TYPE_D = "The type of virtualization of the AMI"
        const val PROP_PUBLIC_LAUNCH_PERMISSIONS = "public_launch_permissions"
        const val PROP_PUBLIC_LAUNCH_PERMISSIONS_D = "Indicates whether the image has public launch permissions"
    }
}

