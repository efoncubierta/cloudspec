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
import cloudspec.aws.ec2.EC2InternetGateway
import cloudspec.aws.ec2.EC2RouteTable
import cloudspec.aws.ec2.EC2Subnet

data class EC2RouteTableAssociation(
        @PropertyDefinition(
                name = "main",
                description = "Indicates whether this is the main route table"
        )
        val main: Boolean?,

        @PropertyDefinition(
                name = "main",
                description = "The ID of the association"
        )
        val routeTableAssociationId: String?,

        @AssociationDefinition(
                name = "route_table",
                description = "The route table",
                targetClass = EC2RouteTable::class
        )
        val routeTableId: String?,

        @AssociationDefinition(
                name = "subnet",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @AssociationDefinition(
                name = "gateway",
                description = "The internet gateway or virtual private gateway",
                targetClass = EC2InternetGateway::class
        )
        val gatewayId: String?, // TODO support VPG

        @PropertyDefinition(
                name = "state",
                description = "The state of the association",
                exampleValues = "associating | associated | disassociating | disassociated | failed"
        )
        val associationState: String?
)
