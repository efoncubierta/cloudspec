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
import cloudspec.aws.ec2.EC2RouteTable

data class EC2TransitGatewayOptions(
        @property:PropertyDefinition(
                name = "auto_accept_shared_attachments",
                description = "Indicates whether attachment requests are automatically accepted"
        )
        val autoAcceptSharedAttachments: Boolean?,

        @property:PropertyDefinition(
                name = "default_route_table_association",
                description = "Indicates whether resource attachments are automatically associated with the default association route table"
        )
        val defaultRouteTableAssociation: Boolean?,

        @property:AssociationDefinition(
                name = "association_default_route_table",
                description = "The default association route table",
                targetClass = EC2RouteTable::class
        )
        val associationDefaultRouteTableId: String?,

        @property:PropertyDefinition(
                name = "default_route_table_propagation",
                description = "Indicates whether resource attachments automatically propagate routes to the default propagation route table"
        )
        val defaultRouteTablePropagation: Boolean?,

        @property:AssociationDefinition(
                name = "propagation_default_route_table",
                description = "The default propagation route table",
                targetClass = EC2RouteTable::class
        )
        val propagationDefaultRouteTableId: String?,

        @property:PropertyDefinition(
                name = "vpn_ecmp_support",
                description = "Indicates whether Equal Cost Multipath Protocol support is enabled"
        )
        val vpnEcmpSupport: Boolean?,

        @property:PropertyDefinition(
                name = "dns_support",
                description = "Indicates whether DNS support is enabled"
        )
        val dnsSupport: Boolean?,

        @property:PropertyDefinition(
                name = "multicast_support",
                description = "Indicates whether multicast is enabled on the transit gateway"
        )
        val multicastSupport: Boolean?
)
