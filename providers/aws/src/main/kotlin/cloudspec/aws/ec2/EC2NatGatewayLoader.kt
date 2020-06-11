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

import cloudspec.aws.IAWSClientsProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.streams.toList

class EC2NatGatewayLoader(clientsProvider: IAWSClientsProvider) :
        EC2ResourceLoader<EC2NatGateway>(clientsProvider) {

    override suspend fun resourcesInRegion(region: String,
                                           ids: List<String>): List<EC2NatGateway> = coroutineScope {
        clientsProvider.ec2ClientForRegion(region).use { client ->
            val filters = buildFilters(
                    mapOf(
                            FILTER_NAT_GATEWAY_ID to ids
                    )
            )

            withContext(Dispatchers.Default) {
                client
                    // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeNatGateways.html
                    .describeNatGateways { builder ->
                        if (filters.isNotEmpty()) {
                            builder.filter(filters)
                        }
                    }
                    .natGateways()
                    .stream()
                    .map { it.toEC2NatGateway(region) }
                    .toList()
            }
        }
    }

    companion object {
        private const val FILTER_NAT_GATEWAY_ID = "nat-gateway-id"
    }
}
