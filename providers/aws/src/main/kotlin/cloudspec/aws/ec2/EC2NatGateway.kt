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
import cloudspec.aws.ec2.nested.EC2NatGatewayAddress
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.NatGateway
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "nat_gateway",
        description = "NAT Gateway"
)
data class EC2NatGateway(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "create_time",
                description = "The date and time the NAT gateway was created"
        )
        val createTime: Instant?,

        @PropertyDefinition(
                name = "delete_time",
                description = "The date and time the NAT gateway was deleted, if applicable"
        )
        val deleteTime: Instant?,

        @PropertyDefinition(
                name = "failure_code",
                description = "If the NAT gateway could not be created, specifies the error code for the failure."
        )
        val failureCode: String?,

        @PropertyDefinition(
                name = "nat_gateway_addresses",
                description = "Information about the IP addresses and network interface associated with the NAT gateway"
        )
        val natGatewayAddresses: List<EC2NatGatewayAddress>?,

        @IdDefinition
        @PropertyDefinition(
                name = "nat_gateway_id",
                description = "The ID of the NAT gateway"
        )
        val natGatewayId: String?
        ,
        @PropertyDefinition(
                name = "state",
                description = "The state of the NAT gateway",
                exampleValues = "pending | failed | available | deleting | deleted"
        )
        val state: String?,

        @AssociationDefinition(
                name = "subnet",
                description = "The subnet in which the NAT gateway is located",
                targetClass = EC2Subnet::class
        )
        val subnetId: String?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC in which the NAT gateway is located",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "tags",
                description = "The tags for the NAT gateway"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, natGateway: NatGateway): EC2NatGateway {
            return natGateway.toEC2NatGateway(region)
        }
    }
}
