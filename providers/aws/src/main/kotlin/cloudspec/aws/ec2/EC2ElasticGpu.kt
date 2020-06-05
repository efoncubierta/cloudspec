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

import cloudspec.annotation.AssociationDefinition
import cloudspec.annotation.IdDefinition
import cloudspec.annotation.PropertyDefinition
import cloudspec.annotation.ResourceDefinition
import cloudspec.model.KeyValue

@ResourceDefinition(
        provider = "aws",
        group = "ec2",
        name = "elastic_gpu",
        description = "Elastic Graphics Accelerator"
)
data class EC2ElasticGpu(
        @PropertyDefinition(
                name = "region",
                description = "The AWS region",
                exampleValues = "us-east-1 | eu-west-1"
        )
        override val region: String?,

        @IdDefinition
        @PropertyDefinition(
                name = "elastic_gpu_id",
                description = "The ID of the Elastic Graphics accelerator"
        )
        val elasticGpuId: String,

        @PropertyDefinition(
                name = "availability_zone",
                description = "The Availability Zone in the which the Elastic Graphics accelerator resides"
        )
        val availabilityZone: String?,

        @PropertyDefinition(
                name = "elastic_gpu_type",
                description = "The type of Elastic Graphics accelerator"
        )
        val elasticGpuType: String?,

        @PropertyDefinition(
                name = "elastic_gpu_health",
                description = "The status of the Elastic Graphics accelerator",
                exampleValues = "OK | IMPAIRED"
        )
        val elasticGpuHealth: String?,

        @PropertyDefinition(
                name = "elastic_gpu_state",
                description = "The state of the Elastic Graphics accelerator",
                exampleValues = "ATTACHED"
        )
        val elasticGpuState: String?,

        @AssociationDefinition(
                name = "instance",
                description = "The instance to which the Elastic Graphics accelerator is attached",
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @PropertyDefinition(
                name = "tags",
                description = "The tags assigned to the Elastic Graphics accelerator"
        )
        val tags: List<KeyValue>?
) : EC2Resource(region)
