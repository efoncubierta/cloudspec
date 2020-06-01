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
package cloudspec.aws.ec2.resource;

import cloudspec.annotation.AssociationDefinition;
import cloudspec.annotation.IdDefinition;
import cloudspec.annotation.PropertyDefinition;
import cloudspec.annotation.ResourceDefinition;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.Snapshot;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2SnapshotResource.RESOURCE_NAME,
        description = "Snapshot"
)
public class EC2SnapshotResource extends EC2Resource {
    public static final String RESOURCE_NAME = "snapshot";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    // private final String dataEncryptionKeyId;

    @PropertyDefinition(
            name = "encrypted",
            description = "Indicates whether the snapshot is encrypted"
    )
    private final Boolean encrypted;

    // private final String kmsKeyId;

    @PropertyDefinition(
            name = "owner_id",
            description = "The AWS account ID of the EBS snapshot owner"
    )
    private final String ownerId;

    @PropertyDefinition(
            name = "progress",
            description = "The progress of the snapshot, as a percentage"
    )
    private final String progress;

    @IdDefinition
    @PropertyDefinition(
            name = "snapshotId",
            description = "The ID of the snapshot. Each snapshot receives a unique identifier when it is created"
    )
    private final String snapshotId;

    @PropertyDefinition(
            name = "start_time",
            description = "The time stamp when the snapshot was initiated"
    )
    private final Date startTime;

    @PropertyDefinition(
            name = "state",
            description = "The snapshot state"
    )
    private final String state;

    @AssociationDefinition(
            name = "volume",
            description = "The volume that was used to create the snapshot",
            targetClass = EC2VolumeResource.class
    )
    private final String volumeId;

    @PropertyDefinition(
            name = "volume_size",
            description = " The size of the volume, in GiB"
    )
    private final Integer volumeSize;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the snapshot"
    )
    private final List<KeyValue> tags;

    public EC2SnapshotResource(String region, Boolean encrypted, String ownerId, String progress, String snapshotId,
                               Date startTime, String state, String volumeId, Integer volumeSize, List<KeyValue> tags) {
        this.region = region;
        this.encrypted = encrypted;
        this.ownerId = ownerId;
        this.progress = progress;
        this.snapshotId = snapshotId;
        this.startTime = startTime;
        this.state = state;
        this.volumeId = volumeId;
        this.volumeSize = volumeSize;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || (getClass() != o.getClass() && !(o instanceof Snapshot))) {
            return false;
        }

        if (o instanceof Snapshot) {
            return sdkEquals((Snapshot) o);
        }

        EC2SnapshotResource that = (EC2SnapshotResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(encrypted, that.encrypted) &&
                Objects.equals(ownerId, that.ownerId) &&
                Objects.equals(progress, that.progress) &&
                Objects.equals(snapshotId, that.snapshotId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(state, that.state) &&
                Objects.equals(volumeId, that.volumeId) &&
                Objects.equals(volumeSize, that.volumeSize) &&
                Objects.equals(tags, that.tags);
    }

    private boolean sdkEquals(Snapshot that) {
        return Objects.equals(encrypted, that.encrypted()) &&
                Objects.equals(ownerId, that.ownerId()) &&
                Objects.equals(progress, that.progress()) &&
                Objects.equals(snapshotId, that.snapshotId()) &&
                Objects.equals(startTime, dateFromSdk(that.startTime())) &&
                Objects.equals(state, that.stateAsString()) &&
                Objects.equals(volumeId, that.volumeId()) &&
                Objects.equals(volumeSize, that.volumeSize()) &&
                Objects.equals(tags, tagsFromSdk(that.tags()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, encrypted, ownerId, progress, snapshotId,
                startTime, state, volumeId, volumeSize, tags);
    }

    public static EC2SnapshotResource fromSdk(String regionName, Snapshot snapshot) {
        return Optional.ofNullable(snapshot)
                       .map(v -> new EC2SnapshotResource(
                                       regionName,
                                       v.encrypted(),
                                       v.ownerId(),
                                       v.progress(),
                                       v.snapshotId(),
                                       dateFromSdk(v.startTime()),
                                       v.stateAsString(),
                                       v.volumeId(),
                                       v.volumeSize(),
                                       tagsFromSdk(v.tags())
                               )
                       )
                       .orElse(null);
    }
}
