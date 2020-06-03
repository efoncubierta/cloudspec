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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.aws.AWSProvider;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.LastError;
import software.amazon.awssdk.services.ec2.model.SecurityGroupIdentifier;
import software.amazon.awssdk.services.ec2.model.VpcEndpoint;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VpcEndpointResource.RESOURCE_NAME,
        description = "VPC Endpoint"
)
public class EC2VpcEndpointResource extends EC2Resource {
    public static final String RESOURCE_NAME = "vpc_endpoint";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @IdDefinition
    @PropertyDefinition(
            name = "vpc_endpoint_id",
            description = "The ID of the VPC endpoint"
    )
    private final String vpcEndpointId;

    @PropertyDefinition(
            name = "vpc_endpoint_type",
            description = "The type of endpoint",
            exampleValues = "Interface | Gateway"
    )
    private final String vpcEndpointType;

    @AssociationDefinition(
            name = "vpc",
            description = "The VPC to which the endpoint is associated",
            targetClass = EC2VpcResource.class
    )
    private final String vpcId;

    @PropertyDefinition(
            name = "service_name",
            description = "The name of the service to which the endpoint is associated"
    )
    private final String serviceName;

    @PropertyDefinition(
            name = "state",
            description = "The state of the VPC endpoint"
    )
    private final String state;

    // TODO review
//    @PropertyDefinition(
//            name = "policy_document",
//            description = "The policy document associated with the endpoint, if applicable"
//    )
//    private final String policyDocument;

    @AssociationDefinition(
            name = "route_tables",
            description = "(Gateway endpoint) One or more route tables associated with the endpoint",
            targetClass = EC2RouteTableResource.class
    )
    private final List<String> routeTableIds;

    @AssociationDefinition(
            name = "subnets",
            description = "(Interface endpoint) One or more subnets in which the endpoint is located",
            targetClass = EC2SubnetResource.class
    )
    private final List<String> subnetIds;

    @AssociationDefinition(
            name = "groups",
            description = "(Interface endpoint) Information about the security groups that are associated with the network interface",
            targetClass = EC2SecurityGroupResource.class
    )
    private final List<String> groups;

    @PropertyDefinition(
            name = "private_dns_enabled",
            description = "(Interface endpoint) Indicates whether the VPC is associated with a private hosted zone"
    )
    private final Boolean privateDnsEnabled;

    @PropertyDefinition(
            name = "requester_managed",
            description = "Indicates whether the VPC endpoint is being managed by its service"
    )
    private final Boolean requesterManaged;

    @AssociationDefinition(
            name = "network_interfaces",
            description = "(Interface endpoint) One or more network interfaces for the endpoint",
            targetClass = EC2NetworkInterfaceResource.class
    )
    private final List<String> networkInterfaceIds;

    // TODO review
//    @PropertyDefinition(
//            name = "dns_entries",
//            description = "(Interface endpoint) The DNS entries for the endpoint"
//    )
//    private final List<DnsEntry> dnsEntries;

    @PropertyDefinition(
            name = "creation_timestamp",
            description = "The date and time that the VPC endpoint was created"
    )
    private final Instant creationTimestamp;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the VPC endpoint"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "owner_id",
            description = "The ID of the AWS account that owns the VPC endpoint"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "last_error",
            description = "The last error that occurred for VPC endpoint"
    )
    private final String lastError;

    public EC2VpcEndpointResource(String region, String vpcEndpointId, String vpcEndpointType, String vpcId,
                                  String serviceName, String state, List<String> routeTableIds, List<String> subnetIds,
                                  List<String> groups, Boolean privateDnsEnabled, Boolean requesterManaged,
                                  List<String> networkInterfaceIds, Instant creationTimestamp, List<KeyValue> tags,
                                  String ownerId, String lastError) {
        this.region = region;
        this.vpcEndpointId = vpcEndpointId;
        this.vpcEndpointType = vpcEndpointType;
        this.vpcId = vpcId;
        this.serviceName = serviceName;
        this.state = state;
        this.routeTableIds = routeTableIds;
        this.subnetIds = subnetIds;
        this.groups = groups;
        this.privateDnsEnabled = privateDnsEnabled;
        this.requesterManaged = requesterManaged;
        this.networkInterfaceIds = networkInterfaceIds;
        this.creationTimestamp = creationTimestamp;
        this.tags = tags;
        this.ownerId = ownerId;
        this.lastError = lastError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof VpcEndpoint))) {
            return false;
        }

        if (o instanceof VpcEndpoint) {
            return sdkEquals((VpcEndpoint) o);
        }

        EC2VpcEndpointResource that = (EC2VpcEndpointResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(vpcEndpointId, that.vpcEndpointId) &&
                Objects.equals(vpcEndpointType, that.vpcEndpointType) &&
                Objects.equals(vpcId, that.vpcId) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(state, that.state) &&
                Objects.equals(routeTableIds, that.routeTableIds) &&
                Objects.equals(subnetIds, that.subnetIds) &&
                Objects.equals(groups, that.groups) &&
                Objects.equals(privateDnsEnabled, that.privateDnsEnabled) &&
                Objects.equals(requesterManaged, that.requesterManaged) &&
                Objects.equals(networkInterfaceIds, that.networkInterfaceIds) &&
                Objects.equals(creationTimestamp, that.creationTimestamp) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(lastError, that.lastError);
    }

    private boolean sdkEquals(VpcEndpoint that) {
        return Objects.equals(vpcEndpointId, that.vpcEndpointId()) &&
                Objects.equals(vpcEndpointType, that.vpcEndpointTypeAsString()) &&
                Objects.equals(vpcId, that.vpcId()) &&
                Objects.equals(serviceName, that.serviceName()) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(routeTableIds, that.routeTableIds()) &&
                Objects.equals(subnetIds, that.subnetIds()) &&
                Objects.equals(groups, securityGroupIdsFromSdk(that.groups())) &&
                Objects.equals(privateDnsEnabled, that.privateDnsEnabled()) &&
                Objects.equals(requesterManaged, that.requesterManaged()) &&
                Objects.equals(networkInterfaceIds, that.networkInterfaceIds()) &&
                Objects.equals(creationTimestamp, that.creationTimestamp()) &&
                Objects.equals(tags, tagsFromSdk(that.tags())) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(lastError, lastErrorCodeFromSdk(that.lastError()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, vpcEndpointId, vpcEndpointType, vpcId, serviceName, state, routeTableIds,
                subnetIds, groups, privateDnsEnabled, requesterManaged, networkInterfaceIds, creationTimestamp,
                tags, ownerId, lastError);
    }

    public static EC2VpcEndpointResource fromSdk(String regionName, VpcEndpoint vpcEndpoint) {
        return Optional.ofNullable(vpcEndpoint)
                       .map(v -> new EC2VpcEndpointResource(
                               regionName,
                               v.vpcEndpointId(),
                               v.vpcEndpointTypeAsString(),
                               v.vpcId(),
                               v.serviceName(),
                               v.stateAsString(),
                               v.routeTableIds(),
                               v.subnetIds(),
                               securityGroupIdsFromSdk(v.groups()),
                               v.privateDnsEnabled(),
                               v.requesterManaged(),
                               v.networkInterfaceIds(),
                               v.creationTimestamp(),
                               tagsFromSdk(v.tags()),
                               v.ownerId(),
                               lastErrorCodeFromSdk(v.lastError())
                       ))
                       .orElse(null);
    }

    public static List<String> securityGroupIdsFromSdk(List<SecurityGroupIdentifier> securityGroupIdentifiers) {
        return Optional.ofNullable(securityGroupIdentifiers)
                       .orElse(Collections.emptyList())
                       .stream()
                       .map(SecurityGroupIdentifier::groupId)
                       .collect(Collectors.toList());
    }

    public static String lastErrorCodeFromSdk(LastError lastError) {
        return Optional.ofNullable(lastError)
                       .map(LastError::code)
                       .orElse(null);
    }
}
