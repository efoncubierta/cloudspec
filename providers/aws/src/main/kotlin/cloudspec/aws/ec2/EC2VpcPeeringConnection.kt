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

import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2VpcPeeringConnectionVpcInfo
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.VpcPeeringConnection
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "vpc_peering_connection",
        description = "VPC Peering Connection"
)
data class EC2VpcPeeringConnection(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "accepter_vpc_info",
                description = "Information about the accepter VP"
        )
        val accepterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @PropertyDefinition(
                name = "expiration_time",
                description = "The time that an unaccepted VPC peering connection will expire"
        )
        val expirationTime: Instant?,

        @PropertyDefinition(
                name = "requester_vpc_info",
                description = "Information about the requester VPC"
        )
        val requesterVpcInfo: EC2VpcPeeringConnectionVpcInfo?,

        @PropertyDefinition(
                name = "status",
                description = "The status of the VPC peering connection",
                exampleValues = "initiating-request | pending-acceptance | active | deleted | rejected | failed | expired | provisioning | deleting"
        )
        val status: String?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the resource"
        )
        val tags: List<KeyValue>?,

        @IdDefinition
        @PropertyDefinition(
                name = "vpc_peering_connection_id",
                description = "The ID of the VPC peering connection"
        )
        val vpcPeeringConnectionId: String?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, vpcPeeringConnection: VpcPeeringConnection): EC2VpcPeeringConnection {
            return vpcPeeringConnection.toEC2VpcPeeringConnection(region)
        }
    }
}
