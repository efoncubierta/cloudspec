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
        @property:PropertyDefinition(
                name = PROP_IP_OWNER_ID,
                description = PROP_IP_OWNER_ID_D
        )
        val ipOwnerId: String?,

        @property:PropertyDefinition(
                name = PROP_PUBLIC_DNS_NAME,
                description = PROP_PUBLIC_DNS_NAME_D
        )
        val publicDnsName: String?,

        @property:PropertyDefinition(
                name = PROP_PUBLIC_IP,
                description = PROP_PUBLIC_IP_D
        )
        val publicIp: String?
) {
    companion object {
        const val PROP_IP_OWNER_ID = "ip_owner_id"
        const val PROP_IP_OWNER_ID_D = "The ID of the Elastic IP address owner"
        const val PROP_PUBLIC_DNS_NAME = "public_dns_name"
        const val PROP_PUBLIC_DNS_NAME_D = "The public DNS name"
        const val PROP_PUBLIC_IP = "public_ip"
        const val PROP_PUBLIC_IP_D = "The address of the Elastic IP address bound to the network interface"
    }
}
