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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.AWSProvider
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.ec2.model.State
import software.amazon.awssdk.services.ec2.model.VpcEndpointType
import java.time.Instant

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2VpcEndpoint.RESOURCE_NAME,
        description = EC2VpcEndpoint.RESOURCE_DESCRIPTION
)
data class EC2VpcEndpoint(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_VPC_ENDPOINT_ID,
                description = PROP_VPC_ENDPOINT_ID_D
        )
        val vpcEndpointId: String,

        @property:PropertyDefinition(
                name = PROP_VPC_ENDPOINT_TYPE,
                description = PROP_VPC_ENDPOINT_TYPE_D
        )
        val vpcEndpointType: VpcEndpointType?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_SERVICE_NAME,
                description = PROP_SERVICE_NAME_D
        )
        val serviceName: String?
        ,
        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val state: State?,

        @property:AssociationDefinition(
                name = ASSOC_ROUTE_TABLES,
                description = ASSOC_ROUTE_TABLES_D,
                targetClass = EC2RouteTable::class
        )
        val routeTableIds: List<String>?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNETS,
                description = ASSOC_SUBNETS_D,
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @property:AssociationDefinition(
                name = ASSOC_GROUPS,
                description = ASSOC_GROUPS_D,
                targetClass = EC2SecurityGroup::class
        )
        val groups: List<String>?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_DNS_ENABLED,
                description = PROP_PRIVATE_DNS_ENABLED_D
        )
        val privateDnsEnabled: Boolean?,

        @property:PropertyDefinition(
                name = PROP_REQUESTER_MANAGED,
                description = PROP_REQUESTER_MANAGED_D
        )
        val requesterManaged: Boolean?,

        @property:AssociationDefinition(
                name = ASSOC_NETWORK_INTERFACES,
                description = ASSOC_NETWORK_INTERFACES_D,
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceIds: List<String>?,

        @property:PropertyDefinition(
                name = PROP_CREATION_TIMESTAMP,
                description = PROP_CREATION_TIMESTAMP_D
        )
        val creationTimestamp: Instant?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = PROP_LAST_ERROR,
                description = PROP_LAST_ERROR_D
        )
        val lastError: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "vpc_endpoint"
        const val RESOURCE_DESCRIPTION = "VPC Endpoint"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_VPC_ENDPOINT_ID = "vpc_endpoint_id"
        const val PROP_VPC_ENDPOINT_ID_D = "The ID of the VPC endpoint"
        const val PROP_VPC_ENDPOINT_TYPE = "vpc_endpoint_type"
        const val PROP_VPC_ENDPOINT_TYPE_D = "The type of endpoint"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The VPC to which the endpoint is associated"
        const val PROP_SERVICE_NAME = "service_name"
        const val PROP_SERVICE_NAME_D = "The name of the service to which the endpoint is associated"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the VPC endpoint"
        const val ASSOC_ROUTE_TABLES = "route_tables"
        const val ASSOC_ROUTE_TABLES_D = "(Gateway endpoint) One or more route tables associated with the endpoint"
        const val ASSOC_SUBNETS = "subnets"
        const val ASSOC_SUBNETS_D = "(Interface endpoint) One or more subnets in which the endpoint is located"
        const val ASSOC_GROUPS = "groups"
        const val ASSOC_GROUPS_D = "(Interface endpoint) Information about the security groups that are associated with the network interface"
        const val PROP_PRIVATE_DNS_ENABLED = "private_dns_enabled"
        const val PROP_PRIVATE_DNS_ENABLED_D = "(Interface endpoint) Indicates whether the VPC is associated with a private hosted zone"
        const val PROP_REQUESTER_MANAGED = "requester_managed"
        const val PROP_REQUESTER_MANAGED_D = "Indicates whether the VPC endpoint is being managed by its service"
        const val ASSOC_NETWORK_INTERFACES = "network_interfaces"
        const val ASSOC_NETWORK_INTERFACES_D = "(Interface endpoint) One or more network interfaces for the endpoint"
        const val PROP_CREATION_TIMESTAMP = "creation_timestamp"
        const val PROP_CREATION_TIMESTAMP_D = "The date and time that the VPC endpoint was created"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the VPC endpoint"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the VPC endpoint"
        const val PROP_LAST_ERROR = "last_error"
        const val PROP_LAST_ERROR_D = "The last error that occurred for VPC endpoint"
    }
}
