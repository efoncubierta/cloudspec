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

data class EC2NetworkInterfacePrivateIpAddress(
        @property:PropertyDefinition(
                name = "association",
                description = "The association information for an Elastic IP address (IPv4) associated with the network interface"
        )
        val association: EC2NetworkInterfaceAssociation?,

        @property:PropertyDefinition(
                name = "primary",
                description = "Indicates whether this IPv4 address is the primary private IPv4 address of the network interface"
        )
        val primary: Boolean?,

        @property:PropertyDefinition(
                name = "private_dns_name",
                description = "The private DNS name"
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = "private_ip_address",
                description = "The private IPv4 address"
        )
        val privateIpAddress: String?
)
