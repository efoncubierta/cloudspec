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
import software.amazon.awssdk.services.ec2.model.Snapshot
import software.amazon.awssdk.services.ec2.model.SnapshotState

object SnapshotGenerator : BaseGenerator() {
    fun snapshotId(): String {
        return "snap-${faker.lorem().characters(30)}"
    }

    fun snapshotState(): SnapshotState {
        return valueFromArray(SnapshotState.values())
    }

    fun snapshots(n: Int? = null): List<Snapshot> {
        return listGenerator(n) { snapshot() }
    }

    fun snapshot(): Snapshot {
        return Snapshot.builder()
                .dataEncryptionKeyId(faker.lorem().word()) // TODO realistic value
                .description(faker.lorem().sentence())
                .encrypted(faker.random().nextBoolean())
                .kmsKeyId(faker.lorem().word()) // TODO realistic value
                .ownerId(CommonGenerator.accountId())
                .progress(faker.random().nextInt(0, 100).toString())
                .snapshotId(snapshotId())
                .startTime(pastInstant())
                .state(snapshotState())
                .stateMessage(faker.lorem().sentence()) // TODO realistic value
                .volumeId(VolumeGenerator.volumeId())
                .volumeSize(faker.random().nextInt(1, 1000))
                .ownerAlias(faker.lorem().word())
                .tags(TagGenerator.tags())
                .build()
    }
}
