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
package cloudspec.aws.ec2.nested

import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.*

fun List<BlockDeviceMapping>.toEC2BlockDeviceMappings(): List<EC2BlockDeviceMapping> {
    return map { o -> o.toEC2BlockDeviceMapping() }
}

fun BlockDeviceMapping.toEC2BlockDeviceMapping(): EC2BlockDeviceMapping {
    return EC2BlockDeviceMapping(
            deviceName(),
            virtualName(),
            ebs()?.toEC2EbsBlockDevice()
    )
}

fun CpuOptions.toEC2CpuOptions(): EC2CpuOptions {
    return EC2CpuOptions(
            coreCount(),
            threadsPerCore()
    )
}

fun List<DhcpConfiguration>.toEC2DhcpConfigurations(): List<EC2DhcpConfiguration> {
    return map { o -> o.toEC2DhcpConfiguration() }
}

fun DhcpConfiguration.toEC2DhcpConfiguration(): EC2DhcpConfiguration {
    return EC2DhcpConfiguration(
            key(),
            values()?.map { o -> o.value() }
    )
}

fun EbsBlockDevice.toEC2EbsBlockDevice(): EC2EbsBlockDevice {
    return EC2EbsBlockDevice(
            deleteOnTermination(),
            iops(),
            snapshotId(),
            volumeSize(),
            volumeTypeAsString(),
            encrypted()
    )
}

fun EbsInstanceBlockDevice.toEC2EbsInstanceBlockDevice(): EC2EbsInstanceBlockDevice {
    return EC2EbsInstanceBlockDevice(
            attachTime(),
            deleteOnTermination(),
            statusAsString(),
            volumeId()
    )
}

fun List<ElasticGpuAssociation>.toElasticGpuIds(): List<String> {
    return map { o ->
        o.elasticGpuId()
    }
}

fun List<ElasticInferenceAcceleratorAssociation>.toElasticInferenceAcceleratorArns(): List<String> {
    return map { o ->
        o.elasticInferenceAcceleratorArn()
    }
}

fun List<GroupIdentifier>.toGroupIds(): List<String> {
    return map { o ->
        o.groupId()
    }
}

fun List<SecurityGroupIdentifier>.toSecurityGroupIds(): List<String> {
    return map { o -> o.groupId() }
}

fun List<InstanceBlockDeviceMapping>.toEC2InstanceBlockDeviceMappings(): List<EC2InstanceBlockDeviceMapping> {
    return map { o -> o.toEC2InstanceBlockDeviceMapping() }
}

fun InstanceBlockDeviceMapping.toEC2InstanceBlockDeviceMapping(): EC2InstanceBlockDeviceMapping {
    return EC2InstanceBlockDeviceMapping(
            deviceName(),
            ebs()?.toEC2EbsInstanceBlockDevice()
    )
}

fun List<InstanceNetworkInterface>.toEC2InstanceNetworkInterfaces(): List<EC2InstanceNetworkInterface> {
    return map { o -> o.toEC2InstanceNetworkInterface() }
}

fun InstanceNetworkInterface.toEC2InstanceNetworkInterface(): EC2InstanceNetworkInterface {
    return EC2InstanceNetworkInterface(
            groups()?.map { o -> o.groupId() },
            ipv6Addresses()?.map { o -> o.ipv6Address() },
            networkInterfaceId(),
            ownerId(),
            privateDnsName(),
            privateIpAddress(),
            privateIpAddresses()?.toEC2InstancePrivateIpAddresses(),
            sourceDestCheck(),
            statusAsString(),
            subnetId(),
            vpcId(),
            interfaceType()
    )
}

fun List<InstancePrivateIpAddress>.toEC2InstancePrivateIpAddresses(): List<EC2InstancePrivateIpAddress> {
    return map { o -> o.toEC2InstancePrivateIpAddress() }
}

fun InstancePrivateIpAddress.toEC2InstancePrivateIpAddress(): EC2InstancePrivateIpAddress {
    return EC2InstancePrivateIpAddress(
            primary(),
            privateDnsName(),
            privateIpAddress()
    )
}

fun List<InternetGatewayAttachment>.toEC2InternetGatewayAttachments(): List<EC2InternetGatewayAttachment> {
    return map { o -> o.toEC2InternetGatewayAttachment() }
}

fun InternetGatewayAttachment.toEC2InternetGatewayAttachment(): EC2InternetGatewayAttachment {
    return EC2InternetGatewayAttachment(
            stateAsString(),
            vpcId()
    )
}

fun List<IpPermission>.toIpPermissions(): List<EC2IpPermission> {
    return map { o -> o.toEC2IpPermission() }
}

fun IpPermission.toEC2IpPermission(): EC2IpPermission {
    return EC2IpPermission(
            fromPort(),
            ipProtocol(),
            ipRanges()?.toEC2IpRanges(),
            ipv6Ranges()?.toEC2Ipv6Ranges(),
            prefixListIds()?.toPrefixListIds(),
            toPort(),
            userIdGroupPairs()?.toEC2UserIdGroupPairs()
    )
}

fun List<IpRange>.toEC2IpRanges(): List<EC2IpRange> {
    return map { o -> o.toEC2IpRange() }
}

fun IpRange.toEC2IpRange(): EC2IpRange {
    return EC2IpRange(
            cidrIp()
    )
}

fun List<Ipv6Range>.toEC2Ipv6Ranges(): List<EC2Ipv6Range> {
    return map { o -> o.toEC2Ipv6Range() }
}

fun Ipv6Range.toEC2Ipv6Range(): EC2Ipv6Range {
    return EC2Ipv6Range(
            cidrIpv6()
    )
}

fun List<LicenseConfiguration>.toLicenseConfigurationArns(): List<String> {
    return map { o ->
        o.licenseConfigurationArn()
    }
}

fun Monitoring.toEC2Monitoring(): EC2Monitoring {
    return EC2Monitoring(stateAsString())
}

fun Placement.toEC2Placement(): EC2Placement {
    return EC2Placement(
            availabilityZone(),
            affinity(),
            groupName(),
            partitionNumber(),
            tenancyAsString()
    )
}

fun List<PrefixListId>.toPrefixListIds(): List<String> {
    return map { o -> o.prefixListId() }
}

fun List<ProductCode>.toEC2ProductCodes(): List<EC2ProductCode> {
    return map { o -> o.toEC2ProductCode() }
}

fun ProductCode.toEC2ProductCode(): EC2ProductCode {
    return EC2ProductCode(
            productCodeId(),
            productCodeTypeAsString()
    )
}

fun List<NatGatewayAddress>.toEC2NatGatewayAddresses(): List<EC2NatGatewayAddress> {
    return map { o -> o.toEC2NatGatewayAddress() }
}

fun NatGatewayAddress.toEC2NatGatewayAddress(): EC2NatGatewayAddress {
    return EC2NatGatewayAddress(
            allocationId(),
            networkInterfaceId(),
            privateIp(),
            publicIp()
    )
}

fun List<NetworkAclEntry>.toEC2NetworkAclEntries(): List<EC2NetworkAclEntry> {
    return map { o -> o.toEC2NetworkAclEntry() }
}

fun NetworkAclEntry.toEC2NetworkAclEntry(): EC2NetworkAclEntry {
    return EC2NetworkAclEntry(
            cidrBlock(),
            egress(),
            icmpTypeCode()?.code(),
            ipv6CidrBlock(),
            portRange()?.toEC2PortRange(),
            protocol(),
            ruleActionAsString()
    )
}

fun NetworkInterfaceAssociation.toEC2NetworkInterfaceAssociation(): EC2NetworkInterfaceAssociation {
    return EC2NetworkInterfaceAssociation(
            ipOwnerId(),
            publicDnsName(),
            publicIp()
    )
}

fun NetworkInterfaceAttachment.toEC2NetworkInterfaceAttachment(): EC2NetworkInterfaceAttachment {
    return EC2NetworkInterfaceAttachment(
            attachTime(),
            deleteOnTermination(),
            instanceId(),
            instanceOwnerId(),
            statusAsString()
    )
}

fun List<NetworkInterfacePrivateIpAddress>.toEC2NetworkInterfacePrivateIpAddresses(): List<EC2NetworkInterfacePrivateIpAddress> {
    return map { o -> o.toEC2NetworkInterfacePrivateIpAddress() }
}

fun NetworkInterfacePrivateIpAddress.toEC2NetworkInterfacePrivateIpAddress(): EC2NetworkInterfacePrivateIpAddress {
    return EC2NetworkInterfacePrivateIpAddress(
            association()?.toEC2NetworkInterfaceAssociation(),
            primary(),
            privateDnsName(),
            privateIpAddress()
    )
}

fun PortRange.toEC2PortRange(): EC2PortRange {
    return EC2PortRange(
            from(),
            to()
    )
}

fun List<RecurringCharge>.toEC2RecurringCharges(): List<EC2RecurringCharge> {
    return map { o -> o.toEC2RecurringCharge() }
}

fun RecurringCharge.toEC2RecurringCharge(): EC2RecurringCharge {
    return EC2RecurringCharge(
            amount(),
            frequencyAsString()
    )
}

fun List<Route>.toEC2Routes(): List<EC2Route> {
    return map { o -> o.toEC2Route() }
}

fun Route.toEC2Route(): EC2Route {
    return EC2Route(
            destinationCidrBlock(),
            destinationIpv6CidrBlock(),
            destinationPrefixListId(),
            egressOnlyInternetGatewayId(),
            gatewayId(),
            instanceId(),
            instanceOwnerId(),
            natGatewayId(),
            transitGatewayId(),
            localGatewayId(),
            networkInterfaceId(),
            originAsString(),
            stateAsString(),
            vpcPeeringConnectionId()
    )
}

fun List<RouteTableAssociation>.toEC2RouteTableAssociations(): List<EC2RouteTableAssociation> {
    return map { o -> o.toEC2RouteTableAssociation() }
}

fun RouteTableAssociation.toEC2RouteTableAssociation(): EC2RouteTableAssociation {
    return EC2RouteTableAssociation(
            main(),
            routeTableAssociationId(),
            routeTableId(),
            subnetId(),
            gatewayId(),
            associationState()?.stateAsString()
    )
}

fun List<SubnetIpv6CidrBlockAssociation>.toEC2SubnetIpv6CidrBlockAssociations(): List<EC2SubnetIpv6CidrBlockAssociation> {
    return map { o -> o.toEC2SubnetIpv6CidrBlockAssociation() }
}

fun SubnetIpv6CidrBlockAssociation.toEC2SubnetIpv6CidrBlockAssociation(): EC2SubnetIpv6CidrBlockAssociation {
    return EC2SubnetIpv6CidrBlockAssociation(
            ipv6CidrBlock(),
            ipv6CidrBlockState()?.stateAsString()
    )
}

fun TransitGatewayOptions.toEC2TransitGatewayOptions(): EC2TransitGatewayOptions {
    return EC2TransitGatewayOptions(
            autoAcceptSharedAttachments() == AutoAcceptSharedAttachmentsValue.ENABLE,
            defaultRouteTableAssociation() == DefaultRouteTableAssociationValue.ENABLE,
            associationDefaultRouteTableId(),
            defaultRouteTablePropagation() == DefaultRouteTablePropagationValue.ENABLE,
            propagationDefaultRouteTableId(),
            vpnEcmpSupport() == VpnEcmpSupportValue.ENABLE,
            dnsSupport() == DnsSupportValue.ENABLE,
            multicastSupport() == MulticastSupportValue.ENABLE
    )
}

fun List<UserIdGroupPair>.toEC2UserIdGroupPairs(): List<EC2UserIdGroupPair> {
    return map { o -> o.toEC2UserIdGroupPair() }
}

fun UserIdGroupPair.toEC2UserIdGroupPair(): EC2UserIdGroupPair {
    return EC2UserIdGroupPair(
            groupId(),
            groupName(),
            peeringStatus(),
            userId(),
            vpcId(),
            vpcPeeringConnectionId()
    )
}

fun List<VolumeAttachment>.toEC2VolumeAttachments(): List<EC2VolumeAttachment> {
    return map { o -> o.toEC2VolumeAttachment() }
}

fun VolumeAttachment.toEC2VolumeAttachment(): EC2VolumeAttachment {
    return EC2VolumeAttachment(
            attachTime(),
            device(),
            instanceId(),
            stateAsString(),
            volumeId(),
            deleteOnTermination()
    )
}

fun List<VpcCidrBlockAssociation>.toEC2VpcCidrBlockAssociation(): List<EC2VpcCidrBlockAssociation> {
    return map { o -> o.toEC2VpcCidrBlockAssociation() }
}

fun VpcCidrBlockAssociation.toEC2VpcCidrBlockAssociation(): EC2VpcCidrBlockAssociation {
    return EC2VpcCidrBlockAssociation(
            cidrBlock(),
            cidrBlockState()?.stateAsString()
    )
}

fun List<VpcIpv6CidrBlockAssociation>.toEC2VpcIpv6CidrBlockAssociations(): List<EC2VpcIpv6CidrBlockAssociation> {
    return map { o -> o.toEC2VpcIpv6CidrBlockAssociation() }
}

fun VpcIpv6CidrBlockAssociation.toEC2VpcIpv6CidrBlockAssociation(): EC2VpcIpv6CidrBlockAssociation {
    return EC2VpcIpv6CidrBlockAssociation(
            ipv6CidrBlock(),
            ipv6CidrBlockState()?.stateAsString(),
            networkBorderGroup(),
            ipv6Pool()
    )
}

fun VpcPeeringConnectionOptionsDescription.toEC2VpcPeeringConnectionOptionsDescription(): EC2VpcPeeringConnectionOptionsDescription {
    return EC2VpcPeeringConnectionOptionsDescription(
            allowDnsResolutionFromRemoteVpc(),
            allowEgressFromLocalClassicLinkToRemoteVpc(),
            allowEgressFromLocalVpcToRemoteClassicLink()
    )
}

fun VpcPeeringConnectionVpcInfo.toEC2VpcPeeringConnectionVpcInfo(): EC2VpcPeeringConnectionVpcInfo {
    return EC2VpcPeeringConnectionVpcInfo(
            cidrBlock(),
            ipv6CidrBlockSet()?.toIpv6CidrBlocks(),
            cidrBlockSet()?.toCidrBlocks(),
            ownerId(),
            peeringOptions()?.toEC2VpcPeeringConnectionOptionsDescription(),
            vpcId(),
            region()
    )
}

fun List<Ipv6CidrBlock>.toIpv6CidrBlocks(): List<String> {
    return map { o -> o.ipv6CidrBlock() }
}

fun List<CidrBlock>.toCidrBlocks(): List<String> {
    return map { o -> o.cidrBlock() }
}

fun List<Tag>.toKeyValues(): List<KeyValue> {
    return this.map { tag: Tag ->
        tag.toKeyValue()
    }
}

fun Tag.toKeyValue(): KeyValue {
    return KeyValue(
            this.key(),
            this.value()
    )
}
