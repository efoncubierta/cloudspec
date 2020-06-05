/*-
 * #%L
 * CloudSpec AWS Provider
 * %%
 * Copyright (C) 2020 Ezequiel Foncubierta
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package cloudspec.aws.ec2.nested

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.aws.ec2.EC2RouteTable

data class EC2TransitGatewayOptions(
        @PropertyDefinition(
                name = "auto_accept_shared_attachments",
                description = "Indicates whether attachment requests are automatically accepted"
        )
        val autoAcceptSharedAttachments: Boolean?,

        @PropertyDefinition(
                name = "default_route_table_association",
                description = "Indicates whether resource attachments are automatically associated with the default association route table"
        )
        val defaultRouteTableAssociation: Boolean?,

        @AssociationDefinition(
                name = "association_default_route_table",
                description = "The default association route table",
                targetClass = EC2RouteTable::class
        )
        val associationDefaultRouteTableId: String?,

        @PropertyDefinition(
                name = "default_route_table_propagation",
                description = "Indicates whether resource attachments automatically propagate routes to the default propagation route table"
        )
        val defaultRouteTablePropagation: Boolean?,

        @AssociationDefinition(
                name = "propagation_default_route_table",
                description = "The default propagation route table",
                targetClass = EC2RouteTable::class
        )
        val propagationDefaultRouteTableId: String?,

        @PropertyDefinition(
                name = "vpn_ecmp_support",
                description = "Indicates whether Equal Cost Multipath Protocol support is enabled"
        )
        val vpnEcmpSupport: Boolean?,

        @PropertyDefinition(
                name = "dns_support",
                description = "Indicates whether DNS support is enabled"
        )
        val dnsSupport: Boolean?,

        @PropertyDefinition(
                name = "multicast_support",
                description = "Indicates whether multicast is enabled on the transit gateway"
        )
        val multicastSupport: Boolean?
)
