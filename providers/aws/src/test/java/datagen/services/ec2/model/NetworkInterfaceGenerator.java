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
import datagen.CommonGenerator;
import software.amazon.awssdk.services.ec2.model.NetworkInterface;
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceStatus;
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceType;

import java.util.List;


public class NetworkInterfaceGenerator extends BaseGenerator {
    public static String networkInterfaceId() {
        return String.format("eni-%s", faker.random().hex(10));
    }

    public static NetworkInterfaceType networkInterfaceType() {
        return fromArray(NetworkInterfaceType.values());
    }

    public static NetworkInterfaceStatus networkInterfaceStatus() {
        return fromArray(NetworkInterfaceStatus.values());
    }

    public static List<NetworkInterface> networkInterfaces() {
        return networkInterfaces(faker.random().nextInt(1, 10));
    }

    public static List<NetworkInterface> networkInterfaces(Integer n) {
        return listGenerator(n, NetworkInterfaceGenerator::networkInterface);
    }

    public static NetworkInterface networkInterface() {
        return NetworkInterface.builder()
                               .association(NetworkInterfaceAssociationGenerator.networkInterfaceAssociation())
                               .attachment(NetworkInterfaceAttachmentGenerator.networkInterfaceAttachment())
                               .availabilityZone(CommonGenerator.availabilityZone())
                               .description(faker.lorem().sentence())
                               .groups(SecurityGroupGenerator.groupIdentifiers())
                               .interfaceType(networkInterfaceType())
                               .ipv6Addresses(NetworkInterfaceIpv6AddressGenerator.networkInterfaceIpv6Addresses())
                               .macAddress(faker.internet().macAddress())
                               .networkInterfaceId(networkInterfaceId())
                               .outpostArn(OutpostGenerator.outpostArn().toString())
                               .ownerId(CommonGenerator.accountId())
                               .privateDnsName(faker.internet().domainName())
                               .privateIpAddress(faker.internet().privateIpV4Address())
                               .privateIpAddresses(NetworkInterfaceIpAddressGenerator.networkInterfacePrivateIpAddresses())
                               .requesterId(CommonGenerator.accountId())
                               .requesterManaged(faker.random().nextBoolean())
                               .sourceDestCheck(faker.random().nextBoolean())
                               .status(networkInterfaceStatus())
                               .subnetId(SubnetGenerator.subnetId())
                               .tagSet(TagGenerator.tags())
                               .vpcId(VpcGenerator.vpcId())
                               .build();
    }
}
