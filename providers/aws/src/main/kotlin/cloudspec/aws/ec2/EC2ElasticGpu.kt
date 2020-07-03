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
import cloudspec.aws.AWSProvider
import cloudspec.model.KeyValue
import cloudspec.model.ResourceDefRef
import software.amazon.awssdk.services.ec2.model.ElasticGpuState
import software.amazon.awssdk.services.ec2.model.ElasticGpuStatus

@ResourceDefinition(
        provider = AWSProvider.PROVIDER_NAME,
        group = EC2Group.GROUP_NAME,
        name = EC2ElasticGpu.RESOURCE_NAME,
        description = EC2ElasticGpu.RESOURCE_DESCRIPTION
)
data class EC2ElasticGpu(
        @property:PropertyDefinition(
                name = PROP_REGION,
                description = PROP_REGION_D
        )
        override val region: String?,

        @property:IdDefinition
        @property:PropertyDefinition(
                name = PROP_ELASTIC_GPU_ID,
                description = PROP_ELASTIC_GPU_ID_D
        )
        val elasticGpuId: String,

        @property:PropertyDefinition(
                name = PROP_AVAILABILITY_ZONE,
                description = PROP_AVAILABILITY_ZONE_D
        )
        val availabilityZone: String?,

        @property:PropertyDefinition(
                name = PROP_ELASTIC_GPU_TYPE,
                description = PROP_ELASTIC_GPU_TYPE_D
        )
        val elasticGpuType: String?,

        @property:PropertyDefinition(
                name = PROP_ELASTIC_GPU_HEALTH,
                description = PROP_ELASTIC_GPU_HEALTH_D
        )
        val elasticGpuHealth: ElasticGpuStatus?,

        @property:PropertyDefinition(
                name = PROP_ELASTIC_GPU_STATE,
                description = PROP_ELASTIC_GPU_STATE_D
        )
        val elasticGpuState: ElasticGpuState?,

        @property:AssociationDefinition(
                name = ASSOC_INSTANCE,
                description = ASSOC_INSTANCE_D,
                targetClass = EC2Instance::class
        )
        val instanceId: String?,

        @property:PropertyDefinition(
                name = PROP_TAGS,
                description = PROP_TAGS_D
        )
        val tags: List<KeyValue>?
) : EC2Resource(region) {
    companion object {
        const val RESOURCE_NAME = "elastic_gpu"
        const val RESOURCE_DESCRIPTION = "Elastic Graphics Accelerator"
        val RESOURCE_DEF = ResourceDefRef(AWSProvider.PROVIDER_NAME,
                                          EC2Group.GROUP_NAME,
                                          RESOURCE_NAME)

        const val PROP_REGION = "region"
        const val PROP_REGION_D = "The AWS region"
        const val PROP_ELASTIC_GPU_ID = "elastic_gpu_id"
        const val PROP_ELASTIC_GPU_ID_D = "The ID of the Elastic Graphics accelerator"
        const val PROP_AVAILABILITY_ZONE = "availability_zone"
        const val PROP_AVAILABILITY_ZONE_D = "The Availability Zone in the which the Elastic Graphics accelerator resides"
        const val PROP_ELASTIC_GPU_TYPE = "elastic_gpu_type"
        const val PROP_ELASTIC_GPU_TYPE_D = "The type of Elastic Graphics accelerator"
        const val PROP_ELASTIC_GPU_HEALTH = "elastic_gpu_health"
        const val PROP_ELASTIC_GPU_HEALTH_D = "The status of the Elastic Graphics accelerator"
        const val PROP_ELASTIC_GPU_STATE = "elastic_gpu_state"
        const val PROP_ELASTIC_GPU_STATE_D = "The state of the Elastic Graphics accelerator"
        const val ASSOC_INSTANCE = "instance"
        const val ASSOC_INSTANCE_D = "The instance to which the Elastic Graphics accelerator is attached"
        const val PROP_TAGS = "tags"
        const val PROP_TAGS_D = "The tags assigned to the Elastic Graphics accelerator"
    }
}
