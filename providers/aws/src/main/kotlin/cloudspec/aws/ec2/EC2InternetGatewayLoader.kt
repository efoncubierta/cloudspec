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
package cloudspec.aws.ec2

import cloudspec.aws.IAWSClientsProvider
import software.amazon.awssdk.services.ec2.model.Filter
import java.util.*

class EC2InternetGatewayLoader(clientsProvider: IAWSClientsProvider) :
        EC2ResourceLoader<EC2InternetGateway>(clientsProvider) {
    override fun getResourcesInRegion(region: String,
                                      ids: List<String>): List<EC2InternetGateway> {
        clientsProvider.ec2ClientForRegion(region).use { client ->
            // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeInternetGateways.html
            val response = client.describeInternetGateways { builder ->
                builder.filters(
                        *buildFilters(ids).toTypedArray()
                )
            }
            return response.internetGateways()
                    .map { internetGateway ->
                        internetGateway.toEC2InternetGateway(region)
                    }
        }
    }

    private fun buildFilters(ids: List<String>): List<Filter> {
        val filters = ArrayList<Filter>()

        // filter by ids
        if (ids.isNotEmpty()) {
            filters.add(
                    Filter.builder()
                            .name(FILTER_INTERNET_GATEWAY_ID)
                            .values(*ids.toTypedArray())
                            .build()
            )
        }
        return filters
    }

    companion object {
        private const val FILTER_INTERNET_GATEWAY_ID = "internet-gateway-id"
    }
}
