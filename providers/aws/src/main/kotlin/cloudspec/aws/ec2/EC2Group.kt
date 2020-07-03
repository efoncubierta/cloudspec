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

import arrow.syntax.collections.flatten
import cloudspec.annotation.ResourceDefReflectionUtil
import cloudspec.aws.AWSGroup
import cloudspec.aws.AWSResourceLoader
import cloudspec.aws.IAWSClientsProvider
import cloudspec.model.GroupDef
import cloudspec.model.ResourceDefRef

class EC2Group(clientsProvider: IAWSClientsProvider) : AWSGroup {
    override val loaders: Map<ResourceDefRef, AWSResourceLoader<*>> = mapOf(
            EC2CapacityReservation.RESOURCE_DEF to EC2CapacityReservationLoader(clientsProvider),
            EC2DhcpOptions.RESOURCE_DEF to EC2DhcpOptionsLoader(clientsProvider),
            EC2ElasticGpu.RESOURCE_DEF to EC2ElasticGpuLoader(clientsProvider),
            EC2FlowLog.RESOURCE_DEF to EC2FlowLogLoader(clientsProvider),
            EC2Image.RESOURCE_DEF to EC2ImageLoader(clientsProvider),
            EC2Instance.RESOURCE_DEF to EC2InstanceLoader(clientsProvider),
            EC2InternetGateway.RESOURCE_DEF to EC2InternetGatewayLoader(clientsProvider),
            EC2LocalGateway.RESOURCE_DEF to EC2LocalGatewayLoader(clientsProvider),
            EC2NatGateway.RESOURCE_DEF to EC2NatGatewayLoader(clientsProvider),
            EC2NetworkAcl.RESOURCE_DEF to EC2NetworkAclLoader(clientsProvider),
            EC2NetworkInterface.RESOURCE_DEF to EC2NetworkInterfaceLoader(clientsProvider),
            EC2ReservedInstances.RESOURCE_DEF to EC2ReservedInstancesLoader(clientsProvider),
            EC2RouteTable.RESOURCE_DEF to EC2RouteTableLoader(clientsProvider),
            EC2SecurityGroup.RESOURCE_DEF to EC2SecurityGroupLoader(clientsProvider),
            EC2Snapshot.RESOURCE_DEF to EC2SnapshotLoader(clientsProvider),
            EC2Subnet.RESOURCE_DEF to EC2SubnetLoader(clientsProvider),
            EC2TransitGateway.RESOURCE_DEF to EC2TransitGatewayLoader(clientsProvider),
            EC2Volume.RESOURCE_DEF to EC2VolumeLoader(clientsProvider),
            EC2VpcPeeringConnection.RESOURCE_DEF to EC2VpcPeeringConnectionLoader(clientsProvider),
            EC2Vpc.RESOURCE_DEF to EC2VpcLoader(clientsProvider)
    )

    override val definition: GroupDef
        get() = GROUP_DEF

    companion object {
        const val GROUP_NAME = "ec2"
        const val GROUP_DESCRIPTION = "EC2"

        private val GROUP_DEF = GroupDef(GROUP_NAME,
                                         GROUP_DESCRIPTION,
                                         listOf(EC2Image::class,
                                                EC2CapacityReservation::class,
                                                EC2DhcpOptions::class,
                                                EC2ElasticGpu::class,
                                                EC2FlowLog::class,
                                                EC2Instance::class,
                                                EC2InternetGateway::class,
                                                EC2LocalGateway::class,
                                                EC2NatGateway::class,
                                                EC2NetworkAcl::class,
                                                EC2NetworkInterface::class,
                                                EC2ReservedInstances::class,
                                                EC2RouteTable::class,
                                                EC2SecurityGroup::class,
                                                EC2Snapshot::class,
                                                EC2Subnet::class,
                                                EC2TransitGateway::class,
                                                EC2Volume::class,
                                                EC2VpcPeeringConnection::class,
                                                EC2Vpc::class)
                                             .map { ResourceDefReflectionUtil.toResourceDef(it) }
                                             .flatten())
    }
}
