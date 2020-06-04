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

import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.aws.ec2.nested.EC2TransitGatewayOptions
import cloudspec.model.KeyValue
import software.amazon.awssdk.services.ec2.model.TransitGateway
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "transit_gateway",
        description = "Transit Gateway"
)
data class EC2TransitGateway(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @PropertyDefinition(
                name = "transit_gateway_id",
                description = "The ID of the transit gateway"
        )
        val transitGatewayId: String?,

        @PropertyDefinition(
                name = "transit_gateway_arn",
                description = "The Amazon Resource Name (ARN) of the transit gateway"
        )
        val transitGatewayArn: String?,

        @PropertyDefinition(
                name = "state",
                description = "The state of the transit gateway",
                exampleValues = "pending | available | modifying | deleting | deleted"
        )
        val state: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account ID that owns the transit gateway"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "creation_time",
                description = "The creation time"
        )
        val creationTime: Instant?,

        @PropertyDefinition(
                name = "options",
                description = "The transit gateway options"
        )
        val options: EC2TransitGatewayOptions?,

        @PropertyDefinition(
                name = "tags",
                description = "The tags for the transit gateway"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        fun fromSdk(region: String, transitGateway: TransitGateway): EC2TransitGateway {
            return transitGateway.toEC2TransitGateway(region)
        }
    }
}
