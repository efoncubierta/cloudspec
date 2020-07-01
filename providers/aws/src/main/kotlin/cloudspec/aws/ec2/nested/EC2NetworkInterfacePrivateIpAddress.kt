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
                name = PROP_ASSOCIATION,
                description = PROP_ASSOCIATION_D
        )
        val association: EC2NetworkInterfaceAssociation?,

        @property:PropertyDefinition(
                name = PROP_PRIMARY,
                description = PROP_PRIMARY_D
        )
        val primary: Boolean?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_DNS_NAME,
                description = PROP_PRIVATE_DNS_NAME_D
        )
        val privateDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PRIVATE_IP_ADDRESS,
                description = PROP_PRIVATE_IP_ADDRESS_D
        )
        val privateIpAddress: String?
) {
    companion object {
        const val PROP_ASSOCIATION = "association"
        const val PROP_ASSOCIATION_D = "The association information for an Elastic IP address (IPv4) associated with the network interface"
        const val PROP_PRIMARY = "primary"
        const val PROP_PRIMARY_D = "Indicates whether this IPv4 address is the primary private IPv4 address of the network interface"
        const val PROP_PRIVATE_DNS_NAME = "private_dns_name"
        const val PROP_PRIVATE_DNS_NAME_D = "The private DNS name"
        const val PROP_PRIVATE_IP_ADDRESS = "private_ip_address"
        const val PROP_PRIVATE_IP_ADDRESS_D = "The private IPv4 address"
    }
}
