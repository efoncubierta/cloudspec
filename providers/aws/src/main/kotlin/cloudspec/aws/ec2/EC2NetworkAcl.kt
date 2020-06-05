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
import cloudspec.aws.ec2.nested.EC2NetworkAclEntry
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "network_acl",
        description = "Network ACL"
)
data class EC2NetworkAcl(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @AssociationDefinition(
                name = "subnets",
                description = "The subnet",
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @PropertyDefinition(
                name = "entries",
                description = "One or more entries (rules) in the network ACL"
        )
        val entries: List<EC2NetworkAclEntry>?,

        @PropertyDefinition(
                name = "is_default",
                description = "Indicates whether this is the default network ACL for the VPC"
        )
        val isDefault: Boolean?,

        @IdDefinition
        @PropertyDefinition(
                name = "network_acl_id",
                description = "The ID of the network ACL"
        )
        val networkAclId: String?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the network ACL"
        )
        val tags: List<KeyValue>?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC for the network ACL",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the network ACL"
        )
        val ownerId: String?
) : EC2Resource(region)
