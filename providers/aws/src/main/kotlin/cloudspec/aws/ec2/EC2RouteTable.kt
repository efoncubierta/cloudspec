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
package cloudspec.aws.ec2

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2Route
import cloudspec.aws.ec2.nested.EC2RouteTableAssociation
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.RouteTable

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "route_table",
        description = "Route Table"
)
data class EC2RouteTable(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "associations",
                description = "The associations between the route table and one or more subnets or a gateway"
        )
        val associations: List<EC2RouteTableAssociation>?,

        @PropertyDefinition(
                name = "propagating_gateway",
                description = "Any virtual private gateway (VGW) propagating routes"
        )
        val propagatingGatewayIds: List<String>?,

        @IdDefinition
        @PropertyDefinition(
                name = "route_table_id",
                description = "The ID of the route table"
        )
        val routeTableId: String,

        @PropertyDefinition(
                name = "routes",
                description = "The routes in the route table"
        )
        val routes: List<EC2Route>?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the route table"
        )
        val tags: List<KeyValue>?,

        @AssociationDefinition(
                name = "vpc",
                description = "The ID of the VPC",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the route table"
        )
        val ownerId: String?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, routeTable: RouteTable): EC2RouteTable {
            return routeTable.toEC2RouteTable(region)
        }
    }
}
