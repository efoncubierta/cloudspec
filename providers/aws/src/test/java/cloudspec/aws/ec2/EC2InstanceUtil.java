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
package cloudspec.aws.ec2;

import cloudspec.aws.ec2.resource.EC2InstanceResource;
import cloudspec.aws.util.AWSResourcesUtil;
import cloudspec.aws.util.IAMResourcesUtil;
import cloudspec.model.KeyValue;
import com.github.javafaker.Faker;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EC2InstanceUtil {
//    private static final Faker faker = new Faker();
//
//    public static String randomInstanceId() {
//        return String.format("i-%s", faker.lorem().characters(17));
//    }
//
//    public static String randomInstanceType() {
//        return InstanceType.values()[faker.random().nextInt(0, InstanceType.values().length - 1)].toString();
//    }
//
//    public static String randomKeyPairName() {
//        return faker.lorem().characters(10);
//    }
//
//    public static String randomArchitecture() {
//        return faker.lorem().characters(10);
//    }
//
//    public static String randomKernelId() {
//        return faker.lorem().characters(10);
//    }
//
//    public static List<EC2InstanceResource.Device> randomEc2InstanceDevices(Integer n) {
//        return IntStream.range(0, n)
//                .mapToObj(i -> randomEc2InstanceDevice())
//                .collect(Collectors.toList());
//    }
//
//    public static EC2InstanceResource.Device randomEc2InstanceDevice() {
//        EC2InstanceResource.Device device = new EC2InstanceResource.Device();
//        device.name = faker.lorem().characters();
//        device.volumeId = randomEbsVolumeId();
//        device.deleteOnTermination = faker.random().nextBoolean();
//        return device;
//    }
//
//    public static List<EC2InstanceResource.NetworkInterface> randomEc2InstanceNetworkInterfaces(Integer n) {
//        return IntStream.range(0, n)
//                .mapToObj(i -> randomEc2InstanceNetworkInterface())
//                .collect(Collectors.toList());
//    }
//
//    public static EC2InstanceResource.NetworkInterface randomEc2InstanceNetworkInterface() {
//        EC2InstanceResource.NetworkInterface iface = new EC2InstanceResource.NetworkInterface();
//        iface.privateIp = faker.internet().ipV4Address();
//        iface.privateDns = faker.internet().domainName();
//        iface.status = faker.lorem().word();
//        iface.networkInterfaceId = randomNetworkInterfaceId();
//        iface.vpcId = randomVpcId();
//        iface.subnetId = randomSubnetId();
//        return iface;
//    }
//
//    public static EC2InstanceResource randomEc2InstanceResource() {
//        EC2InstanceResource resource = new EC2InstanceResource();
//        resource.region = AWSResourcesUtil.randomRegion();
//        resource.id = randomInstanceId();
//        resource.type = randomInstanceType();
//        resource.keyName = randomKeyPairName();
//        resource.architecture = randomArchitecture();
//        resource.kernelId = randomKernelId();
//        resource.ebsOptimized = faker.random().nextBoolean();
//        resource.privateDns = faker.internet().domainName();
//        resource.publicDns = faker.internet().domainName();
//        resource.privateIp = faker.internet().ipV4Address();
//        resource.publicIp = faker.internet().ipV4Address();
//        resource.rootDevice.name = faker.lorem().characters();
//        resource.rootDevice.type = faker.lorem().characters();
//        resource.devices = randomEc2InstanceDevices(4);
//        resource.networkInterfaces = randomEc2InstanceNetworkInterfaces(2);
//        resource.hibernationConfigured = faker.random().nextBoolean();
//        resource.tags = randomTags(5);
//        resource.imageId = randomImageId();
//        resource.vpcId = randomVpcId();
//        resource.subnetId = randomSubnetId();
//        resource.iamInstanceProfileId = IAMResourcesUtil.randomInstanceProfileId();
//        return resource;
//    }
//
//    public static Instance asInstance(EC2InstanceResource resource) {
//        return Instance
//                .builder()
//                .instanceId(resource.id)
//                .instanceType(resource.type)
//                .keyName(resource.keyName)
//                .architecture(resource.architecture)
//                .kernelId(resource.kernelId)
//                .ebsOptimized(resource.ebsOptimized)
//                .privateDnsName(resource.privateDns)
//                .publicDnsName(resource.publicDns)
//                .privateIpAddress(resource.privateIp)
//                .publicIpAddress(resource.publicIp)
//                .rootDeviceName(resource.rootDevice.name)
//                .rootDeviceType(resource.rootDevice.type)
//                .blockDeviceMappings(
//                        resource.devices
//                                .stream()
//                                .map(device ->
//                                        InstanceBlockDeviceMapping.builder()
//                                                .deviceName(device.name)
//                                                .ebs(builder ->
//                                                        builder
//                                                                .volumeId(device.volumeId)
//                                                                .deleteOnTermination(device.deleteOnTermination)
//                                                )
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .networkInterfaces(
//                        resource.networkInterfaces
//                                .stream()
//                                .map(iface ->
//                                        InstanceNetworkInterface.builder()
//                                                .networkInterfaceId(iface.networkInterfaceId)
//                                                .privateIpAddress(iface.privateIp)
//                                                .privateDnsName(iface.privateDns)
//                                                .status(iface.status)
//                                                .vpcId(iface.vpcId)
//                                                .subnetId(iface.subnetId)
//                                                .build()
//                                )
//                                .collect(Collectors.toList())
//                )
//                .hibernationOptions(
//                        builder -> builder.configured(resource.hibernationConfigured)
//                )
//                .tags(asTags(resource.tags))
//                .imageId(resource.imageId)
//                .vpcId(resource.vpcId)
//                .subnetId(resource.subnetId)
//                .iamInstanceProfile(builder -> builder.id(resource.iamInstanceProfileId))
//                .build();
//    }
//
//    public static String randomImageId() {
//        return String.format("ami-%s", faker.lorem().characters(10));
//    }
//
//    public static String randomVpcId() {
//        return String.format("vpc-%s", faker.lorem().characters(10));
//    }
//
//    public static String randomSubnetId() {
//        return String.format("subnet-%s", faker.lorem().characters(10));
//    }
//
//    public static String randomEbsVolumeId() {
//        return String.format("vol-%s", faker.lorem().characters(10));
//    }
//
//    public static String randomNetworkInterfaceId() {
//        return String.format("eni-%s", faker.lorem().characters(10));
//    }
//
//    public static List<KeyValue> randomTags(Integer n) {
//        return IntStream.range(0, n)
//                .mapToObj(i -> randomTag())
//                .collect(Collectors.toList());
//    }
//
//    public static KeyValue randomTag() {
//        return new KeyValue(faker.lorem().characters(10), faker.lorem().characters(10));
//    }
//
//    public static List<Tag> asTags(List<KeyValue> kv) {
//        return kv.stream()
//                .map(EC2InstanceUtil::asTag)
//                .collect(Collectors.toList());
//    }
//
//    public static Tag asTag(KeyValue kv) {
//        return Tag.builder()
//                .key(kv.getKey())
//                .value((String) kv.getValue())
//                .build();
//    }
}
