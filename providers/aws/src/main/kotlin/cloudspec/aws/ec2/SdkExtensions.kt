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
package cloudspec.aws.ec2

import cloudspec.aws.ec2.nested.*
import cloudspec.aws.toInstant
import software.amazon.awssdk.services.ec2.model.*

fun CapacityReservation.toEC2CapacityReservation(region: String): EC2CapacityReservation {
    return EC2CapacityReservation(
            region,
            capacityReservationId(),
            ownerId(),
            capacityReservationArn(),
            instanceType(),
            instancePlatformAsString(),
            availabilityZone(),
            tenancyAsString(),
            totalInstanceCount(),
            availableInstanceCount(),
            ebsOptimized(),
            ephemeralStorage(),
            stateAsString(),
            endDate(),
            endDateTypeAsString(),
            instanceMatchCriteriaAsString(),
            createDate(),
            tags()?.toKeyValues()
    )
}

fun DhcpOptions.toEC2DhcpOptions(region: String?): EC2DhcpOptions {
    return EC2DhcpOptions(
            region,
            dhcpConfigurations()?.toEC2DhcpConfigurations(),
            dhcpOptionsId(),
            ownerId(),
            tags()?.toKeyValues()
    )
}

fun ElasticGpus.toEC2ElasticGpu(regionName: String): EC2ElasticGpu {
    return EC2ElasticGpu(
            regionName,
            elasticGpuId(),
            availabilityZone(),
            elasticGpuType(),
            elasticGpuHealth()?.statusAsString(),
            elasticGpuStateAsString(),
            instanceId(),
            tags()?.toKeyValues()
    )
}

fun FlowLog.toEC2FlowLog(region: String?): EC2FlowLog {
    return EC2FlowLog(
            region,
            creationTime(),
            deliverLogsErrorMessage(),
            deliverLogsPermissionArn(),
            deliverLogsStatus(),
            flowLogId(),
            flowLogStatus(),
            logGroupName(),
            resourceId(),
            trafficTypeAsString(),
            logDestinationTypeAsString(),
            logDestination(),
            logFormat(),
            tags()?.toKeyValues(),
            maxAggregationInterval()
    )
}

fun Image.toEC2Image(regionName: String?): EC2Image {
    return EC2Image(
            regionName,
            architectureAsString(),
            creationDate()?.toInstant(),
            imageId(),
            imageLocation(),
            imageTypeAsString(),
            kernelId(),
            ownerId(),
            platformAsString(),
            productCodes()?.toEC2ProductCodes(),
            stateAsString(),
            blockDeviceMappings()?.toEC2BlockDeviceMappings(),
            enaSupport(),
            hypervisorAsString(),
            name(),
            rootDeviceName(),
            rootDeviceTypeAsString(),
            sriovNetSupport(),
            tags().toKeyValues(),
            virtualizationTypeAsString(),
            publicLaunchPermissions()
    )
}

fun Instance.toResource(region: String): EC2Instance {
    return EC2Instance(
            region,
            imageId(),
            instanceId(),
            instanceTypeAsString(),
            kernelId(),
            keyName(),
            launchTime(),
            monitoring()?.toEC2Monitoring(),
            placement()?.toEC2Placement(),
            platformAsString(),
            privateDnsName(),
            privateIpAddress(),
            productCodes()?.toEC2ProductCodes(),
            publicDnsName(),
            publicIpAddress(),
            state().nameAsString(),
            subnetId(),
            vpcId(),
            architectureAsString(),
            blockDeviceMappings()?.toEC2InstanceBlockDeviceMappings(),
            ebsOptimized(),
            enaSupport(),
            hypervisorAsString(),
            iamInstanceProfile()?.id(),
            instanceLifecycleAsString(),
            elasticGpuAssociations()?.toElasticGpuIds(),
            elasticInferenceAcceleratorAssociations()?.toElasticInferenceAcceleratorArns(),
            networkInterfaces()?.toEC2InstanceNetworkInterfaces(),
            outpostArn(),
            rootDeviceName(),
            rootDeviceTypeAsString(),
            securityGroups()?.toGroupIds(),
            sourceDestCheck(),
            sriovNetSupport(),
            tags()?.toKeyValues(),
            virtualizationTypeAsString(),
            cpuOptions()?.toEC2CpuOptions(),
            capacityReservationId(),
            hibernationOptions()?.configured(),
            licenses()?.toLicenseConfigurationArns()
    )
}

fun InternetGateway.toEC2InternetGateway(region: String): EC2InternetGateway {
    return EC2InternetGateway(
            region,
            attachments()?.toEC2InternetGatewayAttachments(),
            internetGatewayId(),
            ownerId(),
            tags()?.toKeyValues()
    )
}

fun LocalGateway.toEC2LocalGateway(region: String): EC2LocalGateway {
    return EC2LocalGateway(
            region,
            localGatewayId(),
            outpostArn(),
            ownerId(),
            state(),
            tags().toKeyValues()
    )
}

fun NatGateway.toEC2NatGateway(region: String): EC2NatGateway {
    return EC2NatGateway(
            region,
            createTime(),
            deleteTime(),
            failureCode(),
            natGatewayAddresses()?.toEC2NatGatewayAddresses(),
            natGatewayId(),
            stateAsString(),
            subnetId(),
            vpcId(),
            tags()?.toKeyValues()
    )
}

fun NetworkAcl.toEC2NetworkAcl(regionName: String?): EC2NetworkAcl {
    return EC2NetworkAcl(
            regionName,
            associations()?.toSubnetIds(),
            entries()?.toEC2NetworkAclEntries(),
            isDefault,
            networkAclId(),
            tags()?.toKeyValues(),
            vpcId(),
            ownerId()
    )
}

fun List<NetworkAclAssociation>.toSubnetIds(): List<String> {
    return map { o -> o.subnetId() }
}

fun NetworkInterface.toEC2NetworkInterface(region: String): EC2NetworkInterface {
    return EC2NetworkInterface(
            region,
            association()?.toEC2NetworkInterfaceAssociation(),
            attachment()?.toEC2NetworkInterfaceAttachment(),
            availabilityZone(),
            groups()?.toGroupIds(),
            interfaceTypeAsString(),
            ipv6Addresses()?.toIpv6Addresses(),
            networkInterfaceId(),
            outpostArn(),
            ownerId(),
            privateDnsName(),
            privateIpAddress(),
            privateIpAddresses()?.toEC2NetworkInterfacePrivateIpAddresses(),
            sourceDestCheck(),
            statusAsString(),
            subnetId(),
            tagSet()?.toKeyValues(),
            vpcId()
    )
}

fun List<NetworkInterfaceIpv6Address>.toIpv6Addresses(): List<String> {
    return map { o -> o.ipv6Address() }
}

fun ReservedInstances.toEC2ReservedInstances(region: String): EC2ReservedInstances {
    return EC2ReservedInstances(
            region,
            availabilityZone(),
            duration(),
            end(),
            fixedPrice(),
            instanceCount(),
            instanceTypeAsString(),
            reservedInstancesId(),
            start(),
            stateAsString(),
            usagePrice(),
            currencyCodeAsString(),
            instanceTenancyAsString(),
            offeringClassAsString(),
            offeringTypeAsString(),
            recurringCharges()?.toEC2RecurringCharges(),
            scopeAsString(),
            tags()?.toKeyValues()
    )
}

fun RouteTable.toEC2RouteTable(region: String): EC2RouteTable {
    return EC2RouteTable(
            region,
            associations()?.toEC2RouteTableAssociations(),
            propagatingVgws()?.toGatewayIds(),
            routeTableId(),
            routes()?.toEC2Routes(),
            tags()?.toKeyValues(),
            vpcId(),
            ownerId()
    )
}

fun List<PropagatingVgw>.toGatewayIds(): List<String> {
    return map { o -> o.gatewayId() }
}

fun SecurityGroup.toEC2SecurityGroup(region: String): EC2SecurityGroup {
    return EC2SecurityGroup(
            region,
            groupName(),
            ipPermissions()?.toIpPermissions(),
            ownerId(),
            groupId(),
            ipPermissionsEgress()?.toIpPermissions(),
            tags()?.toKeyValues(),
            vpcId()
    )
}

fun Snapshot.toEC2Snapshot(region: String): EC2Snapshot {
    return EC2Snapshot(
            region,
            encrypted(),
            ownerId(),
            progress(),
            snapshotId(),
            startTime(),
            stateAsString(),
            volumeId(),
            volumeSize(),
            tags()?.toKeyValues()
    )
}

fun Subnet.toEC2Subnet(region: String): EC2Subnet {
    return EC2Subnet(
            region,
            availabilityZone(),
            availableIpAddressCount(),
            cidrBlock(),
            defaultForAz(),
            mapPublicIpOnLaunch(),
            stateAsString(),
            subnetId(),
            vpcId(),
            ownerId(),
            assignIpv6AddressOnCreation(),
            ipv6CidrBlockAssociationSet()?.toEC2SubnetIpv6CidrBlockAssociations(),
            tags()?.toKeyValues(),
            subnetArn(),
            outpostArn()
    )
}

fun TransitGateway.toEC2TransitGateway(region: String): EC2TransitGateway {
    return EC2TransitGateway(
            region,
            transitGatewayId(),
            transitGatewayArn(),
            stateAsString(),
            ownerId(),
            creationTime(),
            options()?.toEC2TransitGatewayOptions(),
            tags()?.toKeyValues()
    )
}

fun Volume.toEC2Volume(region: String): EC2Volume {
    return EC2Volume(
            region,
            attachments()?.toEC2VolumeAttachments(),
            availabilityZone(),
            createTime(),
            encrypted(),
            kmsKeyId(),
            outpostArn(),
            size(),
            snapshotId(),
            stateAsString(),
            volumeId(),
            iops(),
            tags()?.toKeyValues(),
            volumeTypeAsString(),
            fastRestored(),
            multiAttachEnabled()
    )
}

fun VpcEndpoint.toEC2VpcEndpoint(region: String): EC2VpcEndpoint {
    return EC2VpcEndpoint(
            region,
            vpcEndpointId(),
            vpcEndpointTypeAsString(),
            vpcId(),
            serviceName(),
            stateAsString(),
            routeTableIds(),
            subnetIds(),
            groups()?.toSecurityGroupIds(),
            privateDnsEnabled(),
            requesterManaged(),
            networkInterfaceIds(),
            creationTimestamp(),
            tags()?.toKeyValues(),
            ownerId(),
            lastError()?.code()
    )
}

fun VpcPeeringConnection.toEC2VpcPeeringConnection(region: String): EC2VpcPeeringConnection {
    return EC2VpcPeeringConnection(
            region,
            accepterVpcInfo()?.toEC2VpcPeeringConnectionVpcInfo(),
            expirationTime(),
            requesterVpcInfo()?.toEC2VpcPeeringConnectionVpcInfo(),
            status()?.codeAsString(),
            tags()?.toKeyValues(),
            vpcPeeringConnectionId()
    )
}

fun Vpc.toEC2Vpc(region: String): EC2Vpc {
    return EC2Vpc(
            region,
            cidrBlock(),
            dhcpOptionsId(),
            stateAsString(),
            vpcId(),
            ownerId(),
            instanceTenancyAsString(),
            ipv6CidrBlockAssociationSet()?.toEC2VpcIpv6CidrBlockAssociations(),
            cidrBlockAssociationSet()?.toEC2VpcCidrBlockAssociation(),
            isDefault,
            tags()?.toKeyValues()
    )
}
