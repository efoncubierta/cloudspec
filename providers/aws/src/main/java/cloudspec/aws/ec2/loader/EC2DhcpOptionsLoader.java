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
package cloudspec.aws.ec2.loader;

import cloudspec.aws.IAWSClientsProvider;
import cloudspec.aws.ec2.resource.EC2DhcpOptionsResource;
import software.amazon.awssdk.services.ec2.model.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class EC2DhcpOptionsLoader extends EC2ResourceLoader<EC2DhcpOptionsResource> {
    private static final String FILTER_DHCP_OPTIONS_ID = "dhcp-options-id";

    public EC2DhcpOptionsLoader(IAWSClientsProvider clientsProvider) {
        super(clientsProvider);
    }

    @Override
    protected Stream<EC2DhcpOptionsResource> getResourcesInRegion(String region,
                                                                  List<String> ids) {
        try (var client = clientsProvider.getEc2ClientForRegion(region)) {
            // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeDhcpOptionss.html
            var response = client.describeDhcpOptions(builder ->
                    builder.filters(
                            buildFilters(ids).toArray(new Filter[0])
                    )
            );

            return response.dhcpOptions()
                           .stream()
                           .map(dhcpOptions -> EC2DhcpOptionsResource.fromSdk(region, dhcpOptions));
        }
    }

    private List<Filter> buildFilters(List<String> ids) {
        var filters = new ArrayList<Filter>();

        // filter by ids
        if (!Objects.isNull(ids) && !ids.isEmpty()) {
            filters.add(
                    Filter.builder()
                          .name(FILTER_DHCP_OPTIONS_ID)
                          .values(ids.toArray(new String[0]))
                          .build()
            );
        }
        return filters;
    }
}
