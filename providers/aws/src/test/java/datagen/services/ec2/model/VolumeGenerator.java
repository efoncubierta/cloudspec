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
import software.amazon.awssdk.services.ec2.model.Volume;
import software.amazon.awssdk.services.ec2.model.VolumeState;
import software.amazon.awssdk.services.ec2.model.VolumeType;

import java.util.List;

public class VolumeGenerator extends BaseGenerator {
    public static String volumeId() {
        return String.format("vol-%s", faker.lorem().characters(10));
    }

    public static VolumeType volumeType() {
        return fromArray(VolumeType.values());
    }

    public static List<Volume> volumes() {
        return volumes(faker.random().nextInt(1, 10));
    }

    public static List<Volume> volumes(Integer n) {
        return listGenerator(n, VolumeGenerator::volume);
    }

    public static Volume volume() {
        return Volume.builder()
                     .attachments(VolumeAttachmentGenerator.volumeAttachments())
                     .availabilityZone(CommonGenerator.availabilityZone())
                     .createTime(pastDate().toInstant())
                     .encrypted(faker.random().nextBoolean())
                     .kmsKeyId(faker.lorem().word()) // TODO realistic value
                     .outpostArn(OutpostGenerator.outpostArn().toString())
                     .size(faker.random().nextInt(1, 1024))
                     .snapshotId(SnapshotGenerator.snapshotId())
                     .state(fromArray(VolumeState.values()))
                     .volumeId(volumeId())
                     .iops(faker.random().nextInt(100, 3000))
                     .tags(TagGenerator.tags())
                     .volumeType(fromArray(VolumeType.values()))
                     .fastRestored(faker.random().nextBoolean())
                     .multiAttachEnabled(faker.random().nextBoolean())
                     .build();
    }
}
