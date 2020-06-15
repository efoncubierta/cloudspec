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

import arrow.core.None
import arrow.core.Some
import datagen.services.ec2.model.CapacityReservationGenerator.capacityReservationId
import datagen.services.ec2.model.CapacityReservationGenerator.capacityReservations
import io.mockk.every
import org.junit.Test
import software.amazon.awssdk.services.ec2.model.DescribeCapacityReservationsRequest
import software.amazon.awssdk.services.ec2.model.DescribeCapacityReservationsResponse
import java.util.function.Consumer
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EC2CapacityReservationLoaderTest : EC2LoaderTest() {
    private val loader = EC2CapacityReservationLoader(clientsProvider)

    @Test
    fun `should load all resources`() {
        val capacityReservations = capacityReservations()

        every {
            ec2Client.describeCapacityReservations(any<Consumer<DescribeCapacityReservationsRequest.Builder>>())
        } answers {
            val builder = DescribeCapacityReservationsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeCapacityReservationsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertTrue(request.capacityReservationIds().isEmpty())

            DescribeCapacityReservationsResponse.builder()
                .capacityReservations(capacityReservations)
                .build()
        }

        val resources = loader.all(emptyList())
        assertNotNull(resources)
        assertEquals(capacityReservations.size, resources.size)
    }

    @Test
    fun `should not load resource by random id`() {
        every {
            ec2Client.describeCapacityReservations(any<Consumer<DescribeCapacityReservationsRequest.Builder>>())
        } answers {
            val builder = DescribeCapacityReservationsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeCapacityReservationsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.capacityReservationIds().isEmpty())

            DescribeCapacityReservationsResponse.builder()
                .build()
        }

        val resourceOpt = loader.byId(emptyList(), capacityReservationId())
        assertTrue(resourceOpt is None)
    }

    @Test
    fun `should load resource by id`() {
        val capacityReservations = capacityReservations()

        every {
            ec2Client.describeCapacityReservations(any<Consumer<DescribeCapacityReservationsRequest.Builder>>())
        } answers {
            val builder = DescribeCapacityReservationsRequest.builder()

            val builderLambda = args[0] as Consumer<DescribeCapacityReservationsRequest.Builder>
            builderLambda.accept(builder)

            val request = builder.build()
            assertFalse(request.capacityReservationIds().isEmpty())

            DescribeCapacityReservationsResponse.builder()
                .capacityReservations(capacityReservations)
                .build()
        }

        val capacityReservation = capacityReservations[0]
        val resourceOpt = loader.byId(emptyList(), capacityReservation.capacityReservationId())
        assertTrue(resourceOpt is Some<*>)
    }
}
