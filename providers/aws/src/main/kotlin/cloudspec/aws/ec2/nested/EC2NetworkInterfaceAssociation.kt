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

data class EC2NetworkInterfaceAssociation(
//    @property:AssociationDefinition(
        //            name = "allocation",
        //            description = "The allocation"
        //    )
        //    private final String allocationId;
        //    @property:AssociationDefinition(
        //            name = "association",
        //            description = "The association"
        //    )
        //    private final String associationId;
        @property:PropertyDefinition(
                name = "ip_owner_id",
                description = "The ID of the Elastic IP address owner"
        )
        val ipOwnerId: String?,

        @property:PropertyDefinition(
                name = "public_dns_name",
                description = "The public DNS name"
        )
        val publicDnsName: String?,

        @property:PropertyDefinition(
                name = "public_ip",
                description = "The address of the Elastic IP address bound to the network interface"
        )
        val publicIp: String?
)
