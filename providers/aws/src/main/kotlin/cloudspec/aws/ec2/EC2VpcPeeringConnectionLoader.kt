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

import arrow.fx.IO
import cloudspec.aws.IAWSClientsProvider
import kotlin.streams.toList

class EC2VpcPeeringConnectionLoader(clientsProvider: IAWSClientsProvider) :
        EC2ResourceLoader<EC2VpcPeeringConnection>(clientsProvider) {

    override fun resourcesInRegion(region: String,
                                   ids: List<String>): IO<List<EC2VpcPeeringConnection>> =
            requestInRegion(region) { client ->
                val filters = buildFilters(
                        mapOf(
                                FILTER_VPC_PEERING_CONNECTION_ID to ids
                        )
                )

                client
                    // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVpcPeeringConnections.html
                    .describeVpcPeeringConnections { builder ->
                        if (filters.isNotEmpty()) {
                            builder.filters(filters)
                        }
                    }
                    .vpcPeeringConnections()
                    .stream()
                    .map { it.toEC2VpcPeeringConnection(region) }
                    .toList()
            }

    companion object {
        private const val FILTER_VPC_PEERING_CONNECTION_ID = "vpc-peering-connection-id"
    }
}
