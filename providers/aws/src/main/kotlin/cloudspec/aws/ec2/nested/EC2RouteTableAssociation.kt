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

data class EC2RouteTableAssociation(
        @property:PropertyDefinition(
                name = "main",
                description = "Indicates whether this is the main route table"
        )
        val main: Boolean?,

        @property:PropertyDefinition(
                name = "main",
                description = "The ID of the association"
        )
        val routeTableAssociationId: String?,

        @property:AssociationDefinition(
                name = "route_table",
                description = "The route table",
                targetClass = EC2RouteTable::class
        )
        val routeTableId: String?,

        @property:AssociationDefinition(
                name = "subnet",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @property:AssociationDefinition(
                name = "gateway",
                description = "The internet gateway or virtual private gateway",
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support VPG

        @property:PropertyDefinition(
                name = "state",
                description = "The state of the association",
                exampleValues = "associating | associated | disassociating | disassociated | failed"
        )
        val associationState: String?
)
