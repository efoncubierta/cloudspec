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
