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
