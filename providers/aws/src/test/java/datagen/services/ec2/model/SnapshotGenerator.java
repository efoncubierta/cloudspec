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
package datagen.services.ec2.model;

import datagen.BaseGenerator;
import datagen.CommonGenerator;
import software.amazon.awssdk.services.ec2.model.Snapshot;
import software.amazon.awssdk.services.ec2.model.SnapshotState;

import java.util.List;

public class SnapshotGenerator extends BaseGenerator {
    public static String snapshotId() {
        return String.format("snap-%s", faker.lorem().characters(10));
    }

    public static SnapshotState snapshotState() {
        return fromArray(SnapshotState.values());
    }

    public static List<Snapshot> snapshots() {
        return snapshots(faker.random().nextInt(1, 10));
    }

    public static List<Snapshot> snapshots(Integer n) {
        return listGenerator(n, SnapshotGenerator::snapshot);
    }

    public static Snapshot snapshot() {
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
                       .build();
    }
}
