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
import software.amazon.awssdk.services.ec2.model.NetworkInterface
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceStatus
import software.amazon.awssdk.services.ec2.model.NetworkInterfaceType

object NetworkInterfaceGenerator : BaseGenerator() {
    fun networkInterfaceId(): String {
        return "eni-${faker.random().hex(30)}"
    }

    fun networkInterfaceType(): NetworkInterfaceType {
        return valueFromArray(NetworkInterfaceType.values())
    }

    fun networkInterfaceStatus(): NetworkInterfaceStatus {
        return valueFromArray(NetworkInterfaceStatus.values())
    }

    fun networkInterfaces(n: Int? = null): List<NetworkInterface> {
        return listGenerator(n) { networkInterface() }
    }

    fun networkInterface(): NetworkInterface {
        return NetworkInterface.builder()
                .association(NetworkInterfaceAssociationGenerator.networkInterfaceAssociation())
                .attachment(NetworkInterfaceAttachmentGenerator.networkInterfaceAttachment())
                .availabilityZone(CommonGenerator.availabilityZone())
                .description(faker.lorem().sentence())
                .groups(GroupIdentifierGenerator.groupIdentifiers())
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
                .build()
    }
}
