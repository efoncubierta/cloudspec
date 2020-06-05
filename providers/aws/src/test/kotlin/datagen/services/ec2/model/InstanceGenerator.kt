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
import software.amazon.awssdk.services.ec2.model.*

object InstanceGenerator : BaseGenerator() {
    fun instanceId(): String {
        return "i-${faker.lorem().characters(30)}"
    }

    fun instanceType(): InstanceType {
        return valueFromArray(InstanceType.values())
    }

    fun architectureValue(): ArchitectureValues {
        return valueFromArray(ArchitectureValues.values())
    }

    fun platformValue(): PlatformValues {
        return valueFromArray(PlatformValues.values())
    }

    fun instanceStateName(): InstanceStateName {
        return valueFromArray(InstanceStateName.values())
    }

    fun instanceState(): InstanceState {
        return InstanceState.builder()
                .code(faker.number().randomDigit())
                .name(instanceStateName())
                .build()
    }

    fun hypervisorType(): HypervisorType {
        return valueFromArray(HypervisorType.values())
    }

    fun instanceLifecycleType(): InstanceLifecycleType {
        return valueFromArray(InstanceLifecycleType.values())
    }

    fun sriovNetSupport(): String {
        // TODO realistic value
        return faker.lorem().word()
    }

    fun instances(n: Int? = null): List<Instance> {
        return listGenerator(n) { instance() }
    }

    fun instance(): Instance {
        return instanceBuilder().build()
    }

    fun instanceBuilders(n: Int?): List<Instance.Builder> {
        return listGenerator(n) { instanceBuilder() }
    }

    fun instanceBuilder(): Instance.Builder {
        return Instance
                .builder()
                .amiLaunchIndex(faker.random().nextInt(0, 10))
                .imageId(ImageGenerator.imageId())
                .instanceId(instanceId())
                .instanceType(instanceType())
                .kernelId(faker.lorem().word())
                .keyName(faker.lorem().word())
                .launchTime(pastInstant())
                .monitoring(MonitoringGenerator.monitoring())
                .placement(PlacementGenerator.placement())
                .platform(platformValue())
                .privateDnsName(faker.internet().domainName())
                .privateIpAddress(faker.internet().privateIpV4Address())
                .productCodes(ProductCodeGenerator.productCodes(5))
                .publicDnsName(faker.internet().domainName())
                .publicIpAddress(faker.internet().publicIpV4Address())
                .ramdiskId(faker.lorem().word()) // TODO realistic value
                .state(instanceState())
                .stateTransitionReason(faker.lorem().sentence()) // TODO realistic value
                .subnetId(SubnetGenerator.subnetId())
                .vpcId(VpcGenerator.vpcId())
                .architecture(architectureValue())
                .blockDeviceMappings(InstanceBlockDeviceMappingGenerator.instanceBlockDeviceMappings(5))
                .clientToken(faker.lorem().word()) // TODO realistic value
                .ebsOptimized(faker.random().nextBoolean())
                .enaSupport(faker.random().nextBoolean())
                .hypervisor(hypervisorType())
                .iamInstanceProfile(IamInstanceProfileGenerator.iamInstanceProfile())
                .instanceLifecycle(instanceLifecycleType())
                .elasticGpuAssociations(ElasticGpuAssociationGenerator.elasticGpuAssociations(2))
                .elasticInferenceAcceleratorAssociations(ElasticInferenceAcceleratorAssociationGenerator.elasticInferenceAcceleratorAssociations(2))
                .networkInterfaces(InstanceNetworkInterfaceGenerator.instanceNetworkInterfaces(2))
                .outpostArn(OutpostGenerator.outpostArn().toString())
                .rootDeviceName(DeviceGenerator.deviceName())
                .rootDeviceType(DeviceGenerator.deviceType())
                .securityGroups(GroupIdentifierGenerator.groupIdentifiers())
                .sourceDestCheck(faker.random().nextBoolean())
                .spotInstanceRequestId(faker.lorem().word()) // TODO realistic value
                .sriovNetSupport(sriovNetSupport())
                .stateReason(StateReasonGenerator.stateReason())
                .tags(TagGenerator.tags())
                .virtualizationType(
                        valueFromArray(VirtualizationType.values())
                )
                .cpuOptions(CpuOptionsGenerator.cpuOptions())
                .capacityReservationId(CapacityReservationGenerator.capacityReservationId())
                .capacityReservationSpecification(
                        CapacityReservationSpecificationResponseGenerator.capacityReservationSpecificationResponse()
                )
                .hibernationOptions(HibernationOptionsGenerator.hibernationOptions())
                .licenses(LicenseConfigurationGenerator.licenseConfigurations(2))
    }
}
