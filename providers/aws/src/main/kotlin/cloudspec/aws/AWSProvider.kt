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
package cloudspec.aws

import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import cloudspec.annotation.ProviderDefinition
import cloudspec.aws.ec2.*
import cloudspec.aws.iam.IAMInstanceProfileResource
import cloudspec.aws.s3.S3BucketLoader
import cloudspec.aws.s3.S3BucketResource
import cloudspec.model.*
import java.util.*

@ProviderDefinition(
        name = "aws",
        description = "Amazon Web Services",
        resources = [
            EC2Image::class,
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
            EC2Vpc::class,
            IAMInstanceProfileResource::class,
            S3BucketResource::class
        ]
)
class AWSProvider(clientsProvider: IAWSClientsProvider) : Provider() {
    private val loaders: MutableMap<String, AWSResourceLoader<*>> = HashMap()

    override val configDefs: ConfigDefs
        get() = AWSConfig.CONFIG_DEFS

    override fun resourcesByRef(sets: SetValues, ref: ResourceDefRef): List<AWSResource> {
        return getLoader(ref).map { it.all(sets) }.getOrElse { emptyList() }
    }

    override fun resource(sets: SetValues, ref: ResourceRef): Option<AWSResource> {
        return getLoader(ref.defRef).flatMap { it.byId(sets, ref.id) }
    }

    private fun getLoader(resourceDefRef: ResourceDefRef): Option<AWSResourceLoader<*>> {
        return loaders[resourceDefRef.toString()].toOption()
    }

    init {
        loaders["aws:ec2:image"] = EC2ImageLoader(clientsProvider)
        loaders["aws:ec2:capacity_reservation"] = EC2CapacityReservationLoader(clientsProvider)
        loaders["aws:ec2:dhcp_options"] = EC2DhcpOptionsLoader(clientsProvider)
        loaders["aws:ec2:elastic_gpu"] = EC2ElasticGpuLoader(clientsProvider)
        loaders["aws:ec2:flow_log"] = EC2FlowLogLoader(clientsProvider)
        loaders["aws:ec2:instance"] = EC2InstanceLoader(clientsProvider)
        loaders["aws:ec2:internet_gateway"] = EC2InternetGatewayLoader(clientsProvider)
        loaders["aws:ec2:local_gateway"] = EC2LocalGatewayLoader(clientsProvider)
        loaders["aws:ec2:nat_gateway"] = EC2NatGatewayLoader(clientsProvider)
        loaders["aws:ec2:network_acl"] = EC2NetworkAclLoader(clientsProvider)
        loaders["aws:ec2:network_interface"] = EC2NetworkInterfaceLoader(clientsProvider)
        loaders["aws:ec2:reserved_instances"] = EC2ReservedInstancesLoader(clientsProvider)
        loaders["aws:ec2:route_table"] = EC2RouteTableLoader(clientsProvider)
        loaders["aws:ec2:security_group"] = EC2SecurityGroupLoader(clientsProvider)
        loaders["aws:ec2:snapshot"] = EC2SnapshotLoader(clientsProvider)
        loaders["aws:ec2:subnet"] = EC2SubnetLoader(clientsProvider)
        loaders["aws:ec2:transit_gateway"] = EC2TransitGatewayLoader(clientsProvider)
        loaders["aws:ec2:volume"] = EC2VolumeLoader(clientsProvider)
        loaders["aws:ec2:vpc_peering_connection"] = EC2VpcPeeringConnectionLoader(clientsProvider)
        loaders["aws:ec2:vpc"] = EC2VpcLoader(clientsProvider)
        loaders["aws:s3:bucket"] = S3BucketLoader(clientsProvider)
    }
}
