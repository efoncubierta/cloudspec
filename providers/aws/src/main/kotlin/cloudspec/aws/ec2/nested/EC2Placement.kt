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
package cloudspec.aws.ec2.nested

import cloudspec.annotation.PropertyDefinition

data class EC2Placement(
        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone of the instance"
        )
        val availabilityZone: String?,

        @PropertyDefinition(
                name = "affinity",
                description = "The affinity setting for the instance on the Dedicated Host"
        )
        val affinity: String?,

        @PropertyDefinition(
                name = "group_name",
                description = "The name of the placement group the instance is in"
        )
        val groupName: String?,

        @PropertyDefinition(
                name = "partition_number",
                description = "The number of the partition the instance is in"
        )
        val partitionNumber: Int?,

        //    @AssociationDefinition(
        //            name = "host",
        //            description = "The ID of the Dedicated Host on which the instance resides"
        //    )
        //    val hostId: String?,

        @PropertyDefinition(
                name = "tenancy",
                description = "The tenancy of the instance (if the instance is running in a VPC)",
                exampleValues = "default | dedicated | host"
        )
        val tenancy: String?
)
