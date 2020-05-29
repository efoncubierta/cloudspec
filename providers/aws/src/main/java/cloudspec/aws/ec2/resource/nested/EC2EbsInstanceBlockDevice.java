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
import cloudspec.aws.ec2.resource.EC2VolumeResource;
import software.amazon.awssdk.services.ec2.model.EbsInstanceBlockDevice;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class EC2EbsInstanceBlockDevice {
    @PropertyDefinition(
            name = "attach_time",
            description = "The time stamp when the attachment initiated"
    )
    private final Date attachTime;

    @PropertyDefinition(
            name = "delete_on_termination",
            description = "Indicates whether the volume is deleted on instance termination"
    )
    private final Boolean deleteOnTermination;

    @PropertyDefinition(
            name = "status",
            description = "The attachment state",
            exampleValues = "attaching | attached | detaching | detached"
    )
    private final String status;

    @AssociationDefinition(
            name = "volume",
            description = "The EBS volume",
            targetClass = EC2VolumeResource.class
    )
    private final String volumeId;

    public EC2EbsInstanceBlockDevice(Date attachTime, Boolean deleteOnTermination, String status, String volumeId) {
        this.attachTime = attachTime;
        this.deleteOnTermination = deleteOnTermination;
        this.status = status;
        this.volumeId = volumeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EC2EbsInstanceBlockDevice that = (EC2EbsInstanceBlockDevice) o;
        return Objects.equals(attachTime, that.attachTime) &&
                Objects.equals(deleteOnTermination, that.deleteOnTermination) &&
                Objects.equals(status, that.status) &&
                Objects.equals(volumeId, that.volumeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachTime, deleteOnTermination, status, volumeId);
    }

    public static EC2EbsInstanceBlockDevice fromSdk(EbsInstanceBlockDevice ebsInstanceBlockDevice) {
        return Optional.ofNullable(ebsInstanceBlockDevice)
                .map(v -> new EC2EbsInstanceBlockDevice(
                                AWSResource.dateFromSdk(v.attachTime()),
                                v.deleteOnTermination(),
                                v.statusAsString(),
                                v.volumeId()
                        )
                )
                .orElse(null);
    }
}
