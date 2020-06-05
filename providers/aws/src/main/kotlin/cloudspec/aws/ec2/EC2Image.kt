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
import cloudspec.aws.ec2.nested.EC2BlockDeviceMapping
import cloudspec.aws.ec2.nested.EC2ProductCode
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "image",
        description = "Amazon Machine Image"
)
data class EC2Image(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "architecture",
                description = "The architecture of the image",
                exampleValues = "i386 | x86_64 | arm64"
        )
        val architecture: String?,

        @PropertyDefinition(
                name = "creation_date",
                description = "The date and time the image was created"
        )
        val creationDate: Instant?,

        @IdDefinition
        @PropertyDefinition(
                name = "image_id",
                description = "The ID of the AMI"
        )
        val imageId: String,

        @PropertyDefinition(
                name = "image_location",
                description = "The location of the AMI"
        )
        val imageLocation: String?,

        @PropertyDefinition(
                name = "type",
                description = "The type of image",
                exampleValues = "machine | kernel | ramdisk"
        )
        val imageType: String?,

        @PropertyDefinition(
                name = "kernel_id",
                description = "The kernel associated with the image, if any. Only applicable for machine images"
        )
        val kernelId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The AWS account ID of the image owner"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "platform",
                description = "This value is set to 'windows' for Windows AMIs; otherwise, it is blank",
                exampleValues = "windows"
        )
        val platform: String?,

        @PropertyDefinition(
                name = "product_codes",
                description = "Any product codes associated with the AMI"
        )
        val productCodes: List<EC2ProductCode>?,

        @PropertyDefinition(
                name = "state",
                description = "State of the image"
        )
        val state: String?,

        @PropertyDefinition(
                name = "block_device_mappings",
                description = "Any block device mapping entries"
        )
        val blockDeviceMappings: List<EC2BlockDeviceMapping>?,

        @PropertyDefinition(
                name = "ena_support",
                description = "Flag indicating ENA support"
        )
        val enaSupport: Boolean?,

        @PropertyDefinition(
                name = "hypervisor",
                description = "The hypervisor type of the image"
        )
        val hypervisor: String?,

        @PropertyDefinition(
                name = "name",
                description = "The name of the AMI that was provided during image creation"
        )
        val name: String?,

        @PropertyDefinition(
                name = "root_device_name",
                description = "The device name of the root device volume"
        )
        val rootDeviceName: String?,

        @PropertyDefinition(
                name = "root_device_type",
                description = "The type of root device used by the AMI",
                exampleValues = "/dev/sda1"
        )
        val rootDeviceType: String?,

        @PropertyDefinition(
                name = "sriov_net_support",
                description = "Specifies whether enhanced networking with the Intel 82599 Virtual Function interface is enabled"
        )
        val sriovNetSupport: String?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the image"
        )
        val tags: List<KeyValue>?,

        @PropertyDefinition(
                name = "virtualization_type",
                description = "The type of virtualization of the AMI"
        )
        val virtualizationType: String?,

        @PropertyDefinition(
                name = "public_launch_permissions",
                description = "Indicates whether the image has public launch permissions"
        )
        val publicLaunchPermissions: Boolean?
) : EC2Resource(region)
