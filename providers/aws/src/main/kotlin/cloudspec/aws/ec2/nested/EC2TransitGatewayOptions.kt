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
                name = PROP_AUTO_ACCEPT_SHARED_ATTACHMENTS,
                description = PROP_AUTO_ACCEPT_SHARED_ATTACHMENTS_D
        )
        val autoAcceptSharedAttachments: Boolean?,

        @property:PropertyDefinition(
                name = PROP_DEFAULT_ROUTE_TABLE_ASSOCIATION,
                description = PROP_DEFAULT_ROUTE_TABLE_ASSOCIATION_D
        )
        val defaultRouteTableAssociation: Boolean?,

        @property:AssociationDefinition(
                name = ASSOC_ASSOCIATION_DEFAULT_ROUTE_TABLE,
                description = ASSOC_ASSOCIATION_DEFAULT_ROUTE_TABLE_D,
                targetClass = EC2RouteTable::class
        )
        val associationDefaultRouteTableId: String?,

        @property:PropertyDefinition(
                name = PROP_DEFAULT_ROUTE_TABLE_PROPAGATION,
                description = PROP_DEFAULT_ROUTE_TABLE_PROPAGATION_D
        )
        val defaultRouteTablePropagation: Boolean?,

        @property:AssociationDefinition(
                name = ASSOC_PROPAGATION_DEFAULT_ROLE_TABLE,
                description = ASSOC_PROPAGATION_DEFAULT_ROLE_TABLE_D,
                targetClass = EC2RouteTable::class
        )
        val propagationDefaultRouteTableId: String?,

        @property:PropertyDefinition(
                name = PROP_VPN_ECMP_SUPPORT,
                description = PROP_VPN_ECMP_SUPPORT_D
        )
        val vpnEcmpSupport: Boolean?,

        @property:PropertyDefinition(
                name = PROP_DNS_SUPPORT,
                description = PROP_DNS_SUPPORT_D
        )
        val dnsSupport: Boolean?,

        @property:PropertyDefinition(
                name = PROP_MULTICAST_SUPPORT,
                description = PROP_MULTICAST_SUPPORT_D
        )
        val multicastSupport: Boolean?
) {
    companion object {
        const val PROP_AUTO_ACCEPT_SHARED_ATTACHMENTS = "auto_accept_shared_attachments"
        const val PROP_AUTO_ACCEPT_SHARED_ATTACHMENTS_D = "Indicates whether attachment requests are automatically accepted"
        const val PROP_DEFAULT_ROUTE_TABLE_ASSOCIATION = "default_route_table_association"
        const val PROP_DEFAULT_ROUTE_TABLE_ASSOCIATION_D = "Indicates whether resource attachments are automatically associated with the default association route table"
        const val ASSOC_ASSOCIATION_DEFAULT_ROUTE_TABLE = "association_default_route_table"
        const val ASSOC_ASSOCIATION_DEFAULT_ROUTE_TABLE_D = "The default association route table"
        const val PROP_DEFAULT_ROUTE_TABLE_PROPAGATION = "default_route_table_propagation"
        const val PROP_DEFAULT_ROUTE_TABLE_PROPAGATION_D = "Indicates whether resource attachments automatically propagate routes to the default propagation route table"
        const val ASSOC_PROPAGATION_DEFAULT_ROLE_TABLE = "propagation_default_route_table"
        const val ASSOC_PROPAGATION_DEFAULT_ROLE_TABLE_D = "The default propagation route table"
        const val PROP_VPN_ECMP_SUPPORT = "vpn_ecmp_support"
        const val PROP_VPN_ECMP_SUPPORT_D = "Indicates whether Equal Cost Multipath Protocol support is enabled"
        const val PROP_DNS_SUPPORT = "dns_support"
        const val PROP_DNS_SUPPORT_D = "Indicates whether DNS support is enabled"
        const val PROP_MULTICAST_SUPPORT = "multicast_support"
        const val PROP_MULTICAST_SUPPORT_D = "Indicates whether multicast is enabled on the transit gateway"
    }
}
