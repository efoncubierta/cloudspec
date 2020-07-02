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
import software.amazon.awssdk.services.ec2.model.Tenancy

data class EC2Placement(
        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_AFFINITY,
                description = PROP_AFFINITY_D
        )
        val affinity: String?,

        @property:PropertyDefinition(
                name = PROP_GROUP_NAME,
                description = PROP_GROUP_NAME_D
        )
        val groupName: String?,

        @property:PropertyDefinition(
                name = PROP_PARTITION_NUMBER,
                description = PROP_PARTITION_NUMBER_D
        )
        val partitionNumber: Int?,

        @property:PropertyDefinition(
                name = PROP_TENANCY,
                description = PROP_TENANCY_D
        )
        val tenancy: Tenancy?
) {
    companion object {
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone of the instance"
        const val PROP_AFFINITY = "affinity"
        const val PROP_AFFINITY_D = "The affinity setting for the instance on the Dedicated Host"
        const val PROP_GROUP_NAME = "group_name"
        const val PROP_GROUP_NAME_D = "The name of the placement group the instance is in"
        const val PROP_PARTITION_NUMBER = "partition_number"
        const val PROP_PARTITION_NUMBER_D = "The number of the partition the instance is in"
        const val PROP_TENANCY = "tenancy"
        const val PROP_TENANCY_D = "The tenancy of the instance (if the instance is running in a VPC)"
    }
}
