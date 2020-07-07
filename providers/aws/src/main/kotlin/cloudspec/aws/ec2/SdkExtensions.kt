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
            instancePlatform(),
            availabilityZone(),
            tenancy(),
            totalInstanceCount(),
            availableInstanceCount(),
            ebsOptimized(),
            ephemeralStorage(),
            state(),
            endDate(),
            endDateType(),
            instanceMatchCriteria(),
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
            elasticGpuHealth()?.status(),
            elasticGpuState(),
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
            trafficType(),
            logDestinationType(),
            logDestination(),
            logFormat(),
            tags()?.toKeyValues(),
            maxAggregationInterval()
    )
}

fun Image.toEC2Image(regionName: String?): EC2Image {
    return EC2Image(
            regionName,
            architecture(),
            creationDate()?.toInstant(),
            imageId(),
            imageLocation(),
            imageType(),
            kernelId(),
            ownerId(),
            platform(),
            productCodes()?.toEC2ProductCodes(),
            state(),
            blockDeviceMappings()?.toEC2BlockDeviceMappings(),
            enaSupport(),
            hypervisor(),
            name(),
            rootDeviceName(),
            rootDeviceType(),
            sriovNetSupport(),
            tags().toKeyValues(),
            virtualizationType(),
            publicLaunchPermissions()
    )
}

fun Instance.toEC2Instance(region: String): EC2Instance {
    return EC2Instance(
            region,
            imageId(),
            instanceId(),
            instanceType(),
            kernelId(),
            keyName(),
            launchTime(),
            monitoring()?.toEC2Monitoring(),
            placement()?.toEC2Placement(),
            platform(),
            privateDnsName(),
            privateIpAddress(),
            productCodes()?.toEC2ProductCodes(),
            publicDnsName(),
            publicIpAddress(),
            state().name(),
            subnetId(),
            vpcId(),
            architecture(),
            blockDeviceMappings()?.toEC2InstanceBlockDeviceMappings(),
            ebsOptimized(),
            enaSupport(),
            hypervisor(),
            iamInstanceProfile()?.id(),
            instanceLifecycle(),
            elasticGpuAssociations()?.toElasticGpuIds(),
            elasticInferenceAcceleratorAssociations()?.toElasticInferenceAcceleratorArns(),
            networkInterfaces()?.toEC2InstanceNetworkInterfaces(),
            outpostArn(),
            rootDeviceName(),
            rootDeviceType(),
            securityGroups()?.toGroupIds(),
            sourceDestCheck(),
            sriovNetSupport(),
            tags()?.toKeyValues(),
            virtualizationType(),
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
            state(),
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
            interfaceType(),
            ipv6Addresses()?.toIpv6Addresses(),
            networkInterfaceId(),
            outpostArn(),
            ownerId(),
            privateDnsName(),
            privateIpAddress(),
            privateIpAddresses()?.toEC2NetworkInterfacePrivateIpAddresses(),
            sourceDestCheck(),
            status(),
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
            instanceType(),
            reservedInstancesId(),
            start(),
            state(),
            usagePrice(),
            currencyCode(),
            instanceTenancy(),
            offeringClass(),
            offeringType(),
            recurringCharges()?.toEC2RecurringCharges(),
            scope(),
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
            kmsKeyId(),
            ownerId(),
            progress(),
            snapshotId(),
            startTime(),
            state(),
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
            state(),
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
            state(),
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
            state(),
            volumeId(),
            iops(),
            tags()?.toKeyValues(),
            volumeType(),
            fastRestored(),
            multiAttachEnabled()
    )
}

fun VpcEndpoint.toEC2VpcEndpoint(region: String): EC2VpcEndpoint {
    return EC2VpcEndpoint(
            region,
            vpcEndpointId(),
            vpcEndpointType(),
            vpcId(),
            serviceName(),
            state(),
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
            status()?.code(),
            tags()?.toKeyValues(),
            vpcPeeringConnectionId()
    )
}

fun Vpc.toEC2Vpc(region: String): EC2Vpc {
    return EC2Vpc(
            region,
            cidrBlock(),
            dhcpOptionsId(),
            state(),
            vpcId(),
            ownerId(),
            instanceTenancy(),
            ipv6CidrBlockAssociationSet()?.toEC2VpcIpv6CidrBlockAssociations(),
            cidrBlockAssociationSet()?.toEC2VpcCidrBlockAssociation(),
            isDefault,
            tags()?.toKeyValues()
    )
}
