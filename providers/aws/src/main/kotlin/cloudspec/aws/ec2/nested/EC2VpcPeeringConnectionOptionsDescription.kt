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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2VpcPeeringConnectionOptionsDescription(
        @property:PropertyDefinition(
                name = PROP_ALLOW_DNS_RESOLUTION_FROM_REMOTE_VPC,
                description = PROP_ALLOW_DNS_RESOLUTION_FROM_REMOTE_VPC_D
        )
        val allowDnsResolutionFromRemoteVpc: Boolean?,

        @property:PropertyDefinition(
                name = PROP_ALLOW_EGRESS_FROM_LOCAL_CLASSIC_LINK_TO_REMOTE_VPC,
                description = PROP_ALLOW_EGRESS_FROM_LOCAL_CLASSIC_LINK_TO_REMOTE_VPC_D
        )
        val allowEgressFromLocalClassicLinkToRemoteVpc: Boolean?,

        @property:PropertyDefinition(
                name = PROP_ALLOW_EGRESS_FROM_LOCAL_VPC_TO_REMOTE_CLASSIC_LINK,
                description = PROP_ALLOW_EGRESS_FROM_LOCAL_VPC_TO_REMOTE_CLASSIC_LINK_D
        )
        val allowEgressFromLocalVpcToRemoteClassicLink: Boolean?
) {
    companion object {
        const val PROP_ALLOW_DNS_RESOLUTION_FROM_REMOTE_VPC = "allow_dns_resolution_from_remote_vpc"
        const val PROP_ALLOW_DNS_RESOLUTION_FROM_REMOTE_VPC_D = "Indicates whether a local VPC can resolve public DNS hostnames to private IP addresses when queried from instances in a peer VPC"
        const val PROP_ALLOW_EGRESS_FROM_LOCAL_CLASSIC_LINK_TO_REMOTE_VPC = "allow_egress_from_local_classic_link_to_remote_vpc"
        const val PROP_ALLOW_EGRESS_FROM_LOCAL_CLASSIC_LINK_TO_REMOTE_VPC_D = "Indicates whether a local ClassicLink connection can communicate with the peer VPC over the VPC peering connection"
        const val PROP_ALLOW_EGRESS_FROM_LOCAL_VPC_TO_REMOTE_CLASSIC_LINK = "allow_egress_from_local_vpc_to_remote_classic_link"
        const val PROP_ALLOW_EGRESS_FROM_LOCAL_VPC_TO_REMOTE_CLASSIC_LINK_D = "Indicates whether a local VPC can communicate with a ClassicLink connection in the peer VPC over the VPC peering connection"
    }
}
