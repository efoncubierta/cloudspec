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
package datagen.services.ec2.model

import datagen.BaseGenerator
import datagen.CommonGenerator
import software.amazon.awssdk.services.ec2.model.ElasticGpuHealth
import software.amazon.awssdk.services.ec2.model.ElasticGpuState
import software.amazon.awssdk.services.ec2.model.ElasticGpuStatus
import software.amazon.awssdk.services.ec2.model.ElasticGpus

object ElasticGpusGenerator : BaseGenerator() {
    fun elasticGpuId(): String {
        return "egpu-${faker.random().hex(30)}"
    }

    fun elasticGpuType(): String {
        // TODO return real gpu type
        return faker.lorem().word()
    }

    fun elasticGpuStatus(): ElasticGpuStatus {
        return valueFromArray(ElasticGpuStatus.values())
    }

    fun elasticGpuHealth(): ElasticGpuHealth {
        return ElasticGpuHealth.builder()
                .status(elasticGpuStatus())
                .build()
    }

    fun elasticGpuState(): ElasticGpuState {
        return valueFromArray(ElasticGpuState.values())
    }

    fun elasticGpusList(n: Int? = null): List<ElasticGpus> {
        return listGenerator(n) { elasticGpus() }
    }

    fun elasticGpus(): ElasticGpus {
        return ElasticGpus.builder()
                .elasticGpuId(elasticGpuId())
                .availabilityZone(CommonGenerator.availabilityZone())
                .elasticGpuType(elasticGpuType())
                .elasticGpuHealth(elasticGpuHealth())
                .elasticGpuState(elasticGpuState())
                .instanceId(InstanceGenerator.instanceId())
                .tags(TagGenerator.tags())
                .build()
    }
}
