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
package datagen.services.ec2.model

import datagen.BaseGenerator
import datagen.CommonGenerator
import software.amazon.awssdk.services.ec2.model.Image
import software.amazon.awssdk.services.ec2.model.ImageState
import software.amazon.awssdk.services.ec2.model.ImageTypeValues
import software.amazon.awssdk.services.ec2.model.VirtualizationType

object ImageGenerator : BaseGenerator() {
    fun imageId(): String {
        return "ami-${faker.lorem().characters(30)}"
    }

    fun imageTypeValue(): ImageTypeValues {
        return valueFromArray(ImageTypeValues.values())
    }

    fun imageState(): ImageState {
        return valueFromArray(ImageState.values())
    }

    fun virtualizationType(): VirtualizationType {
        return valueFromArray(VirtualizationType.values())
    }

    fun images(n: Int? = null): List<Image> {
        return listGenerator(n) { image() }
    }

    fun image(): Image {
        return Image.builder()
                .architecture(InstanceGenerator.architectureValue())
                .creationDate(futureInstant().toString())
                .imageId(imageId())
                .imageLocation(faker.lorem().word()) // TODO use real value
                .imageType(imageTypeValue())
                .kernelId(faker.lorem().word())
                .ownerId(CommonGenerator.accountId())
                .platform(InstanceGenerator.platformValue())
                .productCodes(ProductCodeGenerator.productCodes())
                .state(imageState())
                .blockDeviceMappings(BlockDeviceMappingGenerator.randomBlockDeviceMappings())
                .enaSupport(faker.random().nextBoolean())
                .hypervisor(InstanceGenerator.hypervisorType())
                .name(faker.lorem().word())
                .rootDeviceName(DeviceGenerator.deviceName())
                .rootDeviceType(DeviceGenerator.deviceType())
                .sriovNetSupport(InstanceGenerator.sriovNetSupport())
                .tags(TagGenerator.tags())
                .virtualizationType(virtualizationType())
                .publicLaunchPermissions(faker.random().nextBoolean())
                .build()
    }
}
