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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2InternetGateway
import cloudspec.aws.ec2.EC2RouteTable
import cloudspec.aws.ec2.EC2Subnet
import software.amazon.awssdk.services.ec2.model.RouteTableAssociationStateCode

data class EC2RouteTableAssociation(
        @property:PropertyDefinition(
                name = PROP_MAIN,
                description = PROP_MAIN_D
        )
        val main: Boolean?,

        @property:PropertyDefinition(
                name = PROP_ROUTE_TABLE_ASSOCIATION_ID,
                description = PROP_ROUTE_TABLE_ASSOCIATION_ID_D
        )
        val routeTableAssociationId: String?,

        @property:AssociationDefinition(
                name = ASSOC_ROUTE_TABLE,
                description = ASSOC_ROUTE_TABLE_D,
                targetClass = EC2RouteTable::class
        )
        val routeTableId: String?,

        @property:AssociationDefinition(
                name = ASSOC_SUBNET,
                description = ASSOC_SUBNET_D,
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = ASSOC_GATEWAY,
                description = ASSOC_GATEWAY_D,
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support VPG

        @property:PropertyDefinition(
                name = PROP_STATE,
                description = PROP_STATE_D
        )
        val associationState: RouteTableAssociationStateCode?
) {
    companion object {
        const val PROP_MAIN = "main"
        const val PROP_MAIN_D = "Indicates whether this is the main route table"
        const val PROP_ROUTE_TABLE_ASSOCIATION_ID = "route_table_association_id"
        const val PROP_ROUTE_TABLE_ASSOCIATION_ID_D = "The ID of the association"
        const val ASSOC_ROUTE_TABLE = "route_table"
        const val ASSOC_ROUTE_TABLE_D = "The route table"
        const val ASSOC_SUBNET = "subnet"
        const val ASSOC_SUBNET_D = "The subnet"
        const val ASSOC_GATEWAY = "gateway"
        const val ASSOC_GATEWAY_D = "The internet gateway or virtual private gateway"
        const val PROP_STATE = "state"
        const val PROP_STATE_D = "The state of the association"
    }
}
