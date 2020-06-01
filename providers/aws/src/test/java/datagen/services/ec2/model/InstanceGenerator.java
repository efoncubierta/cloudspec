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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class InstanceGenerator extends BaseGenerator {
    public static String instanceId() {
        return String.format("i-%s", faker.lorem().characters(17));
    }

    public static InstanceType instanceType() {
        return fromArray(InstanceType.values());
    }

    public static ArchitectureValues architectureValue() {
        return fromArray(ArchitectureValues.values());
    }

    public static PlatformValues platformValue() {
        return fromArray(PlatformValues.values());
    }

    public static InstanceStateName instanceStateName() {
        return fromArray(InstanceStateName.values());
    }

    public static InstanceState instanceState() {
        return InstanceState.builder()
                            .code(faker.number().randomDigit())
                            .name(instanceStateName())
                            .build();
    }

    public static HypervisorType hypervisorType() {
        return fromArray(HypervisorType.values());
    }

    public static InstanceLifecycleType instanceLifecycleType() {
        return fromArray(InstanceLifecycleType.values());
    }

    public static String sriovNetSupport() {
        return faker.lorem().word();
    }


    public static List<Instance> instances(Integer n) {
        return listGenerator(n, InstanceGenerator::instance);
    }

    public static Instance instance() {
        return instanceBuilder().build();
    }

    public static List<Instance.Builder> instanceBuilders(Integer n) {
        return listGenerator(n, InstanceGenerator::instanceBuilder);
    }

    public static Instance.Builder instanceBuilder() {
        return Instance
                .builder()
                .imageId(ImageGenerator.imageId())
                .instanceId(instanceId())
                .instanceType(instanceType())
                .kernelId(faker.lorem().word())
                .keyName(faker.lorem().word())
                .launchTime(faker.date().past(100, TimeUnit.DAYS).toInstant())
                .monitoring(MonitoringGenerator.monitoring())
                .placement(PlacementGenerator.placement())
                .platform(platformValue())
                .privateDnsName(faker.internet().domainName())
                .privateIpAddress(faker.internet().privateIpV4Address())
                .productCodes(ProductCodeGenerator.productCodes(5))
                .publicDnsName(faker.internet().domainName())
                .publicIpAddress(faker.internet().publicIpV4Address())
                .state(instanceState())
                .subnetId(SubnetGenerator.subnetId())
                .vpcId(VpcGenerator.vpcId())
                .architecture(architectureValue())
                .blockDeviceMappings(InstanceBlockDeviceMappingGenerator.instanceBlockDeviceMappings(5))
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
                .securityGroups(SecurityGroupGenerator.groupIdentifiers(5))
                .sourceDestCheck(faker.random().nextBoolean())
                .sriovNetSupport(sriovNetSupport())
                .tags(TagGenerator.tags(5))
                .cpuOptions(CpuOptionsGenerator.cpuOptions())
                .capacityReservationId(CapacityReservationGenerator.capacityReservationId())
                .hibernationOptions(HibernationOptionsGenerator.hibernationOptions())
                .licenses(LicenseConfigurationGenerator.licenseConfigurations(2));
    }
}
