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

class EC2VolumeLoader(clientsProvider: IAWSClientsProvider) :
        EC2ResourceLoader<EC2Volume>(clientsProvider) {

    override suspend fun resourcesInRegion(region: String,
                                           ids: List<String>): List<EC2Volume> = coroutineScope {
        clientsProvider.ec2ClientForRegion(region).use { client ->
            val filters = buildFilters(
                    mapOf(
                            FILTER_VOLUME_ID to ids
                    )
            )

            withContext(Dispatchers.Default) {
                // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeVolumes.html
                client
                    .describeVolumes { builder ->
                        if (filters.isNotEmpty()) {
                            builder.filters(filters)
                        }
                    }
                    .volumes()
                    .map {
                        it.toEC2Volume(region)
                    }
            }

        }
    }


    companion object {
        private const val FILTER_VOLUME_ID = "volume-id"
    }
}
