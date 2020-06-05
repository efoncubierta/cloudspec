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
