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
import cloudspec.aws.ec2.resource.EC2CapacityReservationResource;

import java.util.List;
import java.util.stream.Stream;

public class EC2CapacityReservationLoader extends EC2ResourceLoader<EC2CapacityReservationResource> {
    public EC2CapacityReservationLoader(IAWSClientsProvider clientsProvider) {
        super(clientsProvider);
    }

    @Override
    protected Stream<EC2CapacityReservationResource> getResourcesInRegion(String region,
                                                                          List<String> ids) {
        try (var client = clientsProvider.getEc2ClientForRegion(region)) {
            // https://docs.aws.amazon.com/AWSEC2/latest/APIReference/API_DescribeCapacityReservations.html
            var response = client.describeCapacityReservations(builder ->
                    builder.capacityReservationIds(
                            ids.toArray(new String[0])
                    )
            );

            return response.capacityReservations()
                           .stream()
                           .map(capacityReservation -> EC2CapacityReservationResource.fromSdk(region, capacityReservation));
        }
    }
}
