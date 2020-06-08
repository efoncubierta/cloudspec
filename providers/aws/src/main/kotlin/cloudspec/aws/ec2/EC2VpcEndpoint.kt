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
        @property:PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = "vpc_endpoint_id",
                description = "The ID of the VPC endpoint"
        )
        val vpcEndpointId: String?,

        @property:PropertyDefinition(
                name = "vpc_endpoint_type",
                description = "The type of endpoint",
                exampleValues = "Interface | Gateway"
        )
        val vpcEndpointType: String?,

        @property:AssociationDefinition(
                name = "vpc",
                description = "The VPC to which the endpoint is associated",
                targetClass = EC2Vpc::class
        )
        val vpcId: String?,

        @property:PropertyDefinition(
                name = "service_name",
                description = "The name of the service to which the endpoint is associated"
        )
        val serviceName: String?
        ,
        @property:PropertyDefinition(
                name = "state",
                description = "The state of the VPC endpoint"
        )
        val state: String?, // TODO review

        //    @property:PropertyDefinition(
        //            name = "policy_document",
        //            description = "The policy document associated with the endpoint, if applicable"
        //    )
        //    private final String policyDocument;

        @property:AssociationDefinition(
                name = "route_tables",
                description = "(Gateway endpoint) One or more route tables associated with the endpoint",
                targetClass = EC2RouteTable::class
        )
        val routeTableIds: List<String>?,

        @property:AssociationDefinition(
                name = "subnets",
                description = "(Interface endpoint) One or more subnets in which the endpoint is located",
                targetClass = EC2Subnet::class
        )
        val subnetIds: List<String>?,

        @property:AssociationDefinition(
                name = "groups",
                description = "(Interface endpoint) Information about the security groups that are associated with the network interface",
                targetClass = EC2SecurityGroup::class
        )
        val groups: List<String>?,

        @property:PropertyDefinition(
                name = "private_dns_enabled",
                description = "(Interface endpoint) Indicates whether the VPC is associated with a private hosted zone"
        )
        val privateDnsEnabled: Boolean?,

        @property:PropertyDefinition(
                name = "requester_managed",
                description = "Indicates whether the VPC endpoint is being managed by its service"
        )
        val requesterManaged: Boolean?,

        @property:AssociationDefinition(
                name = "network_interfaces",
                description = "(Interface endpoint) One or more network interfaces for the endpoint",
                targetClass = EC2NetworkInterface::class
        )
        val networkInterfaceIds: List<String>?, // TODO review

        //    @property:PropertyDefinition(
        //            name = "dns_entries",
        //            description = "(Interface endpoint) The DNS entries for the endpoint"
        //    )
        //    private final List<DnsEntry> dnsEntries;

        @property:PropertyDefinition(
                name = "creation_timestamp",
                description = "The date and time that the VPC endpoint was created"
        )
        val creationTimestamp: Instant?,

        @property:PropertyDefinition(
                name = "tags",
                description = "Any tags assigned to the VPC endpoint"
        )
        val tags: List<KeyValue>?,

        @property:PropertyDefinition(
                name = "owner_id",
                description = "The ID of the AWS account that owns the VPC endpoint"
        )
        val ownerId: String?,

        @property:PropertyDefinition(
                name = "last_error",
                description = "The last error that occurred for VPC endpoint"
        )
        val lastError: String?
) : EC2Resource(region)
