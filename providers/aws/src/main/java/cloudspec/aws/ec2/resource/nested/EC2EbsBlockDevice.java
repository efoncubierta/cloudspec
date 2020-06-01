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
package cloudspec.aws.ec2.resource.nested;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.aws.ec2.resource.EC2SnapshotResource;
import software.amazon.awssdk.services.ec2.model.EbsBlockDevice;

import java.util.Objects;
import java.util.Optional;

public class EC2EbsBlockDevice {
    @PropertyDefinition(
            name = "delete_on_termination",
            description = "Indicates whether the EBS volume is deleted on instance termination"
    )
    private final Boolean deleteOnTermination;

    @PropertyDefinition(
            name = "iops",
            description = "The number of I/O operations per second (IOPS) that the volume supports"
    )
    private final Integer iops;

    @AssociationDefinition(
            name = "snapshot",
            description = "The ID of the snapshot",
            targetClass = EC2SnapshotResource.class
    )
    private final String snapshotId;

    @PropertyDefinition(
            name = "volume_size",
            description = "The size of the volume, in GiB"
    )
    private final Integer volumeSize;

    @PropertyDefinition(
            name = "volume_type",
            description = "The volume type",
            exampleValues = "gp2, st1, sc1, standard"
    )
    private final String volumeType;

    @PropertyDefinition(
            name = "encrypted",
            description = "Indicates whether the encryption state of an EBS volume is changed while being restored from a backing snapshot"
    )
    private final Boolean encrypted;

    public EC2EbsBlockDevice(Boolean deleteOnTermination, Integer iops, String snapshotId, Integer volumeSize,
                             String volumeType, Boolean encrypted) {
        this.deleteOnTermination = deleteOnTermination;
        this.iops = iops;
        this.snapshotId = snapshotId;
        this.volumeSize = volumeSize;
        this.volumeType = volumeType;
        this.encrypted = encrypted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof EbsBlockDevice))) {
            return false;
        }

        if (o instanceof EbsBlockDevice) {
            return sdkEquals((EbsBlockDevice) o);
        }

        EC2EbsBlockDevice that = (EC2EbsBlockDevice) o;
        return Objects.equals(deleteOnTermination, that.deleteOnTermination) &&
                Objects.equals(iops, that.iops) &&
                Objects.equals(snapshotId, that.snapshotId) &&
                Objects.equals(volumeSize, that.volumeSize) &&
                Objects.equals(volumeType, that.volumeType) &&
                Objects.equals(encrypted, that.encrypted);
    }

    private boolean sdkEquals(EbsBlockDevice that) {
        return Objects.equals(deleteOnTermination, that.deleteOnTermination()) &&
                Objects.equals(iops, that.iops()) &&
                Objects.equals(snapshotId, that.snapshotId()) &&
                Objects.equals(volumeSize, that.volumeSize()) &&
                Objects.equals(volumeType, that.volumeTypeAsString()) &&
                Objects.equals(encrypted, that.encrypted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteOnTermination, iops, snapshotId, volumeSize, volumeType, encrypted);
    }

    public static EC2EbsBlockDevice fromSdk(EbsBlockDevice ebsBlockDevice) {
        return Optional.ofNullable(ebsBlockDevice)
                       .map(v -> new EC2EbsBlockDevice(
                               v.deleteOnTermination(),
                               v.iops(),
                               v.snapshotId(),
                               v.volumeSize(),
                               v.volumeTypeAsString(),
                               v.encrypted()
                       ))
                       .orElse(null);
    }
}
