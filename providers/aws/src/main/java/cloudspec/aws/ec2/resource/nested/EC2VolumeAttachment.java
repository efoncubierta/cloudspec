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
import cloudspec.aws.AWSResource;
import cloudspec.aws.ec2.resource.EC2InstanceResource;
import cloudspec.aws.ec2.resource.EC2VolumeResource;
import software.amazon.awssdk.services.ec2.model.VolumeAttachment;

import java.util.*;
import java.util.stream.Collectors;

public class EC2VolumeAttachment {
    @PropertyDefinition(
            name = "attach_time",
            description = "The time stamp when the attachment initiated"
    )
    private final Date attachTime;

    @PropertyDefinition(
            name = "device",
            description = "The device name"
    )
    private final String device;

    @AssociationDefinition(
            name = "instance",
            description = "The instance",
            targetClass = EC2InstanceResource.class
    )
    private final String instanceId;

    @PropertyDefinition(
            name = "state",
            description = "The attachment state of the volume",
            exampleValues = "attaching | attached | detaching | detached | busy"
    )
    private final String state;

    @AssociationDefinition(
            name = "volume",
            description = "The volume",
            targetClass = EC2VolumeResource.class
    )
    private final String volumeId;

    @PropertyDefinition(
            name = "delete_on_termination",
            description = "Indicates whether the EBS volume is deleted on instance termination"
    )
    private final Boolean deleteOnTermination;

    public EC2VolumeAttachment(Date attachTime, String device, String instanceId, String state, String volumeId,
                               Boolean deleteOnTermination) {
        this.attachTime = attachTime;
        this.device = device;
        this.instanceId = instanceId;
        this.state = state;
        this.volumeId = volumeId;
        this.deleteOnTermination = deleteOnTermination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2VolumeAttachment that = (EC2VolumeAttachment) o;
        return Objects.equals(attachTime, that.attachTime) &&
                Objects.equals(device, that.device) &&
                Objects.equals(instanceId, that.instanceId) &&
                Objects.equals(state, that.state) &&
                Objects.equals(volumeId, that.volumeId) &&
                Objects.equals(deleteOnTermination, that.deleteOnTermination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachTime, device, instanceId, state, volumeId, deleteOnTermination);
    }

    public static List<EC2VolumeAttachment> fromSdk(List<VolumeAttachment> volumeAttachments) {
        return Optional.ofNullable(volumeAttachments)
                .orElse(Collections.emptyList())
                .stream()
                .map(EC2VolumeAttachment::fromSdk)
                .collect(Collectors.toList());
    }

    public static EC2VolumeAttachment fromSdk(VolumeAttachment volumeAttachment) {
        return Optional.ofNullable(volumeAttachment)
                .map(v ->
                        new EC2VolumeAttachment(
                                AWSResource.dateFromSdk(v.attachTime()),
                                v.device(),
                                v.instanceId(),
                                v.stateAsString(),
                                v.volumeId(),
                                v.deleteOnTermination()
                        )
                ).orElse(null);
    }
}
