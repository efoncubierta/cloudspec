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
import cloudspec.aws.ec2.nested.EC2Route
import cloudspec.aws.ec2.nested.EC2RouteTableAssociation
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "route_table",
        description = "Route Table"
)
data class EC2RouteTable(
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:PropertyDefinition(
                name = "associations",
                description = "The associations between the route table and one or more subnets or a gateway"
        )
        val associations: List<EC2RouteTableAssociation>?,

        @property:PropertyDefinition(
                name = "propagating_gateway",
                description = "Any virtual private gateway (VGW) propagating routes"
        )
        val propagatingGatewayIds: List<String>?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "route_table_id",
                description = "The ID of the route table"
        )
        val routeTableId: String,

        @property:PropertyDefinition(
                name = "routes",
                description = "The routes in the route table"
        )
        val routes: List<EC2Route>?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the route table"
        )
        val tags: List<KeyValue>?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The ID of the VPC",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the route table"
        )
        val ownerId: String?
) : EC2Resource(region)
