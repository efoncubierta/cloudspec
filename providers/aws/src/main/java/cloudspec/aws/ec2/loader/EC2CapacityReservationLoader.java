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
import software.amazon.awssdk.services.ec2.model.Region;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EC2CapacityReservationLoader extends EC2ResourceLoader<EC2CapacityReservationResource> {
    private final IAWSClientsProvider clientsProvider;

    public EC2CapacityReservationLoader(IAWSClientsProvider clientsProvider) {
        this.clientsProvider = clientsProvider;
    }

    @Override
    public Optional<EC2CapacityReservationResource> getById(String capacityReservationId) {
        return getCapacityReservations(Collections.singletonList(capacityReservationId)).findFirst();
    }

    @Override
    public List<EC2CapacityReservationResource> getAll() {
        return getCapacityReservations().collect(Collectors.toList());
    }

    private Stream<EC2CapacityReservationResource> getCapacityReservations() {
        return getCapacityReservations(Collections.emptyList());
    }

    private Stream<EC2CapacityReservationResource> getCapacityReservations(List<String> capacityReservationIds) {
        try (var ec2Client = clientsProvider.getEc2Client()) {
            return ec2Client.describeRegions()
                            .regions()
                            .stream()
                            .map(Region::regionName)
                            .flatMap(regionName -> getCapacityReservationsInRegion(regionName, capacityReservationIds));
        }
    }

    private Stream<EC2CapacityReservationResource> getCapacityReservationsInRegion(String regionName,
                                                                                   List<String> capacityReservationIds) {
        try (var client = clientsProvider.getEc2ClientForRegion(regionName)) {
            var response = capacityReservationIds != null && !capacityReservationIds.isEmpty() ?
                           client.describeCapacityReservations(builder ->
                                                                       builder.capacityReservationIds(
                                                                               capacityReservationIds.toArray(new String[0])
                                                                       )
                           ) :
                           client.describeCapacityReservations();

            return response.capacityReservations()
                           .stream()
                           .map(capacityReservation ->
                                        EC2CapacityReservationResource.fromSdk(regionName,
                                                                               capacityReservation)
                           );
        }
    }
}
