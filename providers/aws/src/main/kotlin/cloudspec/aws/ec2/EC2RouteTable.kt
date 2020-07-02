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
import cloudspec.aws.ec2.nested.EC2Route
import cloudspec.aws.ec2.nested.EC2RouteTableAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2RouteTable.RESOURCE_NAME,
        description = EC2RouteTable.RESOURCE_DESCRIPTION
)
data class EC2RouteTable(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = PROP_ASSOCIATIONS,
                description = PROP_ASSOCIATIONS_D
        )
        val associations: List<EC2RouteTableAssociation>?,

        @property:PropertyDefinition(
                name = PROP_PROPAGATING_GATEWAY,
                description = PROP_PROPAGATING_GATEWAY_D
        )
        val propagatingGatewayIds: List<String>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_ROUTE_TABLE_ID,
                description = PROP_ROUTE_TABLE_ID_D
        )
        val routeTableId: String,

        @property:PropertyDefinition(
                name = PROP_ROUTES,
                description = PROP_ROUTES_D
        )
        val routes: List<EC2Route>?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?,

        @property:AssociationDefinition(
                name = ASSOC_VPC,
                description = ASSOC_VPC_D,
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = PROP_OWNER_ID,
                description = PROP_OWNER_ID_D
        )
        val ownerId: String?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "route_table"
        const val RESOURCE_DESCRIPTION = "Route Table"

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ASSOCIATIONS = "associations"
        const val PROP_ASSOCIATIONS_D = "The associations between the route table and one or more subnets or a gateway"
        const val PROP_PROPAGATING_GATEWAY = "propagating_gateway"
        const val PROP_PROPAGATING_GATEWAY_D = "Any virtual private gateway (VGW) propagating routes"
        const val PROP_ROUTE_TABLE_ID = "route_table_id"
        const val PROP_ROUTE_TABLE_ID_D = "The ID of the route table"
        const val PROP_ROUTES = "routes"
        const val PROP_ROUTES_D = "The routes in the route table"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "Any tags assigned to the route table"
        const val ASSOC_VPC = "vpc"
        const val ASSOC_VPC_D = "The ID of the VPC"
        const val PROP_OWNER_ID = "owner_id"
        const val PROP_OWNER_ID_D = "The ID of the AWS account that owns the route table"
    }
}
