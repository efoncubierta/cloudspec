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
        // private final Integer amiLaunchIndex;
        //
        //    private final String imageId;
        //
        //    private final String instanceId;
        //
        //    private final String instanceType;
        //
        //    private final String kernelId;
        //
        //    private final String keyName;
        //
        //    private final Instant launchTime;
        //
        //    private final Monitoring monitoring;
        //
        //    private final Placement placement;
        //
        //    private final String platform;
        //
        //    private final String privateDnsName;
        //
        //    private final String privateIpAddress;
        //
        //    private final List<ProductCode> productCodes;
        //
        //    private final String publicDnsName;
        //
        //    private final String publicIpAddress;
        //
        //    private final String ramdiskId;
        //
        //    private final InstanceState state;
        //
        //    private final String stateTransitionReason;
        //
        //    private final String subnetId;
        //
        //    private final String vpcId;
        //
        //    private final String architecture;
        //
        //    private final List<InstanceBlockDeviceMapping> blockDeviceMappings;
        //
        //    private final String clientToken;
        //
        //    private final Boolean ebsOptimized;
        //
        //    private final Boolean enaSupport;
        //
        //    private final String hypervisor;
        //
        //    private final IamInstanceProfile iamInstanceProfile;
        //
        //    private final String instanceLifecycle;
        //
        //    private final List<ElasticGpuAssociation> elasticGpuAssociations;
        //
        //    private final List<ElasticInferenceAcceleratorAssociation> elasticInferenceAcceleratorAssociations;
        //
        //    private final List<InstanceNetworkInterface> networkInterfaces;
        //
        //    private final String outpostArn;
        //
        //    private final String rootDeviceName;
        //
        //    private final String rootDeviceType;
        //
        //    private final List<GroupIdentifier> securityGroups;
        //
        //    private final Boolean sourceDestCheck;
        //
        //    private final String spotInstanceRequestId;
        //
        //    private final String sriovNetSupport;
        //
        //    private final StateReason stateReason;
        //
        //    private final List<Tag> tags;
        //
        //    private final String virtualizationType;
        //
        //    private final CpuOptions cpuOptions;
        //
        //    private final String capacityReservationId;
        //
        //    private final CapacityReservationSpecificationResponse capacityReservationSpecification;
        //
        //    private final HibernationOptions hibernationOptions;
        //
        //    private final List<LicenseConfiguration> licenses;
        //
        //    private final InstanceMetadataOptionsResponse metadataOptions;
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
                .securityGroups(SecurityGroupGenerator.groupIdentifiers(5))
                .sourceDestCheck(faker.random().nextBoolean())
                .spotInstanceRequestId(faker.lorem().word()) // TODO realistic value
                .sriovNetSupport(sriovNetSupport())
                .stateReason(
                        // TODO realistic value
                        StateReason.builder()
                                   .code(faker.lorem().word())
                                   .message(faker.lorem().sentence())
                                   .build()
                )
                .tags(TagGenerator.tags(5))
                .virtualizationType(
                        fromArray(VirtualizationType.values())
                )
                .cpuOptions(CpuOptionsGenerator.cpuOptions())
                .capacityReservationId(CapacityReservationGenerator.capacityReservationId())
                .capacityReservationSpecification(
                        CapacityReservationSpecificationResponse.builder()
                                                                .capacityReservationPreference(
                                                                        fromArray(CapacityReservationPreference.values())
                                                                )
                                                                .capacityReservationTarget(
                                                                        // TODO realistic value
                                                                        CapacityReservationTargetResponse.builder()
                                                                                                         .capacityReservationId(faker.lorem().word())
                                                                                                         .build()
                                                                )
                                                                .build()
                )
                .hibernationOptions(HibernationOptionsGenerator.hibernationOptions())
                .licenses(LicenseConfigurationGenerator.licenseConfigurations(2));
    }
}
