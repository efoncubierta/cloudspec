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
import software.amazon.awssdk.arns.Arn
import software.amazon.awssdk.services.ec2.model.ElasticInferenceAcceleratorAssociation

object ElasticInferenceAcceleratorAssociationGenerator : BaseGenerator() {
    fun elasticInferenceAcceleratorAssociationId(): String {
        return "eia-${faker.random().hex(33)}"
    }

    fun elasticInferenceAcceleratorAssociationArn(): Arn {
        return Arn.builder()
                .service("elastic-inference")
                .region(CommonGenerator.region().id())
                .accountId(CommonGenerator.accountId())
                .partition("elastic-inference-accelerator")
                .resource(elasticInferenceAcceleratorAssociationId())
                .build()
    }

    fun elasticInferenceAcceleratorAssociations(n: Int?): List<ElasticInferenceAcceleratorAssociation> {
        return listGenerator(n) { elasticInferenceAcceleratorAssociation() }
    }

    fun elasticInferenceAcceleratorAssociation(): ElasticInferenceAcceleratorAssociation {
        return ElasticInferenceAcceleratorAssociation.builder()
                .elasticInferenceAcceleratorAssociationId(elasticInferenceAcceleratorAssociationId())
                .elasticInferenceAcceleratorArn(elasticInferenceAcceleratorAssociationArn().toString())
                .elasticInferenceAcceleratorAssociationTime(pastInstant())
                .elasticInferenceAcceleratorAssociationState(faker.lorem().word())
                .build()
    }
}
