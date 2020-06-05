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
import software.amazon.awssdk.services.ec2.model.Volume
import software.amazon.awssdk.services.ec2.model.VolumeState
import software.amazon.awssdk.services.ec2.model.VolumeType

object VolumeGenerator : BaseGenerator() {
    fun volumeId(): String {
        return "vol-${faker.lorem().characters(30)}"
    }

    fun volumeType(): VolumeType {
        return valueFromArray(VolumeType.values())
    }

    fun volumes(n: Int? = null): List<Volume> {
        return listGenerator(n) { volume() }
    }

    fun volume(): Volume {
        return Volume.builder()
                .attachments(VolumeAttachmentGenerator.volumeAttachments())
                .availabilityZone(CommonGenerator.availabilityZone())
                .createTime(pastInstant())
                .encrypted(faker.random().nextBoolean())
                .kmsKeyId(faker.lorem().word()) // TODO realistic value
                .outpostArn(OutpostGenerator.outpostArn().toString())
                .size(faker.random().nextInt(1, 1024))
                .snapshotId(SnapshotGenerator.snapshotId())
                .state(valueFromArray(VolumeState.values()))
                .volumeId(volumeId())
                .iops(faker.random().nextInt(100, 3000))
                .tags(TagGenerator.tags())
                .volumeType(valueFromArray(VolumeType.values()))
                .fastRestored(faker.random().nextBoolean())
                .multiAttachEnabled(faker.random().nextBoolean())
                .build()
    }
}
