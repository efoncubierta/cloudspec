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
import cloudspec.model.KeyValue
import java.time.Instant

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "vpc_endpoint",
        description = "VPC Endpoint"
)
data class EC2VpcEndpoint(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "vpc_endpoint_id",
                description = "The ID of the VPC endpoint"
        )
        val vpcEndpointId: String?,

        @PropertyDefinition(
                name = "vpc_endpoint_type",
                description = "The type of endpoint",
                exampleValues = "Interface | Gateway"
        )
        val vpcEndpointType: String?,

        @AssociationDefinition(
                name = "vpc",
                description = "The VPC to which the endpoint is associated",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @PropertyDefinition(
                name = "service_name",
                description = "The name of the service to which the endpoint is associated"
        )
        val serviceName: String?
        ,
        @PropertyDefinition(
                name = "state",
                description = "The state of the VPC endpoint"
        )
        val state: String?, // TODO review

        //    @PropertyDefinition(
        //            name = "policy_document",
        //            description = "The policy document associated with the endpoint, if applicable"
        //    )
        //    private final String policyDocument;

        @AssociationDefinition(
                name = "route_tables",
                description = "(Gateway endpoint) One or more route tables associated with the endpoint",
                targetClass = EC2RouteTable::class
        )
        val routeTableIds: List<String>?,

        @AssociationDefinition(
                name = "subnets",
                description = "(Interface endpoint) One or more subnets in which the endpoint is located",
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @AssociationDefinition(
                name = "groups",
                description = "(Interface endpoint) Information about the security groups that are associated with the network interface",
                targetClass = EC2SecurityGroup::class
        )
        val groups: List<String>?,

        @PropertyDefinition(
                name = "private_dns_enabled",
                description = "(Interface endpoint) Indicates whether the VPC is associated with a private hosted zone"
        )
        val privateDnsEnabled: Boolean?,

        @PropertyDefinition(
                name = "requester_managed",
                description = "Indicates whether the VPC endpoint is being managed by its service"
        )
        val requesterManaged: Boolean?,

        @AssociationDefinition(
                name = "network_interfaces",
                description = "(Interface endpoint) One or more network interfaces for the endpoint",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceIds: List<String>?, // TODO review

        //    @PropertyDefinition(
        //            name = "dns_entries",
        //            description = "(Interface endpoint) The DNS entries for the endpoint"
        //    )
        //    private final List<DnsEntry> dnsEntries;

        @PropertyDefinition(
                name = "creation_timestamp",
                description = "The date and time that the VPC endpoint was created"
        )
        val creationTimestamp: Instant?,

        @PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the VPC endpoint"
        )
        val tags: List<KeyValue>?,

        @PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the VPC endpoint"
        )
        val ownerId: String?,

        @PropertyDefinition(
                name = "last_error",
                description = "The last error that occurred for VPC endpoint"
        )
        val lastError: String?
) : EC2Resource(region)
