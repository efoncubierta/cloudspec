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

class EC2SubnetLoader(clientsProvider: IAWSClientsProvider) :
        EC2ResourceLoader<EC2Subnet>(clientsProvider) {

    override fun getResourcesInRegion(region: String,
                                      ids: List<String>): List<EC2Subnet> {
        clientsProvider.ec2ClientForRegion(region).use { client ->
            val filters = buildFilters(
                    mapOf(
                            EC2SubnetLoader.FILTER_SUBNET_ID to ids
                    )
            )

            return client
                    // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeSubnets.html
                    .describeSubnets { builder ->
                        if (filters.isNotEmpty()) {
                            builder.filters(filters)
                        }
                    }
                    .subnets()
                    .map { it.toEC2Subnet(region) }
        }
    }

    companion object {
        private const val FILTER_SUBNET_ID = "subnet-id"
    }
}
