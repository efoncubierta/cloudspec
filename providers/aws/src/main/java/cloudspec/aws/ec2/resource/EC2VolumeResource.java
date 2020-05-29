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
import cloudspec.aws.ec2.resource.nested.EC2VolumeAttachment;
import cloudspec.model.KeyValue;
import cloudspec.model.ResourceDefRef;
import software.amazon.awssdk.services.ec2.model.Volume;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cloudspec.aws.AWSProvider.PROVIDER_NAME;

@ResourceDefinition(
        provider = PROVIDER_NAME,
        group = EC2Resource.GROUP_NAME,
        name = EC2VolumeResource.RESOURCE_NAME,
        description = "Volume"
)
public class EC2VolumeResource extends EC2Resource {
    public static final String RESOURCE_NAME = "volume";
    public static final ResourceDefRef RESOURCE_DEF_REF = new ResourceDefRef(
            PROVIDER_NAME, GROUP_NAME, RESOURCE_NAME
    );

    @PropertyDefinition(
            name = "region",
            description = "The AWS region",
            exampleValues = "us-east-1 | eu-west-1"
    )
    private final String region;

    @PropertyDefinition(
            name = "attachments",
            description = "Information about the volume attachments"
    )
    private final List<EC2VolumeAttachment> attachments;

    @PropertyDefinition(
            name = "availability_zone",
            description = "The Availability Zone for the volume"
    )
    private final String availabilityZone;

    @PropertyDefinition(
            name = "create_time",
            description = "The time stamp when volume creation was initiated"
    )
    private final Date createTime;

    @PropertyDefinition(
            name = "encrypted",
            description = "Indicates whether the volume is encrypted"
    )
    private final Boolean encrypted;

    //    @AssociationDefinition(
//            name = "kms_key",
//            description = "The Amazon Resource Name (ARN) of the AWS Key Management Service (AWS KMS) customer master key (CMK)"
//    )
    private final String kmsKeyId;

    @PropertyDefinition(
            name = "outpost_arn",
            description = "The Amazon Resource Name (ARN) of the Outpost"
    )
    private final String outpostArn;

    @PropertyDefinition(
            name = "size",
            description = "The size of the volume, in GiBs"
    )
    private final Integer size;

    @AssociationDefinition(
            name = "snapshot",
            description = "The snapshot from which the volume was created, if applicable",
            targetClass = EC2SnapshotResource.class
    )
    private final String snapshotId;

    @PropertyDefinition(
            name = "state",
            description = "The volume state",
            exampleValues = "creating | available | in-use | deleting | deleted | error"
    )
    private final String state;

    @IdDefinition
    @PropertyDefinition(
            name = "volume_id",
            description = "The ID of the volume"
    )
    private final String volumeId;

    @PropertyDefinition(
            name = "iops",
            description = "The number of I/O operations per second (IOPS) that the volume supports"
    )
    private final Integer iops;

    @PropertyDefinition(
            name = "tags",
            description = "Any tags assigned to the volume"
    )
    private final List<KeyValue> tags;

    @PropertyDefinition(
            name = "volume_type",
            description = "The volume type",
            exampleValues = "standard | io1 | gp2 | sc1 | st1"
    )
    private final String volumeType;

    @PropertyDefinition(
            name = "fast_restored",
            description = "Indicates whether the volume was created using fast snapshot restore"
    )
    private final Boolean fastRestored;

    @PropertyDefinition(
            name = "multi_attach_enabled",
            description = "Indicates whether Amazon EBS Multi-Attach is enabled"
    )
    private final Boolean multiAttachEnabled;

    public EC2VolumeResource(String region, List<EC2VolumeAttachment> attachments, String availabilityZone,
                             Date createTime, Boolean encrypted, String kmsKeyId, String outpostArn, Integer size,
                             String snapshotId, String state, String volumeId, Integer iops, List<KeyValue> tags,
                             String volumeType, Boolean fastRestored, Boolean multiAttachEnabled) {
        this.region = region;
        this.attachments = attachments;
        this.availabilityZone = availabilityZone;
        this.createTime = createTime;
        this.encrypted = encrypted;
        this.kmsKeyId = kmsKeyId;
        this.outpostArn = outpostArn;
        this.size = size;
        this.snapshotId = snapshotId;
        this.state = state;
        this.volumeId = volumeId;
        this.iops = iops;
        this.tags = tags;
        this.volumeType = volumeType;
        this.fastRestored = fastRestored;
        this.multiAttachEnabled = multiAttachEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2VolumeResource that = (EC2VolumeResource) o;
        return Objects.equals(region, that.region) &&
                Objects.equals(attachments, that.attachments) &&
                Objects.equals(availabilityZone, that.availabilityZone) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(encrypted, that.encrypted) &&
                Objects.equals(kmsKeyId, that.kmsKeyId) &&
                Objects.equals(outpostArn, that.outpostArn) &&
                Objects.equals(size, that.size) &&
                Objects.equals(snapshotId, that.snapshotId) &&
                Objects.equals(state, that.state) &&
                Objects.equals(volumeId, that.volumeId) &&
                Objects.equals(iops, that.iops) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(volumeType, that.volumeType) &&
                Objects.equals(fastRestored, that.fastRestored) &&
                Objects.equals(multiAttachEnabled, that.multiAttachEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, attachments, availabilityZone, createTime, encrypted, kmsKeyId, outpostArn, size,
                snapshotId, state, volumeId, iops, tags, volumeType, fastRestored, multiAttachEnabled);
    }

    public static EC2VolumeResource fromSdk(String regionName, Volume volume) {
        return Optional.ofNullable(volume)
                .map(v ->
                        new EC2VolumeResource(
                                regionName,
                                EC2VolumeAttachment.fromSdk(v.attachments()),
                                v.availabilityZone(),
                                dateFromSdk(v.createTime()),
                                v.encrypted(),
                                v.kmsKeyId(),
                                v.outpostArn(),
                                v.size(),
                                v.snapshotId(),
                                v.stateAsString(),
                                v.volumeId(),
                                v.iops(),
                                tagsFromSdk(v.tags()),
                                v.volumeTypeAsString(),
                                v.fastRestored(),
                                v.multiAttachEnabled()
                        )
                )
                .orElse(null);
    }
}
