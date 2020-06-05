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
